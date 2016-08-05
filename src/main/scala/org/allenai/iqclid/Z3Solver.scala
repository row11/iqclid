package org.allenai.iqclid

import com.microsoft.z3.ArithExpr
import org.allenai.iqclid.api._
import org.allenai.iqclid.z3._

class Z3Solver extends Solver {

  def getFunctionTree(depth: Int, nSeq: NumberSequence, numBaseCases: Int): Seq[Tree] = {
    var upper = 100
    var lower = 0

    var result: Seq[Tree] = Seq()
    var counter = 0

    do {
      val newBound = (upper + lower) / 2
      counter += 1
      try {
        val tmp = getFunctionTreeInner(depth, nSeq, upper, numBaseCases)
        upper = newBound
        result = tmp
      } catch {
        case _: Throwable =>
          lower = newBound
      }
    } while ((Math.abs(upper - lower) >= 2) && counter < 100)


    result
  }

  def getFunctionTreeInner(depth: Int, nSeq: NumberSequence, upper: Int, numBaseCases: Int):
  Seq[Tree] = {

    var treeIndex = 0

    def modelToAst(model: Map[String, String], d: Int): Tree = {
//      println("MODEL: " + model)
      if (d == 0) {
        val leaf = if (model(s"isNumber_$treeIndex") == "true") {
          Number(model(s"number_$treeIndex").toInt)
        } else if (model(s"isIndex_$treeIndex") == "true") {
          I()
        } else if (model(s"isT1_$treeIndex") == "true") {
          T(1)
        } else if (model(s"isT2_$treeIndex") == "true") {
          T(2)
        } else {
          throw new RuntimeException(s"Something is wrong, no leaf chosen at tree index $treeIndex")
        }
        treeIndex += 1
        leaf
      } else {
        val left = modelToAst(model, d - 1)
        val right = modelToAst(model, d - 1)
        val node = if (model(s"isPlus_$treeIndex") == "true") {
          Apply(Plus(), Seq(left, right))
        } else if (model(s"isMinus_$treeIndex") == "true") {
          Apply(Minus(), Seq(left, right))
        } else if (model(s"isTimes_$treeIndex") == "true") {
          Apply(Times(), Seq(left, right))
        } else if (model(s"isDiv_$treeIndex") == "true") {
          Apply(Div(), Seq(left, right))
        } else if (model(s"isPickLeft_$treeIndex") == "true") {
          left
        } else {
          throw new RuntimeException(s"Something is wrong, no op chosen at tree index $treeIndex")
        }
        treeIndex +=1
        node
      }
    }

    val output = ThreadSafeDependencies.withZ3Module[Seq[Tree]] {
      z3Module =>
        val smt = new Z3Interface(z3Module, true)


        smt.add(smt.mkLe(Seq(smt.mkAdd((numBaseCases until nSeq.length).map {
          i =>
            treeIndex = 0
            smt.mkAbs(smt.mkSub(functionCSP(depth, i), smt.mkIntConst(nSeq.seq(i))))
          //            smt.add(smt.mkEq(Seq(
          //              functionCSP(depth, i),
          //              smt.mkIntConst(nSeq.seq(i)))))
        }), smt.mkIntConst(upper))))


        def functionCSP(d: Int, seqIndex: Int): ArithExpr = {
          if (d == 0) {
            val isNumber = smt.mkBoolVar(s"isNumber_$treeIndex")
            val number = smt.mkIntVar(s"number_$treeIndex")
            val isIndex = smt.mkBoolVar(s"isIndex_$treeIndex")
            val index = smt.mkIntConst(seqIndex)
            val isT1 = smt.mkBoolVar(s"isT1_$treeIndex")
            val t1 = smt.mkIntConst(nSeq.seq(seqIndex - 1))
            val returnVal = smt.mkIntVar(s"r_${seqIndex}_$treeIndex")
            // Make sure at least one entity is chosen
            smt.add(smt.mkImplies(isNumber, smt.mkEq(Seq(number, returnVal))))
            smt.add(smt.mkImplies(isIndex, smt.mkEq(Seq(index, returnVal))))
            smt.add(smt.mkImplies(isT1, smt.mkEq(Seq(t1, returnVal))))

            if (numBaseCases < 2) {
              smt.add(smt.mkOr(Seq(isNumber, isIndex, isT1)))
            } else {
              val isT2 = smt.mkBoolVar(s"isT2_$treeIndex")
              val t2 = smt.mkIntConst(nSeq.seq(seqIndex - 2))
              smt.add(smt.mkOr(Seq(isNumber, isIndex, isT1, isT2)))
              smt.add(smt.mkImplies(isT2, smt.mkEq(Seq(t2, returnVal))))
            }

            treeIndex += 1
            returnVal
          } else {
            val left = functionCSP(d - 1, seqIndex)
            val right = functionCSP(d - 1, seqIndex)
            val isPlus = smt.mkBoolVar(s"isPlus_$treeIndex")
            val isMinus = smt.mkBoolVar(s"isMinus_$treeIndex")
            val isTimes = smt.mkBoolVar(s"isTimes_$treeIndex")
//            val isDivide = smt.mkBoolVar(s"isDiv_$treeIndex")
            // This is used to only use a subtree
            val isPickLeft = smt.mkBoolVar(s"isPickLeft_$treeIndex")
            val returnVal = smt.mkIntVar(s"r_${seqIndex}_$treeIndex")
            smt.add(smt.mkOr(Seq(isPlus, isMinus, isTimes, isPickLeft)))
            smt.add(smt.mkImplies(isPlus, smt.mkEq(Seq(smt.mkAdd(Seq(left, right)), returnVal))))
            smt.add(smt.mkImplies(isMinus, smt.mkEq(Seq(smt.mkSub(left, right), returnVal))))
            smt.add(smt.mkImplies(isTimes, smt.mkEq(Seq(smt.mkMul(Seq(left, right)), returnVal))))
//            smt.add(smt.mkImplies(isDivide, smt.mkEq(Seq(smt.mkDiv(left, right), returnVal))))
            smt.add(smt.mkImplies(isPickLeft, smt.mkEq(Seq(left, returnVal))))
            treeIndex += 1
            returnVal
          }
        }

        val status = smt.check()
        val precision = 16 // number of digits of precision for real values in the model
        status match {
          case SmtUnknown(reason) =>
            throw new Exception(s"SMT check() returned status UNKNOWN: $reason")
          case SmtUnsatisfiable =>
//            println("No satisfying assignment found")
            throw new Exception(s"No satisfying assignment found")
          case SmtSatisfiable =>
            treeIndex = 0
            Seq(modelToAst(smt.extractModel(precision), depth))
          case _ => throw new IllegalStateException("Unrecognized SMT status")
        }
    }

    output
  }

  /** Generate proposal trees */
  override def solve(s: NumberSequence): Seq[Solution] = {
    val f = new AccuracyFirstFitness(0.5)
    getFunctionTree(2, s, 1).map(x => Solution(x, f.eval(x, s))) ++
    getFunctionTree(2, s, 2).map(x => Solution(x, f.eval(x, s)))
  }
}
