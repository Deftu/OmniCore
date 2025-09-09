package dev.deftu.omnicore.internal.client

import dev.deftu.omnicore.api.client.input.keybindings.MCKeyBinding
import dev.deftu.omnicore.api.client.render.ImmediateScreenRenderer
import dev.deftu.omnicore.api.client.render.OmniFrameClock
import dev.deftu.omnicore.api.client.render.OmniRenderTicks
import dev.deftu.omnicore.internal.VERSION
import dev.deftu.omnicore.internal.client.events.ClientEventForwarding
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniCoreClientInitializer {
    private val logger = LogManager.getLogger(OmniCoreClientInitializer::class.java)

    public var isInitialized: Boolean = false
        private set

    public fun initialize() {
        if (isInitialized) {
            return
        }

        logger.info("Initializing OmniCore client $VERSION")

        ClientEventForwarding.initialize()
        ImmediateScreenRenderer.initialize()
        MCKeyBinding.initialize()
        OmniFrameClock.initialize()
        OmniRenderTicks.initialize()
        OmniClientCommands.initialize()

        isInitialized = true
    }
}
