package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.annotations.VersionedAbove
import dev.deftu.omnicore.client.OmniClient
import net.minecraft.client.font.TextRenderer

//#if MC == 1.8.9
//#if FABRIC
//$$ import dev.deftu.omnicore.mixins.client.Mixin_MinecraftClient_TimerAccessor
//#else
//$$ import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper
//#endif
//$$
//$$ import net.minecraft.client.render.ClientTickTracker
//$$ import net.minecraft.client.MinecraftClient
//#endif

/**
 * A utility class which provides several helper functions for rendering game elements which may otherwise be difficult to do without the extensive use of preprocessing.
 *
 * @since 0.14.0
 * @author Deftu
 */
@GameSide(Side.CLIENT)
public object OmniGameRendering {

    //#if MC == 1.8.9
    //$$ private val MinecraftClient.deltaTickTracker: ClientTickTracker by lazy {
    //#if FABRIC
    //$$     (this as Mixin_MinecraftClient_TimerAccessor).ticker
    //#else
    //$$     val field = Minecraft::class.java.getDeclaredField(FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(Minecraft::class.java.name, "timer", "Lnet/minecraft/util/Timer;"))
    //$$     field.isAccessible = true
    //$$     field.get(OmniClient.getInstance()) as Timer
    //#endif
    //$$ }
    //#endif

    /**
     * Returns whether the player currently has the "F3"/debug screen open.
     *
     * @since 0.14.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isDebugRendering: Boolean
        get() {
            //#if MC >= 1.20.4
            //$$ return OmniClient.getInstance().debugOverlay.showDebugScreen()
            //#else
            return OmniClient.getInstance().options.debugEnabled
            //#endif
        }

    /**
     * Returns the current tick delta, which is the time in seconds since the last frame was rendered.
     *
     * @param allowStatic Whether to allow the method to return a static value of 1 if the game tick is paused. Only affects version 1.21.1 and above.
     *
     * @since 0.20.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getTickDelta(@VersionedAbove("1.21.1") allowStatic: Boolean = true): Float {
        //#if MC >= 1.21.1
        //$$ return OmniClient.getInstance().timer.getGameTimeDeltaPartialTick(allowStatic)
        //#elseif MC >= 1.12.2
        return OmniClient.getInstance().tickDelta
        //#else
        //$$ return OmniClient.getInstance().deltaTickTracker.tickDelta
        //#endif
    }

    /**
     * Draws a string of text to the screen using Minecraft's built-in font and text renderer.
     *
     * @param stack The matrix stack to use whilst drawing the text.
     * @param text The text to draw.
     * @param x The x-coordinate to draw the text at.
     * @param y The y-coordinate to draw the text at.
     * @param color The color of the text.
     * @param shadow Whether to draw a drop shadow behind the text.
     *
     * @since 0.14.0
     * @author Deftu
     */
    @JvmStatic
    @JvmOverloads
    @GameSide(Side.CLIENT)
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

    /**
     * @return The width of the specified text in pixels if it were to be rendered using Minecraft's built-in font and text renderer.
     * @param text The text to measure.
     * @since 0.14.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getTextWidth(text: String): Int {
        return OmniClient.fontRenderer.getWidth(text)
    }

}
