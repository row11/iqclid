(ns clj.Hello
  (:gen-class
    :methods [#^{:static true} [world [] void]]))

(defn -world []
  (println "Hello world from Clojure!"))
