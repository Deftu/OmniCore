package dev.deftu.omnicore.api.commands.types.color

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.deftu.omnicore.api.color.NamedColor
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.api.commands.ArgumentTypeVariant
import dev.deftu.omnicore.api.commands.CommandCompletable
import dev.deftu.omnicore.api.commands.VarianceArgumentType
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

@ApiStatus.Experimental
public class OmniColorArgumentType : VarianceArgumentType<OmniColor> {
    public companion object {
        private val EXAMPLES: List<String> = listOf(
            OmniColors.RED.toHexARGB(),
            OmniColors.BLUE.toHexRGB(),
            "green",
            "255 255 0",
            "0, 255, 255, 128"
        )

        @JvmStatic
        public fun create(): OmniColorArgumentType {
            return OmniColorArgumentType()
        }

        @JvmStatic
        public fun <T> get(context: CommandContext<T>, name: String): OmniColor {
            return context.getArgument(name, OmniColor::class.java)
        }
    }

    override val variants: List<ArgumentTypeVariant<OmniColor>> = listOf(
        ColorValueTypeVariant,
        HexColorTypeArgument,
        NamedColorTypeArgument,
    )

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return CommandCompletable.suggestMatching({ input ->
            NamedColor.complete(input).map(NamedColor::id)
        }, builder)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}