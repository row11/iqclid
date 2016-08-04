package org.allenai.euclid

import java.util
import java.util.concurrent.Callable

import org.allenai.euclid.api._

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
    import scala.collection.JavaConverters._
    tree match {
      case list: util.ArrayList[Object] =>
        list.asScala.foreach {
          e =>
            toScala(e)
        }
      case s: String =>
        ()
      case l: java.lang.Long =>
        ()
      case s: clojure.lang.Symbol =>
        ()
    }
    Number(123)
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
