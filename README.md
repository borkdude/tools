# tools

This repository contains some babashka scripts that you can install with [bbin](https://github.com/babashka/bbin).

## antq

Point out outdated dependencies and/or update them.

Install: `bbin install https://raw.githubusercontent.com/borkdude/tools/main/antq.clj`

For usage, run `antq --help`.

## ddiff

Compare two EDN files using [deep-diff2](https://github.com/lambdaisland/deep-diff2).

Install: `bbin install https://raw.githubusercontent.com/borkdude/tools/main/ddiff.clj`

Usage: `ddiff file1.edn file2.edn`

## cljfmt

Format Clojure code using [cljfmt](https://github.com/weavejester/cljfmt).

Install: `bbin install https://raw.githubusercontent.com/borkdude/tools/main/cljfmt.clj`

For usage, run `cljfmt --help`.

## lein2deps

Convert `project.clj` to `deps.edn`.

Install: `bbin install https://raw.githubusercontent.com/borkdude/tools/main/lein2deps.clj`

See [tweet](https://twitter.com/borkdude/status/1578050265567076353) for demo.

For usage, run: `lein2deps --help`.

## ls_jar

List files in jar files.

Install: `bbin install https://raw.githubusercontent.com/borkdude/tools/main/ls_jar.clj`

For usage, run `ls_jar --help`.

## nrepl_proxy

Proxy for debugging nREPL interactions.

Install: `bbin install https://raw.githubusercontent.com/borkdude/tools/main/nrepl_proxy.clj`

For usage, run `nrepl_proxy --help`.
