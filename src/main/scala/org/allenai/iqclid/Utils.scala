package org.allenai.iqclid

object Utils {

  def l1Dist(seq1: Seq[Int], seq2: Seq[Int]): Double = {
    seq1.zip(seq2).map {
      case (a,b) => Math.abs(a - b)
    }.sum
  }

  def l2Dist(seq1: Seq[Int], seq2: Seq[Int]): Double = {
    seq1.zip(seq2).map {
      case (a,b) => Math.pow(a - b, 2)
    }.sum
  }



}
