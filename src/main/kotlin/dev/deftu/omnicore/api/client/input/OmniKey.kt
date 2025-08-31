package dev.deftu.omnicore.api.client.input

public data class OmniKey(override val code: Int) : OmniInputCode {
    override val isPressed: Boolean
        get() = OmniKeyboard.isPressed(code)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OmniKey) return false

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }
}
