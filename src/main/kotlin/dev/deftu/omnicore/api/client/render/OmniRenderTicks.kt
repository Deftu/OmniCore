package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.client

//#if MC >= 1.21.1
import net.minecraft.client.render.RenderTickCounter
//#endif

//#if MC == 1.8.9
//#if FABRIC
//$$ import dev.deftu.omnicore.mixins.client.Mixin_MinecraftClient_TimerAccessor
//#endif
//$$
//$$ import net.minecraft.client.render.ClientTickTracker
//$$ import net.minecraft.client.MinecraftClient
//#endif

public object OmniRenderTicks {
    //#if MC == 1.8.9
    //#if FORGE
    //$$ private val timerFieldNames = arrayOf("field_71428_T", "timer") // We have the SRG name first so that it's resolved slightly faster in prod
    //$$
    //#endif
    //$$ private val MinecraftClient.deltaTickTracker: ClientTickTracker by lazy {
    //#if FABRIC
    //$$     (client as Mixin_MinecraftClient_TimerAccessor).ticker
    //#else
    //$$     for (fieldName in timerFieldNames) {
    //$$         try {
    //$$             val field = Minecraft::class.java.getDeclaredField(fieldName)
    //$$             field.isAccessible = true
    //$$             return@lazy field.get(client) as Timer
    //$$         } catch (ignored: NoSuchFieldException) {
    //$$             // no-op
    //$$         }
    //$$     }
    //$$
    //$$     throw IllegalStateException("Failed to find the delta tick tracker field in MinecraftClient")
    //#endif
    //$$ }
    //#endif

    @JvmStatic
    public fun initialize() {
        // Force-init the deltaTickTracker property on MC 1.8.9

        //#if MC == 1.8.9
        //$$ val _ = client.deltaTickTracker
        //$$ //#endif
    }

    @JvmStatic
    @JvmOverloads
    public fun get(
        ignoreFreeze: Boolean = true,
        //#if MC >= 1.21.1
        renderTickCounter: RenderTickCounter = client.renderTickCounter,
        //#endif
    ): Float {
        //#if MC >= 1.21.1
        return renderTickCounter.getTickProgress(ignoreFreeze)
        //#elseif MC >= 1.12.2
        //$$ return client.frameTime
        //#else
        //$$ return client.deltaTickTracker.tickDelta
        //#endif
    }
}
