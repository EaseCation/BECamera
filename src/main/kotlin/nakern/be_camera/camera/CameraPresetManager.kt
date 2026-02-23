package nakern.be_camera.camera

/**
 * Stores camera presets received from Bedrock server.
 */
object CameraPresetManager {
    data class Preset(
        val name: String,
        val parent: String? = null,
        val posX: Float? = null,
        val posY: Float? = null,
        val posZ: Float? = null,
        val rotX: Float? = null,
        val rotY: Float? = null,
        val playerEffects: Boolean? = null,
    )

    private val presets = mutableListOf<Preset>()

    fun setPresets(newPresets: List<Preset>) {
        presets.clear()
        presets.addAll(newPresets)
    }

    fun getPreset(index: Int): Preset? {
        return presets.getOrNull(index)
    }

    fun getPresetByName(name: String): Preset? {
        return presets.find { it.name == name }
    }

    fun getPresets(): List<Preset> = presets.toList()

    fun clear() {
        presets.clear()
    }
}
