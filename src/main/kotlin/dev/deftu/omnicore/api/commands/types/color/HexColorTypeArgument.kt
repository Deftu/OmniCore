package dev.deftu.omnicore.api.commands.types.color

import com.mojang.brigadier.StringReader
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.commands.ArgumentTypeVariant

public object HexColorTypeArgument : ArgumentTypeVariant<OmniColor> {
    private val HEX_6 = "^[0-9a-f]{6}$".toRegex(RegexOption.IGNORE_CASE)
    private val HEX_8 = "^[0-9a-f]{8}$".toRegex(RegexOption.IGNORE_CASE)
    private val HEX_3 = "^[0-9a-f]{3}$".toRegex(RegexOption.IGNORE_CASE)
    private val HEX_4 = "^[0-9a-f]{4}$".toRegex(RegexOption.IGNORE_CASE)

    override fun parse(reader: StringReader): OmniColor? {
        val start = reader.cursor
        while (reader.canRead() && !reader.peek().isWhitespace()) {
            reader.skip()
        }

        val str = normalizeHexString(reader.string.substring(start, reader.cursor))

        return when {
            HEX_8.matches(str) -> {
                val a = str.take(2).toInt(16)
                val r = str.substring(2, 4).toInt(16)
                val g = str.substring(4, 6).toInt(16)
                val b = str.substring(6, 8).toInt(16)
                OmniColor.rgba(r, g, b, a)
            }

            HEX_6.matches(str) -> {
                val r = str.take(2).toInt(16)
                val g = str.substring(2, 4).toInt(16)
                val b = str.substring(4, 6).toInt(16)
                OmniColor.rgb(r, g, b)
            }

            HEX_4.matches(str) -> {
                val a = expandNibble(str[0]).toInt(16)
                val r = expandNibble(str[1]).toInt(16)
                val g = expandNibble(str[2]).toInt(16)
                val b = expandNibble(str[3]).toInt(16)
                OmniColor.rgba(r, g, b, a)
            }

            HEX_3.matches(str) -> {
                val r = expandNibble(str[0]).toInt(16)
                val g = expandNibble(str[1]).toInt(16)
                val b = expandNibble(str[2]).toInt(16)
                OmniColor.rgb(r, g, b)
            }

            else -> null
        }
    }

    private fun normalizeHexString(value: String): String {
        return when {
            value.startsWith("#") -> value.substring(1)
            value.startsWith("0x") -> value.substring(2)
            else -> value
        }
    }

    /**
     * Expands a single hex nibble to a full byte string.
     * E.g. 'F' -> "FF"
     */
    private fun expandNibble(ch: Char): String {
        return "$ch$ch"
    }
}
