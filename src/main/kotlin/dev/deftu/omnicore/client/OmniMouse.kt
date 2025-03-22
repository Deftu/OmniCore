package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.render.OmniResolution
import kotlin.math.max

//#if MC >= 1.15
import org.lwjgl.glfw.GLFW
//#elseif MC <= 1.12.2
//$$ import org.lwjgl.input.Mouse
//#endif

@GameSide(Side.CLIENT)
public object OmniMouse {

    //#if MC >= 1.15
    @JvmField
    @GameSide(Side.CLIENT)
    public val LEFT: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_LEFT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val RIGHT: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_RIGHT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val MIDDLE: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_MIDDLE }

    @JvmField
    @GameSide(Side.CLIENT)
    public val BACK: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_4 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val FORWARD: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_5 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val BUTTON6: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_6 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val BUTTON7: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_7 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val BUTTON8: Int = noInline { GLFW.GLFW_MOUSE_BUTTON_8 }
    //#else
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val LEFT: Int = noInline { 0 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val RIGHT: Int = noInline { 1 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val MIDDLE: Int = noInline { 2 } // FIXME Is this correct?
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val BACK: Int = noInline { 3 } // FIXME Is this correct?
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val FORWARD: Int = noInline { 4 } // FIXME Is this correct?
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val BUTTON6: Int = noInline { 5 } // FIXME Is this correct?
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val BUTTON7: Int = noInline { 6 } // FIXME Is this correct?
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val BUTTON8: Int = noInline { 7 } // FIXME Is this correct?
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val rawX: Double
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().mouse.x
            //#else
            //$$ return Mouse.getX().toDouble()
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val rawY: Double
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().mouse.y
            //#else
            //$$ return OmniResolution.screenHeight - Mouse.getY().toDouble() - 1
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val scaledX: Double
        get() = rawX * OmniResolution.scaledWidth / max(1, OmniResolution.windowWidth)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val scaledY: Double
        get() = rawY * OmniResolution.scaledHeight / max(1, OmniResolution.windowHeight)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public var isCursorGrabbed: Boolean
        get() {
            //#if MC >= 1.14
            return OmniClient.getInstance().mouse.isCursorLocked
            //#else
            //$$ return Mouse.isGrabbed()
            //#endif
        }
        set(value) {
            //#if MC >= 1.14
            if (value) {
                OmniClient.getInstance().mouse.lockCursor()
            } else {
                OmniClient.getInstance().mouse.unlockCursor()
            }
            //#else
            //$$ Mouse.setGrabbed(value)
            //#endif
        }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun isMouseButton(code: Int): Boolean {
        //#if MC >= 1.15
        return code < 20
        //#else
        //$$ return code < 0
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun isPressed(code: Int): Boolean {
        //#if MC >= 1.15
        val handle = OmniClient.getInstance().window.handle
        val state = if (!OmniKeyboard.isKeyboardButton(code)) GLFW.glfwGetMouseButton(handle, code) else OmniKeyboard.isPressed(code)
        return state == GLFW.GLFW_PRESS || state == GLFW.GLFW_REPEAT
        //#else
        //$$ return if (isMouseButton(code)) {
        //$$     Mouse.isButtonDown(code + 100)
        //$$ } else if (OmniKeyboard.isKeyboardButton(code)) {
        //$$     OmniKeyboard.isPressed(code)
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
