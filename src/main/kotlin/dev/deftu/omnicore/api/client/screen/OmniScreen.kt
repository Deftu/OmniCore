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
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

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
    //#if MC >= 1.16.5
    private var prevClickTime = 0L
    //#endif

    public open fun restorePreviousScreen() {
        currentScreen = previousScreen
    }

    public override fun onInitialize(width: Int, height: Int) {
        super.init()
    }

    public override fun onResize(width: Int, height: Int) {
        super.resize(client, width, height)
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
        //$$     super.mouseClickMove(x.toInt(), y.toInt(), button, clickTime)
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

    public override fun onTick() {
        super.tick()
    }

    public override fun onClose() {
        super.close()
    }

    public override fun onRender(
        ctx: OmniRenderingContext,
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float,
    ) {
        //#if MC >= 1.20.1
        super.render(ctx.graphics, mouseX, mouseY, tickDelta)
        //#elseif MC >= 1.16.5
        //$$ super.render(ctx.matrices.toVanillaStack(), mouseX, mouseY, tickDelta)
        //#else
        //$$ super.drawScreen(mouseX, mouseY, tickDelta)
        //#endif
    }

    public override fun onBackgroundRender(
        ctx: OmniRenderingContext,
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float,
    ) {
        // TODO
    }

    //#if MC >= 1.16.5
    final override fun getTitle(): Text? {
        return screenTitle?.asVanilla()
    }

    final override fun shouldPause(): Boolean {
        return isPauseScreen
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

    final override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return onMouseClick(OmniMouseButton(button), mouseX, mouseY)
    }

    final override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return onMouseRelease(OmniMouseButton(button), mouseX, mouseY)
    }

    final override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
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
            //#endif
        )
    }

    final override fun tick() {
        onTick()
    }

    final override fun close() {
        onClose()
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

        onRender(context, mouseX, mouseY, tickDelta)
    }

    final override fun renderBackground(
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

        onBackgroundRender(context, mouseX, mouseY, tickDelta)
    }
}
