package dev.deftu.omnicore.api.client.input

import org.lwjgl.glfw.GLFW

public object OmniMouseButtons {
    @JvmField public val LEFT: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)
    @JvmField public val RIGHT: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT)
    @JvmField public val MIDDLE: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_MIDDLE)
    @JvmField public val BACK: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_4)
    @JvmField public val FORWARD: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_5)
    @JvmField public val BUTTON6: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_6)
    @JvmField public val BUTTON7: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_7)
    @JvmField public val BUTTON8: OmniMouseButton = OmniMouseButton(GLFW.GLFW_MOUSE_BUTTON_8)
}
