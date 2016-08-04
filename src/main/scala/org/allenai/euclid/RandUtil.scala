package org.allenai.euclid

import org.allenai.euclid.api._

object RandUtil {

  def randInt(range: Int): Int = {
    (Math.random() * range).toInt
  }

  def pickWithProb[T](setWithWeights: Seq[(T, Double)]): T = {
    val sum = setWithWeights.map(_._2).sum
    val normalized = setWithWeights.map {
      case (t, w) => (t, w / sum)
    }
    val normalizedCDF = normalized.dropRight(1).foldRight(Seq[(T, Double)](normalized.last)) {
      case (a, accVec) =>
        (a._1, a._2 + accVec.head._2) +: accVec
    }.reverse

    normalizedCDF.find {
      case (t, p) =>
        Math.random() < p
    }.get._1
  }

  def pickNRandom[T](things: Seq[T], n: Int): Seq[T] = {
    if (n < things.size) {
      things.sortBy(_ => scala.util.Random.nextInt()).take(n)
    } else {
      throw new RuntimeException(s"Trying to pick $n things from set of size ${things.size}")
    }
  }

  def randomLeaf: Leaf = {
    pickWithProb(((0 until 5).map(Number(_)) ++ Seq(T(1), I())).map(x => (x, 1.0)))
  }

  def coinFlip(p: Double = 0.5): Boolean = {
    if (Math.random() < p) {
      true
    } else {
      false
    }
  }

  def randomOp: Op = {
    pickNRandom(Tree.listOfOps, 1).head
  }

  // Don't try to read this
  def replaceRandomNode(tree: Tree): Tree = {
    val toReplace = randInt(Tree.size(tree))
    def replaceRandomNodeHelper(tree: Tree, index: Int): (Tree, Option[Int]) = {
      tree match {
        case Apply(op, Seq(arg1, arg2)) =>
          val left = replaceRandomNodeHelper(arg1, index)
          if (left._2.isEmpty) {
            // We've replaced something in the left
            (Apply(op, Seq(left._1, arg2)), None)
          } else {
            val right = replaceRandomNodeHelper(arg1, index + left._2.get)
            if (right._2.isEmpty) {
              // We've replaced something in the right
              (Apply(op, Seq(arg1, right._1)), None)
            } else {
              if ((left._2.get + right._2.get) == toReplace) {
                (Apply(randomOp, Seq(arg1, arg2)), None)
              } else {
                (Apply(op, Seq(arg1, arg2)), Some(left._2.get + right._2.get + 1))
              }
            }
          }
        case l: Leaf =>
          if (index == toReplace) {
            (randomLeaf, None)
          } else {
            (l, Some(1))
          }
      }
    }
    replaceRandomNodeHelper(tree, 0)._1
  }
}
