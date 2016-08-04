package org.allenai.euclid

object Main {

  def main(args: Array[String]): Unit = {

    val s = NumberSequence(Seq(2, 3, 4, 5, 6, 7, 8, 9, 10), 1)

    val search = new BaselineSearch(0.01, 100, 30)
    val best = search.best(s)

    println(best)
  }

}
