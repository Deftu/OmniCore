package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.OmniClient;
import dev.deftu.omnicore.api.client.events.input.InputEvent;
import dev.deftu.omnicore.api.client.events.input.InputState;
import dev.deftu.omnicore.api.client.input.KeyboardModifiers;
import dev.deftu.omnicore.api.client.input.OmniMouseButtons;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class Mixin_InputEvent$MouseButton {
    @Inject(method = "onPress", at = @At("HEAD"))
    private void deftulib$onMouseButton(long handle, int button, int action, int modifiers, CallbackInfo ci) {
        if (handle != OmniClient.getWindowHandle()) {
            return;
        }

        KeyboardModifiers mods = KeyboardModifiers.wrap(modifiers);
        InputState state = action == GLFW.GLFW_PRESS ? InputState.PRESSED : action == GLFW.GLFW_RELEASE ? InputState.RELEASED : InputState.REPEATED;
        OmniCore.getEventBus().post(new InputEvent.MouseButton(state, OmniMouseButtons.from(button), mods));
    }
}
//#endif
