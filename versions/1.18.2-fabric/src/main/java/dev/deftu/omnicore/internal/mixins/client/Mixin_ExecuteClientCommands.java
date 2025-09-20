package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public class Mixin_ExecuteClientCommands {
    @Inject(
        method = "sendMessage(Ljava/lang/String;Z)V",
        at = @At(
            value = "INVOKE",
            //#if MC >= 1.16.5
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V",
            //#else
            //$$ target = "Lnet/minecraft/entity/player/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V",
            //#endif
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
