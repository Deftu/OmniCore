package dev.deftu.omnicore.api.client.render

import dev.deftu.textile.MutableText
import dev.deftu.textile.StringVisitable
import dev.deftu.textile.Text
import dev.deftu.textile.TextContent
import dev.deftu.textile.TextStyle
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public object OmniTextWrapping {
    private enum class TokenKind { WORD, SPACE, HARD_BREAK }
    private data class Token(
        val kind: TokenKind,
        val text: String,
        val style: TextStyle
    )

    @JvmStatic
    public fun wrap(text: StringVisitable, maxWidth: Int): List<Text> {
        require(maxWidth > 0) { "maxWidth must be > 0" }

        val tokens = tokenizeAll(text)
        if (tokens.isEmpty()) {
            return emptyList()
        }

        val lines = mutableListOf<Text>()
        var currentLine: MutableText? = null
        var currentLineWidth = 0

        fun emitLine() {
            val line = currentLine ?: Text.empty()
            lines += line
            currentLine = null
            currentLineWidth = 0
        }

        fun appendPiece(piece: String, style: TextStyle) {
            currentLine = appendStyled(currentLine, piece, style)
            currentLineWidth += measure(piece, style)
        }

        var index = 0
        while (index < tokens.size) {
            val token = tokens[index]
            when (token.kind) {
                TokenKind.HARD_BREAK -> {
                    emitLine()
                    index += 1
                }

                TokenKind.SPACE -> {
                    if (currentLineWidth == 0) {
                        index += 1
                        continue
                    }

                    var lookahead = index + 1
                    var nextWord: Token? = null
                    while (lookahead < tokens.size) {
                        val n = tokens[lookahead]
                        if (n.kind == TokenKind.HARD_BREAK) break
                        if (n.kind == TokenKind.WORD) {
                            nextWord = n
                            break
                        }
                        lookahead += 1
                    }

                    if (nextWord == null) {
                        index += 1
                        continue
                    }

                    val spaceWidth = measure(token.text, token.style)
                    val wordWidth = measure(nextWord.text, nextWord.style)
                    val fits = currentLineWidth + spaceWidth + wordWidth <= maxWidth
                    if (fits) {
                        appendPiece(token.text, token.style)
                        index += 1
                    } else {
                        emitLine()
                        index += 1
                    }
                }

                TokenKind.WORD -> {
                    val wordWidth = measure(token.text, token.style)
                    val remaining = maxWidth - currentLineWidth

                    if (wordWidth <= remaining) {
                        appendPiece(token.text, token.style)
                        index += 1
                    } else if (currentLineWidth == 0) {
                        var rest = token.text
                        var room = remaining
                        while (rest.isNotEmpty()) {
                            val head = fitPrefix(rest, token.style, ::measure, room)
                            if (head.isEmpty()) {
                                val ch = rest.take(1)
                                appendPiece(ch, token.style)
                                rest = rest.drop(1)
                            } else {
                                appendPiece(head, token.style)
                                rest = rest.drop(head.length)
                            }

                            if (rest.isNotEmpty()) {
                                emitLine()
                                room = maxWidth
                            }
                        }
                        index += 1
                    } else {
                        emitLine()
                    }
                }
            }
        }

        if (currentLine != null) {
            emitLine()
        }

        return lines
    }

    @JvmStatic
    public fun wrap(text: String, maxWidth: Int): List<Text> {
        return wrap(StringVisitable.plain(text), maxWidth)
    }

    private fun tokenizeAll(src: StringVisitable): List<Token> {
        val tokens = mutableListOf<Token>()

        val runs = mutableListOf<Pair<String, TextStyle>>()
        src.visit({ content, style ->
            if (content.isNotEmpty()) {
                runs += content to style
            }
            null
        }, TextStyle.EMPTY)

        for ((raw, style) in runs) {
            var cursor = 0
            while (cursor < raw.length) {
                val ch = raw[cursor]
                when {
                    ch == '\n' -> {
                        tokens += Token(TokenKind.HARD_BREAK, "", style)
                        cursor += 1
                    }

                    ch.isWhitespace() -> {
                        val start = cursor
                        var end = cursor + 1
                        while (end < raw.length && raw[end].isWhitespace() && raw[end] != '\n') {
                            end += 1
                        }

                        tokens += Token(TokenKind.SPACE, raw.substring(start, end), style)
                        cursor = end
                    }

                    else -> {
                        val start = cursor
                        var end = cursor + 1
                        while (end < raw.length) {
                            val c = raw[end]
                            if (c == '\n' || c.isWhitespace()) break
                            end += 1
                        }

                        tokens += Token(TokenKind.WORD, raw.substring(start, end), style)
                        cursor = end
                    }
                }
            }
        }

        return tokens
    }

    private fun appendStyled(current: MutableText?, text: String, style: TextStyle): MutableText {
        if (current == null) {
            return Text.literal(text).setStyle(style)
        }

        val siblings = current.siblings
        if (siblings.isNotEmpty()) {
            val tail = siblings.last()
            if (tail is MutableText && tail.style == style && tail.content is TextContent.Literal) {
                val lit = tail.content as TextContent.Literal
                tail.content = TextContent.Literal(lit.text + text)
                return current
            }
        }

        current.append(Text.literal(text).setStyle(style))
        return current
    }

    private fun measure(content: String, style: TextStyle): Int {
        return OmniTextRenderer.width(Text.literal(content).setStyle(style))
    }

    private fun fitPrefix(
        word: String,
        style: TextStyle,
        measure: (String, TextStyle) -> Int,
        available: Int
    ): String {
        if (available <= 0) {
            return ""
        }

        var low = 1
        var high = word.length
        var best = 0

        while (low <= high) {
            val mid = (low + high) ushr 1
            val prefix = word.take(mid)
            val width = measure(prefix, style)
            if (width <= available) {
                best = mid
                low = mid + 1
            } else {
                high = mid - 1
            }
        }

        val safe = adjustForSurrogate(word, best)
        return word.take(safe)
    }

    private fun adjustForSurrogate(s: String, endExclusive: Int): Int {
        if (endExclusive in 1 until s.length) {
            val a = s[endExclusive - 1]
            val b = s[endExclusive]
            if (Character.isHighSurrogate(a) && Character.isLowSurrogate(b)) {
                return endExclusive - 1
            }
        }

        return endExclusive
    }
}
