#!/usr/bin/env bb

(require '[babashka.deps :as deps])
(deps/add-deps '{:deps {io.github.borkdude/lein2deps {:git/sha "43ddacba5b59505cc9b4cacacefb683439047192"}}})
(require '[lein2deps.api])
(apply lein2deps.api/-main *command-line-args*)
