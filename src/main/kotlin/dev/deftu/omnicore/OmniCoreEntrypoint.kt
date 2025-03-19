package dev.deftu.omnicore

import dev.deftu.omnicore.client.OmniClientCommands
import dev.deftu.omnicore.client.events.OmniClientEventPassthrough
import dev.deftu.omnicore.common.OmniLoader
import dev.deftu.omnicore.common.events.OmniCommonEventPassthrough
import dev.deftu.omnicore.server.OmniServer
import dev.deftu.omnicore.server.OmniServerCommands
import org.apache.logging.log4j.LogManager

//#if FABRIC
import net.fabricmc.api.ModInitializer
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
//#else
//$$ import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
//#endif
//#elseif FORGE
//$$ import net.minecraftforge.common.MinecraftForge
//#if MC >= 1.16.5
//$$ import net.minecraftforge.eventbus.api.IEventBus
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#if MC >= 1.18.2
//$$ import net.minecraftforge.event.server.ServerStartingEvent
//#elseif MC >= 1.17.1
//$$ import net.minecraftforge.fmlserverevents.FMLServerStartingEvent
//#else
//$$ import net.minecraftforge.fml.event.server.FMLServerStartingEvent
//#endif
//#else
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.fml.common.Mod
//$$ import net.neoforged.neoforge.common.NeoForge
//$$ import net.neoforged.neoforge.event.server.ServerStartingEvent
//#endif

//#if FORGE-LIKE
//#if MC >= 1.16.5
//$$ import dev.deftu.omnicore.client.OmniKeyBinding
//$$
//$$ @Mod(OmniCore.ID)
//#else
//$$ @Mod(modid = OmniCore.ID, version = OmniCore.VERSION)
//#endif
//#endif
public class OmniCoreEntrypoint
    //#if FABRIC
    : ModInitializer
    //#endif
{

    private val logger = LogManager.getLogger(OmniCore::class.java)

    //#if FORGE && MC >= 1.16.5
    //$$ init {
    //$$     setupForgeEvents(FMLJavaModLoadingContext.get().modEventBus)
    //$$ }
    //#elseif NEOFORGE
    //$$ public constructor(modEventBus: IEventBus) {
    //$$     setupForgeEvents(modEventBus)
    //$$ }
    //#endif

    //#if FABRIC
    override
    //#elseif FORGE && MC <= 1.12.2
    //$$ @Mod.EventHandler
    //$$ private
    //#else
    //$$ private
    //#endif
    fun onInitialize(
        //#if FORGE && MC <= 1.12.2
        //$$ event: FMLInitializationEvent
        //#endif
    ) {
        logger.info("Initializing OmniCore ${OmniCore.VERSION}")

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

        OmniCommonEventPassthrough.initialize()

        if (OmniLoader.isPhysicalClient) {
            //#if FORGE-LIKE && MC >= 1.19.2
            //$$ OmniKeyBinding.initialize()
            //#endif

            OmniClientEventPassthrough.initialize()
            OmniClientCommands.initialize()
        }

        if (OmniLoader.isPhysicalServer) {
            OmniServerCommands.initialize()
        }
    }

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ private fun setupForgeEvents(modEventBus: IEventBus) {
    //$$     OmniLoader.modEventBus = modEventBus
    //$$     onInitialize()
    //$$ }
    //#endif

}
