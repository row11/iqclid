package org.allenai.iqclid

import org.allenai.iqclid.api.{ Evaluator, Solver, Tree }

case class Info(
    baseCases: Seq[Int],
    tree: Tree,
    fitness: Double,
    trainCount: Int,
    testCount: Int
) {
  lazy val seq = Evaluator.evaluate(tree, baseCases, trainCount + testCount)
  lazy val trainSeq = seq.take(trainCount)
  lazy val testSeq = seq.drop(trainCount)
  override def toString = {
    s"""$tree @ $fitness
       |${baseCases.mkString(" ")}.${trainSeq.drop(baseCases.size).mkString(" ")}:${testSeq.mkString(" ")}""".stripMargin
  }
}

case class Evaluation(
    actual: Info,
    expected: Info
) {
  lazy val trainScore = {
    Utils.l2Dist(actual.trainSeq.map(_.toDouble), expected.trainSeq.map(_.toDouble))
  }
  lazy val testScore = {
    Utils.l2Dist(actual.testSeq.map(_.toDouble), expected.testSeq.map(_.toDouble))
  }
  lazy val isTrainCorrect = trainScore == 0.0
  lazy val isCorrect = isTrainCorrect && (testScore == 0.0)
  override def toString = {
    s"""${if (isCorrect) "CORRECT" else if (isTrainCorrect) "FAIL_TEST" else "FAIL"}
       |actual:
       |$actual
       |expected:
       |$expected""".stripMargin
  }
}

class Evaluator {
  def eval(seqs: Seq[DatasetSequence], solver: Solver): Stream[Evaluation] = {
    seqs.toStream.map {
      seq =>
        val results = solver.solve(seq.numberSequence)
        val tree = results.head.tree
        val actual = Info(seq.numberSequence.baseCases(tree), tree, results.head.fitness, seq.numberSequence.length, 1)
        val answer = seq.answer
        val expected = Info(seq.numberSequence.baseCases(answer), answer, 0, seq.numberSequence.length, 1)
        Evaluation(actual, expected)
    }
  }

  def report(evals: Stream[Evaluation]) = {
    evals.foreach {
      eval =>
        println(eval)
        println
    }
    val score = evals.count(_.isCorrect).toDouble / evals.size * 100
    println(f"Score: $score%1.2f %%")
  }
}
