package org.allenai.iqclid.dataset

import org.allenai.iqclid.{ DatasetSequence, _ }
import org.allenai.iqclid.api._

object IqTest extends Dataset {

  val sequences = easy

  val easy = Seq(
    DatasetSequence(
      NumberSequence(Seq(2, 4, 9, 11, 16)),
      18,
      Apply(Plus(), Seq(
        T(1),
        Apply(Plus(), Seq(
          Apply(Times(), Seq(
            Apply(Mod(), Seq(
              Apply(Plus(), Seq(I(), Number(1))), Number(2)
            )),
            Number(3)
          )),
          Number(2)
        ))
      ))
    ),

    DatasetSequence(
      NumberSequence(Seq(30, 28, 25, 21, 16)),
      10,
      Apply(Minus(), Seq(
        T(1), Apply(Plus(), Seq(I(), Number(1)))
      ))
    ),

    DatasetSequence(
      NumberSequence(Seq(-972, 324, -108, 36, -12)),
      4,
      Apply(Div(), Seq(
        T(1), Apply(Minus(), Seq(Number(0), Number(3)))
      ))
    ),

    //    Commented out because we cannot handle decimals
    //    DatasetSequence(
    //      NumberSequence(Seq(0.3, 0.5, 0.8, 1.2, 1.7),
    //        2.3,
    //      Apply(Plus(), Seq(
    //        T(1), Apply(Div(), Seq(Apply(Plus(), Seq(I(), Number(1))), Number(10)))))),

    DatasetSequence(
      NumberSequence(Seq(16, 22, 34, 52, 76)),
      106,
      Apply(Plus(), Seq(
        T(1), Apply(Times(), Seq(Number(6), I()))
      ))
    ),

    DatasetSequence(
      NumberSequence(Seq(123, 135, 148, 160, 173)),
      185,
      Apply(Plus(), Seq(
        T(1), Apply(Plus(), Seq(Number(12), Apply(Mod(), Seq(
          Apply(Plus(), Seq(I(), Number(1))),
          Number(2)
        ))))
      ))
    ),

    DatasetSequence(
      NumberSequence(Seq(4, 5, 7, 11, 19)),
      35,
      Apply(Plus(), Seq(
        T(1), Apply(Pow(), Seq(
          Number(2), Apply(Minus(), Seq(
            I(), Number(1)
          ))
        ))
      ))
    )
  )

  val medium = Seq(

    DatasetSequence(
      NumberSequence(Seq(-2, 5, -4, 3, 6)),
      1,
      Apply(Minus(), Seq(
        T(2), Number(2)
      ))
    ),

    DatasetSequence(
      NumberSequence(Seq(1, 4, 9, 16, 25)),
      36,
      Apply(Plus(), Seq(
        T(1), Apply(Plus(), Seq(Apply(Times(), Seq(
          Number(2), I()
        )), Number(1)))
      ))
    )
  )

}
