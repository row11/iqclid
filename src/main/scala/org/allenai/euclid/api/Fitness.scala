package org.allenai.euclid.api

import org.allenai.euclid.NumberSequence

trait Fitness {
  /** Total fitness of a tree, as a function of accuracy and complexity. Lower is better. */
  def eval(tree: Tree, s: NumberSequence): Double
}

class BaselineFitness(alpha: Double) extends Fitness {
  override def eval(tree: Tree, s: NumberSequence): Double = {
    alpha * accuracy(tree, s) + (1 - alpha) * complexity(tree)
  }

  def accuracy(tree: Tree, target: NumberSequence): Double = {
    try {
      val hypothesis = Evaluator.evaluate(tree, target.baseCases, target.length)
      // l1 distance
      target.withoutBaseCases.zip(hypothesis).map {
        case (a, b) => Math.abs(a - b)
      }.sum

    } catch {
      case e: BadTreeException =>
        Double.MaxValue
    }
  }

  // Take the size of the tree as the complexity
  def complexity(tree: Tree): Double = Tree.size(tree)
}
