package dev.deftu.omnicore.internal.client.events

import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.api.client.events.HudRenderEvent
import dev.deftu.omnicore.api.client.render.OmniRenderTicks
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

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
        //$$
        //#else
        //$$ MinecraftForge.EVENT_BUS.register(this)
        //#endif
    }
}
