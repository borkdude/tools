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
          project-clj-edn (e/parse-string (slurp project-clj) {:read-eval identity})
          {:keys [dependencies source-paths resource-paths]} (apply hash-map (drop 3 project-clj-edn))]
      (pprint/pprint
       {:paths (into source-paths resource-paths)
        :deps (into {} (for [[d v] dependencies] [d {:mvn/version v}]))}))))
