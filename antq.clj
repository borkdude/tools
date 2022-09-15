#!/usr/bin/env bb

(ns antq
  (:require [babashka.cli :as cli]
            [babashka.tasks :refer [clojure]]))

(def version "2.0.895")

(def deps {:deps {'com.github.liquidz/antq {:mvn/version version}}})

(def spec  {:exclude {:coerce []
                      :ref "ARTIFACT_NAME[@VERSION]"
                      :desc "Skip version checking for specified artifacts or versions."}
            :focus {:coerce []
                    :ref "ARTIFACT_NAME"
                    :desc "Focus version checking for specified artifacts."}
            :skip {:coerce []
                   :ref "PROJECT_TYPE"
                   :desc "Skip to search specified project files."}
            :directory {:coerce []
                        :ref "DIRECTORY"
                        :desc "Add search path for projects. Current directory(.) is added by default."}
            :upgrade {:desc "Upgrade outdated versions interactively."}
            :force {:desc "Upgrade without confirmation."}
            :error-format {:ref "ERROR_FORMAT"
                           :desc "Customize outputs for outdated dependencies."}
            :reporter {:ref "REPORTER"
                       :desc "table, format, json or edn"}
            :download {:desc "Download dependencies"}
            :ignore-locals {:desc "Ignore versions from ~/.m2"}
            :check-clojure-tools {:desc "Detect all tools installed in ~/.clojure/tools as dependencies."}
            :no-diff {:desc "Skip checking diff between deps' versions. Disabled by default."}
            :version {:alias :v
                      :desc "Print version"}})

(defn print-help []
  (println "Point out outdated dependencies.

Usage: antq <options>

Options:")
  (println (cli/format-opts {:spec spec
                             :order [:upgrade
                                     :exclude
                                     :directory
                                     :focus
                                     :skip
                                     :error-format
                                     :reporter
                                     :force
                                     :download
                                     :ignore-locals
                                     :check-clojure-tools
                                     :no-diff
                                     :version]}))
  (println)
  (println "Check out antq's README for more documentation: https://github.com/liquidz/antq"))

(let [args (cli/parse-opts *command-line-args* {:spec spec})]
  (cond (:help args) (print-help)
        (:version args) (println version)
        :else (clojure "-Sdeps" deps "-X" (symbol "antq.tool" "outdated") args)))

nil
