package dev.deftu.omnicore.internal.mixins.client;

import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import net.minecraft.client.renderer.RenderStateShard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderStateShard.class)
public class Mixin_OverrideRenderTypeTarget {
    @Unique private Function0<Unit> omnicore$unbind;

    @Inject(method = "setupRenderState", at = @At("RETURN"))
    private void omnicore$overrideRenderTarget(CallbackInfo ci) {
        if (FramebufferInternals.INSTANCE.isBoundOverride()) {
            omnicore$unbind = FramebufferInternals.bind(FramebufferInternals.INSTANCE.getCurrentBoundFramebuffer());
        }
    }

    @Inject(method = "clearRenderState", at = @At("HEAD"))
    private void omnicore$unbindOverridenRenderTarget(CallbackInfo ci) {
        if (FramebufferInternals.INSTANCE.isBoundOverride()) {
            // Usually unnecessary to unbind since Minecraft's will rebind on it's own but we might as well for compatibility with mods that do weird stuff with framebuffers
            if (omnicore$unbind != null) {
                omnicore$unbind.invoke();
                omnicore$unbind = null;
            }
        }
    }
}
