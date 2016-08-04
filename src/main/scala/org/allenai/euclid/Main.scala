package org.allenai.euclid

object Main {

  def main(args: Array[String]): Unit = {

    val s = NumberSequence(Seq(1, 2, 4, 8, 16, 32), 2)

    val search = new BaselineSearch(0.5, 1000, 30)
    val best = search.best(s)
    val accuracy = search.accuracy(best, s)

    println(best)
    println(accuracy)
  }

}
