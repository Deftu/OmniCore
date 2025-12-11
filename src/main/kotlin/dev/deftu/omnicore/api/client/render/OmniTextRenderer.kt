package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack
import dev.deftu.omnicore.api.color.ColorFormat
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.textile.CollapseMode
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCText
import dev.deftu.textile.minecraft.FormattingCodes
import net.minecraft.network.chat.Component as VanillaText

//#if MC >= 1.21.6
import dev.deftu.omnicore.internal.client.render.ImmediateGlyphDrawer
//#endif

//#if MC >= 1.20.1 && MC < 1.21.6
//$$ import net.minecraft.client.gui.Font
//#endif

//#if MC < 1.16.5
//$$ import dev.deftu.omnicore.api.client.render.state.OmniRenderStates
//#endif

public object OmniTextRenderer {
    private inline val textRenderer
        get() = client.font

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
        get() = textRenderer.lineHeight

    @JvmStatic
    public fun render(
        pose: OmniPoseStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        //#if MC >= 1.21.6
        val drawer = ImmediateGlyphDrawer(pose.current.positionMatrix.vanilla)

        val glyph = textRenderer.prepareText(
            text.visualOrderText,
            x,
            y,
            color.pack(ColorFormat.ARGB),
            shadow,
            //#if MC >= 1.21.11
            //$$ true,
            //#endif
            0
        )

        glyph.visit(drawer)
        drawer.flush()

        return (x + width(text)).toInt() + if (shadow) 1 else 0
        //#elseif MC >= 1.20.1
        //$$ return textRenderer.drawInBatch(
        //$$     text,
        //$$     x,
        //$$     y,
        //$$     color.pack(ColorFormat.ARGB),
        //$$     shadow,
        //$$     pose.vanilla.last().pose(),
        //$$     client.renderBuffers().bufferSource(),
        //$$     Font.DisplayMode.NORMAL,
        //$$     0,
        //$$     15728880
        //$$ )
        //#elseif MC >= 1.16.5
        //$$ return if (shadow) {
        //$$     textRenderer.drawShadow(pose.vanilla, text, x, y, color.pack(ColorFormat.ARGB))
        //$$ } else {
        //$$     textRenderer.draw(pose.vanilla, text, x, y, color.pack(ColorFormat.ARGB))
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
        pose: OmniPoseStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(pose, MCText.convert(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        pose: OmniPoseStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(pose, Text.literal(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(context.pose, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(context.pose, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return render(context.pose, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun render(
        pose: OmniPoseStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return when (shadowType) {
            is TextShadowType.None -> render(pose, text, x, y, color, shadow = false)
            is TextShadowType.Drop -> render(pose, text, x, y, color, shadow = true)
            is TextShadowType.Outline -> {
                val outlineColor = shadowType.color

                // If the outline isn't going to be visible anyway, don't waste draw calls
                if (outlineColor.alpha <= 3) {
                    return render(pose, text, x, y, color, shadow = false)
                }

                val strippedText = FormattingCodes.stripColor(MCText.wrap(text).collapseToString(CollapseMode.SCOPED))
                for (i in outlineOffsets.indices step 2) {
                    val dx = outlineOffsets[i]
                    val dy = outlineOffsets[i + 1]
                    render(pose, strippedText, x + dx, y + dy, outlineColor, shadow = false)
                }

                render(pose, text, x, y, color, shadow = false)
            }
        }
    }

    @JvmStatic
    public fun render(
        pose: OmniPoseStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(pose, MCText.convert(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        pose: OmniPoseStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(pose, Text.literal(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(context.pose, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(context.pose, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun render(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return render(context.pose, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        pose: OmniPoseStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        val width = width(text)
        return render(pose, text, x - width / 2f, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        pose: OmniPoseStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(pose, MCText.convert(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        pose: OmniPoseStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(pose, Text.literal(text), x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(context.pose, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(context.pose, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ): Int {
        return renderCentered(context.pose, text, x, y, color, shadow)
    }

    @JvmStatic
    public fun renderCentered(
        pose: OmniPoseStack,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        val width = width(text)
        return render(pose, text, x - width / 2f, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        pose: OmniPoseStack,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(pose, MCText.convert(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        pose: OmniPoseStack,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(pose, Text.literal(text), x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(context.pose, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(context.pose, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun renderCentered(
        context: OmniRenderingContext,
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ): Int {
        return renderCentered(context.pose, text, x, y, color, shadowType)
    }

    @JvmStatic
    public fun width(text: String): Int {
        return textRenderer.width(text)
    }

    @JvmStatic
    public fun width(text: Text): Int {
        return width(MCText.convert(text))
    }

    @JvmStatic
    public fun width(text: VanillaText): Int {
        //#if MC >= 1.16.5
        return textRenderer.width(text)
        //#else
        //$$ return textRenderer.getStringWidth(text.unformattedText)
        //#endif
    }

    @JvmStatic
    public fun height(text: String, maxWidth: Int): Int {
        return height(Text.literal(text), maxWidth)
    }

    @JvmStatic
    public fun height(text: Text, maxWidth: Int): Int {
        return height(MCText.convert(text), maxWidth)
    }

    @JvmStatic
    public fun height(text: VanillaText, maxWidth: Int): Int {
        //#if MC >= 1.19.2
        return textRenderer.wordWrapHeight(text, maxWidth)
        //#else
        //$$ return textRenderer.wordWrapHeight(text.contents, maxWidth)
        //#endif
    }
}
