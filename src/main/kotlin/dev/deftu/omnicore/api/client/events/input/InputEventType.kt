package dev.deftu.omnicore.api.client.events.input

public enum class InputEventType {
    KEY,
    MOUSE;

    public val isKey: Boolean
        get() = this == KEY

    public val isMouse: Boolean
        get() = this == MOUSE
}
