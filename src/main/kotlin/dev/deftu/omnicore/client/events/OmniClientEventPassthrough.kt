package dev.deftu.omnicore.client.events

import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.omnicore.common.events.TickEvent

//#if FABRIC
//#if MC >= 1.16.5
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
//#else
//$$ import net.legacyfabric.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//$$ import net.legacyfabric.fabric.api.client.rendering.v1.HudRenderCallback
//#endif
//#elseif FORGE
//$$ import net.minecraftforge.common.MinecraftForge
//#if MC >= 1.19.2
//$$ import net.minecraftforge.client.event.RenderGuiEvent
//#else
//$$ import net.minecraftforge.client.event.RenderGameOverlayEvent
//#endif
//#if MC <= 1.12.2
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#endif
//#else
//$$ import net.neoforged.neoforge.client.event.RenderGuiEvent
//$$ import net.neoforged.neoforge.common.NeoForge
//#endif

//#if MC >= 1.21.1
//$$ import dev.deftu.omnicore.client.render.OmniGameRendering
//#endif

public object OmniClientEventPassthrough {

    public fun initialize() {
        //#if FABRIC
        ClientTickEvents.START_CLIENT_TICK.register { _ ->
            OmniCore.eventBus.post(TickEvent.Client.Pre)
        }

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            OmniCore.eventBus.post(TickEvent.Client.Post)
        }

        HudRenderCallback.EVENT.register { ctx, tickDelta ->
            val matrixStack = OmniMatrixStack.vanilla(
                //#if MC >= 1.16.5
                ctx
                //#endif
            )

            //#if MC >= 1.21.1
            //$$ val tickDelta = OmniGameRendering.getTickDelta(false)
            //#endif
            OmniCore.eventBus.post(HudRenderEvent(matrixStack, tickDelta))
        }
        //#elseif MC >= 1.20.6
        //$$ NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Pre> { event ->
        //$$     OmniCore.eventBus.post(TickEvent.Client.Pre)
        //$$ }
        //$$
        //$$ NeoForge.EVENT_BUS.addListener<net.neoforged.neoforge.client.event.ClientTickEvent.Post> { event ->
        //$$     OmniCore.eventBus.post(TickEvent.Client.Post)
        //$$ }
        //#elseif MC >= 1.16.5
        //#if FORGE
        //$$ MinecraftForge
        //#else
        //$$ NeoForge
        //#endif
        //$$     .EVENT_BUS.addListener<net.minecraftforge.event.TickEvent.ClientTickEvent> { event ->
        //$$         when (event.phase) {
        //$$             net.minecraftforge.event.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Client.Pre)
        //$$             net.minecraftforge.event.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Client.Post)
        //$$         }
        //$$     }
        //#if MC >= 1.19.2
        //#if FORGE
        //$$ MinecraftForge
        //#else
        //$$ NeoForge
        //#endif
        //$$     .EVENT_BUS.addListener<RenderGuiEvent.Post> { event ->
        //$$         val matrixStack = OmniMatrixStack.vanilla(
        //#if MC >= 1.20.1
        //$$              event.guiGraphics
        //#else
        //$$              event.poseStack
        //#endif
        //$$          )
        //$$
        //$$          OmniCore.eventBus.post(HudRenderEvent(matrixStack, event.partialTick))
        //$$     }
        //#elseif MC >= 1.16.5
        //$$ MinecraftForge.EVENT_BUS.addListener<RenderGameOverlayEvent> { event ->
        //$$     val matrixStack = OmniMatrixStack.vanilla(event.matrixStack)
        //$$     OmniCore.eventBus.post(HudRenderEvent(matrixStack, event.partialTicks))
        //$$ }
        //#else
        //$$ MinecraftForge.EVENT_BUS.register(this)
        //#endif
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onClientTick(event: net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent) {
    //$$     when (event.phase) {
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START -> OmniCore.eventBus.post(TickEvent.Client.Pre)
    //$$         net.minecraftforge.fml.common.gameevent.TickEvent.Phase.END -> OmniCore.eventBus.post(TickEvent.Client.Post)
    //$$         else -> {
    //$$             // no-op
    //$$         }
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onRenderGameOverlay(event: RenderGameOverlayEvent.Post) {
    //$$     val matrixStack = OmniMatrixStack()
    //$$     OmniCore.eventBus.post(HudRenderEvent(matrixStack, event.partialTicks))
    //$$ }
    //#endif

}
