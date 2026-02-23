package nakern.be_camera.camera.path

import net.minecraft.util.math.Vec3d

/**
 * Interpolates camera keyframes using linear, bezier, catmull-rom, or step algorithms.
 * Ported from ECCamera's AnimationInterpolator.py.
 */
class PathInterpolator(keyframes: List<CameraKeyframe>) {

    private val channelKeyframes: Map<String, List<CameraKeyframe>>

    init {
        // Group by channel and sort by time
        channelKeyframes = keyframes
            .groupBy { it.channel }
            .mapValues { (_, kfs) -> kfs.sortedBy { it.time } }
    }

    /**
     * Get interpolated values at the given time for all channels.
     * @return Map of channel name to interpolated Vec3d value
     */
    fun getValueAtTime(timeSec: Float): Map<String, Vec3d> {
        val result = mutableMapOf<String, Vec3d>()
        for ((channel, _) in channelKeyframes) {
            val value = getChannelValueAtTime(timeSec, channel)
            if (value != null) {
                result[channel] = value
            }
        }
        return result
    }

    private fun getChannelValueAtTime(timeSec: Float, channel: String): Vec3d? {
        val keyframes = channelKeyframes[channel] ?: return null
        if (keyframes.isEmpty()) return null

        val epsilon = 1.0f / 1200f

        // Check for exact time match
        for (kf in keyframes) {
            if (kotlin.math.abs(kf.time - timeSec) < epsilon) {
                return kf.values
            }
        }

        // Find before and after keyframes
        var beforeKf: CameraKeyframe? = null
        var afterKf: CameraKeyframe? = null

        for (kf in keyframes) {
            if (kf.time < timeSec) {
                if (beforeKf == null || kf.time > beforeKf.time) {
                    beforeKf = kf
                }
            } else if (kf.time > timeSec) {
                if (afterKf == null || kf.time < afterKf.time) {
                    afterKf = kf
                }
            }
        }

        // Boundary cases
        if (beforeKf == null && afterKf == null) return null
        if (beforeKf == null) return afterKf!!.values
        if (afterKf == null) return beforeKf.values

        // Calculate alpha (time ratio between before and after)
        val dt = afterKf.time - beforeKf.time
        val alpha = if (dt > 0f) (timeSec - beforeKf.time) / dt else 0f

        // Interpolate based on before keyframe's interpolation type
        return when (beforeKf.interpolation) {
            InterpolationType.STEP -> beforeKf.values
            InterpolationType.LINEAR -> linearInterpolation(beforeKf.values, afterKf.values, alpha)
            InterpolationType.BEZIER -> bezierInterpolation(beforeKf, afterKf, alpha)
            InterpolationType.CATMULLROM -> {
                // Find neighbors for catmull-rom (p0 and p3)
                val beforeIndex = keyframes.indexOf(beforeKf)
                val p0 = if (beforeIndex > 0) keyframes[beforeIndex - 1].values else beforeKf.values
                val p3 = if (beforeIndex + 2 < keyframes.size) keyframes[beforeIndex + 2].values else afterKf.values
                catmullRomInterpolation(p0, beforeKf.values, afterKf.values, p3, alpha)
            }
        }
    }

    companion object {
        /**
         * Linear interpolation: v1 + (v2 - v1) * alpha
         */
        fun linearInterpolation(v1: Vec3d, v2: Vec3d, alpha: Float): Vec3d {
            val a = alpha.toDouble()
            return Vec3d(
                v1.x + (v2.x - v1.x) * a,
                v1.y + (v2.y - v1.y) * a,
                v1.z + (v2.z - v1.z) * a
            )
        }

        /**
         * Cubic Bezier interpolation.
         * B(t) = (1-t)^3 * P0 + 3*(1-t)^2*t * P1 + 3*(1-t)*t^2 * P2 + t^3 * P3
         *
         * Control points:
         * - P0 = before value (start)
         * - P1 = P0 + bezierRightValue (from before keyframe)
         * - P2 = P3 + bezierLeftValue (from after keyframe)
         * - P3 = after value (end)
         */
        fun bezierInterpolation(before: CameraKeyframe, after: CameraKeyframe, t: Float): Vec3d {
            val t1 = t.toDouble()
            val p0 = before.values
            val p3 = after.values

            val brv = before.bezierRightValue ?: Vec3d.ZERO
            val blv = after.bezierLeftValue ?: Vec3d.ZERO

            fun bezier1d(p0v: Double, p1v: Double, p2v: Double, p3v: Double): Double {
                val mt = 1.0 - t1
                return mt * mt * mt * p0v +
                        3.0 * mt * mt * t1 * p1v +
                        3.0 * mt * t1 * t1 * p2v +
                        t1 * t1 * t1 * p3v
            }

            return Vec3d(
                bezier1d(p0.x, p0.x + brv.x, p3.x + blv.x, p3.x),
                bezier1d(p0.y, p0.y + brv.y, p3.y + blv.y, p3.y),
                bezier1d(p0.z, p0.z + brv.z, p3.z + blv.z, p3.z)
            )
        }

        /**
         * Catmull-Rom spline interpolation.
         * q(t) = 0.5 * ((2*p1) + (-p0+p2)*t + (2p0-5p1+4p2-p3)*t^2 + (-p0+3p1-3p2+p3)*t^3)
         */
        fun catmullRomInterpolation(p0: Vec3d, p1: Vec3d, p2: Vec3d, p3: Vec3d, t: Float): Vec3d {
            val t1 = t.toDouble()
            val t2 = t1 * t1
            val t3 = t2 * t1

            fun cr1d(v0: Double, v1: Double, v2: Double, v3: Double): Double {
                return 0.5 * ((2.0 * v1) +
                        (-v0 + v2) * t1 +
                        (2.0 * v0 - 5.0 * v1 + 4.0 * v2 - v3) * t2 +
                        (-v0 + 3.0 * v1 - 3.0 * v2 + v3) * t3)
            }

            return Vec3d(
                cr1d(p0.x, p1.x, p2.x, p3.x),
                cr1d(p0.y, p1.y, p2.y, p3.y),
                cr1d(p0.z, p1.z, p2.z, p3.z)
            )
        }
    }
}
