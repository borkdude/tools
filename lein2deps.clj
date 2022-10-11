#!/usr/bin/env bb

(require '[babashka.cli :as cli]
         '[clojure.pprint :as pprint]
         '[clojure.walk :as walk]
         '[edamame.core :as e])

(defmacro defproject [& [_name _version & body]]
  `(apply hash-map
          '~(walk/prewalk (fn [form]
                            (if (and (seq? form)
                                     (= 'clojure.core/unquote (first form)))
                              (eval (second form))
                              form))
                          body)))

(defn safe-parse [file]
  (let [parser (e/reader (slurp file))
        cfg {:read-eval identity}
        form (first (take-while #(or
                                  (and (seq? %)
                                       (= 'defproject (first %)))
                                  (= ::e/eof %)) (repeatedly #(e/parse-next parser cfg))))
        project-clj-edn form]
    (apply hash-map (drop 3 project-clj-edn))))

(defn qualify-dep-name [d]
  (if (simple-symbol? d)
    (symbol (str d) (str d))
    d))

(defn convert-dep [[name version & {:keys [classifier exclusions]}]]
  (let [name (qualify-dep-name name)
        name (if classifier
               (symbol (str name "$" classifier))
               name)
        params (cond-> {:mvn/version version}
                 (seq exclusions)
                 (assoc :exclusions (mapv qualify-dep-name exclusions)))]
    [name params]))

(defn add-prep-lib [deps-edn project-edn]
  (let [{:keys [java-source-paths compile-path javac-options]} project-edn]
    (assoc deps-edn
           :aliases
           {:build
            {:deps
             {'io.github.borkdude/tools {:git/sha "a9a4b27c52542390ab4336fe275d1cb59f1f363c"
                                         :deps/root "lein2deps"}}
             :ns-default 'lein2deps.build
             :lein2deps/compile-java (cond-> {:src-dirs java-source-paths
                                              :class-dir compile-path}
                                       javac-options
                                       (assoc :javac-opts javac-options))}}
           :deps/prep-lib
           {:ensure compile-path
            :alias :build
            :fn 'compile-java})))

(let [opts (cli/parse-opts *command-line-args*)]
  (if (:help opts)
    (println "Usage: lein2deps <opts>

Options:

  --project-clj <file>: defaults to \"project.clj\"
  --eval              : evaluate project.clj. Use at your own risk.")
    (let [project-clj (or (:project-clj opts)
                          "project.clj")
          project-edn (if (:eval opts)
                        (load-file project-clj)
                        (safe-parse project-clj))
          project-edn (merge {:compile-path "target/classes"
                              :source-paths ["src"]}
                             project-edn)
          {:keys [dependencies source-paths resource-paths compile-path java-source-paths]} project-edn
          deps-edn {:paths (cond-> (into (vec source-paths) resource-paths)
                             java-source-paths
                             (conj compile-path))
                    :deps (into {} (map convert-dep) dependencies)}]
      (pprint/pprint
       (cond-> deps-edn
         java-source-paths
         (add-prep-lib project-edn))))))
