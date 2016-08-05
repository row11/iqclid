package org.allenai.iqclid

import java.util
import java.util.concurrent.Callable

import org.allenai.iqclid.api._

object GPSolver {
  def main(args: Array[String]): Unit = {
    println("Hello world from Scala!")
    println(clj.Bridge.greet)
    clj.Bridge.tutorial(15, 15)
    val fitness = new BaselineFitness(0.5)
    val solver = new GPSolver(fitness)
    val sequence = NumberSequence(Seq(1, 2, 3, 4, 5), 1)
    val results = solver.solve(sequence)
    println(s"""$sequence\n=>$results""")
    sys.exit(0)
  }

  def toScala(tree: Object): Tree = {
    println(tree)
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
        }
        val args = elems.tail.map(toScalaRec)
        Apply(op, args)
      case l: java.lang.Long =>
        Number(l.intValue())
      case s: clojure.lang.Symbol =>
        s.toString match {
          case "i" =>
            I()
          case "p1" =>
            T(1)
          case "p2" =>
            T(2)
          case "p3" =>
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
        fitness.eval(tree, s)
      }
    }
    clj.Bridge.search(15, 15, box, callable)
    val result = GPSolver.toScala(box.get(0))
    val score = box.get(1).asInstanceOf[Double]
    Seq(Solution(result, score))
  }
}
