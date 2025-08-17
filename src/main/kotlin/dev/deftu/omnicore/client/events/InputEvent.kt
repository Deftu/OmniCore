package dev.deftu.omnicore.client.events

import dev.deftu.omnicore.annotations.VersionedAbove
import dev.deftu.omnicore.client.OmniKeyboard
import dev.deftu.omnicore.client.OmniMouse

public sealed interface InputEvent {
    public val type: InputEventType
    public val state: InputState

    public data class Key(
        override val state: InputState,
        val keyCode: Int,
        @field:VersionedAbove("1.16.5") val scanCode: Int,
        val modifiers: OmniKeyboard.KeyboardModifiers
    ) : InputEvent {
        override val type: InputEventType = InputEventType.KEY
    }

    public data class MouseButton(
        override val state: InputState,
        val button: Int,
        val modifiers: OmniKeyboard.KeyboardModifiers
    ) : InputEvent {
        override val type: InputEventType = InputEventType.MOUSE

        public val isLeftButton: Boolean
            get() = button == OmniMouse.LEFT

        public val isRightButton: Boolean
            get() = button == OmniMouse.RIGHT

        public val isMiddleButton: Boolean
            get() = button == OmniMouse.MIDDLE

        public val isLeftSideButton: Boolean
            get() = button == OmniMouse.LEFT

        public val isRightSideButton: Boolean
            get() = button == OmniMouse.RIGHT

        public val isSideButton: Boolean
            get() = isLeftSideButton || isRightSideButton
    }
}

public enum class InputEventType {
    KEY,
    MOUSE;

    public val isKey: Boolean
        get() = this == KEY

    public val isMouse: Boolean
        get() = this == MOUSE
}

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
