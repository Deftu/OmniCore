package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class Mixin_ExecuteClientCommands {
    @Inject(method = "commandUnsigned", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, CallbackInfoReturnable<Boolean> cir) {
        if (ClientCommandInternals.execute(command)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, Component component, CallbackInfo ci) {
        if (ClientCommandInternals.execute(command)) {
            ci.cancel();
        }
    }
}
//#endif
