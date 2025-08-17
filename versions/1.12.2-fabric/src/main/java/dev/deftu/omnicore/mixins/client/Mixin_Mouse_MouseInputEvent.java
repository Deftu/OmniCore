package dev.deftu.omnicore.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.OmniCore;
import dev.deftu.omnicore.client.OmniKeyboard;
import dev.deftu.omnicore.client.events.InputEvent;
import dev.deftu.omnicore.client.events.InputState;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class Mixin_Mouse_MouseInputEvent {

    @Inject(method = "tickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(IZ)V", shift = At.Shift.AFTER))
    private void omnicore$onMouse(CallbackInfo ci) {
        int button = Mouse.getEventButton();
        InputState state = !Mouse.getEventButtonState() ? InputState.RELEASED : InputState.PRESSED;
        OmniKeyboard.KeyboardModifiers mods = OmniKeyboard.getModifiers();
        OmniCore.getEventBus().post(new InputEvent.MouseButton(state, button, mods));
    }

}
//#endif
