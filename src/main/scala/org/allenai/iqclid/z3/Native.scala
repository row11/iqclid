package org.allenai.iqclid.z3

import java.nio.file.{Files, Paths}

import org.apache.commons.compress.utils.IOUtils

object Native {
  /** Extracts the lib from jar into ./tmp/...
    * Loads the library into RAM.
    *
    * @param name the path in the jar
    * @param dir the path on disk
    */
  def extractLibraryFromJar(name: String, dir: String) = {
    val bytes = IOUtils.toByteArray(getClass.getResourceAsStream(name))
    val libName = Paths.get(s"$name").getFileName
    val file = Paths.get(s"$dir/$libName")
    Files.deleteIfExists(file)
    Files.createDirectories(file.getParent)
    Files.write(file, bytes)
  }
}