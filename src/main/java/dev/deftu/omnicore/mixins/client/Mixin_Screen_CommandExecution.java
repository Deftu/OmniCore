package dev.deftu.omnicore.mixins.client;

//#if FABRIC
//#if MC <= 1.18.2
//$$ import dev.deftu.omnicore.client.OmniClientCommands;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import net.minecraft.client.gui.screen.Screen;
//$$
//$$ @Mixin(Screen.class)
//$$ public class Mixin_Screen_CommandExecution {
//$$
//$$     @Inject(
//$$         method = "sendMessage(Ljava/lang/String;Z)V",
//$$         at = @At(
//$$             value = "INVOKE",
//#if MC >= 1.16.5
//$$             target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V",
//#else
//$$             target = "Lnet/minecraft/entity/player/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V",
//#endif
//$$             shift = At.Shift.BEFORE
//$$         ),
//$$         cancellable = true
//$$     )
//$$     private void omnicore$onSendMessage(String message, boolean actionBar, CallbackInfo ci) {
//$$         if (!message.startsWith("/")) {
//$$             return;
//$$         }
//$$
//$$         message = message.substring(1);
//$$         if (OmniClientCommands.execute(message)) {
//$$             ci.cancel();
//$$         }
//$$     }
//$$
//$$ }
//#elseif MC <= 1.19.2
//$$ import dev.deftu.omnicore.client.OmniClientCommands;
//$$ import net.minecraft.client.network.ClientPlayerEntity;
//$$ import net.minecraft.text.Text;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$
//$$ @Mixin(value = ClientPlayerEntity.class)
//$$ public class Mixin_Screen_CommandExecution {
//$$
//$$     @Inject(method = "sendCommand(Ljava/lang/String;)Z", at = @At("HEAD"), cancellable = true)
//$$     private void omnicore$overwriteCommandSend(String command, CallbackInfoReturnable<Boolean> cir) {
//$$         if (OmniClientCommands.execute(command)) {
//$$             cir.setReturnValue(true);
//$$         }
//$$     }
//$$
//$$     @Inject(method = "sendCommandInternal", at = @At("HEAD"), cancellable = true)
//$$     private void omnicore$overwriteCommandSend(String command, Text text, CallbackInfo ci) {
//$$         if (OmniClientCommands.execute(command)) {
//$$             ci.cancel();
//$$         }
//$$     }
//$$
//$$ }
//#else
import dev.deftu.omnicore.client.OmniClientCommands;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 1.21.6
import net.minecraft.client.gui.screen.Screen;
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
//#endif
