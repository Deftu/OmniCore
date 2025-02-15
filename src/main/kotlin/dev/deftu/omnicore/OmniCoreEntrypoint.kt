package dev.deftu.omnicore

import org.apache.logging.log4j.LogManager

//#if FABRIC
import net.fabricmc.api.ModInitializer
//#elseif FORGE
//#if MC >= 1.16.5
//$$ import net.minecraftforge.eventbus.api.IEventBus
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#else
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.fml.common.Mod
//$$ import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
//#endif

//#if FORGE-LIKE
//#if MC >= 1.16.5
//$$ import dev.deftu.omnicore.common.OmniLoader
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
        //#if FORGE-LIKE
        //#if MC >= 1.16.5
        //$$ event: FMLCommonSetupEvent
        //#else
        //$$ event: FMLInitializationEvent
        //#endif
        //#endif
    ) {
        logger.info("Initializing OmniCore ${OmniCore.VERSION}")
    }

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ private fun setupForgeEvents(modEventBus: IEventBus) {
    //$$     modEventBus.addListener(this::onInitialize)
    //$$
    //$$     OmniLoader.modEventBus = modEventBus
    //$$
    //$$     if (OmniLoader.isPhysicalClient) {
    //$$         OmniKeyBinding.initialize()
    //$$     }
    //$$ }
    //#endif

}
