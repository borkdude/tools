#!/usr/bin/env bb

(require '[babashka.deps :as deps])
(deps/add-deps '{:deps {io.github.borkdude/lein2deps {:git/sha "60e33d969f624143f03ebd3456a703e68c8b9ab7"}}})
(require '[lein2deps.api])
(apply lein2deps.api/-main *command-line-args*)
