package org.allenai.euclid.ilpsolver

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/** An abstract class for an ILP solver, with implements methods related to
  * computing gradients in the objective function. The gradients show the
  * direction of the biggest changes in the objective function, which can be
  * used for training a system using ILP as inference engine.
  *
  * @tparam V abstract type for ILP variables
  * @tparam C abstract type for ILP constrants
  */
abstract class IlpSolverWithObjGradients[V <: IlpVar, C <: IlpCons](calculateGradients: Boolean) extends IlpSolver[V, C] {
  /** a map connecting parameters, to their variables and scales */
  val parameterVarMap = new mutable.HashMap[String, ArrayBuffer[(V, Double)]]()

  /** returns the cumulative value of the variables related to a given parameter */
  def getParameterRelatedAggregatedValues(parameterName: String): Double = {
    val varScalePairs = parameterVarMap.getOrElse(
      parameterName,
      throw new Exception(s"Parameter $parameterName not found in the objective! This might be because you have not " +
        s"activated calculation of gradients in applications.conf")
    )
    val objectiveVarsSet = getAllVars
    val relatedVarsInObj = varScalePairs.filter { case (v, scale) => objectiveVarsSet.contains(v) }
    relatedVarsInObj.map { case (v, scale) => getSolVal(v) / scale }.sum
  }

  /** add variable to the environment and remember its name */
  def addParamWeight(x: V, relevantParameterScalePair: Option[Seq[(String, Double)]]): Unit = {
    relevantParameterScalePair match {
      case Some(paramNameScaleSeq) =>
        paramNameScaleSeq.foreach {
          case (paramName, scale) =>
            val variablesNamesPairOpt = parameterVarMap.get(paramName)
            variablesNamesPairOpt match {
              case Some(variablesNamesPair) => variablesNamesPair.+=:((x, scale))
              case None => parameterVarMap.put(paramName, ArrayBuffer((x, scale)))
            }
        }
      case None => // do nothing
    }
  }

  /** create a binary variable, and add relevant parameters */
  def createBinaryVar(name: String, obj: Double, relevantParameterScalePair: Option[Seq[(String, Double)]]): V = {
    val x = createBinaryVar(name, obj)
    if (calculateGradients) addParamWeight(x, relevantParameterScalePair)
    x
  }

  /** create a relaxed binary variable, that is, a continuous various with domain [0,1], and add relevant parameters */
  def createRelaxedBinaryVar(name: String, obj: Double, relevantParameterScalePair: Option[Seq[(String, Double)]]): V = {
    val x = createRelaxedBinaryVar(name, obj)
    if (calculateGradients) addParamWeight(x, relevantParameterScalePair)
    x
  }

  /** create an integer variable, and add relevant parameter */
  def createIntegerVar(name: String, lb: Double, ub: Double, objCoeff: Double, relevantParameterScalePair: Option[Seq[(String, Double)]]): V = {
    val x = createIntegerVar(name, lb, ub, objCoeff)
    if (calculateGradients) addParamWeight(x, relevantParameterScalePair)
    x
  }

  /** create a continuous variable and add relevant parameters */
  def createContinuousVar(name: String, lb: Double, ub: Double, objCoeff: Double, relevantParameterScalePair: Option[Seq[(String, Double)]]): V = {
    val x = createContinuousVar(name, lb, ub, objCoeff)
    if (calculateGradients) addParamWeight(x, relevantParameterScalePair)
    x
  }

}
