package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPlayerEntity.class)
public class Mixin_ExecuteClientCommands {
    @Inject(method = "sendCommand(Ljava/lang/String;)Z", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, CallbackInfoReturnable<Boolean> cir) {
        if (ClientCommandInternals.execute(command)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "sendCommandInternal", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, Text text, CallbackInfo ci) {
        if (ClientCommandInternals.execute(command)) {
            ci.cancel();
        }
    }
}
//#endif
