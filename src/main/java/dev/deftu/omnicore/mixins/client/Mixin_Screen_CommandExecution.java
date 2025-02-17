package dev.deftu.omnicore.mixins.client;

//#if FABRIC && MC <= 1.12.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
//$$ import dev.deftu.omnicore.client.OmniClientCommands;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//#if MC >= 1.16.5
//$$ import net.minecraft.client.gui.screens.Screen;
//#else
//$$ import net.minecraft.client.gui.screen.Screen;
//#endif
//$$
//$$ @Mixin(Screen.class)
//$$ public class Mixin_Screen_CommandExecution {
//$$
//$$     @Inject(
//$$         method = "sendMessage(Ljava/lang/String;Z)V",
//$$         at = @At(
//$$             value = "INVOKE",
//#if MC >= 1.16.5
//$$             target = "Lnet/minecraft/client/player/LocalPlayer;chat(Ljava/lang/String;)V",
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
//$$         if (OmniClientCommands.execute$OmniCore(message) == 0) {
//$$             return;
//$$         }
//$$
//$$         ci.cancel();
//$$     }
//$$
//$$ }
//#endif
