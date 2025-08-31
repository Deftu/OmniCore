package dev.deftu.omnicore.api.client.events

import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.client.input.OmniMouseButtons

public sealed interface InputEvent {
    public val type: InputEventType
    public val state: InputState

    public data class Key(
        override val state: InputState,
        val key: OmniKey,
        @field:VersionedAbove("1.16.5") val scanCode: Int,
        val modifiers: KeyboardModifiers
    ) : InputEvent {
        override val type: InputEventType = InputEventType.KEY
    }

    public data class MouseButton(
        override val state: InputState,
        val button: OmniMouseButton,
        val modifiers: KeyboardModifiers
    ) : InputEvent {
        override val type: InputEventType = InputEventType.MOUSE

        public val isLeftButton: Boolean
            get() = button == OmniMouseButtons.LEFT

        public val isRightButton: Boolean
            get() = button == OmniMouseButtons.RIGHT

        public val isMiddleButton: Boolean
            get() = button == OmniMouseButtons.MIDDLE

        public val isForwardSideButton: Boolean
            get() = button == OmniMouseButtons.FORWARD

        public val isBackSideButton: Boolean
            get() = button == OmniMouseButtons.BACK

        public val isSideButton: Boolean
            get() = isForwardSideButton || isBackSideButton
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
