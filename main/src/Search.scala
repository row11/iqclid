import RandUtil._

trait Search {

  /** Evaluate how well the tree approximates the target sequence. Lower is better. */
  def accuracy(tree: Tree, target: NumberSequence): Double

  /** Evaluate the complexity cost of a given tree. Lower is better. */
  def complexity(tree: Tree): Double

  /** Total fitness of a tree, as a function of accuracy and complexity. Lower is better. */
  def fitness(accuracy: Double, complexity: Double): Double

  /** Given the current set of tree candidates, produce the next set. */
  def proposals(trees: Seq[Tree]): Seq[Tree]

  /** The actual search algorithm. */
  def search(target: NumberSequence): Seq[Tree]

  def best(target: NumberSequence): Tree = {
    search(target).minBy(tree => fitness(accuracy(tree, target), complexity(tree)))
  }

}

abstract class StochasticBeamSearch(maxSteps: Int, bestk: Int) extends Search {
  override def search(target: NumberSequence): Seq[Tree] = {
    (0 until maxSteps).foldLeft(Seq[Tree]()) {
      case (accTrees, step) =>
        println(s"STEP: $step")
        val candidates = (accTrees ++ proposals(accTrees)).sortBy {
          tree => fitness(accuracy(tree, target), complexity(tree))
        }
//        println("CANDS: " + candidates.map(tree => fitness(accuracy(tree, target), complexity(tree))))
//        println("BEST SO FAR: " + candidates.head)
      val result = candidates.take(bestk)
        result
    }
  }
}

class BaselineSearch(alpha: Double, maxSteps: Int, bestk: Int) extends StochasticBeamSearch(maxSteps, bestk) {
  def proposals(trees: Seq[Tree]): Seq[Tree] = {
    (0 until 100).foldLeft(Seq[Tree]()) {
      case (accTrees, _) =>
        val newTrees = randInt(3) match {
          case 0 =>
            // Merge two trees with a random op
            if (trees.size >= 2) {
              val args = pickNRandom(trees, 2)
              val op = randomOp
              Seq(Apply(op, args))
            } else {
              Seq()
            }
          case 1 =>
            Seq(replaceRandomNode(pickNRandom(trees, 1).head))
          case 2 =>
            Seq(randomLeaf)
        }
        accTrees ++ newTrees
    }
  }

  def accuracy(tree: Tree, target: NumberSequence): Double = {
    try {
      val hypothesis = Evaluator.evaluate(tree, target.baseCases, target.length)
      // l1 distance
      target.withoutBaseCases.zip(hypothesis).map {
        case (a,b) => Math.abs(a - b)
      }.sum

    } catch {
      case e: BadTreeException =>
        Double.MaxValue
    }
  }

  // Take the size of the tree as the complexity
  def complexity(tree: Tree): Double = Tree.size(tree)

  def fitness(accuracy: Double, complexity: Double): Double = {
    alpha * accuracy + (1 - alpha) * complexity
  }
}

