package org.allenai.euclid.ilpsolver

sealed trait TreeElement
case class TNode(value: ScipVar, operatorTypeSeq: Seq[ScipVar]) extends TreeElement
//case class TLeaf(value: ScipVar, leafType: Seq[ScipVar]) extends TreeElement
case class Tree(matrix: Array[Array[TreeElement]])

object ilpModel {
//  private def buildSimpleModel(scipSolver: ScipSolver, inputSequence: Seq[Int]): Seq[ScipVar] = {
//    val numberOfIndices = inputSequence.length
//
//  }

  def buildASingleLayer(scipSolver: ScipSolver, index: Int): Unit = {
    val triangleDepth = 2
    val rangeOfNumbers = 20

    val singleLayer = Array.ofDim[TNode](triangleDepth,triangleDepth)

    // internal nodes
    for{
      i <- 0 until triangleDepth - 1
      j <- 0 until triangleDepth - 1
    } {
      // operator type
      val invalid = scipSolver.createBinaryVar(s"invalidOperator-$i-$j-index=$index", 0d)
      scipSolver.addVar(invalid)
      val plus = scipSolver.createBinaryVar(s"plusOperator-$i-$j-index=$index", 0d)
      scipSolver.addVar(plus)
      val minus = scipSolver.createBinaryVar(s"minusOperator-$i-$j-index=$index", 0d)
      scipSolver.addVar(minus)

      // only one operator should be active
      scipSolver.addConsBasicLinear("onlyASingleOperator",
        Seq(plus, minus), Seq(1, 1), Some(1d), Some(1d))

      // value
      val nodeValue = scipSolver.createBinaryVar(s"nodeValue-$i-$j-index=$index", 0d)
      scipSolver.addVar(nodeValue)

      singleLayer(i)(j) = TNode(nodeValue, Seq(plus, minus))
    }

    // leaf nodes
    for{
      i <- 0 until triangleDepth - 1
      j = triangleDepth - 1 - i
    } {
      // type: constant or index
      val constant = scipSolver.createBinaryVar(s"leafType-$i-index=$index", 0d)
      scipSolver.addVar(constant)
      val idx = scipSolver.createBinaryVar(s"leafType-$i-index=$index", 0d)
      scipSolver.addVar(idx)
      val invalid = scipSolver.createBinaryVar(s"leafType-$i-index=$index", 0d)
      scipSolver.addVar(invalid)

      // only one operator should be active
      scipSolver.addConsBasicLinear("onlyASingleOperator",
        Seq(constant, idx, invalid), Seq(1, 1), Some(1d), Some(1d))

      // value
      val leafValue = scipSolver.createIntegerVar(s"leafValue-$i-index=$index", 0d, rangeOfNumbers-1, 0)
      scipSolver.addVar(leafValue)

      // if the type is index, the value should be the same as the index
      // i.e. value >= idx * typeIsIdx
      // i.e. value <= idx * typeIsIdx + (1-typeIsIdx) * range = (idx - range) * typeIsIdx + range
      scipSolver.addConsBasicLinear(s"indexValue-$i", Seq(leafValue, idx), Seq(1, -index), Some(0d), None)
      scipSolver.addConsBasicLinear(s"indexValue-$i", Seq(leafValue, idx), Seq(1, rangeOfNumbers-index), None, Some(0d))

      // if the type is constant, it should be the same as the value
      scipSolver.addConsBasicLinear(s"constantValue-$i", Seq(leafValue, constant), Seq(1, -index), Some(0d), None)
      scipSolver.addConsBasicLinear(s"constantValue-$i", Seq(leafValue, constant), Seq(1, rangeOfNumbers-index), None, Some(0d))

      singleLayer(i)(j) = TNode(leafValue, Seq(constant, idx, invalid))
    }

    // constraints between intermediate nodes
    // internal nodes
    for{
      i <- 0 until triangleDepth - 1
      j <- 0 until triangleDepth - 1
    } {
      val node = singleLayer(i)(j)
      val ch1 = singleLayer(i+1)(j)
      val ch2 = singleLayer(i)(j+1)

      // if the type of both kids are invalid, the current operator must be invalid as well
      // i.e. ch1Inv == 1 and ch2Inv == 1 ==> nodeInv == 1
      // or in math form: ch1Inv + ch2Inv - 1 <= nodeInv
      // if only one kid is invalid, the node should be valid
      // i.e.  nodeInv <= ch1Inv and nodeInv <= ch2Inv
      scipSolver.addConsBasicLinear(s"nodeInq1-$i-$j",
        Seq(ch1.operatorTypeSeq.head, ch2.operatorTypeSeq.head, node.operatorTypeSeq.head),
        Seq(1,1,-1), None, Some(1))
      scipSolver.addConsBasicLinear(s"nodeInq2-$i",
        Seq(node.operatorTypeSeq.head, ch1.operatorTypeSeq.head), Seq(1,-1), None, Some(1))
      scipSolver.addConsBasicLinear(s"nodeInq3-$i",
        Seq(node.operatorTypeSeq.head, ch2.operatorTypeSeq.head), Seq(1,-1), None, Some(1))

      // if the type of only one kid is invalid, use the value of the valid kid
      // singleChildValid = 1 iff ( ch1Inv == 1 && ch2Inv = 0 ) or ( ch1Inv == 0 && ch2Inv = 1 )
      // singleChildValid <=  c12Inv + ch2Inv
      // singleChildValid <=  2 - (ch1Inv + ch2Inv)
      val singleChildValid = scipSolver.createBinaryVar(s"singleChildValid-$i-j=$j-index=$index",1d)
      scipSolver.addVar(singleChildValid)
      scipSolver.addConsBasicLinear(s"singleChildValid-$i",
        Seq(singleChildValid, ch1.operatorTypeSeq.head, ch2.operatorTypeSeq.head), Seq(-1, 1, 1), Some(0), None)
      scipSolver.addConsBasicLinear(s"singleChildValid-$i",
        Seq(singleChildValid, ch1.operatorTypeSeq.head, ch2.operatorTypeSeq.head), Seq(1, 1, 1), None, Some(2))

      // if none of the kids are invalid, depending on the type of your operator, merge them
      // summation: nodeValue = ch1Value + ch1Value
      // i.e. in math form:
      // nodeValue * summation <= ch1Value + ch2Value
      // (ch1Value + ch1Value) * summation <= nodeValue
      scipSolver.addConsQuadratic(s"summation-u-i=$i-j=$j",
        Seq(ch1.value, ch2.value), Seq(1, 1), Seq(node.value), Seq(node.operatorTypeSeq(1)), Seq(-1), Some(0), None)
      scipSolver.addConsQuadratic(s"summation-l-i=$i-j=$j", Seq(node.value), Seq(1),
        Seq(ch1.value, ch2.value), Seq(node.operatorTypeSeq(1), node.operatorTypeSeq(1)), Seq(-1, -1), Some(0), None)

      // subtraction: nodeValue = ch1Value - ch2Value
      // nodeValue * summation <= ch1Value - ch2Value
      // (ch1Value - ch1Value) * summation <= nodeValue
      scipSolver.addConsQuadratic(s"subtraction-u-i=$i-j=$j",
        Seq(ch1.value, ch2.value), Seq(1, -1), Seq(node.value), Seq(node.operatorTypeSeq(2)), Seq(-1), Some(0), None)
      scipSolver.addConsQuadratic(s"subtraction-l-i=$i-j=$j", Seq(node.value), Seq(1),
        Seq(ch1.value, ch2.value), Seq(node.operatorTypeSeq(1), node.operatorTypeSeq(2)), Seq(-1, 1), Some(0), None)

      // multiplication: nodeValue = ch1Value * ch2Value
      // make sure: multiVarTmp = ch1 * ch2
      val multiVarTmp = scipSolver.createIntegerVar(s"binary-i-$i-j=$j-index=$index", 0d, rangeOfNumbers - 1, 0d)
      scipSolver.addVar(multiVarTmp)
      scipSolver.addConsQuadratic(s"multi-tmp-var-i=$i-j=$j", Seq(multiVarTmp), Seq(1.0),
        Seq(ch1.value), Seq(ch2.value), Seq(1), Some(0), Some(0))
      // nodeValue * multiplication <= ch1Value * ch2Value
      // (ch1Value * ch1Value) * multiplication <= nodeValue
      scipSolver.addConsQuadratic(s"multiplication-u-i=$i-j=$j", Seq.empty, Seq.empty,
        Seq(node.value, ch1.value), Seq(node.operatorTypeSeq(3), ch2.value), Seq(-1, 1), Some(0), None)
      // extra variable for multiplication of values
      scipSolver.addConsQuadratic(s"multiplication-l-i=$i-j=$j", Seq(node.value), Seq(1),
        Seq(multiVarTmp), Seq(node.operatorTypeSeq(2)), Seq(-1), Some(0), None)
    }

  }

  def main(args: Array[String]): Unit = {
    val scipParams = new ScipParams(10d, 1, "scip.log", messagehdlrQuiet = true, 0)
    val scipSolver = new ScipSolver("example", scipParams, calculateGradients = false)
    /*val varsOfInterest = buildSimpleModel(scipSolver)
    scipSolver.solve()

    // retrieve solution
    val primalBound = scipSolver.getPrimalbound
    val dualBound = scipSolver.getDualbound
    val solutionVals = scipSolver.getSolVals(varsOfInterest)

    // check solution
    println("primal bound: " + primalBound)
    println("dual bound: " + dualBound)
    println(solutionVals)*/
  }
}
