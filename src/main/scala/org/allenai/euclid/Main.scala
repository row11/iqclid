package org.allenai.euclid

object Main {

  def main(args: Array[String]): Unit = {

    val s = NumberSequence(Seq(1, 2, 4, 8, 16), 2)

    val search = new BaselineSearch(0.99, 20, 30)

    val t = Apply(Times(), Seq(T(1), Number(2)))

    println(search.best(s))
  }

}
