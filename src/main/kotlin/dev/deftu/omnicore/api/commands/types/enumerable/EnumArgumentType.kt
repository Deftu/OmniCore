package dev.deftu.omnicore.api.commands.types.enumerable

import com.mojang.brigadier.Message
import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.deftu.omnicore.api.commands.CommandCompletable
import java.util.concurrent.CompletableFuture

public open class EnumArgumentType<T> protected constructor(
    private val values: Array<T>
) : ArgumentType<T> where T : Enum<T>, T : CommandCompletable {
    public companion object {
        private val EXCEPTION = DynamicCommandExceptionType { value: Any ->
            Message { "Invalid value '$value'" }
        }

        @JvmStatic
        public fun <T> of(values: Array<T>): EnumArgumentType<T>
            where T : Enum<T>, T : CommandCompletable
        {
            return EnumArgumentType(values)
        }
    }

    public constructor(supplier: () -> Array<T>) : this(supplier())

    override fun parse(reader: StringReader): T {
        val value = reader.readUnquotedString()
        return values.firstOrNull {
            it.id.equals(value, ignoreCase = true)
        } ?: throw EXCEPTION.createWithContext(reader, value)
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val candidates = values
            .asList()
            .map(CommandCompletable::id)

        return CommandCompletable.suggestMatching(candidates, builder)
    }

    override fun getExamples(): Collection<String> {
        return values
            .asList()
            .map(CommandCompletable::id)
            .take(2)
    }
}
