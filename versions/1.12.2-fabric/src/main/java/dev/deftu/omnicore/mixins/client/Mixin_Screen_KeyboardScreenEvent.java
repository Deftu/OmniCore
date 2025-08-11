package dev.deftu.omnicore.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.OmniCore;
import dev.deftu.omnicore.client.OmniKeyboard;
import dev.deftu.omnicore.client.events.ScreenEvent;
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
public abstract class Mixin_Screen_KeyboardScreenEvent {
    @Shadow protected abstract void keyPressed(char keyChar, int keyCode);
    @Unique private boolean wasPressed = false;

    @Inject(method = "handleKeyboard", at = @At("HEAD"))
    private void omnicore$onKeyboardTick(CallbackInfo ci) {
        if (!Keyboard.getEventKeyState() && wasPressed) {
            wasPressed = false;

            Screen $this = (Screen) (Object) this;
            ScreenEvent.KeyRelease.Pre event = new ScreenEvent.KeyRelease.Pre($this, Keyboard.getEventKey(), -1, OmniKeyboard.getModifiers());
            OmniCore.getEventBus().post(event);
            if (!event.isCancelled()) {
                OmniCore.getEventBus().post(new ScreenEvent.KeyRelease.Post($this, Keyboard.getEventKey(), -1, OmniKeyboard.getModifiers()));
            }
        }
    }

    @Redirect(method = "handleKeyboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;keyPressed(CI)V"))
    private void omnicore$onKeyboardTick(Screen instance, char keyChar, int keyCode) {
        ScreenEvent.KeyPress.Pre event = new ScreenEvent.KeyPress.Pre(instance, keyCode, -1, OmniKeyboard.getModifiers());
        OmniCore.getEventBus().post(event);
        if (!event.isCancelled()) {
            keyPressed(keyChar, keyCode);
            OmniCore.getEventBus().post(new ScreenEvent.KeyPress.Post(instance, keyCode, -1, OmniKeyboard.getModifiers()));
            wasPressed = true;
        }
    }
}
//#endif
