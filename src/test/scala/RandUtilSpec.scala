import org.allenai.common.testkit.UnitSpec
import org.allenai.euclid.RandUtil

class RandUtilSpec extends UnitSpec{

  "pickWithProb" should "work" in {

    val results = (0 until 1000).map { x =>
      RandUtil.pickWithProb(Seq((1, 0.1), (2, 0.2), (3, 0.3), (4, 0.4)))
    }

    println(s"1: ${results.count(_ == 1)}")
    println(s"2: ${results.count(_ == 2)}")
    println(s"3: ${results.count(_ == 3)}")
    println(s"4: ${results.count(_ == 4)}")

  }

}
