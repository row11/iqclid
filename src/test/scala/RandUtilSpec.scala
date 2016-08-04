import org.allenai.common.testkit.UnitSpec
import org.allenai.euclid.api._
import org.allenai.euclid.{ BaselineSearch, NumberSequence, RandUtil }

class RandUtilSpec extends UnitSpec {

  "pickWithProb" should "work" in {

    val results = (0 until 1000).map { x =>
      RandUtil.pickWithProb(Seq((1, 0.1), (2, 0.2), (3, 0.3), (4, 0.4)))
    }

    println(s"1: ${results.count(_ == 1)}")
    println(s"2: ${results.count(_ == 2)}")
    println(s"3: ${results.count(_ == 3)}")
    println(s"4: ${results.count(_ == 4)}")

  }

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

  "baseline search" should "do 1 2 3 4 5" in {
    val s = NumberSequence(Seq(1, 2, 3, 4, 5, 6), 1)
    val search = new BaselineSearch(0.5, 1000, 30)
    val best = search.best(s)
    assert(best == I())
  }

}
