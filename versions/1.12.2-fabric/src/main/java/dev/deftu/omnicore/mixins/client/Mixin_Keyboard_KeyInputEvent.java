package dev.deftu.omnicore.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.OmniCore;
import dev.deftu.omnicore.client.OmniKeyboard;
import dev.deftu.omnicore.client.events.InputEvent;
import dev.deftu.omnicore.client.events.InputState;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class Mixin_Keyboard_KeyInputEvent {

    @Inject(method = "handleKeyInput", at = @At("HEAD"))
    private void omnicore$onKey(CallbackInfo ci) {
        int button = Keyboard.getEventKey();
        InputState state = !Keyboard.getEventKeyState() ? InputState.RELEASED : Keyboard.isRepeatEvent() ? InputState.REPEATED : InputState.PRESSED;
        OmniKeyboard.KeyboardModifiers mods = OmniKeyboard.getModifiers();
        OmniCore.getEventBus().post(new InputEvent.Key(state, button, -1, mods));
    }

}
//#endif
