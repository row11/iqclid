package org.allenai.iqclid.z3

import org.allenai.common.testkit.UnitSpec
import org.allenai.common.testkit.UnitSpec
import org.allenai.iqclid.api._
import org.allenai.iqclid.{BaselineSearch, NumberSequence}

class SmtSolverSpec extends UnitSpec{

  "smt" should "do 1 2 3 4 5" in {
    val s = NumberSequence(Seq(1, 2, 3, 4, 5, 6), 1)
    val smt = new SmtSolver()
    smt.solve(s)
  }

  "smt" should "do 2 4 6 8" in {
    val s = NumberSequence(Seq(2,4,6,8), 1)
    val smt = new SmtSolver()
    smt.solve(s)
  }

  val s = NumberSequence(Seq(1,2,3,1,2,3), 1)
  "smt" should "do 1 2 3 1 2 3" in {
    val smt = new SmtSolver()
    smt.solve(s)
  }

  "smt" should "do 1 4 9 16 25" in {
    val s = NumberSequence(Seq(1,4,9,16,25), 1)
    val smt = new SmtSolver()
    smt.solve(s)
  }

  "smt" should "do 16, 22, 34, 52, 76" in {
    val s = NumberSequence(Seq(16, 22, 34, 52, 76), 2)
    val smt = new SmtSolver()
    smt.solve(s)
  }

  "smt" should "do 30, 28, 25, 21, 16" in {
    val s = NumberSequence(Seq(30, 28, 25, 21, 16), 2)
    val smt = new SmtSolver()
    smt.solve(s)
  }

  "smt" should "do 123, 135, 148, 160, 173" in {
    val s = NumberSequence(Seq(123, 135, 148, 160, 173), 2)
    val smt = new SmtSolver()
    smt.solve(s)
  }

  "smt" should "do 1, 1, 2, 3, 5, 8, 13, 21, 34" in {
    val s = NumberSequence(Seq(1, 1, 2, 3, 5, 8, 13, 21, 34), 2)
    val smt = new SmtSolver()
    smt.solve(s)
  }
}
