package org.allenai.iqclid

import org.allenai.iqclid.api._

import scala.util.Random

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
      things.zip(things.map(_ => scala.util.Random.nextInt())).sortBy(_._2).map(_._1).take(n)
    } else {
      throw new RuntimeException(s"Trying to pick $n things from set of size ${things.size}")
    }
  }

  def randomLeaf: Leaf = {
    pickWithProb(((0 until 5).map(Number(_)) ++ Seq(T(1), T(2), I())).map(x => (x, 1.0)))
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
            val right = replaceRandomNodeHelper(arg2, index + left._2.get)
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

  def randomSimpleTree(): Tree = {
    // random operation
    val operations = Seq(Plus(), Times(), Minus(), Div(), Mod(), Pow() )
    val randomOp = operations(Random.nextInt(operations.size))

    // leaves
    val number = (0 until 10).map(Number(_))
    val leaves: Seq[Leaf] = number ++ Seq(I(), T(1), T(2), T(3))
    val leaf1 = leaves(Random.nextInt(leaves.length))
    val leaf2 = leaves(Random.nextInt(leaves.length))
    Apply(randomOp, Seq(leaf1, leaf2))
  }

  def addToTree(tree: Tree): Tree = {
    val r = scala.util.Random
    tree match {
      case Apply(op, Seq(arg1, arg2)) =>
        if(r.nextBoolean()) {
          Apply(op, Seq(addToTree(arg1), arg2))
        } else {
          Apply(op, Seq(arg1, addToTree(arg2)))
        }
      case l: Leaf =>
        randomSimpleTree()
    }
  }

  def randomTreeOfDepthk(k: Int) : Tree = {
    k match {
      case 0 => randomSimpleTree()
      case _ => addToTree(randomTreeOfDepthk(k-1))
    }
  }

  def main(args: Array[String]): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("data.txt"))
    (0 to 5000000).foreach { _ =>
      try {
        // depth of the tree
        val k = Random.nextInt(5)
        val t = addToTree(randomTreeOfDepthk(k))
        //println(t)
        //println(t.baseCount(t))
        val baseCases = (0 until t.baseCount(t)).map(_ => Random.nextInt(10) + 1)
        //println(baseCases)
        val generated = Evaluator.evaluate(t, baseCases, 10)
        //println(generated)
        if(generated.count(a => math.abs(a) > 10) == 0) {
          pw.write(s"${k+1}\t${generated.mkString(" ")}\t${t.prefix(t)}\t${t.baseCount(t)}\n")
        }
      } catch {
        case _: Throwable => //println("skipping ... ")
      }
    }
    pw.close
  }
}
