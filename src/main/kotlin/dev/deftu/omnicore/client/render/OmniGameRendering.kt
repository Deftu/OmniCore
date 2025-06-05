package dev.deftu.omnicore.client.render

import dev.deftu.eventbus.on
import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.annotations.VersionedAbove
import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.client.events.RenderTickEvent
import net.minecraft.client.font.TextRenderer

//#if MC == 1.8.9
//#if FABRIC
//$$ import dev.deftu.omnicore.mixins.client.Mixin_MinecraftClient_TimerAccessor
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
    //#if FORGE
    //$$ private val timerFieldNames = arrayOf("field_71428_T", "timer") // We have the SRG name first so that it's resolved slightly faster in prod
    //$$
    //#endif
    //$$ private val MinecraftClient.deltaTickTracker: ClientTickTracker by lazy {
    //#if FABRIC
    //$$     (OmniClient.getInstance() as Mixin_MinecraftClient_TimerAccessor).ticker
    //#else
    //$$     for (fieldName in timerFieldNames) {
    //$$         try {
    //$$             val field = Minecraft::class.java.getDeclaredField(fieldName)
    //$$             field.isAccessible = true
    //$$             return@lazy field.get(OmniClient.getInstance()) as Timer
    //$$         } catch (ignored: NoSuchFieldException) {
    //$$             // no-op
    //$$         }
    //$$     }
    //$$
    //$$     throw IllegalStateException("Failed to find the delta tick tracker field in MinecraftClient")
    //#endif
    //$$ }
    //#endif

    private var lastFrameTimeNanos: Long = System.nanoTime()
    private var lastFrameDurationNanos: Long = 0L

    private val frameDurations = ArrayDeque<Long>(100)

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

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val textLineHeight: Int
        get() {
            return OmniClient.fontRenderer.fontHeight
        }

    /**
     * Returns the most recent frame duration in milliseconds.
     * May be inaccurate if not running in the render loop.
     */
    @JvmStatic
    public val lastFrameTimeMillis: Double
        get() = lastFrameDurationNanos / 1_000_000.0

    /**
     * Returns the average frame duration over the last 100 frames in milliseconds.
     */
    @JvmStatic
    public val averageFrameTimeMillis: Double
        get() = if (frameDurations.isEmpty()) 0.0 else frameDurations.average() / 1_000_000.0

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun initialize() {
        // Reset the frame time tracking
        lastFrameTimeNanos = System.nanoTime()
        lastFrameDurationNanos = 0L
        frameDurations.clear()

        OmniCore.eventBus.on<RenderTickEvent.Pre> {
            // Calculate the time since the last frame
            val currentTimeNanos = System.nanoTime()
            lastFrameDurationNanos = currentTimeNanos - lastFrameTimeNanos
            lastFrameTimeNanos = currentTimeNanos

            // Add the frame duration to the deque
            frameDurations.addLast(lastFrameDurationNanos)
            if (frameDurations.size > 100) {
                frameDurations.removeFirst()
            }
        }
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
