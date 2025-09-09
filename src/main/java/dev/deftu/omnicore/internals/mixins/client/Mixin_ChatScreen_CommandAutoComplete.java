package dev.deftu.omnicore.internals.mixins.client;

//#if FABRIC && MC >= 1.16.5 && MC <= 1.18.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import dev.deftu.omnicore.client.OmniClientCommands;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//#if MC >= 1.18.2
//$$ import net.minecraft.client.network.ClientPlayNetworkHandler;
//$$ import net.minecraft.command.CommandSource;
//$$ import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
//#else
//$$ import net.minecraft.client.multiplayer.ClientPacketListener;
//$$ import net.minecraft.commands.SharedSuggestionProvider;
//$$ import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
//#endif
//$$
//#if MC >= 1.18.2
//$$ @Mixin(ClientPlayNetworkHandler.class)
//#else
//$$ @Mixin(ClientPacketListener.class)
//#endif
//$$ public class Mixin_ChatScreen_CommandAutoComplete {
//$$
//$$     @Shadow
//#if MC >= 1.18.2
//$$     public CommandDispatcher<CommandSource> commandDispatcher;
//#else
//$$     public CommandDispatcher<SharedSuggestionProvider> commands;
//#endif
//$$
//$$     @Inject(
//#if MC >= 1.18.2
//$$         method = "onCommandTree",
//#else
//$$         method = "handleCommands",
//#endif
//$$         at = @At("TAIL")
//$$     )
//$$     private void omnicore$mergeClientCommands(
//#if MC >= 1.18.2
//$$             CommandTreeS2CPacket packet,
//#else
//$$             ClientboundCommandsPacket packet,
//#endif
//$$             CallbackInfo ci
//$$     ) {
//$$         OmniClientCommands.copyClientCommands$OmniCore(
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
