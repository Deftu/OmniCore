package dev.deftu.omnicore.api.client.input

import org.lwjgl.input.Keyboard

public object OmniKeys {
    // --- Special / Unknown ---
    @JvmField public val KEY_NONE: OmniKey = OmniKey(Keyboard.KEY_NONE, "Unknown")

    // --- Function Keys ---
    @JvmField public val KEY_ESCAPE: OmniKey = OmniKey(Keyboard.KEY_ESCAPE, "Escape")
    @JvmField public val KEY_F1: OmniKey = OmniKey(Keyboard.KEY_F1, "F1")
    @JvmField public val KEY_F2: OmniKey = OmniKey(Keyboard.KEY_F2, "F2")
    @JvmField public val KEY_F3: OmniKey = OmniKey(Keyboard.KEY_F3, "F3")
    @JvmField public val KEY_F4: OmniKey = OmniKey(Keyboard.KEY_F4, "F4")
    @JvmField public val KEY_F5: OmniKey = OmniKey(Keyboard.KEY_F5, "F5")
    @JvmField public val KEY_F6: OmniKey = OmniKey(Keyboard.KEY_F6, "F6")
    @JvmField public val KEY_F7: OmniKey = OmniKey(Keyboard.KEY_F7, "F7")
    @JvmField public val KEY_F8: OmniKey = OmniKey(Keyboard.KEY_F8, "F8")
    @JvmField public val KEY_F9: OmniKey = OmniKey(Keyboard.KEY_F9, "F9")
    @JvmField public val KEY_F10: OmniKey = OmniKey(Keyboard.KEY_F10, "F10")
    @JvmField public val KEY_F11: OmniKey = OmniKey(Keyboard.KEY_F11, "F11")
    @JvmField public val KEY_F12: OmniKey = OmniKey(Keyboard.KEY_F12, "F12")
    @JvmField public val KEY_F13: OmniKey = OmniKey(Keyboard.KEY_F13, "F13")
    @JvmField public val KEY_F14: OmniKey = OmniKey(Keyboard.KEY_F14, "F14")
    @JvmField public val KEY_F15: OmniKey = OmniKey(Keyboard.KEY_F15, "F15")

    // --- Modifiers ---
    @JvmField public val KEY_LEFT_SHIFT: OmniKey = OmniKey(Keyboard.KEY_LSHIFT, "Left Shift")
    @JvmField public val KEY_RIGHT_SHIFT: OmniKey = OmniKey(Keyboard.KEY_RSHIFT, "Right Shift")
    @JvmField public val KEY_LEFT_CONTROL: OmniKey = OmniKey(Keyboard.KEY_LCONTROL, "Left Control")
    @JvmField public val KEY_RIGHT_CONTROL: OmniKey = OmniKey(Keyboard.KEY_RCONTROL, "Right Control")
    @JvmField public val KEY_LEFT_ALT: OmniKey = OmniKey(Keyboard.KEY_LMENU, "Left Alt")
    @JvmField public val KEY_RIGHT_ALT: OmniKey = OmniKey(Keyboard.KEY_RMENU, "Right Alt")
    @JvmField public val KEY_LEFT_SUPER: OmniKey = OmniKey(Keyboard.KEY_LMETA, "Left Super")
    @JvmField public val KEY_RIGHT_SUPER: OmniKey = OmniKey(Keyboard.KEY_RMETA, "Right Super")
    @JvmField public val KEY_CAPS_LOCK: OmniKey = OmniKey(Keyboard.KEY_CAPITAL, "Caps Lock")
    @JvmField public val KEY_NUM_LOCK: OmniKey = OmniKey(Keyboard.KEY_NUMLOCK, "Num Lock")
    @JvmField public val KEY_SCROLL_LOCK: OmniKey = OmniKey(Keyboard.KEY_SCROLL, "Scroll Lock")

