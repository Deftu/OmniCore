package dev.deftu.omnicore.api.commands.types.color

import com.mojang.brigadier.StringReader
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.commands.ArgumentTypeVariant

public object ColorValueTypeVariant : ArgumentTypeVariant<OmniColor> {
    override fun parse(reader: StringReader): OmniColor? {
        val start = reader.cursor
        val first = parsePart(reader)
        if (first == null) {
            reader.cursor = start
            return null
        }

        whitespaceOrComma(reader)
        val second = parsePart(reader)
        if (second == null) {
            reader.cursor = start
            return null
        }

        whitespaceOrComma(reader)
        val third = parsePart(reader)
        if (third == null) {
            reader.cursor = start
            return null
        }

        // If there's a fourth value, parse it as alpha
        if (reader.canRead() && !reader.peek().isWhitespace()) {
            whitespaceOrComma(reader)
            val fourth = parsePart(reader)
            if (fourth == null) {
                reader.cursor = start
                return null
            }

            return OmniColor.rgba(first, second, third, fourth)
        }

        return OmniColor.rgb(first, second, third)
    }

    private fun parsePart(reader: StringReader): Int? {
        val start = reader.cursor
        while (reader.canRead() && !reader.peek().isWhitespace() && reader.peek() != ',') {
            reader.skip()
        }

        val partStr = reader.string.substring(start, reader.cursor)
        return partStr.toIntOrNull()?.coerceIn(0, 255)
    }

    private fun whitespaceOrComma(reader: StringReader) {
        while (reader.canRead() && (reader.peek().isWhitespace() || reader.peek() == ',')) {
            reader.skip()
        }
    }
}
