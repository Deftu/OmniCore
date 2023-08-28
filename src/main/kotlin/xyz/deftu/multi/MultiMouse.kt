package xyz.deftu.multi

//#if MC >= 1.15
import org.lwjgl.glfw.GLFW
//#elseif MC <= 1.12.2
//$$ import org.lwjgl.input.Mouse
//#endif

import kotlin.math.max

object MultiMouse {
    //#if MC >= 1.15
    @JvmField val LEFT = noInline { GLFW.GLFW_MOUSE_BUTTON_LEFT }
    @JvmField val RIGHT = noInline { GLFW.GLFW_MOUSE_BUTTON_RIGHT }
    @JvmField val MIDDLE = noInline { GLFW.GLFW_MOUSE_BUTTON_MIDDLE }
    @JvmField val BACK = noInline { GLFW.GLFW_MOUSE_BUTTON_4 }
    @JvmField val FORWARD = noInline { GLFW.GLFW_MOUSE_BUTTON_5 }
    @JvmField val BUTTON6 = noInline { GLFW.GLFW_MOUSE_BUTTON_6 }
    @JvmField val BUTTON7 = noInline { GLFW.GLFW_MOUSE_BUTTON_7 }
    @JvmField val BUTTON8 = noInline { GLFW.GLFW_MOUSE_BUTTON_8 }
    //#else
    //$$ @JvmField val LEFT = noInline { 0 }
    //$$ @JvmField val RIGHT = noInline { 1 }
    //$$ @JvmField val MIDDLE = noInline { 2 } // FIXME Is this correct?
    //$$ @JvmField val BACK = noInline { 3 } // FIXME Is this correct?
    //$$ @JvmField val FORWARD = noInline { 4 } // FIXME Is this correct?
    //$$ @JvmField val BUTTON6 = noInline { 5 } // FIXME Is this correct?
    //$$ @JvmField val BUTTON7 = noInline { 6 } // FIXME Is this correct?
    //$$ @JvmField val BUTTON8 = noInline { 7 } // FIXME Is this correct?
    //#endif

    @JvmStatic
    val rawX: Double
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().mouse.x
            //#else
            //$$ return Mouse.getX().toDouble()
            //#endif
        }

    @JvmStatic
    val rawY: Double
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().mouse.y
            //#else
            //$$ return Mouse.getY().toDouble()
            //#endif
        }

    @JvmStatic
    val scaledX: Double
        get() = rawX * MultiResolution.scaledWidth / max(1, MultiResolution.screenWidth)

    @JvmStatic
    val scaledY: Double
        get() = rawY * MultiResolution.scaledHeight / max(1, MultiResolution.screenHeight)

    @JvmStatic
    val isCursorGrabbed: Boolean
        get() {
            //#if MC >= 1.14
            return MultiClient.getInstance().mouse.isCursorLocked
            //#else
            //$$ return Mouse.isGrabbed()
            //#endif
        }

    @JvmStatic
    fun setCursorGrabbed(grabbed: Boolean) {
        //#if MC >= 1.14
        if (grabbed) MultiClient.getInstance().mouse.lockCursor()
        else MultiClient.getInstance().mouse.unlockCursor()
        //#else
        //$$ Mouse.setGrabbed(grabbed)
        //#endif
    }

    @JvmStatic
    fun isMouseButton(code: Int): Boolean {
        //#if MC >= 1.15
        return code < 20
        //#else
        //$$ return code < 0
        //#endif
    }

    @JvmStatic
    fun isPressed(code: Int): Boolean {
        //#if MC >= 1.15
        val handle = MultiClient.getInstance().window.handle
        val state = if (!MultiKeyboard.isKeyboardButton(code)) GLFW.glfwGetMouseButton(handle, code) else MultiKeyboard.isPressed(code)
        return state == GLFW.GLFW_PRESS || state == GLFW.GLFW_REPEAT
        //#else
        //$$ return if (isMouseButton(code)) {
        //$$     Mouse.isButtonDown(code + 100)
        //$$ } else if (MultiKeyboard.isKeyboardButton(code)) {
        //$$     MultiKeyboard.isPressed(code)
        //$$ } else {
        //$$     false
        //$$ }
        //#endif
    }

    /**
     * Adapted from EssentialGG UniversalCraft under LGPL-3.0
     * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
     */
    private inline fun <T> noInline(init: () -> T): T = init()
}
