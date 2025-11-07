@file:Suppress("UnusedImport")

package dev.deftu.omnicore.internal.client.events

import dev.deftu.omnicore.api.client.events.HudRenderEvent
import dev.deftu.omnicore.api.client.render.OmniRenderTicks
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStacks
import dev.deftu.omnicore.api.eventBus
import org.jetbrains.annotations.ApiStatus

//#if FABRIC && MC <= 1.21.3
//#if MC >= 1.16.5
//$$ import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
//#else
//$$ import net.legacyfabric.fabric.api.client.rendering.v1.HudRenderCallback
//#endif
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#endif

//#if FORGE-LIKE
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//$$
//#if FORGE
//#if MC >= 1.19.2
//$$ import net.minecraftforge.client.event.RenderGuiEvent
//#else
//$$ import net.minecraftforge.client.event.RenderGameOverlayEvent
//#endif
//#else
//$$ import net.neoforged.neoforge.client.event.RenderGuiEvent
//#endif
//$$
//$$ private typealias Event =
    //#if MC >= 1.19.2
    //$$ RenderGuiEvent.Post
    //#else
    //$$ RenderGameOverlayEvent.Post
    //#endif
//#endif

@ApiStatus.Internal
public object HudRenderEventForwarding {
    public fun initialize() {
        //#if FABRIC && MC <= 1.21.3
        //$$ HudRenderCallback.EVENT.register { ctx, tickDelta ->
            //#if MC >= 1.16.5
            //$$ val context = OmniRenderingContext.from(ctx)
            //#else
            //$$ val context = OmniRenderingContext.create()
            //#endif
            //#if MC >= 1.21.1
            //$$ val tickDelta = OmniRenderTicks.get(renderTickCounter = tickDelta)
            //#endif
        //$$     eventBus.post(HudRenderEvent(context, tickDelta))
        //$$ }
        //#elseif FORGE-LIKE && MC >= 1.16.5
        //$$ forgeEventBus.addListener<Event> { event ->
        //$$     val context = OmniRenderingContext.from(
                    //#if MC >= 1.20.1
                    //$$ event.guiGraphics,
                    //#elseif MC >= 1.19.2
                    //$$ event.poseStack,
                    //#else
                    //$$ event.matrixStack,
                    //#endif
        //$$     )
        //$$
                //#if MC >= 1.21.1
                //$$ val tickDelta = OmniRenderTicks.get(renderTickCounter = event.partialTick)
                //#elseif MC >= 1.19.2
                //$$ val tickDelta = event.partialTick
                //#else
                //$$ val tickDelta = event.partialTicks
                //#endif
        //$$     eventBus.post(HudRenderEvent(context, tickDelta))
        //$$ }
        //#elseif FORGE
        //$$ forgeEventBus.register(this)
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun event(event: Event) {
    //$$     val context = OmniRenderingContext.create()
    //$$     val tickDelta = event.partialTicks
    //$$     eventBus.post(HudRenderEvent(context, tickDelta))
    //$$ }
    //#endif
}
