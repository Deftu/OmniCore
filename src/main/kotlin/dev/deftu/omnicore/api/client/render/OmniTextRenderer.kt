package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.client.OmniClient
import dev.deftu.textile.minecraft.MCTextHolder

public object OmniTextRenderer {
    private inline val textRenderer
        get() = OmniClient.getInstance().textRenderer

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
        val drawer = ImmediateGlyphDrawer(context.matrixStack.current.positionMatrix)
        textRenderer.prepare(text, x, y, color, shadow, 0).draw(drawer)
        drawer.flush()
        //#elseif MC >= 1.20.1
        //$$ fontRenderer.draw(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color,
        //$$     shadow,
        //$$     context.matrixStack.vanilla.peek().positionMatrix,
        //$$     OmniClient.getInstance().bufferBuilders.entityVertexConsumers,
        //$$     TextRenderer.TextLayerType.NORMAL,
        //$$     0,
        //$$     15728880
        //$$ )
        //#elseif MC >= 1.16.5
        //$$ if (shadow) {
        //$$     fontRenderer.drawWithShadow(context.matrixStack.vanilla, text, x, y, color)
        //$$ } else {
        //$$     fontRenderer.draw(context.matrixStack.vanilla, text, x, y, color)
        //$$ }
        //#else
        //$$ fontRenderer.drawString(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color,
        //$$     shadow
        //$$ )
        //#endif
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
    public fun width(text: String): Int {
        return textRenderer.getWidth(text)
    }

    @JvmStatic
    public fun width(text: MCTextHolder<*>): Int {
        return textRenderer.getWidth(text.asVanilla())
    }

    @JvmStatic
    public fun height(text: String, maxWidth: Int): Int {
        return textRenderer.getWrappedLinesHeight(text, maxWidth)
    }

    @JvmStatic
    public fun height(text: MCTextHolder<*>, maxWidth: Int): Int {
        return textRenderer.getWrappedLinesHeight(text.asVanilla(), maxWidth)
    }
}
