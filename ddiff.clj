#!/usr/bin/env bb

(ns ddiff
  (:require [babashka.deps :as deps]
            [clojure.edn :as edn]
            [babashka.cli :as cli]))

(deps/add-deps '{:deps {lambdaisland/deep-diff2 {:mvn/version "2.11.216"}}})
(require '[lambdaisland.deep-diff2 :as diff2 :refer [pretty-print diff]])

(let [{:keys [left right minimize]}
      (cli/parse-opts *command-line-args*
                      {:spec {:minimize {:coerce :boolean
                                         :alias :m}}
                       :args->opts [:left :right]})
      left (edn/read-string (slurp left))
      right (edn/read-string (slurp right))]
  (pretty-print (cond-> (diff left right)
                  minimize (diff2/minimize))))
