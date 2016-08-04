package org.allenai.euclid

import org.allenai.euclid.api.{ Fitness, Solution, Solver }

object GPSolver {
  def main(args: Array[String]): Unit = {
    println("Hello world from Scala!")
    println(clj.Bridge.greet)
    //clj.Bridge.tutorial(15, 15)
    println("Back to Scala.")
    sys.exit(0)
  }
}

class GPSolver(fitness: Fitness) extends Solver {
  override def solve(s: NumberSequence): Seq[Solution] = ???
}
