package org.allenai.iqclid.z3

import org.allenai.iqclid.NumberSequence
import org.allenai.iqclid.api.{Fitness, Solution, Solver}
import org.allenai.iqclid.z3.ThreadSafeDependencies.Z3Module

class SmtSolver extends Solver {
  override def solve(s: NumberSequence): Seq[Solution]= {
    val seq = s.seq

    ThreadSafeDependencies.withZ3Module {
      z3Module =>
        val sol = new Z3Interface(z3Module,true).solveSequence(seq)
        println(sol)
        sol
    }
  }
}