#!/usr/bin/env bb

(ns nrepl-proxy
  (:require [babashka.cli :as cli]
            [babashka.tasks :refer [clojure]]))

(def version "0.2.8-alpha")

(def deps {:deps {'com.lambdaisland/nrepl-proxy {:mvn/version version}}})

(def spec [[:port {:desc "Your editor connects to this port"}]
           [:attach {:desc "The port of the nREPL server to attach to"}]
           [:help {:desc "Print this help."}]])

(defn print-help []
  (println "Proxy server for debugging nREPL.

Usage: nrepl_proxy <options>

Options:")
  (println (cli/format-opts {:spec spec}))
  (println)
  (println "Check out nrepl-proxy's README for more documentation: https://github.com/lambdaisland/nrepl-proxy"))

(let [args (cli/parse-opts *command-line-args* {:spec spec})]
  (cond (or (empty? args) (:help args)) (print-help)
        (:version args) (println version)
        :else (clojure "-Sdeps" deps "-X" "lambdaisland.nrepl-proxy/start" args)))

nil
