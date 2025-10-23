package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

public interface CommandCompletable {
    public companion object {
        @JvmStatic
        public fun suggestMatching(
            candidates: Collection<String>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            for (candidate in candidates) {
                if (candidate.startsWith(builder.remaining.lowercase())) {
                    builder.suggest(candidate)
                }
            }

            return builder.buildFuture()
        }

        @JvmStatic
        public fun suggestMatching(
            supplier: (String) -> Collection<String>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            val candidates = supplier(builder.remaining.lowercase())
            for (candidate in candidates) {
                if (candidate.startsWith(builder.remaining.lowercase())) {
                    builder.suggest(candidate)
                }
            }

            return builder.buildFuture()
        }
    }

    public val id: String
}