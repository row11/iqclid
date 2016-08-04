package org.allenai.euclid.api

object Tree {
  val listOfOps = Seq(
    Plus(), Times(), Minus(), Div(), Mod()
  )
  def size(tree: Tree): Int = {
    tree match {
      case Apply(_, args) => 1 + args.map(size).sum
      case l: Leaf => 1
    }
  }

}
sealed trait Tree
case class Apply(op: Op, args: Seq[Tree]) extends Tree
trait Leaf extends Tree
case class Number(value: Int) extends Leaf
trait Reference extends Leaf
/** Reference to the index. */
case class I() extends Reference
/** Reference to the ith element before. Ex: T(2) is the element before last. */
case class T(i: Int) extends Reference {
  require(i > 0, "No look ahead cheating!")
}
trait Op extends Tree
case class Plus() extends Op
case class Times() extends Op
case class Minus() extends Op
case class Div() extends Op
case class Mod() extends Op

class BadTreeException extends RuntimeException

object Evaluator {
  def evaluate(tree: Tree, baseCases: Seq[Int], numTerms: Int): Seq[Int] = {
    (baseCases.size until numTerms).foldLeft(baseCases) {
      case (seqSoFar, nextIndex) =>
        seqSoFar :+ evaluateInternal(tree, seqSoFar, nextIndex)
    }
  }

  private def evaluateInternal(tree: Tree, seqSoFar: Seq[Int], index: Int): Int = {
    tree match {
      case Number(v) => v
      case I() => index
      case T(i) => seqSoFar(index - i)
      case Apply(op, args) =>
        (op, args) match {
          case (Plus(), Seq(el1, el2)) =>
            evaluateInternal(el1, seqSoFar, index) + evaluateInternal(el2, seqSoFar, index)
          case (Minus(), Seq(el1, el2)) =>
            evaluateInternal(el1, seqSoFar, index) - evaluateInternal(el2, seqSoFar, index)
          case (Times(), Seq(el1, el2)) =>
            evaluateInternal(el1, seqSoFar, index) * evaluateInternal(el2, seqSoFar, index)
          case (Div(), Seq(el1, el2)) =>
            val denom = evaluateInternal(el2, seqSoFar, index)
            if (denom == 0) {
              throw new BadTreeException()
            } else {
              evaluateInternal(el1, seqSoFar, index) / evaluateInternal(el2, seqSoFar, index)
            }
          case (Mod(), Seq(el1, el2)) =>
            val denom = evaluateInternal(el2, seqSoFar, index)
            if (denom == 0) {
              throw new BadTreeException()
            } else {
              evaluateInternal(el1, seqSoFar, index) % evaluateInternal(el2, seqSoFar, index)
            }
          case _ => throw new BadTreeException()
        }
      case _ => throw new BadTreeException()
    }
  }
}