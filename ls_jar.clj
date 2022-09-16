#!/usr/bin/env bb

(ns ls-jar
  (:require
   [babashka.cli :as cli]
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def spec [[:lib {:desc "Library as fully qualified symbol. Must be accompanied with --version."}]
           [:version {:desc "Version"}]
           [:jar {:desc "Jar file"}]])

(defn print-help []
  (println "Enumerate files from jar files")
  (println "Usage: ls_jar <options>")
  (println)
  (println "Options:")
  (println (cli/format-opts {:spec spec}))
  (println)
  (println "Examples:")
  (println "ls_jar --lib babashka/fs --version 0.1.6")
  (println "ls_jar --jar ~/.m2/repository/babashka/fs/0.1.6/fs-0.1.6.jar"))

(if (empty? *command-line-args*)
  (print-help)
  (let [{:keys [jar lib version help]} (cli/parse-opts *command-line-args* {:spec spec})]
    (if help (print-help)
        (let [file (if jar
                     (io/file jar)
                     (if (and lib version)
                       (let [[_org lib-name] (str/split lib #"/")]
                         (io/file (System/getProperty "user.home")
                                  (format ".m2/repository/%s/%s/%s-%s.jar"
                                          (str/replace lib "." (System/getProperty "file.separator"))
                                          version
                                          lib-name version)))
                       (do (println "Provide either --file or: --lib and --version")
                           (System/exit 1))))]
          (doseq [e (enumeration-seq
                     (.entries (java.util.jar.JarFile. file)))]
            (println (.getName e)))))))
