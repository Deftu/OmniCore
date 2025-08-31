package dev.deftu.omnicore.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.OmniCore;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.api.client.input.KeyboardModifiers;
import dev.deftu.omnicore.client.events.InputEvent;
import dev.deftu.omnicore.client.events.InputState;
import dev.deftu.omnicore.api.client.input.OmniKey;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class Mixin_Keyboard_KeyInputEvent {

    @Inject(method = "onKey", at = @At("HEAD"))
    private void omnicore$onKey(long handle, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (handle != OmniClient.getInstance().getWindow().getHandle()) {
            return;
        }

        KeyboardModifiers mods = KeyboardModifiers.wrap(modifiers);
        InputState state = action == GLFW.GLFW_PRESS ? InputState.PRESSED : action == GLFW.GLFW_RELEASE ? InputState.RELEASED : InputState.REPEATED;

        OmniCore.getEventBus().post(new InputEvent.Key(state, new OmniKey(key), scancode, mods));
    }

}
//#endif
