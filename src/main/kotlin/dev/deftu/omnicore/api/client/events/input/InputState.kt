package dev.deftu.omnicore.api.client.events.input

public enum class InputState {
    PRESSED,
    RELEASED,
    REPEATED,
    INVALID;

    public val isPressed: Boolean
        get() = this == PRESSED || this == REPEATED

    public val isReleased: Boolean
        get() = this == RELEASED
}
