package dev.deftu.omnicore.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.textile.TextHolder
import dev.deftu.textile.minecraft.TranslatableTextHolder
import net.minecraft.client.gui.screen.ChatScreen

//#if MC >= 1.16.5
import dev.deftu.textile.minecraft.toVanilla
//#else
//$$ import java.io.IOException
//$$ import org.lwjgl.input.Mouse
//#endif

//#if MC >= 1.20
import net.minecraft.client.gui.DrawContext
//#elseif MC >= 1.16.5
//$$ import net.minecraft.client.util.math.MatrixStack
//#endif

@GameSide(Side.CLIENT)
public abstract class OmniScreen(
    public val restorePreviousScreen: Boolean = true,
    public val screenTitle: TextHolder? = null
//#if MC >= 1.16.5
) : Screen(screenTitle?.toVanilla() ?: TranslatableTextHolder("").toVanilla()) {
//#else
//$$ ) : GuiScreen() {
//#endif
    public companion object {

        /**
         * @return The current screen the player is in.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public var currentScreen: Screen?
            get() = OmniClient.getInstance().currentScreen
            set(value) { OmniClient.getInstance().setScreen(value) }

        /**
         * @return True if the player is in a screen, false otherwise.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isInScreen: Boolean
            get() = currentScreen != null

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val isInChat: Boolean
            get() = currentScreen is ChatScreen

        /**
         * Closes the current screen.
         *
         * @since 0.2.2
         * @author Deftu
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun closeScreen() {
            currentScreen = null
        }

        /**
         * @param screenClz The screen class to check.
         * @return True if the player is in the specified screen, false otherwise.
         *
         * @since 0.2.2
         * @author Deftu
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun isInScreen(screenClz: Class<out Screen>): Boolean {
            return currentScreen?.javaClass == screenClz
        }

    }

    public enum class KeyPressTrigger {
        KEY_CODE_EVENT,
        CHAR_TYPE_EVENT,
        AMBIGUOUS
    }

    public constructor(
        restorePreviousScreen: Boolean = true,
        titleKey: String? = null
    ) : this(restorePreviousScreen, TranslatableTextHolder(titleKey ?: ""))

    @JvmOverloads
    public constructor(
        restorePreviousScreen: Boolean = true
    ) : this(restorePreviousScreen, null as TextHolder?)

    private val previousScreen = if (restorePreviousScreen) currentScreen else null

    //#if MC >= 1.15
    private var lastClick = 0L
    private var dragDx = -1.0
    private var dragDy = -1.0
    private var scrolledX = -1.0
    private var scrolledY = -1.0
    //#if MC >= 1.20.4
    //$$ private var isCancellingBackground = false
    //$$ private var scrolledDX = 0.0
    //$$ private var backgroundMouseX = 0
    //$$ private var backgroundMouseY = 0
    //$$ private var backgroundDelta = 0f
    //#endif
    //#endif

    //#if MC >= 1.20
    private var contexts = mutableListOf<DrawContext>()
    //#endif

    @GameSide(Side.CLIENT)
    public open fun handleInitialize(width: Int, height: Int) {
        //#if MC >= 1.15
        super.init()
        //#else
        //$$ super.initGui()
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleRender(
        stack: OmniMatrixStack,
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float
    ) {
        //#if MC >= 1.20
        //#if MC >= 1.20.4
        //$$ isCancellingBackground = true
        //#endif
        withDrawContext(stack) { ctx ->
            super.render(ctx, mouseX, mouseY, tickDelta)
        }
        //#if MC >= 1.20.4
        //$$ isCancellingBackground = false
        //#endif
        //#elseif MC >= 1.16
        //$$ super.render(stack.toVanillaStack(), mouseX, mouseY, tickDelta)
        //#elseif MC >= 1.15
        //$$ super.render(mouseX, mouseY, tickDelta)
        //#else
        //$$ super.drawScreen(mouseX, mouseY, tickDelta)
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleKeyPress(
        code: Int,
        char: Char,
        modifiers: OmniKeyboard.KeyboardModifiers,
        trigger: KeyPressTrigger
    ): Boolean {
        //#if MC >= 1.15
        if (trigger == KeyPressTrigger.KEY_CODE_EVENT) {
            return super.keyPressed(code, 0, modifiers.toInt())
        }

        if (trigger == KeyPressTrigger.CHAR_TYPE_EVENT) {
            return super.charTyped(char, modifiers.toInt())
        }
        //#else
        //$$ try {
        //$$     super.keyTyped(char, code)
        //$$     return true // Default to true
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$ }
        //#endif

        return false
    }

    @GameSide(Side.CLIENT)
    public open fun handleKeyRelease(
        code: Int,
        char: Char,
        modifiers: Int
    ): Boolean {
        //#if MC >= 1.15
        if (code != 0) {
            return super.keyReleased(code, 0, modifiers)
        }
        //#endif

        return false
    }

    @GameSide(Side.CLIENT)
    public open fun handleMouseClick(
        x: Double,
        y: Double,
        button: Int
    ): Boolean {
        //#if MC >= 1.15
        if (button == 1) {
            lastClick = OmniClient.getTimeSinceStart()
        }

        return super.mouseClicked(x, y, button)
        //#else
        //$$ try {
        //$$     super.mouseClicked(x.toInt(), y.toInt(), button)
        //$$     return true // Default to true
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$     return false
        //$$ }
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleMouseReleased(
        x: Double,
        y: Double,
        state: Int
    ): Boolean {
        //#if MC >= 1.15
        return super.mouseReleased(x, y, state)
        //#else
        //$$ super.mouseReleased(x.toInt(), y.toInt(), state)
        //$$ return true // Default to true
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleMouseDragged(
        x: Double,
        y: Double,
        button: Int,
        clickTime: Long
    ): Boolean {
        //#if MC >= 1.15
        return super.mouseDragged(x, y, button, dragDx, dragDy)
        //#else
        //$$ super.mouseClickMove(x.toInt(), y.toInt(), button, clickTime)
        //$$ return true // Default to true
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleMouseScrolled(
        delta: Double
    ): Boolean {
        //#if MC >= 1.15
        return super.mouseScrolled(
            scrolledX,
            scrolledY,
            //#if MC >= 1.20.4
            //$$ scrolledDX,
            //#endif
            delta
        )
        //#else
        //$$ return true // Default to true
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleTick() {
        //#if MC >= 1.15
        super.tick()
        //#else
        //$$ super.updateScreen()
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleClose() {
        //#if MC >= 1.15
        super.close()
        //#else
        //$$ super.onGuiClosed()
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleResize(width: Int, height: Int) {
        //#if MC >= 1.15
        super.resize(OmniClient.getInstance(), width, height)
        //#else
        //$$ super.setWorldAndResolution(OmniClient.getInstance(), width, height)
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun handleBackgroundRender(
        stack: OmniMatrixStack
    ) {
        //#if MC >= 1.20
        withDrawContext(stack) { ctx ->
            super.renderBackground(
                ctx,
                //#if MC >= 1.20.4
                //$$ backgroundMouseX,
                //$$ backgroundMouseY,
                //$$ backgroundDelta
                //#endif
            )
        }
        //#elseif MC >= 1.16
        //$$ super.renderBackground(stack.toVanillaStack())
        //#elseif MC >= 1.15
        //$$ super.renderBackground()
        //#else
        //$$ super.drawDefaultBackground()
        //#endif
    }

    @GameSide(Side.CLIENT)
    public open fun doesPauseGame(): Boolean = super.shouldPause()

    @GameSide(Side.CLIENT)
    public fun restorePreviousScreen() {
        currentScreen = previousScreen
    }

    //#if MC >= 1.20
    private inline fun <R> withDrawContext(stack: OmniMatrixStack, block: (DrawContext) -> R) {
        val client = this.client!!
        val context = contexts.lastOrNull() ?: DrawContext(client, client.bufferBuilders.entityVertexConsumers)
        context.matrices.push()
        val vanilla = context.matrices.peek()
        val self = stack.peek()
        vanilla.positionMatrix.set(self.matrix)
        vanilla.normalMatrix.set(self.normal)
        block(context)
        context.matrices.pop()
    }
    //#endif

    //#if MC >= 1.15
    final override fun getTitle(): net.minecraft.text.Text = title

    final override fun init() {
        handleInitialize(width, height)
    }

    //#if MC >= 1.20
    final override fun render(ctx: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        contexts.add(ctx)
        handleRender(OmniMatrixStack(ctx.matrices), mouseX, mouseY, tickDelta)
        contexts.removeLast()
    }
    //#elseif MC >= 1.16
    //$$ final override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
    //$$     handleRender(OmniMatrixStack(stack), mouseX, mouseY, tickDelta)
    //$$ }
    //#else
    //$$ final override fun render(mouseX: Int, mouseY: Int, tickDelta: Float) {
    //$$     handleRender(OmniMatrixStack(), mouseX, mouseY, tickDelta)
    //$$ }
    //#endif

    final override fun keyPressed(code: Int, scancode: Int, modifiers: Int): Boolean {
        handleKeyPress(code, 0.toChar(), modifiers.toKeyboardModifiers(), KeyPressTrigger.KEY_CODE_EVENT)
        return false
    }

    final override fun charTyped(char: Char, modifiers: Int): Boolean {
        handleKeyPress(0, char, modifiers.toKeyboardModifiers(), KeyPressTrigger.CHAR_TYPE_EVENT)
        return false
    }

    final override fun keyReleased(code: Int, scancode: Int, modifiers: Int): Boolean {
        handleKeyRelease(code, 0.toChar(), modifiers)
        return false
    }

    final override fun mouseClicked(mouseX: Double, mouseY: Double, mouseBtn: Int): Boolean {
        handleMouseClick(mouseX, mouseY, mouseBtn)
        return false
    }

    final override fun mouseReleased(mouseX: Double, mouseY: Double, mouseBtn: Int): Boolean {
        handleMouseReleased(mouseX, mouseY, mouseBtn)
        return false
    }

    final override fun mouseDragged(mouseX: Double, mouseY: Double, mouseBtn: Int, dx: Double, dy: Double): Boolean {
        dragDx = dx
        dragDy = dy
        handleMouseDragged(mouseX, mouseY, mouseBtn, OmniClient.getTimeSinceStart() - lastClick)
        return false
    }

    final override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        //#if MC >= 1.20.4
        //$$ horizontalScroll: Double,
        //#endif
        scrollDelta: Double
    ): Boolean {
        scrolledX = mouseX
        scrolledY = mouseY
        //#if MC >= 1.20.2
        //$$ scrolledDX = horizontalScroll
        //#endif
        handleMouseScrolled(scrollDelta)
        return false
    }

    final override fun tick() {
        handleTick()
    }

    final override fun resize(client: MinecraftClient, width: Int, height: Int) {
        handleResize(width, height)
    }

    final override fun close() {
        handleClose()
    }

    //#if MC >= 1.20
    final override fun renderBackground(
        ctx: DrawContext,
        //#if MC >= 1.20.4
        //$$ mouseX: Int,
        //$$ mouseY: Int,
        //$$ delta: Float
        //#endif
    ) {
        //#if MC >= 1.20.4
        //$$ backgroundMouseX = mouseX
        //$$ backgroundMouseY = mouseY
        //$$ backgroundDelta = delta
        //$$ if (isCancellingBackground) return
        //$$
        //#endif
        contexts.add(ctx)
        handleBackgroundRender(OmniMatrixStack(ctx.matrices))
    }
    //#elseif MC >= 1.16
    //$$ final override fun renderBackground(stack: MatrixStack) {
    //$$     handleBackgroundRender(OmniMatrixStack(stack))
    //$$ }
    //#else
    //$$ final override fun renderBackground() {
    //$$     handleBackgroundRender(OmniMatrixStack())
    //$$ }
    //#endif

    final override fun shouldPause(): Boolean = doesPauseGame()
    //#else
    //$$ final override fun initGui() {
    //$$     handleInitialize(width, height)
    //$$ }
    //$$
    //$$ final override fun drawScreen(mouseX: Int, mouseY: Int, tickDelta: Float) {
    //$$     handleRender(OmniMatrixStack(), mouseX, mouseY, tickDelta)
    //$$ }
    //$$
    //$$ final override fun keyTyped(typedChar: Char, keyCode: Int) {
    //$$     handleKeyPress(keyCode, typedChar, OmniKeyboard.modifiers, KeyPressTrigger.AMBIGUOUS)
    //$$ }
    //$$
    //$$ final override fun mouseClicked(mouseX: Int, mouseY: Int, mouseBtn: Int) {
    //$$     handleMouseClick(mouseX.toDouble(), mouseY.toDouble(), mouseBtn)
    //$$ }
    //$$
    //$$ final override fun mouseReleased(mouseX: Int, mouseY: Int, mouseBtn: Int) {
    //$$     handleMouseReleased(mouseX.toDouble(), mouseY.toDouble(), mouseBtn)
    //$$ }
    //$$
    //$$ final override fun mouseClickMove(mouseX: Int, mouseY: Int, mouseBtn: Int, clickTime: Long) {
    //$$     handleMouseDragged(mouseX.toDouble(), mouseY.toDouble(), mouseBtn, clickTime)
    //$$ }
    //$$
    //$$ final override fun handleMouseInput() {
    //$$     super.handleMouseInput()
    //$$     val scrollDelta = Mouse.getEventDWheel()
    //$$     if (scrollDelta != 0) {
    //$$         handleMouseScrolled(scrollDelta.toDouble())
    //$$     }
    //$$ }
    //$$
    //$$ final override fun updateScreen() {
    //$$     handleTick()
    //$$ }
    //$$
    //$$ final override fun setWorldAndResolution(mc: Minecraft, width: Int, height: Int) {
    //$$     handleResize(width, height)
    //$$ }
    //$$
    //$$ final override fun onGuiClosed() {
    //$$     handleClose()
    //$$ }
    //$$
    //$$ final override fun drawWorldBackground(tint: Int) {
    //$$     handleBackgroundRender(OmniMatrixStack())
    //$$ }
    //$$
    //$$ final override fun doesGuiPauseGame(): Boolean = doesPauseGame()
    //#endif
}
