import org.allenai.common.testkit.UnitSpec
import org.allenai.iqclid.api.{Apply, I, Number, Plus}
import org.allenai.iqclid.{BaselineSearch, NumberSequence, Z3Solver}

class Z3Spec extends UnitSpec {

  "" should "" in {
    val s = NumberSequence(Seq(1, 2, 3, 4, 5), 1)
    val solver = new Z3Solver
    println("ANSWER IS: "+ solver.solve(s).head)
  }

}
