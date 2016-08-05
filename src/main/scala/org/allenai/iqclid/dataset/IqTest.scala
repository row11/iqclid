package org.allenai.iqclid.dataset

import org.allenai.iqclid.{DatasetSequence, _}
import org.allenai.iqclid.api._

object IqTest extends Dataset {

  override lazy val sequences = agiPaper

  val easy = Seq(
    DatasetSequence(
      NumberSequence(Seq(2, 4, 9, 11, 16)),
      18,
      Apply(Plus(), Seq(
        T(1),
        Apply(Plus(), Seq(
          Apply(Times(), Seq(
            Apply(Mod(), Seq(
              Apply(Plus(), Seq(I(), Number(1))), Number(2))),
            Number(3))),
          Number(2))))), 1),

    DatasetSequence(
      NumberSequence(Seq(30, 28, 25, 21, 16)),
      10,
      Apply(Minus(), Seq(
        T(1), Apply(Plus(), Seq(I(), Number(1))))), 1),

    DatasetSequence(
      NumberSequence(Seq(-972, 324, -108, 36, -12)),
      4,
      Apply(Div(), Seq(
        T(1), Apply(Minus(), Seq(Number(0), Number(3))))), 1),

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
        T(1), Apply(Times(), Seq(Number(6), I())))), 1),

    DatasetSequence(
      NumberSequence(Seq(123, 135, 148, 160, 173)),
      185,
      Apply(Plus(), Seq(
        T(1), Apply(Plus(), Seq(Number(12), Apply(Mod(), Seq(Apply(Plus(), Seq(I(), Number(1))),
          Number(2))))))), 1),

    DatasetSequence(
      NumberSequence(Seq(4, 5, 7, 11, 19)),
      35,
      Apply(Plus(), Seq(
        T(1), Apply(Pow(), Seq(
          Number(2), Apply(Minus(), Seq(
            I(), Number(1))))))), 1))

  val medium = Seq(
    DatasetSequence(
      NumberSequence(Seq(-2, 5, -4, 3, -6)),
      1,
      Apply(Minus(), Seq(
        T(2), Number(2))), 2),

    DatasetSequence(
      NumberSequence(Seq(1, 4, 9, 16, 25)),
      36,
      Apply(Plus(), Seq(
        T(1), Apply(Plus(), Seq(Apply(Times(), Seq(
          Number(2), I())), Number(1))))), 1),


    // TODO(row11) fix it later
    //    DatasetSequence(
    //      NumberSequence(Seq(75, 15, 25, 5, 15), 1),
    //      3,
    //      Apply(Plus(),
    //        Seq(
    //          Apply(Div(), Seq(
    //            Apply(Times(),
    //              Seq(
    //                Apply(Mod(), Seq(I(), Number(2))),
    //                T(1)
    //              )
    //            ),
    //            Number(5))
    //          ),
    //      Apply(Times(),
    //        Seq(
    //        Apply(Mod(), Seq(
    //          Apply(Plus(), Seq(
    //            I(), Number(1))),
    //          Number(2))),
    //          Number(10))
    //      )))),

    DatasetSequence(
      NumberSequence(Seq(1, 2, 6, 24, 120)),
      720,
      Apply(Times(), Seq(
        T(1),
        Apply(Plus(), Seq(I(), Number(1)))
      )), 1),

    //    Too annoying to encode
    //    DatasetSequence(
    //      NumberSequence(Seq(183, 305, 527, 749, 961), 1),
    //      720,
    //      ???)

    DatasetSequence(
      NumberSequence(Seq(16, 22, 34, 58, 106)),
      202,
      Apply(Minus(), Seq(
        Apply(Times(), Seq(T(1), Number(3))),
        Apply(Times(), Seq(T(2), Number(2)))
      )), 2),

    DatasetSequence(
      NumberSequence(Seq(17, 40, 61, 80, 97)),
      112,
      Apply(Minus(), Seq(
        Apply(Minus(), Seq(
          Apply(Times(), Seq(T(1), Number(2))),
          T(2)
        )),
        Number(2))), 2),

    DatasetSequence(
      NumberSequence(Seq(55, 34, 21, 13, 8)),
      5,
      Apply(Minus(), Seq(
        T(2),
        T(1)
      )), 2),

    DatasetSequence(
      NumberSequence(Seq(259, 131, 67, 35, 19)),
      11,
      Apply(Div(), Seq(
        Apply(Plus(), Seq(T(1), Number(3))),
        Number(2)
      )), 1),


    DatasetSequence(
      NumberSequence(Seq(93, 74, 57, 42, 29)),
      18,
      Apply(Plus(),
        Seq(
          Apply(Minus(), Seq(Apply(Times(), Seq(Number(2), T(1))), T(2))),
          Number(2)
        )), 2),

    DatasetSequence(
      NumberSequence(Seq(7, 21, 14, 42, 28)),
      84,
      Apply(Times(), Seq(
        T(2),
        Number(2)
      )), 2),

    DatasetSequence(
      NumberSequence(Seq(2, -12, -32, -58, -90)),
      -128,
      Apply(Plus(),
        Seq(
          Apply(Minus(), Seq(Apply(Times(), Seq(Number(2), T(1))), T(2))),
          Number(-6)
        )), 2),

    DatasetSequence(
      NumberSequence(Seq(0, 9, 36, 81, 144)),
      225,
      Apply(Pow(), Seq(
        Apply(Times(), Seq(Number(3), I())),
        Number(2)
      )), 2)
  )

  val eulid = Seq(
    DatasetSequence(
      NumberSequence(Seq(7, 14, 28, 56, 112)),
      224,
      Apply(Times(), Seq(T(1), Number(2))), 1),

    DatasetSequence(
      NumberSequence(Seq(-2, -1, 0, 1, 2, 1, 0, -1, -2, -1, 0, 1)),
      2,
      Apply(Plus(), Seq(T(8), Number(0))), 8),

    DatasetSequence(
      NumberSequence(Seq(1, 2, 3, 1, 2, 3, 1, 2)),
      3,
      Apply(Plus(), Seq(
        Number(1),
        Apply( Mod(), Seq(I(), Number(3)))
      )), 0),

    DatasetSequence(
      NumberSequence(Seq(1, 2, 3, 1, 2, 3, 1, 2)),
      3,
      Apply(Plus(), Seq(
        Number(1),
        Apply( Mod(), Seq(I(), Number(3)))
      )), 0),

    DatasetSequence(
      NumberSequence(Seq(-1, 0, 1, -1, 0, 1)),
      -1,
      Apply(Plus(), Seq(
        Number(-1),
        Apply( Mod(), Seq(I(), Number(3)))
      )), 0),

    DatasetSequence(
      NumberSequence(Seq(-1, 1, 2, -1, 1, 2, -1, 1, 2)),
      -1,
      Apply(Plus(), Seq(
        Number(0),
        T(3)
      )), 3),

    DatasetSequence(
      NumberSequence(Seq(1, 2, 1, 2, 1, 2)),
      1,
      Apply(Plus(), Seq(
        Number(1),
        Apply( Mod(), Seq(I(), Number(2)))
      )), 0)
  )


  val agiPaper = Seq(
    DatasetSequence(
      NumberSequence(Seq(2,5,8,11,14,17,20)),
      23,
      Apply(Plus(), Seq(T(1), Number(3))), 1),

    DatasetSequence(
      NumberSequence(Seq(25,22,19,16,13,10,7)),
      4,
      Apply(Plus(), Seq(T(1), Number(-3))), 1),

    DatasetSequence(
      NumberSequence(Seq(8,12,16,20,24,28,32)),
      36,
      Apply(Plus(), Seq(T(1), Number(4))), 1),

    DatasetSequence(
      NumberSequence(Seq(54,48,42,36,30,24)),
      18,
      Apply(Plus(), Seq(T(1), Number(-6))), 1),

    DatasetSequence(
      NumberSequence(Seq(28,33,31,36,34,39)),
      37,
      Apply(Plus(), Seq(T(2), Number(3))), 2),

    DatasetSequence(
      NumberSequence(Seq(6,8,5,7,4,6,3)),
      5,
      Apply(Plus(), Seq(T(2), Number(-1))), 2),

    DatasetSequence(
      NumberSequence(Seq(9,20,6,17,3,14,0)),
      11,
      Apply(Plus(), Seq(T(2), Number(-3))), 2),

    DatasetSequence(
      NumberSequence(Seq( 12,15,8,11,4,7,0)),
      3,
      Apply(Plus(), Seq(T(2), Number(-4))), 2),

    DatasetSequence(
      NumberSequence(Seq(4,11,15,26,41,67)),
      108,
      Apply(Plus(), Seq(T(2), T(1))), 2),

    DatasetSequence(
      NumberSequence(Seq(3,6,12,24,48,96)),
      192,
      Apply(Times(), Seq(T(1), Number(2))), 1),

    DatasetSequence(
      NumberSequence(Seq(3,7,15,31,63,127)),
      255,
      Apply (Plus(), Seq(Apply(Times(), Seq(T(1), Number(2))), Number(1))), 1),

    DatasetSequence(
      NumberSequence(Seq(2,3,5,9,17,33,65)),
      129,
      Apply (Plus(), Seq(Apply(Times(), Seq(T(1), Number(2))), Number(-1))), 1),

    DatasetSequence(
      NumberSequence(Seq( 2,12,21,29,36,42,47)),
      51,
      Apply (Plus(), Seq(
        Apply(Plus(), Seq(T(1), Number(11))),
        Apply(Times(), Seq(I(), Number(-1)))
      )
      ), 1
    ),

    DatasetSequence(
      NumberSequence(Seq(148,84,52,36,28,24)),
      22,
      Apply (Plus(), Seq(
        Apply(Div(), Seq(T(1), Number(2))),
        Number(10)
      )
      ), 1
    ),

    DatasetSequence(
      NumberSequence(Seq(148,84,52,36,28,24)),
      22,
      Apply (Plus(), Seq(
        Apply(Div(), Seq(T(1), Number(2))),
        Number(10)
      )
      ), 1
    ),

    DatasetSequence(
      NumberSequence(Seq(2,5,9,19,37,75,149)),
      299,
      Apply (Plus(), Seq(
        Apply(Times(), Seq(T(1), Number(2))),
        Apply(Pow(), Seq(Number(-1), Apply(Minus(), Seq(I(), Number(1)))))
      )
      ), 1
    )
  )
}