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
      //      val fitness = new SmallFirstFitness
      val solver = new GPSolver(fitness)
      //    val sequence = NumberSequence(Seq(1, 2, 3, 4, 5), 1)
      IqTest.easy.foreach {
        sequence =>
          val results = solver.solve(sequence.numberSequence)
          println(
            s"""actual: ${Evaluator.evaluate(results(0).tree, sequence.numberSequence.baseCases, sequence.numberSequence.length)}
               |actual: ${results.head}
               |expected: ${sequence.numberSequence.seq}
               |expected: ${sequence.answer}
             """.stripMargin
          )
      }
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
        val op = elems.head.toString match {
          case "+" =>
            Plus()
          case "-" =>
            Minus()
          case "*" =>
            Times()
          case "/" =>
            Div()
          case "mod" =>
            Mod()
          case "pow" =>
            Pow()
        }
        val args = elems.tail.map(toScalaRec)
        Apply(op, args)
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
