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
        val audioListener: Byte? = null,
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

    /**
     * Resolves a preset by index, merging parent fields for any nullable values.
     */
    fun resolvePreset(index: Int): Preset? {
        val preset = getPreset(index) ?: return null
        if (preset.parent == null) return preset
        val parent = getPresetByName(preset.parent) ?: return preset
        return Preset(
            name = preset.name,
            parent = preset.parent,
            posX = preset.posX ?: parent.posX,
            posY = preset.posY ?: parent.posY,
            posZ = preset.posZ ?: parent.posZ,
            rotX = preset.rotX ?: parent.rotX,
            rotY = preset.rotY ?: parent.rotY,
            playerEffects = preset.playerEffects ?: parent.playerEffects,
            audioListener = preset.audioListener ?: parent.audioListener,
        )
    }

    fun getPresets(): List<Preset> = presets.toList()

    fun clear() {
        presets.clear()
    }
}
