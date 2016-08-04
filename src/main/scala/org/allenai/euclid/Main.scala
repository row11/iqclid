package org.allenai.euclid

object Main {

  def main(args: Array[String]): Unit = {
    val s = NumberSequence(Seq(0, 3, 6, 9, 12), 2)
    val search = new BaselineSearch(0.999, 1000, 30)
    val best = search.best(s)
    val accuracy = search.accuracy(best, s)

    println(best)

    println(accuracy)
  }

}
