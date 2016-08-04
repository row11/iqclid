object Tree {
  val listOfOps = Seq(
    Plus(), Times(), Minus(), Div(), Mod()
  )
}
sealed trait Tree
case class Apply(op: Leaf, args: Seq[Tree]) extends Tree
trait Leaf extends Tree
case class Number(value: Int) extends Leaf
trait Reference extends Leaf
/** Reference to the index. */
case class I() extends Reference
/** Reference to the ith element before. Ex: T(2) is the element before last. */
case class T(i: Int) extends Reference {
  require(i > 0, "No look ahead cheating!")
}
trait Op extends Leaf
case class Plus() extends Op
case class Times() extends Op
case class Minus() extends Op
case class Div() extends Op
case class Mod() extends Op

class BadTreeException extends RuntimeException

object Evaluator {

  def evaluate(tree: Tree, index: Int, nseq: NumberSequence): Double = {
    val seq = nseq.seq
    tree match {
      case Number(v) => v
      case I() => index
      case T(i) => seq(index - i)
      case Apply(op, args) =>
        (op, args) match {
          case (Plus(), Seq(el1, el2)) =>
            evaluate(el1, index, nseq) + evaluate(el2, index, nseq)
          case (Minus(), Seq(el1, el2)) =>
            evaluate(el1, index, nseq) - evaluate(el2, index, nseq)
          case (Times(), Seq(el1, el2)) =>
            evaluate(el1, index, nseq) * evaluate(el2, index, nseq)
          case (Div(), Seq(el1, el2)) =>
            val denom = evaluate(el2, index, nseq)
            if (denom == 0) {
              throw new BadTreeException()
            } else {
              evaluate(el1, index, nseq) / evaluate(el2, index, nseq)
            }
          case (Mod(), Seq(el1, el2)) =>
            evaluate(el1, index, nseq) % evaluate(el2, index, nseq)
          case _ => throw new BadTreeException()
        }
      case _ => throw new BadTreeException()
    }


  }

}