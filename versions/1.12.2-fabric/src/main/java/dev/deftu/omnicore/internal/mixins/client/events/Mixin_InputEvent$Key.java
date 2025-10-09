package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.input.InputEvent;
import dev.deftu.omnicore.api.client.events.input.InputState;
import dev.deftu.omnicore.api.client.input.KeyboardModifiers;
import dev.deftu.omnicore.api.client.input.OmniKeys;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class Mixin_InputEvent$Key {

    @Inject(method = "handleKeyInput", at = @At("HEAD"))
    private void omnicore$onKey(CallbackInfo ci) {
        int key = Keyboard.getEventKey();
        InputState state = !Keyboard.getEventKeyState() ? InputState.RELEASED : Keyboard.isRepeatEvent() ? InputState.REPEATED : InputState.PRESSED;
        KeyboardModifiers mods = KeyboardModifiers.get();
        OmniCore.getEventBus().post(new InputEvent.Key(state, OmniKeys.from(key), -1, mods));
    }

}
//#endif