    // --- Control / Editing ---
    @JvmField public val KEY_TAB: OmniKey = OmniKey(Keyboard.KEY_TAB, "Tab")
    @JvmField public val KEY_ENTER: OmniKey = OmniKey(Keyboard.KEY_RETURN, "Enter")
    @JvmField public val KEY_BACKSPACE: OmniKey = OmniKey(Keyboard.KEY_BACK, "Backspace")
    @JvmField public val KEY_DELETE: OmniKey = OmniKey(Keyboard.KEY_DELETE, "Delete")
    @JvmField public val KEY_INSERT: OmniKey = OmniKey(Keyboard.KEY_INSERT, "Insert")
    @JvmField public val KEY_PAGE_UP: OmniKey = OmniKey(Keyboard.KEY_PRIOR, "Page Up")
    @JvmField public val KEY_PAGE_DOWN: OmniKey = OmniKey(Keyboard.KEY_NEXT, "Page Down")
    @JvmField public val KEY_HOME: OmniKey = OmniKey(Keyboard.KEY_HOME, "Home")
    @JvmField public val KEY_END: OmniKey = OmniKey(Keyboard.KEY_END, "End")

    // --- Arrows ---
    @JvmField public val KEY_LEFT: OmniKey = OmniKey(Keyboard.KEY_LEFT, "Left Arrow")
    @JvmField public val KEY_RIGHT: OmniKey = OmniKey(Keyboard.KEY_RIGHT, "Right Arrow")
    @JvmField public val KEY_UP: OmniKey = OmniKey(Keyboard.KEY_UP, "Up Arrow")
    @JvmField public val KEY_DOWN: OmniKey = OmniKey(Keyboard.KEY_DOWN, "Down Arrow")

    // --- Numbers / Letters ---
    @JvmField public val KEY_0: OmniKey = OmniKey(Keyboard.KEY_0, "0")
    @JvmField public val KEY_1: OmniKey = OmniKey(Keyboard.KEY_1, "1")
    @JvmField public val KEY_2: OmniKey = OmniKey(Keyboard.KEY_2, "2")
    @JvmField public val KEY_3: OmniKey = OmniKey(Keyboard.KEY_3, "3")
    @JvmField public val KEY_4: OmniKey = OmniKey(Keyboard.KEY_4, "4")
    @JvmField public val KEY_5: OmniKey = OmniKey(Keyboard.KEY_5, "5")
    @JvmField public val KEY_6: OmniKey = OmniKey(Keyboard.KEY_6, "6")
    @JvmField public val KEY_7: OmniKey = OmniKey(Keyboard.KEY_7, "7")
    @JvmField public val KEY_8: OmniKey = OmniKey(Keyboard.KEY_8, "8")
    @JvmField public val KEY_9: OmniKey = OmniKey(Keyboard.KEY_9, "9")

    @JvmField public val KEY_A: OmniKey = OmniKey(Keyboard.KEY_A, "A")
    @JvmField public val KEY_B: OmniKey = OmniKey(Keyboard.KEY_B, "B")
    @JvmField public val KEY_C: OmniKey = OmniKey(Keyboard.KEY_C, "C")
    @JvmField public val KEY_D: OmniKey = OmniKey(Keyboard.KEY_D, "D")
    @JvmField public val KEY_E: OmniKey = OmniKey(Keyboard.KEY_E, "E")
    @JvmField public val KEY_F: OmniKey = OmniKey(Keyboard.KEY_F, "F")
    @JvmField public val KEY_G: OmniKey = OmniKey(Keyboard.KEY_G, "G")
    @JvmField public val KEY_H: OmniKey = OmniKey(Keyboard.KEY_H, "H")
    @JvmField public val KEY_I: OmniKey = OmniKey(Keyboard.KEY_I, "I")
    @JvmField public val KEY_J: OmniKey = OmniKey(Keyboard.KEY_J, "J")
    @JvmField public val KEY_K: OmniKey = OmniKey(Keyboard.KEY_K, "K")
    @JvmField public val KEY_L: OmniKey = OmniKey(Keyboard.KEY_L, "L")
    @JvmField public val KEY_M: OmniKey = OmniKey(Keyboard.KEY_M, "M")
    @JvmField public val KEY_N: OmniKey = OmniKey(Keyboard.KEY_N, "N")
    @JvmField public val KEY_O: OmniKey = OmniKey(Keyboard.KEY_O, "O")
    @JvmField public val KEY_P: OmniKey = OmniKey(Keyboard.KEY_P, "P")
    @JvmField public val KEY_Q: OmniKey = OmniKey(Keyboard.KEY_Q, "Q")
    @JvmField public val KEY_R: OmniKey = OmniKey(Keyboard.KEY_R, "R")
    @JvmField public val KEY_S: OmniKey = OmniKey(Keyboard.KEY_S, "S")
    @JvmField public val KEY_T: OmniKey = OmniKey(Keyboard.KEY_T, "T")
    @JvmField public val KEY_U: OmniKey = OmniKey(Keyboard.KEY_U, "U")
    @JvmField public val KEY_V: OmniKey = OmniKey(Keyboard.KEY_V, "V")
    @JvmField public val KEY_W: OmniKey = OmniKey(Keyboard.KEY_W, "W")
    @JvmField public val KEY_X: OmniKey = OmniKey(Keyboard.KEY_X, "X")
    @JvmField public val KEY_Y: OmniKey = OmniKey(Keyboard.KEY_Y, "Y")
    @JvmField public val KEY_Z: OmniKey = OmniKey(Keyboard.KEY_Z, "Z")

