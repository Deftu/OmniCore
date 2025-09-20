package dev.deftu.omnicore.api.client.events.input

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
