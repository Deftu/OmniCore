package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.color.ColorFormat
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.textile.minecraft.MCTextFormat
import dev.deftu.textile.minecraft.MCTextHolder

//#if MC >= 1.20.1 && MC < 1.21.6
//$$ import net.minecraft.client.font.TextRenderer
//#endif

public object OmniTextRenderer {
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
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        //#if MC >= 1.21.6
        val drawer = ImmediateGlyphDrawer(context.matrices.current.positionMatrix)
        textRenderer.prepare(text, x, y, color.pack(ColorFormat.ARGB), shadow, 0).draw(drawer)
        drawer.flush()
        //#elseif MC >= 1.20.1
        //$$ textRenderer.draw(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color.pack(ColorFormat.ARGB),
        //$$     shadow,
        //$$     context.matrices.vanilla.peek().positionMatrix,
        //$$     client.bufferBuilders.entityVertexConsumers,
        //$$     TextRenderer.TextLayerType.NORMAL,
        //$$     0,
        //$$     15728880
        //$$ )
        //#elseif MC >= 1.16.5
        //$$ if (shadow) {
        //$$     textRenderer.drawWithShadow(context.matrices.vanilla, text, x, y, color.pack(ColorFormat.ARGB))
        //$$ } else {
        //$$     textRenderer.draw(context.matrices.vanilla, text, x, y, color.pack(ColorFormat.ARGB))
        //$$ }
        //#else
        //$$ textRenderer.drawString(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color.pack(ColorFormat.ARGB),
        //$$     shadow
        //$$ )
        //#endif
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        when (shadowType) {
            is TextShadowType.None -> render(context, text, x, y, color, shadow = false)
            is TextShadowType.Drop -> render(context, text, x, y, color, shadow = true)
            is TextShadowType.Outline -> {
                val outlineColor = shadowType.color

                // If the outline isn't going to be visible anyway, don't waste draw calls
                if (outlineColor.alpha <= 3) {
                    render(context, text, x, y, color, shadow = false)
                    return
                }

                val strippedText = MCTextFormat.strip(text)
                for (i in outlineOffsets.indices step 2) {
                    val dx = outlineOffsets[i]
                    val dy = outlineOffsets[i + 1]
                    render(context, strippedText, x + dx, y + dy, outlineColor, shadow = false)
                }

                render(context, text, x, y, color, shadow = false)
            }
        }
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        val width = width(text)
        render(context, text, x - width / 2f, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        val width = width(text)
        render(context, text, x - width / 2f, y, color, shadowType)
    }

    @JvmStatic
    public fun width(text: String): Int {
        return textRenderer.getWidth(text)
    }

    @JvmStatic
    public fun width(text: MCTextHolder<*>): Int {
        //#if MC >= 1.16.5
        return textRenderer.getWidth(text.asVanilla())
        //#else
        //$$ return width(text.asString())
        //#endif
    }

    @JvmStatic
    public fun height(text: String, maxWidth: Int): Int {
        return textRenderer.getWrappedLinesHeight(text, maxWidth)
    }

    @JvmStatic
    public fun height(text: MCTextHolder<*>, maxWidth: Int): Int {
        //#if MC >= 1.19.2
        return textRenderer.getWrappedLinesHeight(text.asVanilla(), maxWidth)
        //#else
        //$$ return height(text.asString(), maxWidth)
        //#endif
    }
}
