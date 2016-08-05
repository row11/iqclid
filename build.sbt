name := "iqclid"

version := "1.0"

scalaVersion := "2.11.8"

fork in run := true

// Manual steps to fix clojure plugin [publishes it in local artifact repo]:
// $ git clone https://github.com/cristipp/sbt-clojure.git
// $ cd sbt-clojure
// $ sbt publish-local

seq(clojure.settings :_*)

libraryDependencies += "org.clojure" % "clojure" % "1.5.1"

val z3LibPath = "./lib/native"

libraryDependencies ++= Seq(
  "org.allenai.common" %% "common-core" % "1.4.3",
  "org.allenai.common" %% "common-testkit" % "1.4.3",
  "org.allenai.third_party" % "z3" % "4.4.1-0",
  "org.allenai.third_party" % "z3-native-linux" % "4.4.1-0",
  "org.allenai.third_party" % "z3-native-macos" % "4.4.1-0",
  "org.apache.commons" % "commons-compress" % "1.12"
)
