package nakern.be_camera.easings

import kotlin.math.*

typealias EasingFn = (Float) -> Float;

object Easings {
    fun none(x: Float): Float {
        return 1.0f;
    }

    fun linear(x: Float): Float {
        return x;
    }

    fun easeInSine(x: Float): Float {
        return (1 - cos(x * PI / 2)).toFloat()
    }

    fun easeOutSine(x: Float): Float {
        return cos(x * PI / 2).toFloat()
    }

    fun easeInOutSine(x: Float): Float {
        return (-(cos(PI * x) - 1) / 2).toFloat();
    }

    fun easeInQuad(x: Float): Float {
        return x.pow(2);
    }

    fun easeOutQuad(x: Float): Float {
        return 1f - (1f - x).pow(2);
    }

    fun easeInOutQuad(x: Float): Float {
        return if (x < 0.5) {
            2 * x.pow(2)
        } else {
            1 - (-2 * x + 2).pow(2) / 2
        }
    }

    fun easeInCubic(x: Float): Float {
        return x.pow(3);
    }

    fun easeOutCubic(x: Float): Float {
        return 1 - (1 - x).pow(3);
    }

    fun easeInOutCubic(x: Float): Float {
        return if (x < 0.5) {
            4 * x.pow(3)
        } else {
            1 - (-2 * x + 2).pow(3) / 2
        }
    }

    fun easeInQuart(x: Float): Float {
        return x.pow(4);
    }

    fun easeOutQuart(x: Float): Float {
        return 1 - (1 - x).pow(4)
    }

    fun easeInOutQuart(x: Float): Float {
        return if (x < 0.5) {
            8 * x.pow(4)
        } else {
            1 - (-2 * x + 2).pow(4) / 2
        }
    }

    fun easeInQuint(x: Float): Float {
        return x.pow(5);
    }

    fun easeOutQuint(x: Float): Float {
        return 1 - (1 - x).pow(5);
    }

    fun easeInOutQuint(x: Float): Float {
        return if (x < 0.5) {
            16 * x.pow(5)
        } else {
            1 - (-2 * x + 2).pow(5) / 2
        }
    }

    fun easeInExpo(x: Float): Float {
        return if (x == 0f) {
            0f
        } else {
            2f.pow(10 * x - 10)
        }
    }

    fun easeOutExpo(x: Float): Float {
        return if (x == 1f) {
            1f
        } else {
            1 - 2f.pow(-10 * x)
        }
    }

    fun easeInOutExpo(x: Float): Float {
        return if (x == 0f) {
            0f
        } else {
            if (x == 1f) {
                1f
            } else {
                if (x < 0.5f) {
                    2f.pow(20 * x - 10) / 2;
                } else {
                    2 - 2f.pow(-20 * x + 10) / 2;
                }
            }
        }
    }

    fun easeInCirc(x: Float): Float {
        return 1 - sqrt(1 - x.pow(2))
    }

    fun easeOutCirc(x: Float): Float {
        return sqrt(1 - (x - 1).pow(2))
    }

    fun easeInOutCirc(x: Float): Float {
        return if (x < 0.5f) {
            (1 - sqrt(1 - (2 * x).pow(2))) / 2
        } else {
            (sqrt(1 - (-2 * x + 2).pow(2) + 1)) / 2
        }
    }

    fun easeInBack(x: Float): Float {
        val c1 = 1.70158;
        val c3 = c1 + 1f;

        return (c3 * x.pow(3) - c1 * x.pow(2)).toFloat()
    }

    fun easeOutBack(x: Float): Float {
        val c1 = 1.70158;
        val c3 = c1 + 1f;

        return (1f + c3 * (x - 1).pow(3) + c1 * (x - 1).pow(2)).toFloat()
    }

