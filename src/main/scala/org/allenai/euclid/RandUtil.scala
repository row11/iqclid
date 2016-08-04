package org.allenai.euclid

object RandUtil {

  def randInt(range: Int): Int = {
    (Math.random()*range).toInt
  }

  def pickNRandom[T](things: Seq[T], n: Int): Seq[T] = {
    if (n < things.size) {
      things.sortBy(_ => scala.util.Random.nextInt()).take(n)
    } else {
      throw new RuntimeException(s"Trying to pick $n things from set of size ${things.size}")
    }
  }
}
