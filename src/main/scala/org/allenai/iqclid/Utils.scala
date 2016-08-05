package org.allenai.iqclid

object Utils {

  def l1Dist(seq1: Seq[Double], seq2: Seq[Double]): Double = {
    seq1.zip(seq2).map {
      case (a, b) => Math.abs(a - b)
    }.sum
  }

  def l2Dist(seq1: Seq[Double], seq2: Seq[Double]): Double = {
    seq1.zip(seq2).map {
      case (a, b) => math.sqrt((a - b) * (a - b))
    }.sum
  }

}
