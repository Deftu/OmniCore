package dev.deftu.omnicore.mixins.client;

//#if FABRIC && MC <= 1.12.2
//$$ import com.google.common.collect.ObjectArrays;
//$$ import dev.deftu.omnicore.client.OmniClientCommands;
//$$ import dev.deftu.textile.minecraft.MinecraftTextFormat;
//$$ import net.minecraft.client.gui.widget.TextFieldWidget;
//$$ import net.minecraft.entity.ai.pathing.PathNodeMaker;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Unique;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.ModifyVariable;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ import java.util.HashSet;
//$$ import java.util.Set;
//$$
//$$ @Mixin(PathNodeMaker.class)
//$$ public class Mixin_ChatScreen_CommandAutoComplete {
//$$
//$$     @Unique
//$$     private final Set<String> omnicore$autoCompletions = new HashSet<>();
//$$
//$$     @Inject(
//#if MC == 1.8.9
//$$             method = "requestAutocomplete",
//#else
//$$             method = "method_12184",
//#endif
//$$             at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I", shift = At.Shift.AFTER)
//$$     )
//$$     private void omnicore$populateAutoComplete(
//$$             String partialMessage,
//#if MC == 1.8.9
//$$             String nextWord,
//#endif
//$$             CallbackInfo ci
//$$     ) {
//$$         if (!partialMessage.startsWith("/")) {
//$$             return;
//$$         }
//$$
//$$         partialMessage = partialMessage.substring(1);
//$$
//$$         this.omnicore$autoCompletions.clear();
//$$         this.omnicore$autoCompletions.addAll(OmniClientCommands.retrieveAutoComplete$OmniCore(partialMessage));
//$$     }
//$$
//$$     @ModifyVariable(
//#if MC == 1.8.9
//$$             method = "setSuggestions",
//#else
//$$             method = "method_12185",
//#endif
//$$             at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER),
//$$             argsOnly = true
//$$     )
//$$     private String[] omnicore$provideAutoComplete(String[] original) {
//$$         String[] fullAutoComplete = this.omnicore$autoCompletions.toArray(new String[0]);
//$$         if (fullAutoComplete.length > 0) {
//$$             original = ObjectArrays.concat(fullAutoComplete, original, String.class);
//$$         }
//$$
//$$         return original;
//$$     }
//$$
//$$     @Redirect(
//#if MC == 1.8.9
//$$             method = {"showSuggestion", "setSuggestions"},
//#else
//$$        method = {"method_12183", "method_12185"},
//#endif
//$$             at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;write(Ljava/lang/String;)V")
//$$     )
//$$     private void omnicore$removeSuggestionFormatting(TextFieldWidget textField, String text) {
//$$         String stripped = MinecraftTextFormat.strip(text);
//$$         textField.write(stripped);
//$$     }
//$$
//$$ }
//#elseif FABRIC && MC >= 1.16.5 && MC <= 1.18.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
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
