package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class Mixin_ExecuteClientCommands {
    @Inject(
        method = "sendMessage(Ljava/lang/String;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;chat(Ljava/lang/String;)V",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void omnicore$onSendMessage(String message, boolean actionBar, CallbackInfo ci) {
        if (!message.startsWith("/")) {
            return;
        }

        message = message.substring(1);
        if (ClientCommandInternals.execute(message)) {
            ci.cancel();
        }
    }
}
//#endif
