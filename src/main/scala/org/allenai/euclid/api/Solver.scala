package org.allenai.euclid.api

import org.allenai.euclid.NumberSequence

case class Solution(tree: Tree, fitness: Double)

trait Solver {
  /** Generate proposal trees */
  def solve(s: NumberSequence): Seq[Solution]
}
