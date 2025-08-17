package dev.deftu.omnicore.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.OmniCore;
import dev.deftu.omnicore.client.OmniClient;
import dev.deftu.omnicore.client.OmniKeyboard;
import dev.deftu.omnicore.client.OmniKeyboardKt;
import dev.deftu.omnicore.client.events.InputEvent;
import dev.deftu.omnicore.client.events.InputState;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class Mixin_Mouse_MouseInputEvent {

    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void deftulib$onMouseButton(long handle, int button, int action, int modifiers, CallbackInfo ci) {
        if (handle != OmniClient.getInstance().getWindow().getHandle()) {
            return;
        }

        OmniKeyboard.KeyboardModifiers mods = OmniKeyboardKt.toKeyboardModifiers(modifiers);
        InputState state = action == GLFW.GLFW_PRESS ? InputState.PRESSED : action == GLFW.GLFW_RELEASE ? InputState.RELEASED : InputState.REPEATED;

        OmniCore.getEventBus().post(new InputEvent.MouseButton(state, button, mods));
    }

}
//#endif
