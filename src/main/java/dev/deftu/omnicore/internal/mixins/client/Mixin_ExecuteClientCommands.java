package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.21.6
import net.minecraft.client.gui.screens.Screen;
//#else
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(value = ClientPacketListener.class, priority = 999)
public class Mixin_ExecuteClientCommands {
    //#if MC >= 1.21.6
    @Inject(method = "sendUnattendedCommand", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, Screen screen, CallbackInfo ci) {
        if (ClientCommandInternals.execute(command)) {
            ci.cancel();
        }
    }
    //#else
    //$$ @Inject(method = "sendUnsignedCommand", at = @At("HEAD"), cancellable = true)
    //$$ private void omnicore$overwriteCommandSend(String command, CallbackInfoReturnable<Boolean> cir) {
    //$$     if (ClientCommandInternals.execute(command)) {
    //$$         cir.setReturnValue(true);
    //$$     }
    //$$ }
    //#endif

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, CallbackInfo ci) {
        if (ClientCommandInternals.execute(command)) {
            ci.cancel();
        }
    }
}
//#endif
