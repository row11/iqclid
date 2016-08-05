package org.allenai.iqclid.z3

import org.allenai.iqclid.NumberSequence
import org.allenai.iqclid.api._
import org.allenai.iqclid.z3.ThreadSafeDependencies.Z3Module
import org.allenai.iqclid.api.{Evaluator, Tree}

class SmtSolver extends Solver {
  override def solve(s: NumberSequence): Seq[Solution]= {
    val seq = s.seq

    val solutionSet = {
      val sol = ThreadSafeDependencies.withZ3Module {
        z3Module =>
          new Z3Interface(z3Module, true).solveSequence(seq, 1)
      }
      if (!sol.isEmpty) {
        sol
      } else {
        val sol2 = ThreadSafeDependencies.withZ3Module {
          z3Module =>
            new Z3Interface(z3Module, true).solveSequence(seq, 3)
        }
        if (!sol2.isEmpty) {
          sol2
        } else {
          ThreadSafeDependencies.withZ3Module {
            z3Module =>
              new Z3Interface(z3Module, true).solveSequence(seq, 2)
          }
        }
      }
    }
    println(solutionSet)
    println(Evaluator.evaluate(solutionSet.head.tree, s.baseCases, s.seq.length + 1))
    solutionSet
  }
}