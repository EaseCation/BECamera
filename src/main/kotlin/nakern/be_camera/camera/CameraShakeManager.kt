package nakern.be_camera.camera

import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * Manages camera shake effects. Supports multiple concurrent shakes with decay.
 */
object CameraShakeManager {
    /**
     * @param type 0 = Positional, 1 = Rotational
     */
    data class ShakeEntry(
        val intensity: Float,
        val durationMs: Long,
        val type: Int,
        val startMs: Long,
        val seed: Long = Random.nextLong()
    )

    private val shakes = mutableListOf<ShakeEntry>()

    /**
     * Adds a camera shake effect.
     * @param intensity Shake intensity (clamped to 0..4)
     * @param durationSec Duration in seconds
     * @param type 0 = Positional, 1 = Rotational
     */
    fun addShake(intensity: Float, durationSec: Float, type: Int) {
        shakes.add(
            ShakeEntry(
                intensity = min(intensity, 4f),
                durationMs = (durationSec * 1000).toLong(),
                type = type,
                startMs = System.currentTimeMillis()
            )
        )
    }

    /**
     * Stops all active camera shakes.
     */
    fun stopAll() {
        shakes.clear()
    }

    /**
     * Returns the positional offset from all active positional shakes.
     */
    fun getPositionalOffset(): Vec3d {
        val now = System.currentTimeMillis()
        removeExpired(now)

        var x = 0.0
        var y = 0.0
        var z = 0.0

        for (shake in shakes) {
            if (shake.type != 0) continue
            val decay = calcDecay(shake, now)
            val t = (now - shake.startMs).toFloat() / 50f // time factor
            x += shake.intensity * decay * noise(t, shake.seed, 0) * 0.1
            y += shake.intensity * decay * noise(t, shake.seed, 1) * 0.1
            z += shake.intensity * decay * noise(t, shake.seed, 2) * 0.1
        }

        return Vec3d(x, y, z)
    }

    /**
     * Returns the rotational offset (pitch, yaw) from all active rotational shakes.
     */
    fun getRotationalOffset(): Vec2f {
        val now = System.currentTimeMillis()
        removeExpired(now)

        var pitch = 0f
        var yaw = 0f

        for (shake in shakes) {
            if (shake.type != 1) continue
            val decay = calcDecay(shake, now)
            val t = (now - shake.startMs).toFloat() / 50f
            pitch += shake.intensity * decay * noise(t, shake.seed, 3) * 2f
            yaw += shake.intensity * decay * noise(t, shake.seed, 4) * 2f
        }

        return Vec2f(pitch, yaw)
    }

    /**
     * Whether any shakes are currently active.
     */
    fun isActive(): Boolean {
        removeExpired(System.currentTimeMillis())
        return shakes.isNotEmpty()
    }

    /**
     * Resets all shake state.
     */
    fun reset() {
        shakes.clear()
    }

    private fun calcDecay(shake: ShakeEntry, now: Long): Float {
        val elapsed = (now - shake.startMs).toFloat()
        return max(0f, 1f - elapsed / shake.durationMs.toFloat())
    }

    private fun removeExpired(now: Long) {
        shakes.removeAll { now - it.startMs > it.durationMs }
    }

    private fun noise(t: Float, seed: Long, channel: Int): Float {
        val s = seed + channel * 31337L
        return sin(t * (7.0 + (s % 5)) + (s % 1000) * 0.001).toFloat()
    }
}
