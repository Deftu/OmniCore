package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.client.OmniClient
import net.minecraft.client.font.TextRenderer

public object OmniGameRendering {

    @JvmStatic
    public val isDebugRendering: Boolean
        get() {
            //#if MC >= 1.20.4
            //$$ return OmniClient.getInstance().debugOverlay.showDebugScreen()
            //#else
            return OmniClient.getInstance().options.debugEnabled
            //#endif
        }

    @JvmStatic
    @JvmOverloads
    public fun drawText(
        stack: OmniMatrixStack,
        text: String,
        x: Float,
        y: Float,
        color: Int,
        shadow: Boolean = true
    ) {
        val fontRenderer = OmniClient.fontRenderer

        //#if MC >= 1.20
        fontRenderer.draw(
            text,
            x,
            y,
            color,
            shadow,
            stack.toVanillaStack().peek().positionMatrix,
            OmniClient.getInstance().bufferBuilders.entityVertexConsumers,
            TextRenderer.TextLayerType.NORMAL,
            0,
            15728880
        )
        //#elseif MC >= 1.16.5
        //$$ if (shadow) {
        //$$     fontRenderer.drawWithShadow(stack.toVanillaStack(), text, x, y, color)
        //$$ } else {
        //$$     fontRenderer.draw(stack.toVanillaStack(), text, x, y, color)
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
    public fun getTextWidth(text: String): Int {
        return OmniClient.fontRenderer.getWidth(text)
    }

}
