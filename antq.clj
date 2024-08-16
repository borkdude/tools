#!/usr/bin/env bb

(ns antq
  (:require [babashka.cli :as cli]
            [babashka.tasks :refer [clojure]]))

(def version "2.8.1206")

(def deps {:deps {'com.github.liquidz/antq {:mvn/version version}
                  'org.slf4j/slf4j-nop {:mvn/version "2.1.0-alpha1"}}})

(def spec [[:upgrade {:desc "Upgrade outdated versions interactively."}]
           [:exclude {:coerce []
                      :ref "ARTIFACT_NAME[@VERSION]"
                      :desc "Skip version checking for specified artifacts or versions."}]
           [:directory {:coerce []
                        :ref "DIRECTORY"
                        :desc "Add search path for projects. Current directory(.) is added by default."}]
           [:focus {:coerce []
                    :ref "ARTIFACT_NAME"
                    :desc "Focus version checking for specified artifacts."}]
           [:skip {:coerce []
                   :ref "PROJECT_TYPE"
                   :desc "Skip to search specified project files."}]
           [:error-format {:ref "ERROR_FORMAT"
                           :desc "Customize outputs for outdated dependencies."}]
           [:reporter {:ref "REPORTER"
                       :desc "table, format, json or edn"}]
           [:force {:desc "Upgrade without confirmation."}]
           [:download {:desc "Download dependencies"}]
           [:ignore-locals {:desc "Ignore versions from ~/.m2"}]
           [:check-clojure-tools {:desc "Detect all tools installed in ~/.clojure/tools as dependencies."}]
           [:no-diff {:desc "Skip checking diff between deps' versions. Disabled by default."}]
           [:version {:alias :v
                      :desc "Print version"}]])

(defn print-help []
  (println "Point out outdated dependencies.

Usage: antq <options>

Options:")
  (println (cli/format-opts {:spec spec}))
  (println)
  (println "Check out antq's README for more documentation: https://github.com/liquidz/antq"))

(let [args (cli/parse-opts *command-line-args* {:spec spec})]
  (cond (or (:help args) (empty? args)) (print-help)
        (:version args) (println version)
        :else (clojure "-Sdeps" deps "-X" (symbol "antq.tool" "outdated") args)))

nil
