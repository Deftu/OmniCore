package dev.deftu.omnicore.api.client.input

public data class OmniMouseButton(override val code: Int) : OmniInputCode {
    override val isPressed: Boolean
        get() = OmniMouse.isPressed(code)
}
