package dev.deftu.omnicore

import dev.deftu.omnicore.api.scheduling.TickSchedulers
import dev.deftu.omnicore.common.events.OmniCommonEventPassthrough
import dev.deftu.omnicore.server.OmniCoreServer
import org.apache.logging.log4j.LogManager

//#if FABRIC
import net.fabricmc.api.ModInitializer
import net.fabricmc.api.ClientModInitializer
//#elseif FORGE
//#if MC >= 1.16.5
//$$ import net.minecraftforge.eventbus.api.IEventBus
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#else
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.fml.common.Mod
//#endif

//#if FORGE-LIKE
//#if MC >= 1.16.5
//$$ import dev.deftu.omnicore.common.OmniLoader
//$$
//$$ @Mod(OmniCore.ID)
//#else
//$$ @Mod(modid = OmniCore.ID, version = OmniCore.VERSION)
//#endif
//#endif
public class OmniCoreEntrypoint
    //#if FABRIC
    : ModInitializer, ClientModInitializer
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

        OmniCommonEventPassthrough.initialize()
        OmniCoreServer.initialize()
        TickSchedulers.initialize()
    }

    //#if FABRIC
    override
    //#elseif FORGE && MC <= 1.12.2
    //$$ @Mod.EventHandler
    //$$ private
    //#else
    //$$ private
    //#endif
    fun onInitializeClient(
        //#if FORGE && MC <= 1.12.2
        //$$ event: FMLInitializationEvent
        //#endif
    ) {
        OmniCoreClient.initialize()
    }

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ private fun setupForgeEvents(modEventBus: IEventBus) {
    //$$     OmniLoader.modEventBus = modEventBus
    //$$     onInitialize()
    //$$
    //$$     if (OmniLoader.isPhysicalClient) {
    //$$         onInitializeClient()
    //$$     }
    //$$ }
    //#endif

}
