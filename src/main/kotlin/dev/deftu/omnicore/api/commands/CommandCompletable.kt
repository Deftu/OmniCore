package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.StringReader
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

        @JvmStatic
        public fun parseInteger(reader: StringReader): Int? {
            val start = reader.cursor
            while (reader.canRead() && !reader.peek().isWhitespace() && reader.peek() != ',') {
                reader.skip()
            }

            val partStr = reader.string.substring(start, reader.cursor)
            return partStr.toIntOrNull()
        }

        @JvmStatic
        public fun whitespaceOrComma(reader: StringReader) {
            while (reader.canRead() && (reader.peek().isWhitespace() || reader.peek() == ',')) {
                reader.skip()
            }
        }
    }

    public val id: String
}