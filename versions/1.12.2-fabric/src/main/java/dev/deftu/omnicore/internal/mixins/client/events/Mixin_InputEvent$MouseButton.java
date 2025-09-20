package dev.deftu.omnicore.internal.mixins.client.events;

//#if FABRIC
import dev.deftu.omnicore.api.OmniCore;
import dev.deftu.omnicore.api.client.events.input.InputEvent;
import dev.deftu.omnicore.api.client.events.input.InputState;
import dev.deftu.omnicore.api.client.input.KeyboardModifiers;
import dev.deftu.omnicore.api.client.input.OmniMouseButton;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class Mixin_InputEvent$MouseButton {
    @Inject(
            //#if MC >= 1.12.2
            method = "tickMouse",
            //#else
            //$$ method = "tick",
            //#endif
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(IZ)V",
                    shift = At.Shift.AFTER
                    //#if MC < 1.12.2
                    //$$ , ordinal = 0
                    //#endif
            )
    )
    private void omnicore$onMouse(CallbackInfo ci) {
        int button = Mouse.getEventButton();
        InputState state = !Mouse.getEventButtonState() ? InputState.RELEASED : InputState.PRESSED;
        KeyboardModifiers mods = KeyboardModifiers.get();
        OmniCore.getEventBus().post(new InputEvent.MouseButton(state, new OmniMouseButton(button), mods));
    }
}
//#endif
