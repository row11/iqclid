package org.allenai.iqclid

import org.allenai.iqclid.api.{ Apply, Evaluator, T, Tree }

trait Dataset {
  val sequences: Seq[DatasetSequence]

  def verifyDataset: Boolean = {
    sequences.forall {
      s =>
        val generated =
          Evaluator.evaluate(
            s.answer, s.numberSequence.baseCases(s.answer), s.numberSequence.seq.length + 1
          )
        generated == s.numberSequence.seq :+ s.nextTerm
    }
  }

}

case class DatasetSequence(numberSequence: NumberSequence, nextTerm: Int, answer: Tree)

/** Class representing a number sequence. numBaseCases represents the number of terms that need
  * to be fixed to generate the sequence. Ex: For Fibonacci, numBaseCases is 2.
  */
case class NumberSequence(seq: Seq[Int]) {
  val length = seq.length
  def baseCases(tree: Tree): Seq[Int] = {
    seq.take(tree.baseCount(tree))
  }

}
