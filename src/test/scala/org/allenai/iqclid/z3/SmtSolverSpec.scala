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

  "smt" should "do 1 2 3 1 2 3" in {
    val s = NumberSequence(Seq(1,2,3,1,2,3), 1)
    val smt = new SmtSolver()
    smt.solve(s)
  }

  "smt" should "do 1 4 9 16 25" in {
    val s = NumberSequence(Seq(1,4,9,16,25), 1)
    val smt = new SmtSolver()
    smt.solve(s)
  }
}
