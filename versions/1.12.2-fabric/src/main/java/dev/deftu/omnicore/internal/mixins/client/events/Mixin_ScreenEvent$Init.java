package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.ScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class Mixin_ScreenEvent$Init {
    @Inject(method = "setWorldAndResolution", at = @At("HEAD"))
    private void omnicore$onInit(Minecraft client, int width, int height, CallbackInfo ci) {
        ScreenEvent.Init.Pre event = new ScreenEvent.Init.Pre((GuiScreen) (Object) this);
        OmniCore.getEventBus().post(event);
    }

    @Inject(method = "setWorldAndResolution", at = @At("TAIL"))
    private void omnicore$onInitReturn(Minecraft client, int width, int height, CallbackInfo ci) {
        ScreenEvent.Init.Post event = new ScreenEvent.Init.Post((GuiScreen) (Object) this);
        OmniCore.getEventBus().post(event);
    }
}
//#endif
