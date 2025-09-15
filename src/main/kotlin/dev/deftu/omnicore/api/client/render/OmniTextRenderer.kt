package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.client
import dev.deftu.textile.minecraft.MCTextHolder

//#if MC >= 1.20.1 && MC < 1.21.6
//$$ import net.minecraft.client.font.TextRenderer
//#endif

public object OmniTextRenderer {
    private inline val textRenderer
        get() = client.textRenderer

    @JvmStatic
    public val lineHeight: Int
        get() = textRenderer.fontHeight

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: Int,
        shadow: Boolean = true,
    ) {
        val color = if (color and 0xFF000000.toInt() == 0) {
            color or 0xFF000000.toInt()
        } else {
            color
        }

        //#if MC >= 1.21.6
        val drawer = ImmediateGlyphDrawer(context.matrices.current.positionMatrix)
        textRenderer.prepare(text, x, y, color, shadow, 0).draw(drawer)
        drawer.flush()
        //#elseif MC >= 1.20.1
        //$$ textRenderer.draw(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color,
        //$$     shadow,
        //$$     context.matrices.vanilla.peek().positionMatrix,
        //$$     client.bufferBuilders.entityVertexConsumers,
        //$$     TextRenderer.TextLayerType.NORMAL,
        //$$     0,
        //$$     15728880
        //$$ )
        //#elseif MC >= 1.16.5
        //$$ if (shadow) {
        //$$     textRenderer.drawWithShadow(context.matrices.vanilla, text, x, y, color)
        //$$ } else {
        //$$     textRenderer.draw(context.matrices.vanilla, text, x, y, color)
        //$$ }
        //#else
        //$$ textRenderer.drawString(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color,
        //$$     shadow
        //$$ )
        //#endif
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: Int,
        type: TextShadowType
    ) {
        when (type) {
            is TextShadowType.None -> render(context, text, x, y, color, shadow = false)
            is TextShadowType.Drop -> render(context, text, x, y, color, shadow = true)
            is TextShadowType.Outline -> {
                render(context, text, x - 1, y, type.color, shadow = false)
                render(context, text, x + 1, y, type.color, shadow = false)
                render(context, text, x, y - 1, type.color, shadow = false)
                render(context, text, x, y + 1, type.color, shadow = false)
                render(context, text, x, y, color, shadow = false)
            }
        }
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: Int,
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
        color: Int,
        type: TextShadowType
    ) {
        val width = width(text)
        render(context, text, x - width / 2f, y, color, type)
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
