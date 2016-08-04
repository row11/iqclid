package org.allenai.euclid.ilpsolver

/** Various parameters for the SCIP ILP solver
  *
  * @param timeLimit overall time limit for SCIP in seconds once it starts solving the model
  * @param threads number of threads SCIP may use when solving the LPs (0: automatic)
  * @param logFile log file where SCIP output is stored for debugging purposes
  * @param messagehdlrQuiet whether to have SCIP's message handler be quiet or write to stdout
  * @param printVersion integer, indicating whether to print SCIP's version to log
  */
class ScipParams (
  val timeLimit: Double,
  val threads: Int,
  val logFile: String,
  val messagehdlrQuiet: Boolean,
  val printVersion: Int
) {}

/** An object to capture the default SCIP parameters */
object ScipParams {
  val Default = new ScipParams(
    timeLimit = 180d,
    threads = 1,
    logFile = "scip.log",
    messagehdlrQuiet = false,
    printVersion = 0
  )
}
