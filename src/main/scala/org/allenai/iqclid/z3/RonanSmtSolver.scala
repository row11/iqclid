package org.allenai.iqclid.z3

import org.allenai.iqclid.NumberSequence
import org.allenai.iqclid.api._
import org.allenai.iqclid.z3.ThreadSafeDependencies.Z3Module
import org.allenai.iqclid.api.{ Evaluator, Tree }

class RonanSmtSolver extends Solver {
  override def solve(s: NumberSequence): Seq[Solution] = {
    try {
      val seq = s.seq

      val solutionSet = {
        val sol =
          try {
            ThreadSafeDependencies.withZ3Module {
              z3Module =>
                new Z3Interface(z3Module, true).solveSequence(seq, 1)
            }
          } catch {
            case _ =>
              Seq()
          }
        if (!sol.isEmpty) {
          sol
        } else {
          val sol2 = try {
            ThreadSafeDependencies.withZ3Module {
              z3Module =>
                new Z3Interface(z3Module, true).solveSequence(seq, 3)
            }
          } catch {
            case _ =>
              Seq()
          }
          if (!sol2.isEmpty) {
            sol2
          } else {
            try {
              ThreadSafeDependencies.withZ3Module {
                z3Module =>
                  new Z3Interface(z3Module, true).solveSequence(seq, 2)
              }
            } catch {
              case _ =>
                Seq()
            }
          }
        }
      }
      println(solutionSet)
      val tree = solutionSet.head.tree
      println(Evaluator.evaluate(tree, s.baseCases(tree), s.seq.length + 1))

      solutionSet
    } catch {
      case _ =>
        Seq()
    }
  }
}