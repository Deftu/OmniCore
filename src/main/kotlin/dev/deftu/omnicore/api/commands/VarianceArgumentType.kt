package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType

public interface VarianceArgumentType<T> : ArgumentType<T> {
    public companion object {
        private val EXCEPTION = SimpleCommandExceptionType {
            "No variants matched"
        }

        @JvmStatic
        public fun <T> of(
            variants: List<ArgumentTypeVariant<T>>
        ): VarianceArgumentType<T> {
            return object : VarianceArgumentType<T> {
                override val variants: List<ArgumentTypeVariant<T>> = variants
            }
        }
    }

    public val variants: List<ArgumentTypeVariant<T>>

    override fun parse(reader: StringReader): T? {
        for (variant in variants) {
            val start = reader.cursor

            try {
                val result = variant.parse(reader)
                if (result != null) {
                    return result
                }
            } catch (_: Exception) {
            }

            reader.cursor = start
        }

        return parseDefault(reader)
    }

    public fun parseDefault(reader: StringReader): T? {
        throw EXCEPTION.createWithContext(reader)
    }
}
