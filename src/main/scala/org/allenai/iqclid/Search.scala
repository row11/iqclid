package org.allenai.iqclid

import org.allenai.iqclid.RandUtil._
import org.allenai.iqclid.api.{I, T, _}

trait Search extends Solver {

  val fitness: Fitness

  /** Given the current set of tree candidates, produce the next set. */
  def proposals(trees: Seq[Tree]): Seq[Tree]

  def best(target: NumberSequence): Tree = {
    solve(target).minBy(sol => sol.fitness).tree
  }
}

abstract class BeamSearch(maxSteps: Int, bestk: Int) extends Search {
  override def solve(target: NumberSequence): Seq[Solution] = {
    (0 until maxSteps).foldLeft(Seq[Solution]()) {
      case (accSols, step) =>
        println(s"STEP: $step")
        val accTrees = accSols.map(_.tree)
        val p = proposals(accTrees)
        val candidates = (accTrees ++ proposals(accTrees))
            .distinct
            .map(x => Solution(x, fitness.eval(x, target)))
            .sortBy(_.fitness)
        candidates.take(bestk)
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
    val createLeaves = (0 until 5).map(Number(_))  ++ Seq(T(1), T(2), I())
    val replaceSubtrees = trees.map(replaceRandomNode)
    mergeTrees ++ createLeaves ++ replaceSubtrees

  }

  override val fitness = new AccuracyFirstFitness(alpha)
}

