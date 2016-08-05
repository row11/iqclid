package org.allenai.iqclid

import org.allenai.iqclid.api.{AccuracyFirstFitness, Ensemble, Evaluator, MultipleRunSolver}

object Demo {


  def main(args: Array[String]): Unit = {
    val fitness = new AccuracyFirstFitness(0.5)
    val gpSolver = new MultipleRunSolver(new GPSolver(fitness), 10)
    val smtSolver = new Z3Solver
    val search = new BaselineSearch(0.01, 100, 30)
    val ensemble = new Ensemble(Seq(gpSolver, smtSolver, search))

    val numberSequence = new NumberSequence(Seq(7, 3, 84))

    val tree = ensemble.solve(numberSequence).head.tree
    println(tree)
    println(Evaluator.evaluate(tree, numberSequence.baseCases(tree), 10))
    System.exit(0)
  }

}
