#!/usr/bin/env bb

(require '[babashka.deps :as deps])
(deps/add-deps '{:deps {io.github.borkdude/lein2deps {:git/sha "30c12aa7b7a0167a4580cc8695916ef4deed3322"}}})
(require '[lein2deps.api])
(apply lein2deps.api/-main *command-line-args*)
