package dev.deftu.omnicore.internal

import dev.deftu.omnicore.api.VERSION
import dev.deftu.omnicore.api.scheduling.TickSchedulers
import dev.deftu.omnicore.internal.events.EventForwarding
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
//#else
//$$ import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
//#endif
//#elseif FORGE && MC >= 1.16.5
//#if MC >= 1.18.2
//$$ import net.minecraftforge.event.server.ServerStartingEvent
//#elseif MC >= 1.17.1
//$$ import net.minecraftforge.fmlserverevents.FMLServerStartingEvent
//#else
//$$ import net.minecraftforge.fml.event.server.FMLServerStartingEvent
//#endif
//$$
//$$ import net.minecraftforge.common.MinecraftForge
//#elseif NEOFORGE
//$$ import net.neoforged.neoforge.common.NeoForge
//$$ import net.neoforged.neoforge.event.server.ServerStartingEvent
//#endif

@ApiStatus.Internal
public object OmniCoreInitializer {
    private val logger = LogManager.getLogger(OmniCoreInitializer::class.java)

    public var isInitialized: Boolean = false
        private set

    public fun initialize() {
        if (isInitialized) {
            return
        }

        logger.info("Initializing OmniCore common $VERSION")

        EventForwarding.initialize()
        TickSchedulers.initialize()

        //#if FABRIC
        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            serverInstance = server
        }
        //#elseif FORGE-LIKE && MC >= 1.16.5
        //$$ forgeEventBus.addListener<
        //#if MC >= 1.18.2
        //$$     ServerStartingEvent
        //#else
        //$$     FMLServerStartingEvent
        //#endif
        //$$ > { event ->
        //$$     serverInstance = event.server
        //$$ }
        //#endif

        isInitialized = true
    }
}
