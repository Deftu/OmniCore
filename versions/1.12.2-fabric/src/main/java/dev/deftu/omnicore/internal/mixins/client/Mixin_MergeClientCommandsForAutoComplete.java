package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import com.google.common.collect.ObjectArrays;
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(PathNodeMaker.class)
public class Mixin_MergeClientCommandsForAutoComplete {
    @Unique private final Set<String> omnicore$autoCompletions = new HashSet<>();

     @Inject(
             //#if MC == 1.8.9
             //$$ method = "requestAutocomplete",
             //#else
             method = "method_12184",
             //#endif
            at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I", shift = At.Shift.AFTER)
    )
    private void omnicore$populateAutoComplete(
            String partialMessage,
            //#if MC == 1.8.9
            //$$ String nextWord,
            //#endif
            CallbackInfo ci
    ) {
        if (!partialMessage.startsWith("/")) {
            return;
        }

        partialMessage = partialMessage.substring(1);

        this.omnicore$autoCompletions.clear();
        this.omnicore$autoCompletions.addAll(ClientCommandInternals.retrieveAutoComplete(partialMessage));
    }

    @ModifyVariable(
            //#if MC == 1.8.9
            //$$ method = "setSuggestions",
            //#else
            method = "method_12185",
            //#endif
            at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER),
            argsOnly = true
    )
    private String[] omnicore$provideAutoComplete(String[] original) {
        String[] fullAutoComplete = this.omnicore$autoCompletions.toArray(new String[0]);
        if (fullAutoComplete.length > 0) {
            original = ObjectArrays.concat(fullAutoComplete, original, String.class);
        }

        return original;
    }

    @Redirect(
            //#if MC == 1.8.9
            //$$ method = {"showSuggestion", "setSuggestions"},
            //#else
            method = {"method_12183", "method_12185"},
            //#endif
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;write(Ljava/lang/String;)V")
    )
    private void omnicore$removeSuggestionFormatting(TextFieldWidget textField, String text) {
        String stripped = Formatting.strip(text);
        textField.write(stripped);
    }
}
//#endif
