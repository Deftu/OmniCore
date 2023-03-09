package xyz.deftu.multi

//#if MC>=11500
import net.minecraft.client.util.math.MatrixStack
//#if MC<11900
//$$ import net.minecraft.text.TranslatableText
//#endif
//#else
//$$ import java.io.IOException
//$$ import org.lwjgl.input.Mouse
//#endif

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import xyz.deftu.text.Text
import xyz.deftu.text.utils.toVanilla

abstract class MultiScreen(
    val restorePreviousScreen: Boolean = true,
    val titleKey: String? = null
//#if MC>=11500
) : Screen(Text.translatable(titleKey ?: "").toVanilla()) {
//#else
//$$ ) : GuiScreen() {
//#endif
    companion object {
        @JvmStatic
        fun openScreen(screen: Screen?) {
            MultiClient.getInstance().setScreen(screen)
        }
    }

    @JvmOverloads
    constructor(restorePreviousScreen: Boolean = true) : this(restorePreviousScreen, null)

    private val previousScreen = if (restorePreviousScreen) MultiClient.getCurrentScreen() else null

    //#if MC>=11500
    private var lastClick = 0L
    private var dragDx = -1.0
    private var dragDy = -1.0
    private var scrolledX = -1.0
    private var scrolledY = -1.0
    //#endif

    open fun handleInitialize(width: Int, height: Int) {
        //#if MC>=11500
        super.init()
        //#else
        //$$ super.initGui()
        //#endif
    }

    open fun handleRender(
        stack: MultiMatrixStack,
        mouseX: Int,
        mouseY: Int,
        tickDelta: Float
    ) {
        //#if MC>=11600
        super.render(stack.toVanillaStack(), mouseX, mouseY, tickDelta)
        //#elseif MC>=11500
        //$$ super.render(mouseX, mouseY, tickDelta)
        //#else
        //$$ super.drawScreen(mouseX, mouseY, tickDelta)
        //#endif
    }

    open fun handleKeyPress(
        code: Int,
        char: Char,
        modifiers: Int
    ) {
        //#if MC>=11500
        if (code != 0) {
            super.keyPressed(code, 0, modifiers)
        }

        if (char != 0.toChar()) {
            super.charTyped(char, modifiers)
        }
        //#else
        //$$ try {
        //$$     super.keyTyped(char, code)
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$ }
        //#endif
    }

    open fun handleKeyRelease(
        code: Int,
        char: Char,
        modifiers: Int
    ) {
        //#if MC>=11500
        if (code != 0) {
            super.keyReleased(code, 0, modifiers)
        }
        //#endif
    }

    open fun handleMouseClick(
        x: Double,
        y: Double,
        button: Int
    ) {
        //#if MC>=11500
        if (button == 1) lastClick = MultiClient.getTime()
        super.mouseClicked(x, y, button)
        //#else
        //$$ try {
        //$$     super.mouseClicked(x.toInt(), y.toInt(), button)
        //$$ } catch (e: IOException) {
        //$$     e.printStackTrace()
        //$$ }
        //#endif
    }

    open fun handleMouseReleased(
        x: Double,
        y: Double,
        state: Int
    ) {
        //#if MC>=11500
        super.mouseReleased(x, y, state)
        //#else
        //$$ super.mouseReleased(x.toInt(), y.toInt(), state)
        //#endif
    }

    open fun handleMouseDragged(
        x: Double,
        y: Double,
        button: Int,
        clickTime: Long
    ) {
        //#if MC>=11500
        super.mouseDragged(x, y, button, dragDx, dragDy)
        //#else
        //$$ super.mouseClickMove(x.toInt(), y.toInt(), button, clickTime)
        //#endif
    }

    open fun handleMouseScrolled(
        delta: Double
    ) {
        //#if MC>=11500
        super.mouseScrolled(scrolledX, scrolledY, delta)
        //#endif
    }

    open fun handleTick() {
        //#if MC>=11500
        super.tick()
        //#else
        //$$ super.updateScreen()
        //#endif
    }

    open fun handleClose() {
        //#if MC>=11500
        super.close()
        //#else
        //$$ super.onGuiClosed()
        //#endif
    }

    open fun handleResize(width: Int, height: Int) {
        //#if MC>=11500
        super.resize(MultiClient.getInstance(), width, height)
        //#else
        //$$ super.setWorldAndResolution(MultiClient.getInstance(), width, height)
        //#endif
    }

    open fun handleBackgroundRender(
        stack: MultiMatrixStack,
        tint: Int
    ) {
        //#if MC>=11600
        super.renderBackground(stack.toVanillaStack(), tint)
        //#elseif MC>=11500
        //$$ super.renderBackground(tint)
        //#else
        //$$ super.drawWorldBackground(tint)
        //#endif
    }

    open fun doesPauseGame() = super.shouldPause()

    fun restorePreviousScreen() {
        openScreen(previousScreen)
    }

    //#if MC>=11500
    final override fun getTitle() = Text.translatable(titleKey ?: "").toVanilla()

    final override fun init() {
        handleInitialize(width, height)
    }

    //#if MC>=11600
    final override fun render(stack: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        handleRender(MultiMatrixStack(stack), mouseX, mouseY, tickDelta)
    }
    //#else
    //$$ final override fun render(mouseX: Int, mouseY: Int, tickDelta: Float) {
    //$$     handleRender(MultiMatrixStack(), mouseX, mouseY, tickDelta)
    //$$ }
    //#endif

    final override fun keyPressed(code: Int, scancode: Int, modifiers: Int): Boolean {
        handleKeyPress(code, 0.toChar(), modifiers)
        return false
    }

    final override fun charTyped(char: Char, modifiers: Int): Boolean {
        handleKeyPress(0, char, modifiers)
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
        handleMouseDragged(mouseX, mouseY, mouseBtn, MultiClient.getTime() - lastClick)
        return false
    }

    final override fun mouseScrolled(mouseX: Double, mouseY: Double, scrollDelta: Double): Boolean {
        scrolledX = mouseX
        scrolledY = mouseY
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

    //#if MC>=11600
    final override fun renderBackground(stack: MatrixStack, tint: Int) {
        handleBackgroundRender(MultiMatrixStack(stack), tint)
    }
    //#else
    //$$ final override fun renderBackground(tint: Int) {
    //$$     handleBackgroundRender(MultiMatrixStack(), tint)
    //$$ }
    //#endif

    final override fun shouldPause() = doesPauseGame()
    //#else
    //$$ final override fun initGui() {
    //$$     handleInitialize(width, height)
    //$$ }
    //$$
    //$$ final override fun drawScreen(mouseX: Int, mouseY: Int, tickDelta: Float) {
    //$$     handleRender(MultiMatrixStack(), mouseX, mouseY, tickDelta)
    //$$ }
    //$$
    //$$ final override fun keyTyped(typedChar: Char, keyCode: Int) {
    //$$     handleKeyPress(keyCode, typedChar, 0)
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
    //$$     handleBackgroundRender(MultiMatrixStack(), tint)
    //$$ }
    //$$
    //$$ final override fun doesGuiPauseGame() = doesPauseGame()
    //#endif
}
