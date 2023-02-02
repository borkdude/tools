(require '[babashka.deps :as deps])

(deps/add-deps '{:deps {cljfmt/cljfmt {:git/url "https://github.com/weavejester/cljfmt" :git/sha "7dfd55d5dd0756f30311a90f206c2dd32e56d18b" :deps/root "cljfmt"}}})

(require 'cljfmt.main)

(apply cljfmt.main/-main *command-line-args*)
