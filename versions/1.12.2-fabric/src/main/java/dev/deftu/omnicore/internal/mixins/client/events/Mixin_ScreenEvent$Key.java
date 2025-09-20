package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.ScreenEvent;
import dev.deftu.omnicore.api.client.input.KeyboardModifiers;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class Mixin_ScreenEvent$Key {
    @Shadow protected abstract void keyPressed(char keyChar, int keyCode);
    @Unique private boolean wasPressed = false;

    @Inject(method = "handleKeyboard", at = @At("HEAD"))
    private void omnicore$onKeyboardTick(CallbackInfo ci) {
        if (!Keyboard.getEventKeyState() && wasPressed) {
            wasPressed = false;

            Screen $this = (Screen) (Object) this;
            ScreenEvent.KeyRelease.Pre event = new ScreenEvent.KeyRelease.Pre($this, Keyboard.getEventKey(), -1, KeyboardModifiers.get());
            OmniCore.getEventBus().post(event);
            if (!event.isCancelled()) {
                OmniCore.getEventBus().post(new ScreenEvent.KeyRelease.Post($this, Keyboard.getEventKey(), -1, KeyboardModifiers.get()));
            }
        }
    }

    @Redirect(method = "handleKeyboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;keyPressed(CI)V"))
    private void omnicore$onKeyboardTick(Screen instance, char keyChar, int keyCode) {
        ScreenEvent.KeyPress.Pre event = new ScreenEvent.KeyPress.Pre(instance, keyCode, -1, KeyboardModifiers.get());
        OmniCore.getEventBus().post(event);
        if (!event.isCancelled()) {
            keyPressed(keyChar, keyCode);
            OmniCore.getEventBus().post(new ScreenEvent.KeyPress.Post(instance, keyCode, -1, KeyboardModifiers.get()));
            wasPressed = true;
        }
    }
}
//#endif
