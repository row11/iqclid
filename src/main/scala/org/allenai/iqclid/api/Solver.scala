package org.allenai.iqclid.api

import org.allenai.iqclid.NumberSequence

case class Solution(tree: Tree, fitness: Double) {
  override def toString = {
    s"$tree @ $fitness"
  }
}

trait Solver {
  /** Generate proposal trees */
  def solve(s: NumberSequence): Seq[Solution]
}

class Ensemble(solvers: Seq[Solver]) extends Solver {
  override def solve(s: NumberSequence): Seq[Solution] = {
    val solutions = solvers.flatMap(_.solve(s))
    val maxScore = solutions.minBy(x => x.fitness).fitness
    solutions.filter { x => x.fitness == maxScore }
  }
}

class MultipleRunSolver(solver: Solver, iterations: Int) extends Solver {
  override def solve(s: NumberSequence): Seq[Solution] = {
    val solutions = (0 until iterations).foldLeft(Seq[Solution]()) {
      case (acc, _) =>
        acc ++ solver.solve(s)
    }
    val maxScore = solutions.minBy(x => x.fitness).fitness
    solutions.filter { x => x.fitness == maxScore }
  }

}
