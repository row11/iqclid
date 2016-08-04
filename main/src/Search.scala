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
      candidates.take(bestk)
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
              val op = pickNRandom(Tree.listOfOps, 1).head
              Seq(Apply(op, args))
            } else {
              Seq()
            }
          case 1 =>
            // Choose a random number from 1 to 10
            Seq(Number(randInt(10)))
          case 2 =>
            // Choose a random reference from the last two trees
            Seq(T(randInt(2) + 1))
          case 3 =>
            // The index of this term
            Seq(I())
        }
        accTrees ++ newTrees
    }
  }

  def accuracy(tree: Tree, target: NumberSequence): Double = {
    try {
      val hypothesis = (target.numBaseCases until target.seq.length).map {
        ind =>
          Evaluator.evaluate(tree, ind, target)
      }

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
  def complexity(tree: Tree): Double = {
    tree match {
      case Apply(_, args) => 1 + args.map(complexity).sum
      case l: Leaf => 1
    }
  }

  def fitness(accuracy: Double, complexity: Double): Double = {
    alpha * accuracy + (1 - alpha) * complexity
  }
}

