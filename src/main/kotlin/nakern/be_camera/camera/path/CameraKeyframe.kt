package nakern.be_camera.camera.path

import net.minecraft.util.math.Vec3d

/**
 * A single keyframe in a camera path animation.
 *
 * @param channel The animation channel: "position", "rotation", or "fov"
 * @param time The time of this keyframe in seconds
 * @param values The values: (x,y,z) for position, (pitch,yaw,0) for rotation, (fov,0,0) for fov.
 *               Values should be in Minecraft world coordinates (caller handles coordinate conversion).
 * @param interpolation The interpolation type to use when transitioning FROM this keyframe
 * @param bezierLeftTime Bezier left control point time offsets (per axis), only for BEZIER
 * @param bezierLeftValue Bezier left control point value offsets (per axis), only for BEZIER
 * @param bezierRightTime Bezier right control point time offsets (per axis), only for BEZIER
 * @param bezierRightValue Bezier right control point value offsets (per axis), only for BEZIER
 */
data class CameraKeyframe(
    val channel: String,
    val time: Float,
    val values: Vec3d,
    val interpolation: InterpolationType,
    val bezierLeftTime: Vec3d? = null,
    val bezierLeftValue: Vec3d? = null,
    val bezierRightTime: Vec3d? = null,
    val bezierRightValue: Vec3d? = null,
)
