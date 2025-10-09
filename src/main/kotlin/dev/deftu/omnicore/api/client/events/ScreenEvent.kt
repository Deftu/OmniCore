package dev.deftu.omnicore.api.client.events

import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.input.OmniKey
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.events.CancellableEvent
import net.minecraft.client.gui.screen.Screen

public sealed interface ScreenEvent {
    public val screen: Screen

    public sealed interface Init : ScreenEvent {
        public data class Pre(override val screen: Screen) : Init
        public data class Post(override val screen: Screen) : Init
    }

    public sealed interface Render : ScreenEvent {
        public val context: OmniRenderingContext
        public val tickDelta: Float

        public data class Pre(
            override val screen: Screen,
            override val context: OmniRenderingContext,
            override val tickDelta: Float
        ) : Render

        public data class Post(
            override val screen: Screen,
            override val context: OmniRenderingContext,
            override val tickDelta: Float
        ) : Render
    }

    public sealed interface KeyPress : ScreenEvent {
        public val key: OmniKey
        public val scanCode: Int
        public val modifiers: KeyboardModifiers

        public data class Pre(
            override val screen: Screen,
            override val key: OmniKey,
            override val scanCode: Int,
            override val modifiers: KeyboardModifiers
        ) : KeyPress, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val key: OmniKey,
            override val scanCode: Int,
            override val modifiers: KeyboardModifiers
        ) : KeyPress
    }

    public sealed interface KeyRelease : ScreenEvent {
        public val key: OmniKey
        public val scanCode: Int
        public val modifiers: KeyboardModifiers

        public data class Pre(
            override val screen: Screen,
            override val key: OmniKey,
            override val scanCode: Int,
            override val modifiers: KeyboardModifiers
        ) : KeyRelease, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val key: OmniKey,
            override val scanCode: Int,
            override val modifiers: KeyboardModifiers
        ) : KeyRelease
    }

    public sealed interface MouseClick : ScreenEvent {
        public val button: OmniMouseButton
        public val x: Double
        public val y: Double

        public data class Pre(
            override val screen: Screen,
            override val button: OmniMouseButton,
            override val x: Double,
            override val y: Double
        ) : MouseClick, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val button: OmniMouseButton,
            override val x: Double,
            override val y: Double
        ) : MouseClick
    }

    public sealed interface MouseRelease : ScreenEvent {
        public val button: OmniMouseButton
        public val x: Double
        public val y: Double

        public data class Pre(
            override val screen: Screen,
            override val button: OmniMouseButton,
            override val x: Double,
            override val y: Double
        ) : MouseRelease, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val button: OmniMouseButton,
            override val x: Double,
            override val y: Double
        ) : MouseRelease
    }
}
