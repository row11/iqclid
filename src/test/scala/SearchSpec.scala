import org.allenai.common.testkit.UnitSpec
import org.allenai.euclid.api._
import org.allenai.euclid.{BaselineSearch, NumberSequence}

class SearchSpec extends UnitSpec{

  "baseline search" should "do 1 2 3 4 5" in {
    val s = NumberSequence(Seq(1, 2, 3, 4, 5, 6, 7, 8, 9), 1)
    val search = new BaselineSearch(0.5, 100, 30)
    val best = search.best(s)
    assert(best == Apply(Plus(), List(I(), Number(1))))
  }

  "baseline search" should "do 1 3 5, 7, 9" in {
    val s = NumberSequence(Seq(1, 3, 5, 7, 9), 1)
    val search = new BaselineSearch(0.5, 100, 30)
    val best = search.best(s)
    assert(best == Apply(Plus(), List(T(1), Number(2))))
  }
}
