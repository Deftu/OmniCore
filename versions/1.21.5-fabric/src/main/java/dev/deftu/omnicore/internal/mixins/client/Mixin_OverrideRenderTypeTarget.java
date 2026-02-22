package dev.deftu.omnicore.internal.mixins.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals;
import dev.deftu.omnicore.internal.client.render.OmniRenderTarget;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderStateShard.OutputStateShard.class)
public class Mixin_OverrideRenderTypeTarget {
    @Inject(method = "getRenderTarget", at = @At("RETURN"), cancellable = true)
    private static void omnicore$overrideRenderTarget(CallbackInfoReturnable<RenderTarget> cir) {
        if (FramebufferInternals.INSTANCE.isBoundOverride()) {
            cir.setReturnValue(new OmniRenderTarget(FramebufferInternals.INSTANCE.getCurrentBoundFramebuffer()));
        }
    }
}
