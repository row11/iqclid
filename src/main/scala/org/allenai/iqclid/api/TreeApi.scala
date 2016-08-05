package org.allenai.iqclid.api

object Tree {
  val listOfOps = Seq(
    Plus(), Times(), Minus(), Div(), Mod(), Pow()
  )
  def size(tree: Tree): Int = {
    tree match {
      case Apply(_, args) =>
        1 + args.map(size).sum
      case Number(n) =>
        n
      case l: Leaf =>
        1
    }
  }
}

sealed trait Tree {
  override def toString = {
    this match {
      case Apply(op, args) =>
        s"($op ${args.map(_.toString).mkString(" ")})"
      case Number(n) =>
        n.toString
      case I() =>
        "i"
      case T(i) =>
        s"t$i"
      case Plus() =>
        "+"
      case Minus() =>
        "-"
      case Times() =>
        "*"
      case Div() =>
        "/"
      case Mod() =>
        "mod"
      case Pow() =>
        "pow"
    }
  }
}
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
case class Pow() extends Op

class BadTreeException(msg: String) extends RuntimeException(msg)

object Evaluator {
  def evaluate(tree: Tree, baseCases: Seq[Int], numTerms: Int): Seq[Int] = {
    var results = baseCases.map(_.toDouble).toIndexedSeq
    (results.size until numTerms).foreach {
      i =>
        results = results :+ evaluateInternal(tree, results, i)
    }
    results.map(_.toInt)
  }

  private def evaluateInternal(tree: Tree, seqSoFar: IndexedSeq[Double], index: Int): Double = {
    tree match {
      case Number(v) => v
      case I() => index
      case T(i) =>
        if (index - i < 0) {
          throw new BadTreeException(s"Bad inded $tree")
        }
        seqSoFar(index - i)
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
              throw new BadTreeException(s"Bad denom $tree")
            } else {
              evaluateInternal(el1, seqSoFar, index) / evaluateInternal(el2, seqSoFar, index)
            }
          case (Mod(), Seq(el1, el2)) =>
            val denom = evaluateInternal(el2, seqSoFar, index)
            if (denom == 0) {
              throw new BadTreeException(s"Bad denom $tree")
            } else {
              evaluateInternal(el1, seqSoFar, index) % evaluateInternal(el2, seqSoFar, index)
            }
          case (Pow(), Seq(el1, el2)) =>
            Math.pow(
              evaluateInternal(el1, seqSoFar, index), evaluateInternal(el2, seqSoFar, index)
            )
          case _ =>
            throw new BadTreeException(s"Bad apply $tree")
        }
      case _ =>
        throw new BadTreeException(s"Bad tree $tree")
    }
  }
}