package org.allenai.euclid

object Main {

  def main(args: Array[String]): Unit = {

    val s = NumberSequence(Seq(1, 2, 4, 8, 16, 32), 1)

    val search = new BaselineSearch(0.1, 100, 30)
    val best = search.best(s)

    println(best)
  }

}
