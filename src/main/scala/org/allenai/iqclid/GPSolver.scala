package org.allenai.iqclid

import java.util
import java.util.concurrent.Callable

import org.allenai.iqclid.api._
import org.allenai.iqclid.dataset.IqTest

object GPSolver {
  def main(args: Array[String]): Unit = {
    try {
      println("Hello world from Scala!")
      println(clj.Bridge.greet)
      //    clj.Bridge.tutorial(15, 15)
      val fitness = new AccuracyFirstFitness(0.5)
      //      val fitness = new SmallFirstFitness(0.1)
      val solver = new GPSolver(fitness)
      //    val sequence = NumberSequence(Seq(1, 2, 3, 4, 5), 1)
      val evaluator = new Evaluator
      val evals = evaluator.eval(IqTest.easy ++ IqTest.medium, solver)
      evaluator.report(evals, "Easy and Med", "GP SOLVER")
    } catch {
      case ex: Exception =>
        println(ex)
    } finally {
      sys.exit(0)
    }
  }

  def toScala(tree: Object): Tree = {
    toScalaRec(tree)
  }

  def toScalaRec(tree: Object): Tree = {
    import scala.collection.JavaConverters._
    tree match {
      case list: util.ArrayList[Object] =>
        val elems = list.asScala
        val args = elems.tail.map(toScalaRec)
        elems.head.toString match {
          case "+" =>
            require(args.size == 2)
            Apply(Plus(), args)
          case "-" =>
            require(args.size == 2)
            Apply(Minus(), args)
          case "*" =>
            require(args.size == 2)
            Apply(Times(), args)
          case "/" =>
            require(args.size == 2)
            Apply(Div(), args)
          case "mod" =>
            require(args.size == 2)
            Apply(Mod(), args)
          case "pow" =>
            require(args.size == 2)
            Apply(Pow(), args)
        }
      case l: java.lang.Long =>
        Number(l.intValue())
      case s: clojure.lang.Symbol =>
        s.toString match {
          case "i" =>
            I()
          case "t1" =>
            T(1)
          case "t2" =>
            T(2)
          case "t3" =>
            T(3)
        }
    }
  }
}

class GPSolver(fitness: Fitness) extends Solver {
  override def solve(s: NumberSequence): Seq[Solution] = {
    val box = new util.ArrayList[Object]()
    box.add("dummy")
    box.add("dummy")
    val callable = new Callable[Double] {
      override def call(): Double = {
        val tree = GPSolver.toScala(box.get(0))
        val f = fitness.eval(tree, s)
        if (f < 0.0) {
          //          throw new Exception("Bad fitness.")
          Double.MaxValue
        } else {
          f
        }
      }
    }
    clj.Bridge.search(15, 15, box, callable)
    val result = GPSolver.toScala(box.get(0))
    val score = box.get(1).asInstanceOf[Double]
    Seq(Solution(result, score))
  }
}
