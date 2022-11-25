#!/usr/bin/env bb

(ns ddiff
  (:require [babashka.deps :as deps]
            [clojure.edn :as edn]))

(deps/add-deps '{:deps {lambdaisland/deep-diff2 {:mvn/version "2.7.169"}}})

(def left (edn/read-string (slurp (first *command-line-args*))))
(def right (edn/read-string (slurp (second *command-line-args*))))

(require '[lambdaisland.deep-diff2 :refer [pretty-print diff]])

(pretty-print (diff left right))
