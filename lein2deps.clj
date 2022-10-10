#!/usr/bin/env bb

(require '[babashka.cli :as cli]
         '[clojure.pprint :as pprint]
         '[edamame.core :as e])

(let [opts (cli/parse-opts *command-line-args*)]
  (if (:help opts)
    (println "Usage: lein2deps <opts>

Options:

  --project-clj <file>: defaults to \"project.clj\"")
    (let [project-clj (or (:project-clj opts)
                          "project.clj")
          parser (e/reader (slurp project-clj))
          cfg {:read-eval identity}
          form (first (take-while #(or
                                   (and (seq? %)
                                        (= 'defproject (first %)))
                                   (= ::e/eof %)) (repeatedly #(e/parse-next parser cfg))))
          project-clj-edn form
          {:keys [dependencies source-paths resource-paths]} (apply hash-map (drop 3 project-clj-edn))]
      (pprint/pprint
       {:paths (into (vec source-paths) resource-paths)
        :deps (into {} (for [[d v] dependencies] [d {:mvn/version v}]))}))))
