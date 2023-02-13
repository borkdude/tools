#!/usr/bin/env bb

(require '[babashka.deps :as deps])
(deps/add-deps '{:deps {io.github.borkdude/lein2deps {:git/sha "4f6d4a0140ddea10c5ac28cc24b49643660f42f6"}}})
(require '[lein2deps.api])
(apply lein2deps.api/-main *command-line-args*)
