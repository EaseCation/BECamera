package nakern.be_camera.camera

import net.minecraft.client.gui.DrawContext

object FadeDrawer {
    fun draw(context: DrawContext) {
        val (baseColor, alpha) = CameraManager.getFadeInfo()
        if (alpha <= 0f) return

        val a = (alpha * 255).toInt().coerceIn(0, 255)
        val r = (baseColor shr 16) and 0xFF
        val g = (baseColor shr 8) and 0xFF
        val b = baseColor and 0xFF
        val color = (a shl 24) or (r shl 16) or (g shl 8) or b

        context.fill(
            0, 0,
            context.scaledWindowWidth, context.scaledWindowHeight,
            color
        )
    }
}
