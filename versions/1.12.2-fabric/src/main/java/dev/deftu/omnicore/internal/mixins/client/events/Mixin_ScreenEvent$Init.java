package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.ScreenEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class Mixin_ScreenEvent$Init {
    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("HEAD"))
    private void omnicore$onInit(MinecraftClient client, int width, int height, CallbackInfo ci) {
        ScreenEvent.Init.Pre event = new ScreenEvent.Init.Pre((Screen) (Object) this);
        OmniCore.getEventBus().post(event);
    }

    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("TAIL"))
    private void omnicore$onInitReturn(MinecraftClient client, int width, int height, CallbackInfo ci) {
        ScreenEvent.Init.Post event = new ScreenEvent.Init.Post((Screen) (Object) this);
        OmniCore.getEventBus().post(event);
    }
}
//#endif
