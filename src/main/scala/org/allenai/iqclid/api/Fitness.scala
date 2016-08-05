package org.allenai.iqclid.api

import org.allenai.iqclid.{ NumberSequence, Utils }

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
      val hypothesis = Evaluator.evaluate(tree, target.baseCases(tree), target.length)
      Utils.l2Dist(target.seq.map(_.toDouble), hypothesis.map(_.toDouble))
    } catch {
      case e: BadTreeException =>
        Double.MaxValue
    }
  }

  /** Evaluate the complexity cost of a given tree. Lower is better. */
  def complexity(tree: Tree): Double = {

    


    Tree.size(tree)
  }
}

class AccuracyFirstFitness(alpha: Double) extends BaselineFitness(alpha) {

  override def eval(tree: Tree, target: NumberSequence): Double = {
    val acc = accuracy(tree, target)
    val comp = complexity(tree)
    if (acc == 0) {
      2 * (1.0 / (1.0 + Math.pow(Math.E, -comp)) - 0.5)
    } else {
      1 + alpha * acc + (1 - alpha) * comp
    }
  }

}

class SmallFirstFitness(alpha: Double) extends BaselineFitness(alpha) {
  override def eval(tree: Tree, target: NumberSequence): Double = {
    val acc = accuracy(tree, target)
    val comp = complexity(tree)
    if (Tree.size(tree) > 20) {
      Double.MaxValue
    } else {
      1 + alpha * acc + (1 - alpha) * comp
    }
  }

}
