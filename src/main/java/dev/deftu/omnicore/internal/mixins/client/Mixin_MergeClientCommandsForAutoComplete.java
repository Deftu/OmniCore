package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC && MC >= 1.16.5 && MC <= 1.18.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
//$$ import net.minecraft.client.multiplayer.ClientPacketListener;
//$$ import net.minecraft.commands.SharedSuggestionProvider;
//$$ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(ClientPacketListener.class)
//$$ public class Mixin_MergeClientCommandsForAutoComplete {
//$$
//$$     @Shadow
//#if MC >= 1.18.2
//$$     public CommandDispatcher<SharedSuggestionProvider> commandDispatcher;
//#else
//$$     public CommandDispatcher<SharedSuggestionProvider> commands;
//#endif
//$$
//$$     @Inject(method = "handleCommands", at = @At("TAIL"))
//$$     private void omnicore$mergeClientCommands(ClientboundCommandsPacket packet, CallbackInfo ci) {
//$$         ClientCommandInternals.copyClientCommands(
//#if MC >= 1.18.2
//$$             this.commandDispatcher
//#else
//$$             this.commands
//#endif
//$$         );
//$$     }
//$$
//$$ }
//#endif
