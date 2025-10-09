package dev.deftu.omnicore.api.client.input

import org.lwjgl.glfw.GLFW

public object OmniMouseButtons {
    @JvmField public val LEFT: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT, "Left")
    @JvmField public val RIGHT: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT, "Right")
    @JvmField public val MIDDLE: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_MIDDLE, "Middle")
    @JvmField public val BACK: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_4, "Side Back")
    @JvmField public val FORWARD: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_5, "Side Forward")
    @JvmField public val BUTTON6: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_6, "Button6")
    @JvmField public val BUTTON7: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_7, "Button7")
    @JvmField public val BUTTON8: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_8, "Button8")

    @JvmField public  val ALL: List<OmniMouseButton> = listOf(LEFT, RIGHT, MIDDLE, BACK, FORWARD, BUTTON6, BUTTON7, BUTTON8)
    @JvmField public val BY_CODE: Map<Int, OmniMouseButton> = ALL.associateBy(OmniMouseButton::code)

    @JvmStatic
    public fun from(code: Int): OmniMouseButton {
        return BY_CODE[code] ?: OmniMouseButton(code, "Unknown")
    }

    @JvmStatic
    public fun name(code: Int): String {
        return from(code).name
    }
}
