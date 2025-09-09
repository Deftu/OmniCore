@file:Suppress("UnusedImport")

package dev.deftu.omnicore.internal.client.events

import dev.deftu.omnicore.api.client.events.HudRenderEvent
import dev.deftu.omnicore.api.client.render.OmniRenderTicks
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import org.jetbrains.annotations.ApiStatus

//#if FABRIC && MC <= 1.21.3
//$$ import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
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
//$$     RenderGuiEvent.Post
//#else
//$$     RenderGameOverlayEvent.Post
//#endif
//#endif

@ApiStatus.Internal
public object HudRenderEventForwarding {
    public fun initialize() {
        //#if FABRIC && MC <= 1.21.3
        //$$ HudRenderCallback.EVENT.register { ctx, tickDelta ->
        //$$     val matrixStack = OmniMatrixStacks.vanilla(ctx)
        //$$     val context = OmniRenderingContext(
        //$$         //#if MC >= 1.20.1
        //$$         ctx,
        //$$         //#endif
        //$$         matrixStack,
        //$$     )
        //$$
        //$$     //#if MC >= 1.21.1
        //$$     val tickDelta = OmniRenderTicks.get(renderTickCounter = tickDelta)
        //$$     //#endif
        //$$     OmniCore.eventBus.post(HudRenderEvent(context, tickDelta))
        //$$ }
        //#elseif MC >= 1.16.5
        //$$ forgeEventBus.addListener<Event> { event ->
        //$$     val matrixStack = OmniMatrixStacks.vanilla(
        //#if MC >= 1.20.1
        //$$         event.guiGraphics,
        //#else
        //$$         event.poseStack,
        //#endif
        //$$     )
        //$$
        //$$     val context = OmniRenderingContext(
        //#if MC >= 1.20.1
        //$$         event.guiGraphics,
        //#endif
        //$$         matrixStack,
        //$$     )
        //$$
        //#if MC >= 1.21.1
        //$$     val tickDelta = OmniRenderTicks.get(renderTickCounter = event.partialTick)
        //#else
        //$$     val tickDelta = event.partialTick
        //#endif
        //$$     OmniCore.eventBus.post(HudRenderEvent(context, tickDelta))
        //$$ }
        //#else
        //$$ MinecraftForge.EVENT_BUS.register(this)
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun event(event: Event) {
    //$$     val matrixStack = OmniMatrixStacks.vanilla()
    //$$     val context = OmniRenderingContext(matrixStack)
    //$$     val tickDelta = event.partialTicks
    //$$     OmniCore.eventBus.post(HudRenderEvent(context, tickDelta))
    //$$ }
    //#endif
}
