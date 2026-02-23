package nakern.be_camera

import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

object BedrockCameraLib : ClientModInitializer {
    private val logger = LoggerFactory.getLogger("bedrockcameralib")

    override fun onInitializeClient() {
        logger.info("BECamera initialized")
    }
}
