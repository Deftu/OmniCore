package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.OmniClient;
import dev.deftu.omnicore.api.client.events.input.InputEvent;
import dev.deftu.omnicore.api.client.events.input.InputState;
import dev.deftu.omnicore.api.client.input.KeyboardModifiers;
import dev.deftu.omnicore.api.client.input.OmniMouseButtons;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.21.9
import net.minecraft.client.input.MouseInput;
//#endif

@Mixin(Mouse.class)
public class Mixin_InputEvent$MouseButton {
    @Inject(method = "onMouseButton", at = @At("HEAD"))
    private void deftulib$onMouseButton(
            long handle,
            //#if MC >= 1.21.9
            MouseInput event,
            int action,
            //#else
            //$$ int button,
            //$$ int action,
            //$$ int modifiers,
            //#endif
            CallbackInfo ci
    ) {
        if (handle != OmniClient.getWindowHandle()) {
            return;
        }

        //#if MC >= 1.21.9
        int button = event.button();
        int modifiers = event.modifiers();
        //#endif
        KeyboardModifiers mods = KeyboardModifiers.wrap(modifiers);
        InputState state = action == GLFW.GLFW_PRESS ? InputState.PRESSED : action == GLFW.GLFW_RELEASE ? InputState.RELEASED : InputState.REPEATED;
        OmniCore.getEventBus().post(new InputEvent.MouseButton(state, OmniMouseButtons.from(button), mods));
    }
}
//#endif
