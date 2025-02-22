package dev.deftu.omnicore.client

//#if MC >= 1.15
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
//#else
//$$ import org.lwjgl.input.Keyboard
//#endif

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public object OmniKeyboard {

    @GameSide(Side.CLIENT)
    public data class KeyboardModifiers(
        val shift: Boolean,
        val ctrl: Boolean,
        val alt: Boolean
    )

    //#if MC >= 1.15
    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NONE: Int = noInline { InputUtil.UNKNOWN_KEY.code }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_ESCAPE: Int = noInline { GLFW.GLFW_KEY_ESCAPE }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_LMETA: Int = noInline { GLFW.GLFW_KEY_LEFT_SUPER }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_RMETA: Int = noInline { GLFW.GLFW_KEY_RIGHT_SUPER }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_LCONTROL: Int = noInline { GLFW.GLFW_KEY_LEFT_CONTROL }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_RCONTROL: Int = noInline { GLFW.GLFW_KEY_RIGHT_CONTROL }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_LSHIFT: Int = noInline { GLFW.GLFW_KEY_LEFT_SHIFT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_RSHIFT: Int = noInline { GLFW.GLFW_KEY_RIGHT_SHIFT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_LMENU: Int = noInline { GLFW.GLFW_KEY_LEFT_ALT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_RMENU: Int = noInline { GLFW.GLFW_KEY_RIGHT_ALT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_MINUS: Int = noInline { GLFW.GLFW_KEY_MINUS }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_EQUALS: Int = noInline { GLFW.GLFW_KEY_EQUAL }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_BACKSPACE: Int = noInline { GLFW.GLFW_KEY_BACKSPACE }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_ENTER: Int = noInline { GLFW.GLFW_KEY_ENTER }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_TAB: Int = noInline { GLFW.GLFW_KEY_TAB }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_LBRACKET: Int = noInline { GLFW.GLFW_KEY_LEFT_BRACKET }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_RBRACKET: Int = noInline { GLFW.GLFW_KEY_RIGHT_BRACKET }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_SEMICOLON: Int = noInline { GLFW.GLFW_KEY_SEMICOLON }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_APOSTROPHE: Int = noInline { GLFW.GLFW_KEY_APOSTROPHE }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_GRAVE: Int = noInline { GLFW.GLFW_KEY_GRAVE_ACCENT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_BACKSLASH: Int = noInline { GLFW.GLFW_KEY_BACKSLASH }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_COMMA: Int = noInline { GLFW.GLFW_KEY_COMMA }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_PERIOD: Int = noInline { GLFW.GLFW_KEY_PERIOD }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_SLASH: Int = noInline { GLFW.GLFW_KEY_SLASH }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_MULTIPLY: Int = noInline { GLFW.GLFW_KEY_KP_MULTIPLY }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_SPACE: Int = noInline { GLFW.GLFW_KEY_SPACE }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_CAPITAL: Int = noInline { GLFW.GLFW_KEY_CAPS_LOCK }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_LEFT: Int = noInline { GLFW.GLFW_KEY_LEFT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_UP: Int = noInline { GLFW.GLFW_KEY_UP }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_RIGHT: Int = noInline { GLFW.GLFW_KEY_RIGHT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_DOWN: Int = noInline { GLFW.GLFW_KEY_DOWN }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMLOCK: Int = noInline { GLFW.GLFW_KEY_NUM_LOCK }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_SCROLL: Int = noInline { GLFW.GLFW_KEY_SCROLL_LOCK }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_SUBTRACT: Int = noInline { GLFW.GLFW_KEY_KP_SUBTRACT }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_ADD: Int = noInline { GLFW.GLFW_KEY_KP_ADD }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_DIVIDE: Int = noInline { GLFW.GLFW_KEY_KP_DIVIDE }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_DECIMAL: Int = noInline { GLFW.GLFW_KEY_KP_DECIMAL }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD0: Int = noInline { GLFW.GLFW_KEY_KP_0 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD1: Int = noInline { GLFW.GLFW_KEY_KP_1 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD2: Int = noInline { GLFW.GLFW_KEY_KP_2 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD3: Int = noInline { GLFW.GLFW_KEY_KP_3 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD4: Int = noInline { GLFW.GLFW_KEY_KP_4 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD5: Int = noInline { GLFW.GLFW_KEY_KP_5 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD6: Int = noInline { GLFW.GLFW_KEY_KP_6 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD7: Int = noInline { GLFW.GLFW_KEY_KP_7 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD8: Int = noInline { GLFW.GLFW_KEY_KP_8 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_NUMPAD9: Int = noInline { GLFW.GLFW_KEY_KP_9 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_A: Int = noInline { GLFW.GLFW_KEY_A }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_B: Int = noInline { GLFW.GLFW_KEY_B }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_C: Int = noInline { GLFW.GLFW_KEY_C }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_D: Int = noInline { GLFW.GLFW_KEY_D }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_E: Int = noInline { GLFW.GLFW_KEY_E }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F: Int = noInline { GLFW.GLFW_KEY_F }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_G: Int = noInline { GLFW.GLFW_KEY_G }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_H: Int = noInline { GLFW.GLFW_KEY_H }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_I: Int = noInline { GLFW.GLFW_KEY_I }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_J: Int = noInline { GLFW.GLFW_KEY_J }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_K: Int = noInline { GLFW.GLFW_KEY_K }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_L: Int = noInline { GLFW.GLFW_KEY_L }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_M: Int = noInline { GLFW.GLFW_KEY_M }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_N: Int = noInline { GLFW.GLFW_KEY_N }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_O: Int = noInline { GLFW.GLFW_KEY_O }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_P: Int = noInline { GLFW.GLFW_KEY_P }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_Q: Int = noInline { GLFW.GLFW_KEY_Q }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_R: Int = noInline { GLFW.GLFW_KEY_R }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_S: Int = noInline { GLFW.GLFW_KEY_S }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_T: Int = noInline { GLFW.GLFW_KEY_T }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_U: Int = noInline { GLFW.GLFW_KEY_U }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_V: Int = noInline { GLFW.GLFW_KEY_V }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_W: Int = noInline { GLFW.GLFW_KEY_W }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_X: Int = noInline { GLFW.GLFW_KEY_X }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_Y: Int = noInline { GLFW.GLFW_KEY_Y }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_Z: Int = noInline { GLFW.GLFW_KEY_Z }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_0: Int = noInline { GLFW.GLFW_KEY_0 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_1: Int = noInline { GLFW.GLFW_KEY_1 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_2: Int = noInline { GLFW.GLFW_KEY_2 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_3: Int = noInline { GLFW.GLFW_KEY_3 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_4: Int = noInline { GLFW.GLFW_KEY_4 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_5: Int = noInline { GLFW.GLFW_KEY_5 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_6: Int = noInline { GLFW.GLFW_KEY_6 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_7: Int = noInline { GLFW.GLFW_KEY_7 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_8: Int = noInline { GLFW.GLFW_KEY_8 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_9: Int = noInline { GLFW.GLFW_KEY_9 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F1: Int = noInline { GLFW.GLFW_KEY_F1 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F2: Int = noInline { GLFW.GLFW_KEY_F2 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F3: Int = noInline { GLFW.GLFW_KEY_F3 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F4: Int = noInline { GLFW.GLFW_KEY_F4 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F5: Int = noInline { GLFW.GLFW_KEY_F5 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F6: Int = noInline { GLFW.GLFW_KEY_F6 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F7: Int = noInline { GLFW.GLFW_KEY_F7 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F8: Int = noInline { GLFW.GLFW_KEY_F8 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F9: Int = noInline { GLFW.GLFW_KEY_F9 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F10: Int = noInline { GLFW.GLFW_KEY_F10 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F11: Int = noInline { GLFW.GLFW_KEY_F11 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F12: Int = noInline { GLFW.GLFW_KEY_F12 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F13: Int = noInline { GLFW.GLFW_KEY_F13 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F14: Int = noInline { GLFW.GLFW_KEY_F14 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F15: Int = noInline { GLFW.GLFW_KEY_F15 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F16: Int = noInline { GLFW.GLFW_KEY_F16 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F17: Int = noInline { GLFW.GLFW_KEY_F17 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F18: Int = noInline { GLFW.GLFW_KEY_F18 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_F19: Int = noInline { GLFW.GLFW_KEY_F19 }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_DELETE: Int = noInline { GLFW.GLFW_KEY_DELETE }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_HOME: Int = noInline { GLFW.GLFW_KEY_HOME }

    @JvmField
    @GameSide(Side.CLIENT)
    public val KEY_END: Int = noInline { GLFW.GLFW_KEY_END }
    //#else
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NONE: Int = noInline { Keyboard.KEY_NONE }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_ESCAPE: Int = noInline { Keyboard.KEY_ESCAPE }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_LMETA: Int = noInline { Keyboard.KEY_LMETA }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_RMETA: Int = noInline { Keyboard.KEY_RMETA }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_LCONTROL: Int = noInline { Keyboard.KEY_LCONTROL }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_RCONTROL: Int = noInline { Keyboard.KEY_RCONTROL }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_LSHIFT: Int = noInline { Keyboard.KEY_LSHIFT }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_RSHIFT: Int = noInline { Keyboard.KEY_RSHIFT }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_LMENU: Int = noInline { Keyboard.KEY_LMENU }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_RMENU: Int = noInline { Keyboard.KEY_RMENU }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_MINUS: Int = noInline { Keyboard.KEY_MINUS }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_EQUALS: Int = noInline { Keyboard.KEY_EQUALS }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_BACKSPACE: Int = noInline { Keyboard.KEY_BACK }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_ENTER: Int = noInline { Keyboard.KEY_RETURN }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_TAB: Int = noInline { Keyboard.KEY_TAB }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_LBRACKET: Int = noInline { Keyboard.KEY_LBRACKET }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_RBRACKET: Int = noInline { Keyboard.KEY_RBRACKET }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_SEMICOLON: Int = noInline { Keyboard.KEY_SEMICOLON }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_APOSTROPHE: Int = noInline { Keyboard.KEY_APOSTROPHE }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_GRAVE: Int = noInline { Keyboard.KEY_GRAVE }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_BACKSLASH: Int = noInline { Keyboard.KEY_BACKSLASH }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_COMMA: Int = noInline { Keyboard.KEY_COMMA }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_PERIOD: Int = noInline { Keyboard.KEY_PERIOD }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_SLASH: Int = noInline { Keyboard.KEY_SLASH }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_MULTIPLY: Int = noInline { Keyboard.KEY_MULTIPLY }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_SPACE: Int = noInline { Keyboard.KEY_SPACE }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_CAPITAL: Int = noInline { Keyboard.KEY_CAPITAL }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_LEFT: Int = noInline { Keyboard.KEY_LEFT }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_UP: Int = noInline { Keyboard.KEY_UP }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_RIGHT: Int = noInline { Keyboard.KEY_RIGHT }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_DOWN: Int = noInline { Keyboard.KEY_DOWN }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMLOCK: Int = noInline { Keyboard.KEY_NUMLOCK }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_SCROLL: Int = noInline { Keyboard.KEY_SCROLL }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_SUBTRACT: Int = noInline { Keyboard.KEY_SUBTRACT }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_ADD: Int = noInline { Keyboard.KEY_ADD }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_DIVIDE: Int = noInline { Keyboard.KEY_DIVIDE }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_DECIMAL: Int = noInline { Keyboard.KEY_DECIMAL }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD0: Int = noInline { Keyboard.KEY_NUMPAD0 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD1: Int = noInline { Keyboard.KEY_NUMPAD1 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD2: Int = noInline { Keyboard.KEY_NUMPAD2 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD3: Int = noInline { Keyboard.KEY_NUMPAD3 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD4: Int = noInline { Keyboard.KEY_NUMPAD4 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD5: Int = noInline { Keyboard.KEY_NUMPAD5 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD6: Int = noInline { Keyboard.KEY_NUMPAD6 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD7: Int = noInline { Keyboard.KEY_NUMPAD7 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD8: Int = noInline { Keyboard.KEY_NUMPAD8 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_NUMPAD9: Int = noInline { Keyboard.KEY_NUMPAD9 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_A: Int = noInline { Keyboard.KEY_A }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_B: Int = noInline { Keyboard.KEY_B }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_C: Int = noInline { Keyboard.KEY_C }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_D: Int = noInline { Keyboard.KEY_D }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_E: Int = noInline { Keyboard.KEY_E }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F: Int = noInline { Keyboard.KEY_F }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_G: Int = noInline { Keyboard.KEY_G }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_H: Int = noInline { Keyboard.KEY_H }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_I: Int = noInline { Keyboard.KEY_I }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_J: Int = noInline { Keyboard.KEY_J }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_K: Int = noInline { Keyboard.KEY_K }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_L: Int = noInline { Keyboard.KEY_L }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_M: Int = noInline { Keyboard.KEY_M }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_N: Int = noInline { Keyboard.KEY_N }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_O: Int = noInline { Keyboard.KEY_O }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_P: Int = noInline { Keyboard.KEY_P }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_Q: Int = noInline { Keyboard.KEY_Q }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_R: Int = noInline { Keyboard.KEY_R }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_S: Int = noInline { Keyboard.KEY_S }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_T: Int = noInline { Keyboard.KEY_T }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_U: Int = noInline { Keyboard.KEY_U }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_V: Int = noInline { Keyboard.KEY_V }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_W: Int = noInline { Keyboard.KEY_W }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_X: Int = noInline { Keyboard.KEY_X }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_Y: Int = noInline { Keyboard.KEY_Y }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_Z: Int = noInline { Keyboard.KEY_Z }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_0: Int = noInline { Keyboard.KEY_0 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_1: Int = noInline { Keyboard.KEY_1 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_2: Int = noInline { Keyboard.KEY_2 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_3: Int = noInline { Keyboard.KEY_3 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_4: Int = noInline { Keyboard.KEY_4 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_5: Int = noInline { Keyboard.KEY_5 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_6: Int = noInline { Keyboard.KEY_6 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_7: Int = noInline { Keyboard.KEY_7 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_8: Int = noInline { Keyboard.KEY_8 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_9: Int = noInline { Keyboard.KEY_9 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F1: Int = noInline { Keyboard.KEY_F1 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F2: Int = noInline { Keyboard.KEY_F2 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F3: Int = noInline { Keyboard.KEY_F3 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F4: Int = noInline { Keyboard.KEY_F4 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F5: Int = noInline { Keyboard.KEY_F5 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F6: Int = noInline { Keyboard.KEY_F6 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F7: Int = noInline { Keyboard.KEY_F7 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F8: Int = noInline { Keyboard.KEY_F8 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F9: Int = noInline { Keyboard.KEY_F9 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F10: Int = noInline { Keyboard.KEY_F10 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F11: Int = noInline { Keyboard.KEY_F11 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F12: Int = noInline { Keyboard.KEY_F12 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F13: Int = noInline { Keyboard.KEY_F13 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F14: Int = noInline { Keyboard.KEY_F14 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F15: Int = noInline { Keyboard.KEY_F15 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F16: Int = noInline { Keyboard.KEY_F16 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F17: Int = noInline { Keyboard.KEY_F17 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F18: Int = noInline { Keyboard.KEY_F18 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_F19: Int = noInline { Keyboard.KEY_F19 }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_DELETE: Int = noInline { Keyboard.KEY_DELETE }
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_HOME: Int = noInline { Keyboard.KEY_HOME }
    //$$
    //$$
    //$$ @JvmField
    //$$ @GameSide(Side.CLIENT)
    //$$ public val KEY_END: Int = noInline { Keyboard.KEY_END }
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isShiftKeyPressed: Boolean
        get() = isPressed(KEY_LSHIFT) || isPressed(KEY_RSHIFT)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isCtrlKeyPressed: Boolean
        get() = isPressed(KEY_LCONTROL) || isPressed(KEY_RCONTROL)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isAltKeyPressed: Boolean
        get() = isPressed(KEY_LMENU) || isPressed(KEY_RMENU)

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val modifiers: KeyboardModifiers
        get() = KeyboardModifiers(
            shift = isShiftKeyPressed,
            ctrl = isCtrlKeyPressed,
            alt = isAltKeyPressed
        )

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun allowRepeatEvents(@Suppress("UNUSED_PARAMETER") enabled: Boolean) {
        //#if MC >= 1.19.3
        // This function was removed in 1.19.3. Repeat events are permanently enabled.
        //#elseif MC >= 1.15.2
        //$$ OmniClient.getInstance().keyboardHandler.setSendRepeatsToGui(enabled)
        //#else
        //$$ Keyboard.enableRepeatEvents(enabled)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun isKeyboardButton(code: Int): Boolean {
        //#if MC >= 1.15
        return code > 20
        //#else
        //$$ return code >= 0 && code < Keyboard.KEYBOARD_SIZE
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun isPressed(code: Int): Boolean {
        if (code == 0) return false // TODO

        //#if MC >= 1.16.5
        val handle = OmniClient.getInstance().window.handle
        val state = if (!OmniMouse.isMouseButton(code)) GLFW.glfwGetKey(handle, code) else OmniMouse.isPressed(code)
        return state == GLFW.GLFW_PRESS || state == GLFW.GLFW_REPEAT
        //#else
        //$$ return if (isKeyboardButton(code)) {
        //$$     Keyboard.isKeyDown(code)
        //$$ } else if (OmniMouse.isMouseButton(code)) {
        //$$     OmniMouse.isPressed(code)
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

//#if MC >= 1.16.5
@GameSide(Side.CLIENT)
public fun OmniKeyboard.KeyboardModifiers?.toInt(): Int = listOf(
    this?.shift to GLFW.GLFW_MOD_SHIFT,
    this?.ctrl to GLFW.GLFW_MOD_CONTROL,
    this?.alt to GLFW.GLFW_MOD_ALT
).sumOf { (mod, glfw) ->
    if (mod == true) glfw else 0
}

@GameSide(Side.CLIENT)
public fun Int.toKeyboardModifiers(): OmniKeyboard.KeyboardModifiers = OmniKeyboard.KeyboardModifiers(
    shift = (this and GLFW.GLFW_MOD_SHIFT) != 0,
    ctrl = (this and GLFW.GLFW_MOD_CONTROL) != 0,
    alt = (this and GLFW.GLFW_MOD_ALT) != 0
)
//#endif
