package dev.deftu.omnicore.api.client.input

public class OmniMouseButton internal constructor(override val code: Int, public val name: String) : OmniInputCode {
    override val isPressed: Boolean
        get() = OmniMouse.isPressed(code)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OmniMouseButton) return false

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }

    override fun toString(): String {
        return buildString {
            append(this@OmniMouseButton::class.simpleName)
            append("[").append(name).append(" (").append(code).append(")]")
        }
    }
}
