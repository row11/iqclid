package org.allenai.euclid

import org.allenai.euclid.RandUtil._
import org.allenai.euclid.api.{I, T, _}

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

abstract class StochasticBeamSearch(maxSteps: Int, bestk: Int) extends Search {
  override def search(target: NumberSequence): Seq[Tree] = {
    (0 until maxSteps).foldLeft(Seq[Tree]()) {
      case (accTrees, step) =>
        println(s"STEP: $step")
        val candidates = (accTrees ++ proposals(accTrees)).distinct.sortBy {
          tree => fitness.eval(tree, target)
        }
        println(candidates.map(tree => fitness.eval(tree, target)))
        val result = candidates.take(bestk)
        result
    }
  }
}

class BaselineSearch(alpha: Double, maxSteps: Int, bestk: Int) extends StochasticBeamSearch(maxSteps, bestk) {
  def proposals(trees: Seq[Tree]): Seq[Tree] = {
    (0 until 100).foldLeft(Seq[Tree]()) {
      case (accTrees, _) =>
        val mergeTrees = if (trees.size >= 2) {
          val args = pickNRandom(trees, 2)
          val op = randomOp
          Seq(Apply(op, args))
        } else {
          Seq()
        }
        val createLeaves = (0 until 10).map(Number(_))  ++ Seq(T(1), I())
        val replaceSubtrees = trees.map(replaceRandomNode)
        val mutations = mergeTrees ++ createLeaves ++ replaceSubtrees
        accTrees :+ RandUtil.pickWithProb(mutations.map(x => (x, 1.0)))
    }
  }

  override val fitness = new BaselineFitness(alpha)
}

