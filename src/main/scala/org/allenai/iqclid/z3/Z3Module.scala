package org.allenai.iqclid.z3

import com.microsoft.z3.Version

object ThreadSafeDependencies {
  trait Z3Module

  def init(): Unit = {
    Z3ModuleInstance.init
  }

  def withZ3Module[T](fn: (Z3Module) => T): T = {
    Z3ModuleInstance.init
    Z3ModuleInstance.synchronized {
      fn(Z3ModuleInstance)
    }
  }


  /** An object for one time set up of native library paths for Z3
    * Use it via ThreadSafeDependencies.withZ3
    */
  private object Z3ModuleInstance extends Z3Module {
    lazy val init = {
      val path = "./lib/native"
      require(
        System.getProperty("java.library.path").split(":").contains(path),
        s"$path is not in java.library.path property"
      )
      Native.extractLibraryFromJar("/native/libz3.dylib", ".")
      Native.extractLibraryFromJar("/native/libz3java.dylib", path)
      Native.extractLibraryFromJar("/native/libz3.so", path)
      Native.extractLibraryFromJar("/native/libz3java.so", path)
    }
  }
}
