package dev.deftu.omnicore.internal.mixins.client.events;

import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.HudRenderEvent;
import dev.deftu.omnicore.api.client.render.OmniRenderTicks;
import dev.deftu.omnicore.api.client.render.OmniRenderingContext;
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack;
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class Mixin_HudRenderEvent {
    @Inject(method = "render", at = @At("TAIL"))
    private void omnicore$onHudRender(DrawContext ctx, RenderTickCounter renderTickCounter, CallbackInfo ci) {
        OmniMatrixStack matrixStack = OmniMatrixStacks.vanilla(ctx);
        OmniRenderingContext context = new OmniRenderingContext(ctx, matrixStack);
        float tickDelta = OmniRenderTicks.get(true, renderTickCounter);
        OmniCore.getEventBus().post(new HudRenderEvent(context, tickDelta));
    }
}