    // --- Symbols ---
    @JvmField public val KEY_SPACE: OmniKey = OmniKey(Keyboard.KEY_SPACE, "Space")
    @JvmField public val KEY_APOSTROPHE: OmniKey = OmniKey(Keyboard.KEY_APOSTROPHE, "Apostrophe")
    @JvmField public val KEY_COMMA: OmniKey = OmniKey(Keyboard.KEY_COMMA, "Comma")
    @JvmField public val KEY_MINUS: OmniKey = OmniKey(Keyboard.KEY_MINUS, "Minus")
    @JvmField public val KEY_PERIOD: OmniKey = OmniKey(Keyboard.KEY_PERIOD, "Period")
    @JvmField public val KEY_SLASH: OmniKey = OmniKey(Keyboard.KEY_SLASH, "Slash")
    @JvmField public val KEY_SEMICOLON: OmniKey = OmniKey(Keyboard.KEY_SEMICOLON, "Semicolon")
    @JvmField public val KEY_EQUAL: OmniKey = OmniKey(Keyboard.KEY_EQUALS, "Equal")
    @JvmField public val KEY_LEFT_BRACKET: OmniKey = OmniKey(Keyboard.KEY_LBRACKET, "Left Bracket")
    @JvmField public val KEY_BACKSLASH: OmniKey = OmniKey(Keyboard.KEY_BACKSLASH, "Backslash")
    @JvmField public val KEY_RIGHT_BRACKET: OmniKey = OmniKey(Keyboard.KEY_RBRACKET, "Right Bracket")
    @JvmField public val KEY_GRAVE_ACCENT: OmniKey = OmniKey(Keyboard.KEY_GRAVE, "Grave Accent")

    // --- Keypad ---
    @JvmField public val KEY_NUMPAD_0: OmniKey = OmniKey(Keyboard.KEY_NUMPAD0, "Numpad 0")
    @JvmField public val KEY_NUMPAD_1: OmniKey = OmniKey(Keyboard.KEY_NUMPAD1, "Numpad 1")
    @JvmField public val KEY_NUMPAD_2: OmniKey = OmniKey(Keyboard.KEY_NUMPAD2, "Numpad 2")
    @JvmField public val KEY_NUMPAD_3: OmniKey = OmniKey(Keyboard.KEY_NUMPAD3, "Numpad 3")
    @JvmField public val KEY_NUMPAD_4: OmniKey = OmniKey(Keyboard.KEY_NUMPAD4, "Numpad 4")
    @JvmField public val KEY_NUMPAD_5: OmniKey = OmniKey(Keyboard.KEY_NUMPAD5, "Numpad 5")
    @JvmField public val KEY_NUMPAD_6: OmniKey = OmniKey(Keyboard.KEY_NUMPAD6, "Numpad 6")
    @JvmField public val KEY_NUMPAD_7: OmniKey = OmniKey(Keyboard.KEY_NUMPAD7, "Numpad 7")
    @JvmField public val KEY_NUMPAD_8: OmniKey = OmniKey(Keyboard.KEY_NUMPAD8, "Numpad 8")
    @JvmField public val KEY_NUMPAD_9: OmniKey = OmniKey(Keyboard.KEY_NUMPAD9, "Numpad 9")

