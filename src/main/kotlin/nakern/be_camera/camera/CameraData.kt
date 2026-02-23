package nakern.be_camera.camera

import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d

/**
 * Describes player's camera state. Location is mandatory.
 * Priority for rotation: [targetLocation] > [rotation] > default (0, 0).
 */
data class CameraData(
    /**
     * The location of the camera.
     */
    val location: Vec3d,
    /**
     * The target location of the camera. The camera will point at that position.
     * Takes priority over [rotation] if both are set.
     */
    val targetLocation: Vec3d? = null,
    /**
     * Direct rotation of the camera (pitch, yaw) in degrees.
     * Used when [targetLocation] is null.
     */
    val rotation: Vec2f? = null,
    /**
     * Easing options for the camera. Determines the transition from the old state to the new one.
     */
    val easeOptions: EaseOptions? = null
)
