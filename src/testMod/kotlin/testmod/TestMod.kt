package testmod

//#if FABRIC
import net.fabricmc.api.ClientModInitializer
//#else
//#if FORGE
//#if MC >= 1.15.2
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#else
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.common.Mod.EventHandler
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
//#else
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.fml.common.Mod
//$$ import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
//#endif
//#endif

import dev.deftu.omnicore.client.OmniKeyBinding
import dev.deftu.omnicore.client.OmniKeyboard

//#if FABRIC
class TestMod : ClientModInitializer {
//#else
//#if MC >= 1.15.2
//$$ @Mod(value = "test-mod")
//#else
//$$ @Mod(modid = "test-mod")
//#endif
//$$ class TestMod {
//#endif

    //#if FORGE && MC >= 1.16.5
    //$$ init {
    //$$     FMLJavaModLoadingContext.get().modEventBus.register(this)
    //$$ }
    //#endif

    //#if NEOFORGE
    //$$ constructor(modEventBus: IEventBus) {
    //$$     modEventBus.register(this)
    //$$ }
    //#endif

    val keyBinding = OmniKeyBinding(
        "Test KeyBinding",
        "Test Mod",
        OmniKeyboard.KEY_U
    )

    //#if FABRIC
    override
    //#endif
    fun onInitializeClient(
        //#if FORGE-LIKE
        //#if MC >= 1.15.2
        //$$ event: FMLClientSetupEvent
        //#else
        //$$ event: FMLInitializationEvent
        //#endif
        //#endif
    ) {
        println("Initializing TestMod")
        keyBinding.attemptRegister()
    }

}
