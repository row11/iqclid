package org.allenai.iqclid

import org.allenai.iqclid.RandUtil._
import org.allenai.iqclid.api.{ I, T, _ }

trait Search {

  val fitness: Fitness

  /** Given the current set of tree candidates, produce the next set. */
  def proposals(trees: Seq[Tree]): Seq[Tree]

  /** The actual search algorithm. */
  def search(target: NumberSequence): Seq[Tree]

  def best(target: NumberSequence): Tree = {
    search(target).minBy(tree => fitness.eval(tree, target))
  }
}

abstract class BeamSearch(maxSteps: Int, bestk: Int) extends Search {
  override def search(target: NumberSequence): Seq[Tree] = {
    (0 until maxSteps).foldLeft(Seq[Tree]()) {
      case (accTrees, step) =>
        println(s"STEP: $step")
        val p = proposals(accTrees)
        val candidates = (accTrees ++ proposals(accTrees)).distinct.sortBy {
          tree => fitness.eval(tree, target)
        }
        val result = candidates.take(bestk)
        result
    }
  }
}

class BaselineSearch(alpha: Double, maxSteps: Int, bestk: Int) extends BeamSearch(maxSteps, bestk) {
  def proposals(trees: Seq[Tree]): Seq[Tree] = {
    val mergeTrees = if (trees.size >= 2) {
      val args = pickNRandom(trees, 2)
      val op = randomOp
      Seq(Apply(op, args))
    } else {
      Seq()
    }
    val createLeaves = (0 until 5).map(Number(_)) ++ Seq(T(1), T(2), I())
    val replaceSubtrees = trees.map(replaceRandomNode)
    mergeTrees ++ createLeaves ++ replaceSubtrees

  }

  override val fitness = new AccuracyFirstFitness(alpha)
}

