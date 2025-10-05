package dev.deftu.omnicore.internal.client.events

import org.jetbrains.annotations.ApiStatus

//#if FORGE-LIKE
//#if MC <= 1.12.2
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#endif
//$$
//$$ import dev.deftu.omnicore.api.eventBus
//$$ import dev.deftu.omnicore.api.client.events.RenderTickEvent
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//#endif

@ApiStatus.Internal
public object RenderTickEventForwarding {
    public fun initialize() {
        //#if FORGE-LIKE
        //#if MC >= 1.20.6
        //$$ forgeEventBus.addListener<net.neoforged.neoforge.client.event.RenderFrameEvent.Pre> { event ->
        //$$     eventBus.post(RenderTickEvent.Pre)
        //$$ }
        //$$
        //$$ forgeEventBus.addListener<net.neoforged.neoforge.client.event.RenderFrameEvent.Post> { event ->
        //$$     eventBus.post(RenderTickEvent.Post)
        //$$ }
        //#elseif MC >= 1.16.5
        //$$ forgeEventBus.addListener<net.neoforged.neoforge.event.TickEvent.RenderTickEvent> { event ->
        //$$     when (event.phase) {
        //$$         net.neoforged.neoforge.event.TickEvent.Phase.START -> eventBus.post(RenderTickEvent.Pre)
        //$$         net.neoforged.neoforge.event.TickEvent.Phase.END -> eventBus.post(RenderTickEvent.Post)
        //$$         else -> {  } // no-op
        //$$     }
        //$$ }
        //#else
        //$$ forgeEventBus.register(this)
        //#endif
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onRenderTick(event: net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent) {
    //$$     when (event.phase) {
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> eventBus.post(RenderTickEvent.Pre)
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> eventBus.post(RenderTickEvent.Post)
    //$$         else -> {  } // no-op
    //$$     }
    //$$ }
    //#endif
}
