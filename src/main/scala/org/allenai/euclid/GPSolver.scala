package org.allenai.euclid

import org.allenai.euclid.api.{ Fitness, Solution, Solver }

object GPSolver {
  def main(args: Array[String]) = {
    println("Hello world from Scala!")
    println(clj.Bridge.greet)
  }
}

class GPSolver(fitness: Fitness) extends Solver {
  override def solve(s: NumberSequence): Seq[Solution] = ???
}