    @JvmField public val KEY_NUMPAD_DECIMAL: OmniKey = OmniKey(Keyboard.KEY_DECIMAL, "Numpad Decimal")
    @JvmField public val KEY_NUMPAD_DIVIDE: OmniKey = OmniKey(Keyboard.KEY_DIVIDE, "Numpad Divide")
    @JvmField public val KEY_NUMPAD_MULTIPLY: OmniKey = OmniKey(Keyboard.KEY_MULTIPLY, "Numpad Multiply")
    @JvmField public val KEY_NUMPAD_SUBTRACT: OmniKey = OmniKey(Keyboard.KEY_SUBTRACT, "Numpad Subtract")
    @JvmField public val KEY_NUMPAD_ADD: OmniKey = OmniKey(Keyboard.KEY_ADD, "Numpad Add")
    @JvmField public val KEY_NUMPAD_ENTER: OmniKey = OmniKey(Keyboard.KEY_NUMPADENTER, "Numpad Enter")
    @JvmField public val KEY_NUMPAD_EQUAL: OmniKey = OmniKey(Keyboard.KEY_NUMPADEQUALS, "Numpad Equal")

    // --- Miscellaneous ---
    @JvmField public val KEY_PRINT_SCREEN: OmniKey = OmniKey(Keyboard.KEY_SYSRQ, "Print Screen")
    @JvmField public val KEY_PAUSE: OmniKey = OmniKey(Keyboard.KEY_PAUSE, "Pause")
    @JvmField public val KEY_MENU: OmniKey = OmniKey(Keyboard.KEY_APPS, "Menu")

    @JvmField public val ALL: List<OmniKey> = listOf(
        KEY_NONE,

        KEY_ESCAPE, KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5, KEY_F6, KEY_F7, KEY_F8, KEY_F9, KEY_F10, KEY_F11, KEY_F12, KEY_F13, KEY_F14, KEY_F15,

        KEY_LEFT_SHIFT, KEY_RIGHT_SHIFT, KEY_LEFT_CONTROL, KEY_RIGHT_CONTROL, KEY_LEFT_ALT, KEY_RIGHT_ALT, KEY_LEFT_SUPER, KEY_RIGHT_SUPER,
        KEY_CAPS_LOCK, KEY_NUM_LOCK, KEY_SCROLL_LOCK,

        KEY_TAB, KEY_ENTER, KEY_BACKSPACE, KEY_DELETE, KEY_INSERT, KEY_PAGE_UP, KEY_PAGE_DOWN, KEY_HOME, KEY_END,

        KEY_LEFT, KEY_RIGHT, KEY_UP, KEY_DOWN,

        KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9,
        KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I, KEY_J,
        KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S,
        KEY_T, 	KEY_U,	KEY_V,	KEY_W,	KEY_X,	KEY_Y,	KEY_Z,

        KEY_SPACE,
        KEY_APOSTROPHE,
        KEY_COMMA,
        KEY_MINUS,
        KEY_PERIOD,
        KEY_SLASH,
        KEY_SEMICOLON,
        KEY_EQUAL,
        KEY_LEFT_BRACKET,
        KEY_BACKSLASH,
        KEY_RIGHT_BRACKET,
        KEY_GRAVE_ACCENT,

        KEY_NUMPAD_0,
        KEY_NUMPAD_1,
        KEY_NUMPAD_2,
        KEY_NUMPAD_3,
        KEY_NUMPAD_4,
        KEY_NUMPAD_5,
        KEY_NUMPAD_6,
        KEY_NUMPAD_7,
        KEY_NUMPAD_8,
        KEY_NUMPAD_9,

        KEY_NUMPAD_DECIMAL,
        KEY_NUMPAD_DIVIDE,
        KEY_NUMPAD_MULTIPLY,
        KEY_NUMPAD_SUBTRACT,
        KEY_NUMPAD_ADD,
        KEY_NUMPAD_ENTER,
        KEY_NUMPAD_EQUAL,

        KEY_PRINT_SCREEN,
        KEY_PAUSE,
        KEY_MENU,
    )

    @JvmField public val BY_CODE: Map<Int, OmniKey> = ALL.associateBy(OmniKey::code)

    @JvmStatic
    public fun from(code: Int): OmniKey {
        return BY_CODE[code] ?: KEY_NONE
    }

    @JvmStatic
    public fun name(code: Int): String {
        return from(code).name ?: KEY_NONE.name!!
    }
}
