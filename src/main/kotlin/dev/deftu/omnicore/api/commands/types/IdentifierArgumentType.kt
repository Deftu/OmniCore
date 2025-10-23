package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import dev.deftu.omnicore.api.identifierOrNull
import net.minecraft.util.Identifier

public class IdentifierArgumentType : ArgumentType<Identifier> {
    public companion object {
        private val EXAMPLES = listOf("foo", "foo:bar", "012")
        private val EXCEPTION = SimpleCommandExceptionType {
            "Invalid resource location"
        }

        public fun identifier(): IdentifierArgumentType {
            return IdentifierArgumentType()
        }

        private fun Char.isValid(): Boolean {
            return this in '0'..'9' || this in 'a'..'z' || this in 'A'..'Z' || this == '_' || this == '-' || this == '.' || this == '/' || this == ':'
        }
    }

    override fun parse(reader: StringReader): Identifier? {
        val start = reader.cursor
        while (reader.canRead() && reader.peek().isValid()) {
            reader.skip()
        }

        val string = reader.string.substring(start, reader.cursor)
        return identifierOrNull(string) ?: throw EXCEPTION.createWithContext(reader)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}
