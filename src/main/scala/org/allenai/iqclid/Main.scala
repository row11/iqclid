package org.allenai.iqclid

import org.allenai.iqclid.api.{ AccuracyFirstFitness, Ensemble, MultipleRunSolver }
import org.allenai.iqclid.dataset.IqTest
import org.allenai.iqclid.z3.RonanSmtSolver

object Main {

  def main(args: Array[String]): Unit = {

    try {
      println("Hello world from Scala!")
      println(clj.Bridge.greet)
      //    clj.Bridge.tutorial(15, 15)
      val fitness = new AccuracyFirstFitness(0.5)
      //      val fitness = new SmallFirstFitness(0.1)
      //      val gpSolver = new MultipleRunSolver(new GPSolver(fitness), 3)
      //      val smtSolver = new Z3Solver
      //      val search = new MultipleRunSolver(new BaselineSearch(0.01, 100, 30), 3)
      //      val ensemble = new Ensemble(Seq(gpSolver, smtSolver, search))
      val ronan = new RonanSmtSolver
      val evaluator = new Evaluator

      evaluator.report(evaluator.eval(IqTest.euclid, ronan), "euclid", "Ronan")
      evaluator.report(evaluator.eval(IqTest.easy, ronan), "IQ easy", "Ronan")
      evaluator.report(evaluator.eval(IqTest.medium, ronan), "IQ medium", "Ronan")
      evaluator.report(evaluator.eval(IqTest.agiPaper, ronan), "agi", "Ronan")

      //      evaluator.report(evaluator.eval(IqTest.euclid, gpSolver), "euclid", "GPSolver")
      //      evaluator.report(evaluator.eval(IqTest.easy, gpSolver), "IQ easy", "GPSolver")
      //      evaluator.report(evaluator.eval(IqTest.medium, gpSolver), "IQ medium", "GPSolver")
      //      evaluator.report(evaluator.eval(IqTest.agiPaper, gpSolver), "agi", "GPSolver")
      //
      //      evaluator.report(evaluator.eval(IqTest.euclid, smtSolver), "euclid", "SMT")
      //      evaluator.report(evaluator.eval(IqTest.easy, smtSolver), "IQ easy", "SMT")
      //      evaluator.report(evaluator.eval(IqTest.medium, smtSolver), "IQ medium", "SMT")
      //      evaluator.report(evaluator.eval(IqTest.agiPaper, smtSolver), "agi", "SMT")
      //
      //      evaluator.report(evaluator.eval(IqTest.euclid, search), "euclid", "Local Search")
      //      evaluator.report(evaluator.eval(IqTest.easy, search), "IQ easy", "Local Search")
      //      evaluator.report(evaluator.eval(IqTest.medium, search), "IQ medium", "Local Search")
      //      evaluator.report(evaluator.eval(IqTest.agiPaper, search), "agi", "Local Search")
      //
      //      evaluator.report(evaluator.eval(IqTest.euclid, ensemble), "euclid", "Ensemble")
      //      evaluator.report(evaluator.eval(IqTest.easy, ensemble), "IQ easy", "Ensemble")
      //      evaluator.report(evaluator.eval(IqTest.medium, ensemble), "IQ medium", "Ensemble")
      //      evaluator.report(evaluator.eval(IqTest.agiPaper, ensemble), "agi", "Ensemble")

    } catch {
      case ex: Exception =>
        println(ex)
    } finally {
      sys.exit(0)
    }

  }

}
