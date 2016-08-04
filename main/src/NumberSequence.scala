/** Class representing a number sequence. numBaseCases represents the number of terms that need
  * to be fixed to generate the sequence. Ex: For Fibonacci, numBaseCases is 2. */
case class NumberSequence(seq: Seq[Int], numBaseCases: Int = 1) {
  val withoutBaseCases = seq.drop(numBaseCases)
  val baseCases = seq.take(numBaseCases)
  val length = seq.length
}
