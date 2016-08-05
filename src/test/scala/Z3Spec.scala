import org.allenai.common.testkit.UnitSpec
import org.allenai.iqclid.{ NumberSequence, Z3Solver}

class Z3Spec extends UnitSpec {

  "" should "" in {
    val s = NumberSequence(Seq(1, 2, 3, 4, 5))
    val solver = new Z3Solver
    println("ANSWER IS: "+ solver.solve(s).head)
  }

}
