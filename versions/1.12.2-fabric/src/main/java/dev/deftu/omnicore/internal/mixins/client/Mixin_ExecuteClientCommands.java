package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class Mixin_ExecuteClientCommands {
    @Inject(
        method = "sendChatMessage(Ljava/lang/String;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/entity/EntityPlayerSP;sendChatMessage(Ljava/lang/String;)V",
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
