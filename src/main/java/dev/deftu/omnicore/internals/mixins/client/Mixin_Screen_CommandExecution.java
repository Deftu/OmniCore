package dev.deftu.omnicore.internals.mixins.client;

//#if FABRIC
import dev.deftu.omnicore.client.OmniClientCommands;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.21.6
import net.minecraft.client.gui.screen.Screen;
//#else
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(value = ClientPlayNetworkHandler.class, priority = 999)
public class Mixin_Screen_CommandExecution {

    //#if MC >= 1.21.6
    @Inject(method = "runClickEventCommand", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, Screen screen, CallbackInfo ci) {
        if (OmniClientCommands.execute(command)) {
            ci.cancel();
        }
    }
    //#else
    //$$ @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    //$$ private void omnicore$overwriteCommandSend(String command, CallbackInfoReturnable<Boolean> cir) {
    //$$     if (OmniClientCommands.execute(command)) {
    //$$         cir.setReturnValue(true);
    //$$     }
    //$$ }
    //#endif

    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    private void omnicore$overwriteCommandSend(String command, CallbackInfo ci) {
        if (OmniClientCommands.execute(command)) {
            ci.cancel();
        }
    }

}
//#endif
