package dev.deftu.omnicore.internal.client.compat.config

//#if FORGE-LIKE && MC >= 1.16.5
//$$ import dev.deftu.omnicore.api.client.compat.config.ConfigScreenProvider
//$$ import dev.deftu.omnicore.internal.serverInstance
//$$ import org.jetbrains.annotations.ApiStatus
//$$
//#if FORGE
//$$ import net.minecraftforge.fml.ModLoadingContext
//#else
//$$ import net.neoforged.fml.ModLoadingContext
//#endif
//$$
//#if MC >= 1.20.6
//$$ import net.neoforged.neoforge.client.gui.IConfigScreenFactory
//#elseif MC >= 1.19.2
//#if FORGE
//$$ import net.minecraftforge.client.ConfigScreenHandler
//#else
//$$ import net.neoforged.neoforge.client.ConfigScreenHandler
//#endif
//#elseif MC >= 1.18.2
//$$ import net.minecraftforge.client.ConfigGuiHandler
//#elseif MC >= 1.17.1
//$$ import net.minecraftforge.fmlclient.ConfigGuiHandler
//#elseif MC >= 1.16.5
//$$ import net.minecraftforge.fml.ExtensionPoint
//$$ import java.util.function.BiFunction
//#endif
//$$
//$$ @ApiStatus.Internal
//$$ public object ModernForgeConfigCompat {
//$$     public fun register(provider: ConfigScreenProvider) {
//$$         if (serverInstance != null) {
//$$             return
//$$         }
//$$
//$$         val ctx = ModLoadingContext.get()
        //#if MC >= 1.20.6
        //$$ ctx.registerExtensionPoint(IConfigScreenFactory::class.java) {
        //$$     IConfigScreenFactory { _, parent ->
        //$$         provider.build(parent)
        //$$     }
        //$$ }
        //#elseif MC >= 1.19.2
        //$$ ctx.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
        //$$     ConfigScreenHandler.ConfigScreenFactory { _, parent ->
        //$$         provider.build(parent)
        //$$     }
        //$$ }
        //#elseif MC >= 1.17.1
        //$$ ctx.registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory::class.java) {
        //$$     ConfigGuiHandler.ConfigGuiFactory { _, parent ->
        //$$         provider.build(parent)
        //$$     }
        //$$ }
        //#else
        //$$ ctx.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY) {
        //$$     BiFunction { _, parent ->
        //$$         provider.build(parent)
        //$$     }
        //$$ }
        //#endif
//$$     }
//$$ }
//#endif
