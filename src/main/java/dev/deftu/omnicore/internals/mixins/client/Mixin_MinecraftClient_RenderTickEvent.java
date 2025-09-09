package dev.deftu.omnicore.internals.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.OmniCore;
import dev.deftu.omnicore.client.events.RenderTickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class Mixin_MinecraftClient_RenderTickEvent {

    @Inject(
            //#if MC >= 1.16.5
            method = "render",
            //#else
            //$$ method = "runGameLoop",
            //#endif
            at = @At("HEAD")
    )
    public void onRenderTick(
            //#if MC >= 1.16.5
            boolean ticking,
            //#endif
            CallbackInfo ci
    ) {
        OmniCore.getEventBus().post(RenderTickEvent.Pre.INSTANCE);
    }

    @Inject(
            //#if MC >= 1.16.5
            method = "render",
            //#else
            //$$ method = "runGameLoop",
            //#endif
            at = @At("RETURN")
    )
    public void onRenderTickEnd(
            //#if MC >= 1.16.5
            boolean ticking,
            //#endif
            CallbackInfo ci
    ) {
        OmniCore.getEventBus().post(RenderTickEvent.Post.INSTANCE);
    }

}
//#endif
