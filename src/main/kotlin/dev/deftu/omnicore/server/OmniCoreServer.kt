package dev.deftu.omnicore.server

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

public object OmniCoreServer {
    public var isInitialized: Boolean = false
        private set

    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC
        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            OmniServer.server = server
        }
        //#elseif FORGE-LIKE && MC >= 1.16.5
        //#if FORGE
        //$$ MinecraftForge
        //#else
        //$$ NeoForge
        //#endif
        //$$     .EVENT_BUS
        //$$     .addListener<
        //#if MC >= 1.18.2
        //$$         ServerStartingEvent
        //#else
        //$$         FMLServerStartingEvent
        //#endif
        //$$     > { event ->
        //$$         OmniServer.server = event.server
        //$$     }
        //#endif

        OmniServerCommands.initialize()
    }
}
