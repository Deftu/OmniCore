package dev.deftu.omnicore.api.client.input

import dev.deftu.omnicore.client.OmniClient

//#if MC >= 1.16.5
import org.lwjgl.glfw.GLFW
//#else
//$$ import org.lwjgl.input.Keyboard
//#endif

public object OmniKeyboard {
    @JvmStatic
    public val isShiftKeyPressed: Boolean
        get() = OmniKeys.KEY_LEFT_SHIFT.isPressed || OmniKeys.KEY_RIGHT_SHIFT.isPressed

    @JvmStatic
    public val isCtrlKeyPressed: Boolean
        get() = OmniKeys.KEY_LEFT_CONTROL.isPressed || OmniKeys.KEY_RIGHT_CONTROL.isPressed

    @JvmStatic
    public val isAltKeyPressed: Boolean
        get() = OmniKeys.KEY_LEFT_ALT.isPressed || OmniKeys.KEY_RIGHT_ALT.isPressed

    @JvmStatic
    public val isSuperKeyPressed: Boolean
        get() = OmniKeys.KEY_LEFT_SUPER.isPressed || OmniKeys.KEY_RIGHT_SUPER.isPressed

    @JvmStatic
    public fun allowRepeatEvents(@Suppress("UNUSED_PARAMETER") enabled: Boolean) {
        //#if MC >= 1.19.3
        // This function was removed in 1.19.3. Repeat events are permanently enabled.
        //#elseif MC >= 1.16.5
        //$$ OmniClient.getInstance().keyboardHandler.setSendRepeatsToGui(enabled)
        //#else
        //$$ Keyboard.enableRepeatEvents(enabled)
        //#endif
    }

    @JvmStatic
    public fun isKeyboardButton(code: Int): Boolean {
        //#if MC >= 1.16.5
        return code in 0 until GLFW.GLFW_KEY_LAST
        //#else
        //$$ return code in 0..Keyboard.KEYBOARD_SIZE && code != Keyboard.KEY_NONE
        //#endif
    }

    @JvmStatic
    public fun isPressed(code: Int): Boolean {
        if (!isKeyboardButton(code)) {
            return false
        }

        //#if MC >= 1.16.5
        val handle = OmniClient.getInstance().window.handle
        val state = GLFW.glfwGetKey(handle, code)
        return state == GLFW.GLFW_PRESS || state == GLFW.GLFW_REPEAT
        //#else
        //$$ return Keyboard.isKeyDown(code)
        //#endif
    }
}
