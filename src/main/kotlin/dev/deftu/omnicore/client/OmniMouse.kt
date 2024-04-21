package dev.deftu.omnicore.client

//#if MC >= 1.15
import org.lwjgl.glfw.GLFW
//#elseif MC <= 1.12.2
//$$ import org.lwjgl.input.Mouse
//#endif

import kotlin.math.max

public object OmniMouse {
    //#if MC >= 1.15
    @JvmField public val LEFT: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_LEFT }
    @JvmField public val RIGHT: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_RIGHT }
    @JvmField public val MIDDLE: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_MIDDLE }
    @JvmField public val BACK: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_4 }
    @JvmField public val FORWARD: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_5 }
    @JvmField public val BUTTON6: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_6 }
    @JvmField public val BUTTON7: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_7 }
    @JvmField public val BUTTON8: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_8 }
    //#else
    //$$ @JvmField public val LEFT: Int = noInline { 0 }
    //$$ @JvmField public val RIGHT: Int = noInline { 1 }
    //$$ @JvmField public val MIDDLE: Int = noInline { 2 } // FIXME Is this correct?
    //$$ @JvmField public val BACK: Int = noInline { 3 } // FIXME Is this correct?
    //$$ @JvmField public val FORWARD: Int = noInline { 4 } // FIXME Is this correct?
    //$$ @JvmField public val BUTTON6: Int = noInline { 5 } // FIXME Is this correct?
    //$$ @JvmField public val BUTTON7: Int = noInline { 6 } // FIXME Is this correct?
    //$$ @JvmField public val BUTTON8: Int = noInline { 7 } // FIXME Is this correct?
    //#endif

    @JvmStatic
    public val rawX: Double
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().mouse.x
            //#else
            //$$ return Mouse.getX().toDouble()
            //#endif
        }

    @JvmStatic
    public val rawY: Double
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().mouse.y
            //#else
            //$$ return Mouse.getY().toDouble()
            //#endif
        }

    @JvmStatic
    public val scaledX: Double
        get() = rawX * OmniResolution.scaledWidth / max(1, OmniResolution.screenWidth)

    @JvmStatic
    public val scaledY: Double
        get() = rawY * OmniResolution.scaledHeight / max(1, OmniResolution.screenHeight)

    @JvmStatic
    public val isCursorGrabbed: Boolean
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().mouse.isCursorLocked
            //#else
            //$$ return Mouse.isGrabbed()
            //#endif
        }

    @JvmStatic
    public fun setCursorGrabbed(grabbed: Boolean) {
        //#if MC >= 1.14
        if (grabbed) OmniClient.getInstance().mouse.lockCursor()
        else OmniClient.getInstance().mouse.unlockCursor()
        //#else
        //$$ Mouse.setGrabbed(grabbed)
        //#endif
    }

    @JvmStatic
    public fun isMouseButton(code: Int): Boolean {
        //#if MC >= 1.15
        return code < 20
        //#else
        //$$ return code < 0
        //#endif
    }

    @JvmStatic
    public fun isPressed(code: Int): Boolean {
        //#if MC >= 1.15
        val handle = OmniClient.getInstance().window.handle
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
