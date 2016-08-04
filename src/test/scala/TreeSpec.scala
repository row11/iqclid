import org.allenai.common.testkit.UnitSpec
import org.allenai.euclid.NumberSequence
import org.allenai.euclid.api.{BaselineFitness, I, Plus, _}

class TreeSpec extends UnitSpec{

  "evaluate" should "correctly generate sequences" in {
    assert(
      Evaluator.evaluate(Apply(Times(), Seq(Number(2), T(1))), Seq(1), 5) ==
          Seq(1, 2, 4, 8, 16))
    assert(
      Evaluator.evaluate(Apply(Plus(), Seq(Number(4), I())), Seq(), 5) ==
          Seq(4, 5, 6, 7, 8))
  }

  "complexity" should "correctly determine tree size" in {
    val fitness = new BaselineFitness(0.5)
    assert(
      fitness.complexity(I()) == 1)
    assert(
      fitness.complexity(Apply(Plus(), Seq(Number(4), I()))) == 3)
  }
  "accuracy" should "correctly determine distance to a sequence" in {
    val fitness = new BaselineFitness(0.5)
    assert(
      fitness.accuracy(
        Apply(Plus(), Seq(Number(4), I())),
        NumberSequence(Seq(4, 5, 6, 7, 8), 0))
          == 0)
    assert(
      fitness.accuracy(
        Apply(Plus(), Seq(Number(5), I())),
        NumberSequence(Seq(4, 5, 6, 7, 8), 0))
          == 5)
  }

}
