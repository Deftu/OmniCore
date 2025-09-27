package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.OmniClient;
import dev.deftu.omnicore.api.client.events.input.InputEvent;
import dev.deftu.omnicore.api.client.events.input.InputState;
import dev.deftu.omnicore.api.client.input.KeyboardModifiers;
import dev.deftu.omnicore.api.client.input.OmniKey;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.21.9
//$$ import net.minecraft.client.input.KeyInput;
//#endif

@Mixin(Keyboard.class)
public class Mixin_InputEvent$Key {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void omnicore$onKey(
            long handle,
            //#if MC >= 1.21.9
            //$$ int action,
            //$$ KeyInput event,
            //#else
            int key,
            int scancode,
            int action,
            int modifiers,
            //#endif
            CallbackInfo ci
    ) {
        if (handle != OmniClient.getWindowHandle()) {
            return;
        }

        //#if MC >= 1.21.9
        //$$ int key = event.key();
        //$$ int scancode = event.scancode();
        //$$ int modifiers = event.modifiers();
        //#endif
        KeyboardModifiers mods = KeyboardModifiers.wrap(modifiers);
        InputState state = action == GLFW.GLFW_PRESS ? InputState.PRESSED : action == GLFW.GLFW_RELEASE ? InputState.RELEASED : InputState.REPEATED;
        OmniCore.getEventBus().post(new InputEvent.Key(state, new OmniKey(key), scancode, mods));
    }
}
//#endif
