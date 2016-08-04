name := "iqclid"

version := "1.0"

scalaVersion := "2.11.8"

description := "An ILP based Table Inference solver"

libraryDependencies ++= Seq(
  "org.allenai.common" %% "common-core" % "1.4.3",
  "org.allenai.common" %% "common-testkit" % "1.4.3"
)

// Make sure SCIP libraries are locatable.
javaOptions += s"-Djava.library.path=${unmanagedBase.value.absolutePath}"
envVars ++= Map(
  "LD_LIBRARY_PATH" -> s"${unmanagedBase.value.absolutePath}",
  "DYLD_LIBRARY_PATH" -> s"${unmanagedBase.value.absolutePath}"
)

includeFilter in unmanagedJars := "*.jar" || "*.so" || "*.dylib"

fork := true
