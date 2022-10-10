#!/usr/bin/env bb

(require '[babashka.cli :as cli]
         '[clojure.pprint :as pprint]
         '[clojure.walk :as walk]
         '[edamame.core :as e])

(defmacro defproject [& [_name _version & body]]
  (list 'quote
        (walk/prewalk (fn [form]
                        (if (and (seq? form)
                                 (= 'clojure.core/unquote (first form)))
                          (eval (second form))
                          form)) body)))

(defn safe-parse [file]
  (let [parser (e/reader (slurp file))
        cfg {:read-eval identity}
        form (first (take-while #(or
                                  (and (seq? %)
                                       (= 'defproject (first %)))
                                  (= ::e/eof %)) (repeatedly #(e/parse-next parser cfg))))
        project-clj-edn form]
    (apply hash-map (drop 3 project-clj-edn))))

(let [opts (cli/parse-opts *command-line-args*)]
  (if (:help opts)
    (println "Usage: lein2deps <opts>

Options:

  --project-clj <file>: defaults to \"project.clj\"
  --eval              : evaluate project.clj. Use at your own risk.")
    (let [project-clj (or (:project-clj opts)
                          "project.clj")
          parsed (if (:eval opts)
                   (load-file project-clj)
                   (safe-parse project-clj))
          {:keys [dependencies source-paths resource-paths]} parsed]
      (pprint/pprint
       {:paths (into (vec source-paths) resource-paths)
        :deps (into {} (for [[d v] dependencies] [d {:mvn/version v}]))}))))
