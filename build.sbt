name := "iqclid"

version := "1.0"

scalaVersion := "2.11.8"

fork in run := true

seq(clojure.settings :_*)

libraryDependencies += "org.clojure" % "clojure" % "1.5.1"
