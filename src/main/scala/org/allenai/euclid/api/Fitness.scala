package org.allenai.euclid.api

import org.allenai.euclid.{NumberSequence, Utils}

trait Fitness {
  /** Total fitness of a tree, as a function of accuracy and complexity. Lower is better. */
  def eval(tree: Tree, s: NumberSequence): Double
}

class BaselineFitness(alpha: Double) extends Fitness {
  override def eval(tree: Tree, target: NumberSequence): Double = {
    alpha * accuracy(tree, target) + (1 - alpha) * complexity(tree)
  }

  /** Evaluate how well the tree approximates the target sequence. Lower is better. */
  def accuracy(tree: Tree, target: NumberSequence): Double = {
    try {
      val hypothesis = Evaluator.evaluate(tree, target.baseCases, target.length)
      // l1 distance
      Utils.l1Dist(target.seq, hypothesis)
    } catch {
      case e: BadTreeException =>
        Double.MaxValue
    }
  }

  /** Evaluate the complexity cost of a given tree. Lower is better. */
  def complexity(tree: Tree): Double = Tree.size(tree)
}


class AccuracyFirstFitness(alpha: Double) extends BaselineFitness(alpha) {

  override def eval(tree: Tree, target: NumberSequence): Double = {
    val acc = accuracy(tree, target)
    val comp = complexity(tree)
    if (acc == 0) {
      1.0 / (1.0 + Math.pow(Math.E, -comp))
    } else {
      alpha * acc + (1 - alpha) * comp
    }
  }

}