    fun easeInOutBack(x: Float): Float {
        val c1 = 1.70158;
        val c2 = c1 * 1.525;

        return if (x < 0.5) {
            ((2 * x).pow(2) * ((c2 + 1) * 2 * x - c2) / 2).toFloat()
        } else {
            (((2 * x - 2).pow(2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2).toFloat()
        }
    }

    fun easeInElastic(x: Float): Float {
        val c4 = (2 * PI) / 3;

        return when (x) {
            0f -> {
                0f
            }
            1f -> {
                1f
            }
            else -> {
                (-(2f.pow(10 * x - 10)) * sin(c4 * (x * 10 - 10.75))).toFloat()
            }
        }
    }

    fun easeOutElastic(x: Float): Float {
        val c4 = (2 * PI) / 3;

        return when (x) {
            0f -> {
                0f
            }
            1f -> {
                1f
            }
            else -> {
                (2f.pow(-10 * x) * sin(c4 * (x * 10 - 0.75))).toFloat() + 1f
            }
        }
    }

    fun easeInOutElastic(x: Float): Float {
        val c5 = (2 * Math.PI) / 4.5

        return (when {
            x == 0f -> 0f
            x == 1f -> 1f
            x < 0.5 -> -((2f.pow(20 * x - 10) * sin((20 * x - 11.125) * c5)) / 2)
            else -> (2f.pow(-20 * x + 10) * sin((20 * x - 11.125) * c5)) / 2 + 1
        }).toFloat()
    }

    fun spring(x: Float): Float {
        return (1.0 - cos(x * PI * 4) * exp(-x * 6.0)).toFloat()
    }

    fun easeInBounce(x: Float): Float {
        return 1f - easeOutBounce(1f - x)
    }

    fun easeOutBounce(x: Float): Float {
        val n1 = 7.5625f
        val d1 = 2.75f
        return when {
            x < 1f / d1 -> n1 * x * x
            x < 2f / d1 -> { val t = x - 1.5f / d1; n1 * t * t + 0.75f }
            x < 2.5f / d1 -> { val t = x - 2.25f / d1; n1 * t * t + 0.9375f }
            else -> { val t = x - 2.625f / d1; n1 * t * t + 0.984375f }
        }
    }

    fun easeInOutBounce(x: Float): Float {
        return if (x < 0.5f) {
            (1f - easeOutBounce(1f - 2f * x)) / 2f
        } else {
            (1f + easeOutBounce(2f * x - 1f)) / 2f
        }
    }

    /**
     * Lookup easing function by Bedrock EasingType ordinal.
     *
     * Bedrock order: linear(0), spring(1), in_quad(2), out_quad(3), in_out_quad(4),
     * in_cubic(5), out_cubic(6), in_out_cubic(7), in_quart(8), out_quart(9), in_out_quart(10),
     * in_quint(11), out_quint(12), in_out_quint(13), in_sine(14), out_sine(15), in_out_sine(16),
     * in_expo(17), out_expo(18), in_out_expo(19), in_circ(20), out_circ(21), in_out_circ(22),
     * in_bounce(23), out_bounce(24), in_out_bounce(25), in_back(26), out_back(27), in_out_back(28),
     * in_elastic(29), out_elastic(30), in_out_elastic(31)
     */
    @JvmStatic
    fun byBedrockOrdinal(ordinal: Int): EasingFn {
        return BEDROCK_EASINGS.getOrElse(ordinal) { Easings::linear }
    }

    private val BEDROCK_EASINGS: Array<EasingFn> = arrayOf(
        Easings::linear,            // 0
        Easings::spring,            // 1
        Easings::easeInQuad,        // 2
        Easings::easeOutQuad,       // 3
        Easings::easeInOutQuad,     // 4
        Easings::easeInCubic,       // 5
        Easings::easeOutCubic,      // 6
        Easings::easeInOutCubic,    // 7
        Easings::easeInQuart,       // 8
        Easings::easeOutQuart,      // 9
        Easings::easeInOutQuart,    // 10
        Easings::easeInQuint,       // 11
        Easings::easeOutQuint,      // 12
        Easings::easeInOutQuint,    // 13
        Easings::easeInSine,        // 14
        Easings::easeOutSine,       // 15
        Easings::easeInOutSine,     // 16
        Easings::easeInExpo,        // 17
        Easings::easeOutExpo,       // 18
        Easings::easeInOutExpo,     // 19
        Easings::easeInCirc,        // 20
        Easings::easeOutCirc,       // 21
        Easings::easeInOutCirc,     // 22
        Easings::easeInBounce,      // 23
        Easings::easeOutBounce,     // 24
        Easings::easeInOutBounce,   // 25
        Easings::easeInBack,        // 26
        Easings::easeOutBack,       // 27
        Easings::easeInOutBack,     // 28
        Easings::easeInElastic,     // 29
        Easings::easeOutElastic,    // 30
        Easings::easeInOutElastic,  // 31
    )
}