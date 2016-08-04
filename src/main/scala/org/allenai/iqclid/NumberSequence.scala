package org.allenai.iqclid

import org.allenai.iqclid.api.{Evaluator, Tree}

trait Dataset {
  val sequences: Seq[DatasetSequence]

  def verifyDataset: Boolean = {
    sequences.forall {
      s =>
        val generated =
          Evaluator.evaluate(s.answer, s.numberSequence.baseCases, s.numberSequence.seq.length + 1)
        generated == s.numberSequence.seq :+ s.nextTerm
    }
  }


}

case class DatasetSequence(numberSequence: NumberSequence, nextTerm: Int, answer: Tree)

/** Class representing a number sequence. numBaseCases represents the number of terms that need
  * to be fixed to generate the sequence. Ex: For Fibonacci, numBaseCases is 2. */
case class NumberSequence(seq: Seq[Int], numBaseCases: Int = 1) {
  val withoutBaseCases = seq.drop(numBaseCases)
  val baseCases = seq.take(numBaseCases)
  val length = seq.length
}
