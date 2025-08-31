package dev.deftu.omnicore.api.client.input

import dev.deftu.omnicore.client.OmniClient
import dev.deftu.omnicore.client.render.OmniResolution
import kotlin.math.max

//#if MC >= 1.15
import org.lwjgl.glfw.GLFW
//#elseif MC <= 1.12.2
//$$ import org.lwjgl.input.Mouse
//#endif

public object OmniMouse {
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
            //$$ return OmniResolution.windowHeight - Mouse.getY().toDouble() - 1
            //#endif
        }

    @JvmStatic
    public val scaledX: Double
        get() = rawX * OmniResolution.scaledWidth / max(1, OmniResolution.windowWidth)

    @JvmStatic
    public val scaledY: Double
        get() = rawY * OmniResolution.scaledHeight / max(1, OmniResolution.windowHeight)

    @JvmStatic
    public var isCursorGrabbed: Boolean
        get() {
            //#if MC >= 1.16.5
            return OmniClient.getInstance().mouse.isCursorLocked
            //#else
            //$$ return Mouse.isGrabbed()
            //#endif
        }
        set(value) {
            //#if MC >= 1.16.5
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
    public fun isMouseButton(code: Int): Boolean {
        //#if MC >= 1.16.5
        return code in GLFW.GLFW_MOUSE_BUTTON_1..GLFW.GLFW_MOUSE_BUTTON_8
        //#else
        //$$ return code in 0..8
        //#endif
    }

    @JvmStatic
    public fun isPressed(code: Int): Boolean {
        if (!isMouseButton(code)) {
            return false
        }

        //#if MC >= 1.16.5
        val handle = OmniClient.getInstance().window.handle
        val state = GLFW.glfwGetMouseButton(handle, code)
        return state == GLFW.GLFW_PRESS || state == GLFW.GLFW_REPEAT
        //#else
        //$$ return Mouse.isButtonDown(code + 100)
        //#endif
    }
}
