package org.allenai.iqclid

import com.microsoft.z3.ArithExpr
import org.allenai.iqclid.api._
import org.allenai.iqclid.z3._

class Z3Solver {

  //  def getFunctionTree(depth: Int, nSeq: NumberSequence): Seq[Tree] = {
  //
  //    var treeIndex = 0
  //
  //    ThreadSafeDependencies.withZ3Module {
  //      z3Module =>
  //        val smt = new Z3Interface(z3Module, true)
  //        (nSeq.numBaseCases until nSeq.length).foreach {
  //          i =>
  //            treeIndex = 0
  //            smt.add(smt.mkEq(Seq(
  //              functionCSP(depth, nSeq.seq, i),
  //              smt.mkIntConst(nSeq.seq(i)))))
  //        }
  //
  //        def functionCSP(d: Int, seq: Seq[Int], seqIndex: Int): ArithExpr = {
  //          if (d == 0) {
  //            val isNumber = smt.mkBoolVar(s"isNumber_$treeIndex")
  //            val number = smt.mkIntVar(s"number_$treeIndex")
  //            val isIndex = smt.mkBoolVar(s"isIndex_$treeIndex")
  //            val index = smt.mkIntConst(seqIndex)
  //            val isT1 = smt.mkBoolVar(s"isT1_$treeIndex")
  //            val t1 = smt.mkIntConst(seq(seqIndex - 1))
  //            val isT2 = smt.mkBoolVar(s"isT2_$treeIndex")
  //            val t2 = smt.mkIntConst(seq(seqIndex - 2))
  //            val returnVal = smt.mkIntVar(s"r_${seqIndex}_$treeIndex")
  //            // Make sure at least one entity is chosen
  //            smt.add(smt.mkOr(Seq(isNumber, isIndex, isT1, isT2)))
  //            smt.mkImplies(isNumber, smt.mkEq(Seq(number, returnVal)))
  //            smt.mkImplies(isIndex, smt.mkEq(Seq(index, returnVal)))
  //            smt.mkImplies(isT1, smt.mkEq(Seq(t1, returnVal)))
  //            smt.mkImplies(isT2, smt.mkEq(Seq(t2, returnVal)))
  //            treeIndex += 1
  //            returnVal
  //          } else {
  //            val left = functionCSP(d - 1, seq, seqIndex)
  //            val right = functionCSP(d - 1, seq, seqIndex)
  //            val isPlus = smt.mkBoolVar(s"isPlus_$treeIndex")
  //            val isMinus = smt.mkBoolVar(s"isMinus_$treeIndex")
  //            val isTimes = smt.mkBoolVar(s"isTimes_$treeIndex")
  //            val isDivide = smt.mkBoolVar(s"isDiv_$treeIndex")
  //            // This is used to only use a subtree
  //            val isPickLeft = smt.mkBoolVar(s"isPickLeft_$treeIndex")
  //            val returnVal = smt.mkIntVar(s"r_${seqIndex}_$treeIndex")
  //            smt.add(smt.mkOr(Seq(isPlus, isMinus, isTimes, isDivide, isPickLeft)))
  //            smt.mkImplies(isPlus, smt.mkEq(Seq(smt.mkAdd(Seq(left, right)), returnVal)))
  //            smt.mkImplies(isMinus, smt.mkEq(Seq(smt.mkSub(left, right), returnVal)))
  //            smt.mkImplies(isTimes, smt.mkEq(Seq(smt.mkMul(Seq(left, right)), returnVal)))
  //            smt.mkImplies(isDivide, smt.mkEq(Seq(smt.mkDiv(left, right), returnVal)))
  //            smt.mkImplies(isPickLeft, smt.mkEq(Seq(left, returnVal)))
  //            treeIndex += 1
  //            returnVal
  //          }
  //        }
  //
  //        val status = smt.check()
  //        val precision = 16 // number of digits of precision for real values in the model
  //        status match {
  //          case SmtUnknown(reason) =>
  //            throw new Exception(s"SMT check() returned status UNKNOWN: $reason")
  //          case SmtUnsatisfiable =>
  //            println("No satisfying assignment found")
  //            Seq()
  //          case SmtSatisfiable =>
  //            treeIndex = 0
  //            Seq(modelToAst(smt.extractModel(precision), depth))
  //          case _ => throw new IllegalStateException("Unrecognized SMT status")
  //        }
  //    }
  //
  //    def modelToAst(model: Map[String, String], d: Int): Tree = {
  //      if (d == 0) {
  //        val leaf = if (model(s"isNumber_$treeIndex") == "true") {
  //          Number(model(s"number_$treeIndex").toInt)
  //        } else if (model(s"isIndex_$treeIndex") == "true") {
  //          I()
  //        } else if (model(s"isT1_$treeIndex") == "true") {
  //          T(1)
  //        } else if (model(s"isT2_$treeIndex") == "true") {
  //          T(2)
  //        } else {
  //          throw new RuntimeException(s"Something is wrong, no leaf chosen at tree index $treeIndex")
  //        }
  //        treeIndex += 1
  //        leaf
  //      } else {
  //        val left = modelToAst(model, d - 1)
  //        val right = modelToAst(model, d - 1)
  //        val node = if (model(s"isPlus_$treeIndex") == "true") {
  //          Apply(Plus(), Seq(left, right))
  //        } else if (model(s"isMinus_$treeIndex") == "true") {
  //          Apply(Minus(), Seq(left, right))
  //        } else if (model(s"isTimes_$treeIndex") == "true") {
  //          Apply(Times(), Seq(left, right))
  //        } else if (model(s"isDiv_$treeIndex") == "true") {
  //          Apply(Div(), Seq(left, right))
  //        } else {
  //          throw new RuntimeException(s"Something is wrong, no op chosen at tree index $treeIndex")
  //        }
  //        treeIndex +=1
  //        node
  //      }
  //    }
  //
  //  }

}
