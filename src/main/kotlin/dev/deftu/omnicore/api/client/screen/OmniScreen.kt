package dev.deftu.omnicore.api.client.screen

import dev.deftu.omnicore.api.client.OmniClientRuntime
import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniKeys
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.client.input.OmniMouseButtons
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextHolder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

//#if MC < 1.20.4
//$$ import dev.deftu.omnicore.api.client.input.OmniMouse
//$$ import dev.deftu.omnicore.api.client.render.OmniRenderTicks
//#endif

//#if MC >= 1.20.1
import net.minecraft.client.gui.DrawContext
//#endif

//#if MC < 1.20.1 && MC >= 1.16.5
//$$ import net.minecraft.client.util.math.MatrixStack
//#endif

//#if MC < 1.16.5
//$$ import org.lwjgl.input.Keyboard
//$$ import org.lwjgl.input.Mouse
//$$ import java.io.IOException
//#endif

public abstract class OmniScreen @JvmOverloads public constructor(
    public val screenTitle: MCTextHolder<*>? = null,
    public val storePreviousScreen: Boolean = true,
) : Screen(
    //#if MC >= 1.16.5
    (screenTitle ?: MCSimpleTextHolder("")).asVanilla()
    //#endif
), OmniScreenController {
    public companion object {
        public const val INVALID_CHAR: Char = '\u0000'
    }

    public open val isPauseScreen: Boolean
        get() = super.shouldPause()

    public val previousScreen: Screen?
        get() = if (storePreviousScreen) currentScreen else null

    // Internal state tracking
    //#if MC >= 1.20.4
    private var isBackgroundSuppressed =
        //#if MC >= 1.21.6
        true
        //#else
        //$$ false
        //#endif
    //#endif

    //#if MC >= 1.16.5
    private var prevClickTime = 0L
    //#endif

    public open fun restorePreviousScreen() {
        currentScreen = previousScreen
    }

    public override fun onInitialize(width: Int, height: Int) {
        //#if MC >= 1.16.5
        super.init()
        //#else
        //$$ super.initGui()
        //#endif
    }

    public override fun onResize(width: Int, height: Int) {
        //#if MC >= 1.16.5
        super.resize(client, width, height)
        //#else
        //$$ super.setWorldAndResolution(mc, width, height)
        //#endif
    }

    public override fun onKeyPress(
        key: OmniKey,
        scanCode: Int,
        typedChar: Char,
        modifiers: KeyboardModifiers,
        event: KeyPressEvent,
    ): Boolean {
        //#if MC >= 1.16.5
        if (event == KeyPressEvent.PRESSED) {
            return super.keyPressed(key.code, 0, modifiers.toMods())
        }

        if (event == KeyPressEvent.TYPED) {
            return super.charTyped(typedChar, modifiers.toMods())
        }
        //#else
        //$$ try {
        //$$     super.keyTyped(typedChar, key.code)
        //$$     return true // Default to true
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$ }
        //#endif

        return false
    }

    public override fun onKeyRelease(
        key: OmniKey,
        scanCode: Int,
        modifiers: KeyboardModifiers,
    ): Boolean {
        //#if MC >= 1.16.5
        val code = key.code
        if (code != 0) {
            return super.keyReleased(code, 0, modifiers.toMods())
        }
        //#endif

        return false
    }

    public override fun onMouseClick(
        button: OmniMouseButton,
        x: Double,
        y: Double,
    ): Boolean {
        //#if MC >= 1.16.5
        if (button == OmniMouseButtons.LEFT) {
            prevClickTime = OmniClientRuntime.nowMillis
        }

        return super.mouseClicked(x, y, button.code)
        //#else
        //$$ return try {
        //$$     super.mouseClicked(x.toInt(), y.toInt(), button.code)
        //$$     true // success!
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$     false // uh oh :(
        //$$ }
        //#endif
    }

    public override fun onMouseRelease(
        button: OmniMouseButton,
        x: Double,
        y: Double,
    ): Boolean {
        //#if MC >= 1.16.5
        return super.mouseReleased(x, y, button.code)
        //#else
        //$$ return try {
        //$$     super.mouseReleased(x.toInt(), y.toInt(), button.code)
        //$$     true // success!
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$     false // uh oh :(
        //$$ }
        //#endif
    }

    public override fun onMouseDrag(
        button: OmniMouseButton,
        x: Double,
        y: Double,
        deltaX: Double,
        deltaY: Double,
        clickTime: Long,
    ): Boolean {
        //#if MC >= 1.16.5
        return super.mouseDragged(x, y, button.code, deltaX, deltaY)
        //#else
        //$$ return try {
        //$$     super.mouseClickMove(x.toInt(), y.toInt(), button.code, clickTime)
        //$$     true // success!
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$     false // uh oh :(
        //$$ }
        //#endif
    }

    public override fun onMouseScroll(
        x: Double,
        y: Double,
        /** vertical scroll amount */
        amount: Double,
        horizontalAmount: Double,
    ): Boolean {
        //#if MC >= 1.16.5
        return super.mouseScrolled(
            x,
            y,
            //#if MC >= 1.20.4
            horizontalAmount,
            //#endif
            amount
        )
        //#else
        //$$ return true // no need to implement before 1.16.5
        //#endif
    }

    public override fun onScreenTick() {
        //#if MC >= 1.16.5
        super.tick()
        //#else
        //$$ super.updateScreen()
        //#endif
    }

    public override fun onScreenClose() {
        //#if MC >= 1.16.5
        super.close()
        //#else
        //$$ super.onGuiClosed()
        //#endif
    }

    public override fun onRender(
        ctx: OmniRenderingContext,
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float,
    ) {
        //#if MC >= 1.20.4 && MC < 1.21.6
        //$$ isBackgroundSuppressed = true
        //#endif

        //#if MC >= 1.20.1
        super.render(ctx.graphics, mouseX, mouseY, tickDelta)
        //#elseif MC >= 1.16.5
        //$$ super.render(ctx.matrices.vanilla, mouseX, mouseY, tickDelta)
        //#else
        //$$ super.drawScreen(mouseX, mouseY, tickDelta)
        //#endif

        //#if MC >= 1.20.4 && MC < 1.21.6
        //$$ isBackgroundSuppressed = false
        //#endif
    }

    public override fun onBackgroundRender(
        ctx: OmniRenderingContext,
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float,
    ) {
        //#if MC >= 1.21.6
        ctx.graphics.createNewRootLayer()
        //#endif

        //#if MC >= 1.20.1
        super.renderBackground(
            ctx.graphics,
            //#if MC >= 1.20.4
            mouseX,
            mouseY,
            tickDelta
            //#endif
        )
        //#elseif MC >= 1.16.5
        //$$ super.renderBackground(ctx.matrices.vanilla)
        //#else
        //$$ super.drawDefaultBackground()
        //#endif

        //#if MC >= 1.21.6
        ctx.graphics.createNewRootLayer()
        //#endif
    }

    //#if MC >= 1.16.5
    final override fun getTitle(): Text? {
        return screenTitle?.asVanilla()
    }

    final override fun init() {
        onInitialize(width, height)
    }

    final override fun resize(client: MinecraftClient?, width: Int, height: Int) {
        onResize(width, height)
    }

    final override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return onKeyPress(OmniKey(keyCode), scanCode, INVALID_CHAR, KeyboardModifiers.wrap(modifiers), KeyPressEvent.PRESSED)
    }

    final override fun charTyped(typedChar: Char, modifiers: Int): Boolean {
        return onKeyPress(OmniKeys.KEY_NONE, 0, typedChar, KeyboardModifiers.wrap(modifiers), KeyPressEvent.TYPED)
    }

    final override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return onKeyRelease(OmniKey(keyCode), scanCode, KeyboardModifiers.wrap(modifiers))
    }

    final override fun mouseClicked(
        mouseX: Double,
        mouseY: Double,
        button: Int
    ): Boolean {
        return onMouseClick(OmniMouseButton(button), mouseX, mouseY)
    }

    final override fun mouseReleased(
        mouseX: Double,
        mouseY: Double,
        button: Int
    ): Boolean {
        return onMouseRelease(OmniMouseButton(button), mouseX, mouseY)
    }

    final override fun mouseDragged(
        mouseX: Double,
        mouseY: Double,
        button: Int,
        deltaX: Double,
        deltaY: Double
    ): Boolean {
        val clickTime = if (button == OmniMouseButtons.LEFT.code) {
            OmniClientRuntime.nowMillis - prevClickTime
        } else {
            0L
        }

        return onMouseDrag(OmniMouseButton(button), mouseX, mouseY, deltaX, deltaY, clickTime)
    }

    final override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        //#if MC >= 1.20.4
        horizontalAmount: Double,
        //#endif
        amount: Double
    ): Boolean {
        return onMouseScroll(
            mouseX,
            mouseY,
            amount,
            //#if MC >= 1.20.4
            horizontalAmount
            //#else
            //$$ amount,
            //#endif
        )
    }

    final override fun tick() {
        onScreenTick()
    }

    final override fun close() {
        onScreenClose()
    }

    final override fun render(
        //#if MC >= 1.20.1
        ctx: DrawContext,
        //#elseif MC >= 1.16.5
        //$$ ctx: MatrixStack,
        //#endif
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float,
    ) {
        val context = OmniRenderingContext.from(
            //#if MC >= 1.16.5
            ctx
            //#endif
        )

        //#if MC >= 1.21.6
        isBackgroundSuppressed = false
        //#endif
        onRender(context, mouseX, mouseY, tickDelta)
        //#if MC >= 1.21.6
        isBackgroundSuppressed = true
        //#endif
    }

    final override fun renderBackground(
        //#if MC >= 1.20.1
        ctx: DrawContext,
        //#elseif MC >= 1.16.5
        //$$ ctx: MatrixStack,
        //#endif

        //#if MC >= 1.20.4
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float,
        //#endif
    ) {
        //#if MC < 1.20.4
        //$$ val mouseX = OmniMouse.scaledX
        //$$ val mouseY = OmniMouse.scaledY
        //$$ val tickDelta = OmniRenderTicks.get()
        //#endif

        val context = OmniRenderingContext.from(
            //#if MC >= 1.16.5
            ctx
            //#endif
        )

        onBackgroundRender(context, mouseX, mouseY, tickDelta)
    }
    //#else
    //$$ final override fun initGui() {
    //$$     onInitialize(width, height)
    //$$ }
    //$$
    //$$ final override fun setWorldAndResolution(mcIn: Minecraft, width: Int, height: Int) {
    //$$     onResize(width, height)
    //$$ }
    //$$
    //$$ final override fun keyTyped(typedChar: Char, keyCode: Int) {
    //$$     if (keyCode != 0) {
    //$$         onKeyPress(OmniKey(keyCode), 0, INVALID_CHAR, KeyboardModifiers.current, KeyPressEvent.PRESSED)
    //$$     }
    //$$
    //$$     if (typedChar != 0.toChar() || typedChar != INVALID_CHAR) {
    //$$         onKeyPress(OmniKeys.KEY_NONE, 0, typedChar, KeyboardModifiers.current, KeyPressEvent.TYPED)
    //$$     }
    //$$ }
    //$$
    //$$ final override fun handleKeyboardInput() {
    //$$     super.handleKeyboardInput()
    //$$     if (!Keyboard.getEventKeyState()) {
    //$$         onKeyRelease(OmniKey(Keyboard.getEventKey()), 0, KeyboardModifiers.current)
    //$$     }
    //$$ }
    //$$
    //$$ final override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
    //$$     onMouseClick(OmniMouseButton(button), mouseX.toDouble(), mouseY.toDouble())
    //$$ }
    //$$
    //$$ final override fun mouseReleased(mouseX: Int, mouseY: Int, button: Int) {
    //$$     onMouseRelease(OmniMouseButton(button), mouseX.toDouble(), mouseY.toDouble())
    //$$ }
    //$$
    //$$ final override fun mouseClickMove(
    //$$     mouseX: Int,
    //$$     mouseY: Int,
    //$$     button: Int,
    //$$     clickTime: Long,
    //$$ ) {
    //$$     onMouseDrag(OmniMouseButton(button), mouseX.toDouble(), mouseY.toDouble(), 0.0, 0.0, clickTime)
    //$$ }
    //$$
    //$$ final override fun handleMouseInput() {
    //$$     super.handleMouseInput()
    //$$     val scrollDelta = Mouse.getEventDWheel()
    //$$     if (scrollDelta != 0) {
    //$$         val mouseX = OmniMouse.scaledX
    //$$         val mouseY = OmniMouse.scaledY
    //$$         onMouseScroll(mouseX, mouseY, scrollDelta.toDouble(), 0.0)
    //$$     }
    //$$ }
    //$$
    //$$ final override fun updateScreen() {
    //$$     onScreenTick()
    //$$ }
    //$$
    //$$ final override fun onGuiClosed() {
    //$$     onScreenClose()
    //$$ }
    //$$
    //$$ final override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
    //$$     val context = OmniRenderingContext.from()
    //$$     onRender(context, mouseX, mouseY, partialTicks)
    //$$ }
    //$$
    //$$ final override fun drawWorldBackground(tint: Int) {
    //$$     val context = OmniRenderingContext.from()
    //$$     val mouseX = OmniMouse.scaledX.toInt()
    //$$     val mouseY = OmniMouse.scaledY.toInt()
    //$$     val tickDelta = OmniRenderTicks.get()
    //$$     onBackgroundRender(context, mouseX, mouseY, tickDelta)
    //$$ }
    //#endif

    final override fun shouldPause(): Boolean {
        return isPauseScreen
    }
}
