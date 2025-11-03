package dev.deftu.omnicore.internal.mixins.client.events;

import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.HudRenderEvent;
import dev.deftu.omnicore.api.client.render.OmniRenderTicks;
import dev.deftu.omnicore.api.client.render.OmniRenderingContext;
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack;
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStacks;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class Mixin_HudRenderEvent {
    @Inject(method = "render", at = @At("TAIL"))
    private void omnicore$onHudRender(GuiGraphics ctx, DeltaTracker renderTickCounter, CallbackInfo ci) {
        OmniPoseStack pose = OmniPoseStacks.vanilla(ctx);
        OmniRenderingContext context = new OmniRenderingContext(ctx, pose);
        float tickDelta = OmniRenderTicks.get(true, renderTickCounter);
        OmniCore.getEventBus().post(new HudRenderEvent(context, tickDelta));
    }
}
