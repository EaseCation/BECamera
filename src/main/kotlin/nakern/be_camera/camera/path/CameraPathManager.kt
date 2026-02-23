package nakern.be_camera.camera.path

import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

/**
 * Manages camera path playback using keyframe-based animations.
 * Provides per-frame interpolated position, rotation, and FOV.
 */
object CameraPathManager {

    private var interpolator: PathInterpolator? = null
    private var startTimeMs: Long = 0
    private var durationSec: Float = 0f
    private var originPos: Vec3d = Vec3d.ZERO
    private var loop: Boolean = false
    private var active: Boolean = false

    // Cached per-frame values
    private var currentPosition: Vec3d = Vec3d.ZERO
    private var currentRotation: Vec2f = Vec2f.ZERO
    private var currentFov: Float? = null

    /**
     * Start playing a camera path animation.
     *
     * @param keyframes List of keyframes (position/rotation/fov channels, in MC world coordinates)
     * @param durationSec Total animation duration in seconds
     * @param originPos World position offset applied to all position keyframes
     * @param loop Whether to loop the animation
     */
    fun startPath(keyframes: List<CameraKeyframe>, durationSec: Float,
                  originPos: Vec3d, loop: Boolean = false) {
        this.interpolator = PathInterpolator(keyframes)
        this.durationSec = durationSec
        this.originPos = originPos
        this.loop = loop
        this.startTimeMs = System.currentTimeMillis()
        this.active = true
        // Compute initial frame
        updateFrame(0f)
    }

    /**
     * Stop the current path animation.
     */
    fun stopPath() {
        active = false
        interpolator = null
        currentFov = null
    }

    /**
     * Whether a path animation is currently active.
     */
    fun isActive(): Boolean = active

    /**
     * Get the current interpolated camera position (world coordinates).
     */
    fun getPosition(): Vec3d {
        updateIfNeeded()
        return currentPosition
    }

    /**
     * Get the current interpolated camera rotation.
     * @return Vec2f(pitch, yaw) in degrees
     */
    fun getRotation(): Vec2f {
        updateIfNeeded()
        return currentRotation
    }

    /**
     * Get the current interpolated FOV, or null if FOV is not controlled by the path.
     */
    fun getFov(): Float? {
        updateIfNeeded()
        return currentFov
    }

    /**
     * Reset all path state. Called on disconnect.
     */
    fun reset() {
        stopPath()
    }

    private fun updateIfNeeded() {
        if (!active || interpolator == null) return
        val elapsed = (System.currentTimeMillis() - startTimeMs) / 1000f

        if (elapsed > durationSec) {
            if (loop) {
                // Reset start time for seamless loop
                startTimeMs = System.currentTimeMillis()
                updateFrame(0f)
            } else {
                stopPath()
            }
            return
        }

        updateFrame(elapsed)
    }

    private fun updateFrame(timeSec: Float) {
        val interp = interpolator ?: return
        val values = interp.getValueAtTime(timeSec)

        // Position: add origin offset
        val pos = values["position"]
        if (pos != null) {
            currentPosition = originPos.add(pos)
        }

        // Rotation: (pitch, yaw, roll) - roll is ignored
        val rot = values["rotation"]
        if (rot != null) {
            currentRotation = Vec2f(rot.x.toFloat(), rot.y.toFloat())
        }

        // FOV
        val fov = values["fov"]
        currentFov = fov?.x?.toFloat()
    }
}
