package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import com.google.common.collect.ObjectArrays;
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(GuiChat.class)
public class Mixin_MergeClientCommandsForAutoComplete {
    @Unique private final Set<String> omnicore$autoCompletions = new HashSet<>();

     @Inject(method = "sendAutocompleteRequest", at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I", shift = At.Shift.AFTER))
    private void omnicore$populateAutoComplete(
            String partialMessage,
            String nextWord,
            CallbackInfo ci
    ) {
        if (!partialMessage.startsWith("/")) {
            return;
        }

        partialMessage = partialMessage.substring(1);

        this.omnicore$autoCompletions.clear();
        this.omnicore$autoCompletions.addAll(ClientCommandInternals.retrieveAutoComplete(partialMessage));
    }

    @ModifyVariable(method = "onAutocompleteResponse", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER), argsOnly = true)
    private String[] omnicore$provideAutoComplete(String[] original) {
        String[] fullAutoComplete = this.omnicore$autoCompletions.toArray(new String[0]);
        if (fullAutoComplete.length > 0) {
            original = ObjectArrays.concat(fullAutoComplete, original, String.class);
        }

        return original;
    }

    @Redirect(method = {"onAutocompleteResponse", "autocompletePlayerNames"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;writeText(Ljava/lang/String;)V"))
    private void omnicore$removeSuggestionFormatting(GuiTextField textField, String text) {
        String stripped = EnumChatFormatting.getTextWithoutFormattingCodes(text);
        textField.writeText(stripped);
    }
}
//#endif
