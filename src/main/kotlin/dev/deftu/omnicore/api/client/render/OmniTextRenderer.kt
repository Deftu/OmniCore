package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.color.ColorFormat
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.textile.Text
import dev.deftu.textile.TextStyle
import dev.deftu.textile.minecraft.MCText
import dev.deftu.textile.minecraft.MCTextStyle
import net.minecraft.text.Text as VanillaText
import kotlin.math.abs

//#if MC >= 1.21.6
import dev.deftu.omnicore.internal.client.render.ImmediateGlyphDrawer
//#elseif MC >= 1.16.5
//$$ import net.minecraft.text.Style
//#endif

//#if MC >= 1.20.1 && MC < 1.21.6
//$$ import net.minecraft.client.font.TextRenderer
//#endif

//#if MC < 1.16.5
//$$ import dev.deftu.omnicore.api.client.render.state.OmniRenderStates
//#endif

public object OmniTextRenderer {
    public const val COLOR_CHAR: Char = '\u00a7'
    @JvmField public val FORMATTING_CODE_PATTERN: Regex = Regex("§[0-9a-fk-or]", RegexOption.IGNORE_CASE)

    private inline val textRenderer
        get() = client.textRenderer

    @JvmStatic
    public val outlineOffsets: FloatArray by lazy(LazyThreadSafetyMode.PUBLICATION) {
        buildList(5 * 5 * 2) {
            for (dx in -2..2) {
                for (dy in -2..2) {
                    if (dx * dx != dy * dy) {
                        add(dx / 2f)
                        add(dy / 2f)
                    }
                }
            }
        }.toFloatArray()
    }

    @JvmStatic
    public val lineHeight: Int
        get() = textRenderer.fontHeight

    @JvmStatic
    public fun render(
        matrices: OmniMatrixStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        //#if MC >= 1.21.6
        val drawer = ImmediateGlyphDrawer(matrices.current.positionMatrix.vanilla)

        val glyph = textRenderer.prepare(text.asOrderedText(), x, y, color.pack(ColorFormat.ARGB), shadow, 0)
        glyph.draw(drawer)
        drawer.flush()

        return (x + width(text)).toInt() + if (shadow) 1 else 0
        //#elseif MC >= 1.20.1
        //$$ return textRenderer.draw(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color.pack(ColorFormat.ARGB),
        //$$     shadow,
        //$$     matrices.vanilla.peek().positionMatrix,
        //$$     client.bufferBuilders.entityVertexConsumers,
        //$$     TextRenderer.TextLayerType.NORMAL,
        //$$     0,
        //$$     15728880
        //$$ )
        //#elseif MC >= 1.16.5
        //$$ return if (shadow) {
        //$$     textRenderer.drawWithShadow(matrices.vanilla, text, x, y, color.pack(ColorFormat.ARGB))
        //$$ } else {
        //$$     textRenderer.draw(matrices.vanilla, text, x, y, color.pack(ColorFormat.ARGB))
        //$$ }
        //#else
        //$$ val blendState = OmniRenderStates.blend.asEnabled()
        //$$ blendState.submit()
        //$$ val xPos = textRenderer.drawString(
        //$$     text.formattedText,
        //$$     x,
        //$$     y,
        //$$     color.pack(ColorFormat.ARGB),
        //$$     shadow
        //$$ )
        //$$
        //$$ blendState.restore()
        //$$ return xPos
        //#endif
    }

    @JvmStatic
    public fun render(
        matrices: OmniMatrixStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(matrices, MCText.convert(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        matrices: OmniMatrixStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(matrices, Text.literal(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(context.matrices, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(context.matrices, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(context.matrices, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        matrices: OmniMatrixStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return when (shadowType) {
            is TextShadowType.None -> render(matrices, text, x, y, color, shadow = false)
            is TextShadowType.Drop -> render(matrices, text, x, y, color, shadow = true)
            is TextShadowType.Outline -> {
                val outlineColor = shadowType.color

                // If the outline isn't going to be visible anyway, don't waste draw calls
                if (outlineColor.alpha <= 3) {
                    return render(matrices, text, x, y, color, shadow = false)
                }

                val strippedText = stripColor(MCText.wrap(text).collapseToString())
                for (i in outlineOffsets.indices step 2) {
                    val dx = outlineOffsets[i]
                    val dy = outlineOffsets[i + 1]
                    render(matrices, strippedText, x + dx, y + dy, outlineColor, shadow = false)
                }

                render(matrices, text, x, y, color, shadow = false)
            }
        }
    }

    @JvmStatic
    public fun render(
        matrices: OmniMatrixStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(matrices, MCText.convert(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        matrices: OmniMatrixStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(matrices, Text.literal(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(context.matrices, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(context.matrices, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(context.matrices, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        matrices: OmniMatrixStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        val width = width(text)
        return render(matrices, text, x - width / 2f, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        matrices: OmniMatrixStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(matrices, MCText.convert(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        matrices: OmniMatrixStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(matrices, Text.literal(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(context.matrices, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(context.matrices, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(context.matrices, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        matrices: OmniMatrixStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        val width = width(text)
        return render(matrices, text, x - width / 2f, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        matrices: OmniMatrixStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(matrices, MCText.convert(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        matrices: OmniMatrixStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(matrices, Text.literal(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(context.matrices, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(context.matrices, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(context.matrices, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun width(text: String): Int {
        return textRenderer.getWidth(text)
    }

    @JvmStatic
    public fun width(text: Text): Int {
        return width(MCText.convert(text))
    }

    @JvmStatic
    public fun width(text: VanillaText): Int {
        //#if MC >= 1.16.5
        return textRenderer.getWidth(text)
        //#else
        //$$ return textRenderer.getStringWidth(text.unformattedText)
        //#endif
    }

    @JvmStatic
    public fun height(text: String, maxWidth: Int): Int {
        return textRenderer.getWrappedLinesHeight(text, maxWidth)
    }

    @JvmStatic
    public fun height(text: Text, maxWidth: Int): Int {
        return height(MCText.convert(text), maxWidth)
    }

    @JvmStatic
    public fun height(text: VanillaText, maxWidth: Int): Int {
        //#if MC >= 1.19.2
        return textRenderer.getWrappedLinesHeight(text, maxWidth)
        //#else
        //$$ return textRenderer.getWrappedLinesHeight(text.asString(), maxWidth)
        //#endif
    }

    @JvmStatic
    public fun stripColor(text: String): String {
        return text.replace(COLOR_CHAR, '§').replace(FORMATTING_CODE_PATTERN) {
            if (it.value[1] in '0'..'9') "" else it.value
        }
    }

    @JvmStatic
    public fun stripFormat(text: String): String {
        return text.replace(COLOR_CHAR, '§').replace(FORMATTING_CODE_PATTERN) {
            if (it.value[1] in 'a'..'r') "" else it.value
        }
    }
}
