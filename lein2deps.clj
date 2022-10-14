#!/usr/bin/env bb

(require '[babashka.deps :as deps])
(deps/add-deps '{:deps {io.github.borkdude/lein2deps {:git/sha "e26edeb114c9d88a5c4d3abb683306588fcaad13"}}})
(require '[lein2deps.api])
(apply lein2deps.api/-main *command-line-args*)
