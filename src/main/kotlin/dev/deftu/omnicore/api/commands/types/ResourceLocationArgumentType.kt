package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import dev.deftu.omnicore.api.locationOrNull
import net.minecraft.resources.ResourceLocation

public class ResourceLocationArgumentType : ArgumentType<ResourceLocation> {
    public companion object {
        private val EXAMPLES = listOf("foo", "foo:bar", "012")
        private val EXCEPTION = SimpleCommandExceptionType {
            "Invalid resource location"
        }

        @JvmStatic
        public fun resourceLocation(): ResourceLocationArgumentType {
            return ResourceLocationArgumentType()
        }

        @JvmStatic
        public fun <T> get(context: CommandContext<T>, name: String): ResourceLocation {
            return context.getArgument(name, ResourceLocation::class.java)
        }

        private fun Char.isValid(): Boolean {
            return this in '0'..'9' || this in 'a'..'z' || this in 'A'..'Z' || this == '_' || this == '-' || this == '.' || this == '/' || this == ':'
        }
    }

    override fun parse(reader: StringReader): ResourceLocation? {
        val start = reader.cursor
        while (reader.canRead() && reader.peek().isValid()) {
            reader.skip()
        }

        val string = reader.string.substring(start, reader.cursor)
        return locationOrNull(string) ?: throw EXCEPTION.createWithContext(reader)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}
