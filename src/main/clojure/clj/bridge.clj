(ns clj.Bridge
  (:gen-class
    :methods [#^{:static true} [greet [] void]]))

(defn -greet []
  (println "Hello world from Clojure!"))
