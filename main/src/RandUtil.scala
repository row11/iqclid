object RandUtil {

  def randInt(range: Int): Int = {
    (Math.random() * range).toInt
  }

  def pickNRandom[T](things: Seq[T], n: Int): Seq[T] = {
    if (n < things.size) {
      things.sortBy(_ => scala.util.Random.nextInt()).take(n)
    } else {
      throw new RuntimeException(s"Trying to pick $n things from set of size ${things.size}")
    }
  }

  def randomLeaf: Leaf = {
    randInt(3) match {
      case 0 =>
        // Choose a random number from 1 to 10
        Number(randInt(10))
      case 1 =>
        // Choose a random reference from the last two trees
        T(randInt(2) + 1)
      case 2 =>
        // The index of this term
        I()
    }
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

  //TODO finish this
  def replaceRandomNode(tree: Tree): Tree = {
    val toReplace = randInt(Tree.size(tree))
//    def replaceRandomLeafHelper(tree: Tree, treeIndex: Int): (Tree, Option[Int]) = {
//      if (treeIndex == -1) {
//        (tree, None)
//      } else {
//        tree match {
//          case Apply(op, args) =>
//            if (treeIndex == toReplace) {
//              (Apply(randomOp, args), None)
//            } else {
//              (op, Some(1))
//            }
//          case l: Leaf =>
//            if (treeIndex == toReplace) {
//              (randomLeaf, None)
//            } else {
//              (l, Some(1))
//            }
//        }
//      }
//    }
    ???
  }
}
