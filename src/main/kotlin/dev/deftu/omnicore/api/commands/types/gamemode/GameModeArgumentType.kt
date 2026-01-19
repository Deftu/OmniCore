package dev.deftu.omnicore.api.commands.types.gamemode

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.deftu.omnicore.api.OmniGameMode
import dev.deftu.omnicore.api.commands.ArgumentTypeVariant
import dev.deftu.omnicore.api.commands.CommandCompletable
import dev.deftu.omnicore.api.commands.VarianceArgumentType
import java.util.concurrent.CompletableFuture
import kotlin.text.startsWith

public class GameModeArgumentType : VarianceArgumentType<OmniGameMode> {
    public companion object {
        @JvmStatic
        public fun create(): GameModeArgumentType {
            return GameModeArgumentType()
        }

        @JvmStatic
        public fun <T> get(context: CommandContext<T>, name: String): OmniGameMode {
            return context.getArgument(name, OmniGameMode::class.java)
        }
    }

    override val variants: List<ArgumentTypeVariant<OmniGameMode>> = listOf(
        NamedModeVariant,
        IndexedModeVariant,
    )

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S?>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return CommandCompletable.suggestMatching({ input ->
            val input = input.lowercase()
            OmniGameMode.COMPLETABLE.map { mode ->
                buildList {
                    if (mode.index.toString().startsWith(input)) {
                        add(mode.index.toString())
                    }

                    if (mode.id.startsWith(input)) {
                        add(mode.id)
                    }

                    for (alias in mode.aliases) {
                        if (alias.startsWith(input)) {
                            add(alias)
                        }
                    }
                }
            }.flatten()
        }, builder)
    }
}
