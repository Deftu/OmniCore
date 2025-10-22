package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.Message
import dev.deftu.omnicore.api.color.NamedColor
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.color.OmniColors
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

@ApiStatus.Experimental
public class OmniColorArgumentType : ArgumentType<OmniColor> {
    public companion object {
        private val EXAMPLES: List<String> = listOf(
            OmniColors.RED.toHexARGB(),
            OmniColors.BLUE.toHexRGB(),
            "green",
            "255 255 0",
            "0, 255, 255, 128"
        )

        private val EXCEPTION: SimpleCommandExceptionType = SimpleCommandExceptionType(Message { "Unknown color (try a name, hex like #RRGGBB or AARRGGBB, or RGB[A] ints)" })

        private val HEX_6 = Regex("^[0-9a-f]{6}$", RegexOption.IGNORE_CASE)
        private val HEX_8 = Regex("^[0-9a-f]{8}$", RegexOption.IGNORE_CASE)
        private val HEX_3 = Regex("^[0-9a-f]{3}$", RegexOption.IGNORE_CASE)
        private val HEX_4 = Regex("^[0-9a-f]{4}$", RegexOption.IGNORE_CASE)

        private val SPLIT_RGB = Regex("[,\\s]+")

        @JvmStatic
        public fun color(): OmniColorArgumentType = OmniColorArgumentType()

        @JvmStatic
        public fun <T> getColor(context: CommandContext<T>, name: String): OmniColor {
            return context.getArgument(name, OmniColor::class.java)
        }

        private fun clamp255(value: Int): Int {
            return value.coerceIn(0, 255)
        }

        // e.g. 'F' -> "FF"
        private fun expandHex1(ch: Char): String {
            return "$ch$ch"
        }
    }

    override fun parse(reader: StringReader): OmniColor {
        val start = reader.cursor
        val token = reader.readString()

        // Named colors
        val named: NamedColor? = NamedColor.from(token)
        if (named != null) {
            return named.color
        }

        // Hex (with optional prefixes and shorthand)
        tryParseHex(token)?.let { return it }

        // RGB(A) ints
        tryParseRGBA(token)?.let { return it }

        reader.cursor = start
        throw EXCEPTION.createWithContext(reader)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val input = builder.remaining.lowercase()
        for (named in NamedColor.complete(input)) {
            builder.suggest(named.id)
        }

        return builder.buildFuture()
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }

    private fun tryParseHex(raw: String): OmniColor? {
        println("tryParseHex: raw='$raw'")
        var s = raw.trim()
        println("After trim: '$s'")
        if (s.startsWith("#")) {
            s = s.removePrefix("#")
        }

        if (s.startsWith("0x", ignoreCase = true)) {
            s = s.removePrefix("0x")
        }

        println("Trying hex parse: '$s'")
        when {
            HEX_8.matches(s) -> {
                val argb = s.toUInt(16).toInt()
                return OmniColor.argb(argb) // AARRGGBB
            }

            HEX_6.matches(s) -> {
                val rgb = s.toInt(16)
                return OmniColor.rgb(rgb) // RRGGBB
            }

            HEX_4.matches(s) -> {
                // `#ARGB` -> expand each nibble
                val a = expandHex1(s[0]).toInt(16)
                val r = expandHex1(s[1]).toInt(16)
                val g = expandHex1(s[2]).toInt(16)
                val b = expandHex1(s[3]).toInt(16)
                return OmniColor.argb(a, r, g, b)
            }

            HEX_3.matches(s) -> {
                // `#RGB` -> expand each nibble
                val r = expandHex1(s[0]).toInt(16)
                val g = expandHex1(s[1]).toInt(16)
                val b = expandHex1(s[2]).toInt(16)
                return OmniColor.rgb(r, g, b)
            }

            else -> return null
        }
    }

    private fun tryParseRGBA(raw: String): OmniColor? {
        val parts = raw.trim().split(SPLIT_RGB).filter { it.isNotEmpty() }
        if (parts.size != 3 && parts.size != 4) {
            return null
        }

        val numbers = parts.map { tok ->
            tok.toIntOrNull() ?: return null
        }

        val r = clamp255(numbers[0])
        val g = clamp255(numbers[1])
        val b = clamp255(numbers[2])
        val a = if (numbers.size == 4) clamp255(numbers[3]) else 255

        return OmniColor.rgba(r, g, b, a)
    }
}
