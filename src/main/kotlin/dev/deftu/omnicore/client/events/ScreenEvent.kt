package dev.deftu.omnicore.client.events

import dev.deftu.omnicore.api.client.input.OmniKeyboard
import dev.deftu.omnicore.common.events.CancellableEvent
import net.minecraft.client.gui.screen.Screen

public sealed interface ScreenEvent {

    public val screen: Screen

    public sealed interface Init : ScreenEvent {

        public data class Pre(override val screen: Screen) : Init
        public data class Post(override val screen: Screen) : Init

    }

    public sealed interface Render : ScreenEvent {
        public val matrixStack: OmniMatrixStack
        public val tickDelta: Float

        public data class Pre(
            override val screen: Screen,
            override val matrixStack: OmniMatrixStack,
            override val tickDelta: Float
        ) : Render

        public data class Post(
            override val screen: Screen,
            override val matrixStack: OmniMatrixStack,
            override val tickDelta: Float
        ) : Render
    }

    public sealed interface KeyPress : ScreenEvent {

        public val key: Int
        public val scanCode: Int
        public val modifiers: OmniKeyboard.KeyboardModifiers

        public data class Pre(
            override val screen: Screen,
            override val key: Int,
            override val scanCode: Int,
            override val modifiers: OmniKeyboard.KeyboardModifiers
        ) : KeyPress, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val key: Int,
            override val scanCode: Int,
            override val modifiers: OmniKeyboard.KeyboardModifiers
        ) : KeyPress

    }

    public sealed interface KeyRelease : ScreenEvent {

        public val key: Int
        public val scanCode: Int
        public val modifiers: OmniKeyboard.KeyboardModifiers

        public data class Pre(
            override val screen: Screen,
            override val key: Int,
            override val scanCode: Int,
            override val modifiers: OmniKeyboard.KeyboardModifiers
        ) : KeyRelease, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val key: Int,
            override val scanCode: Int,
            override val modifiers: OmniKeyboard.KeyboardModifiers
        ) : KeyRelease

    }

    public sealed interface MouseClick : ScreenEvent {

        public val button: Int
        public val x: Double
        public val y: Double

        public data class Pre(
            override val screen: Screen,
            override val button: Int,
            override val x: Double,
            override val y: Double
        ) : MouseClick, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val button: Int,
            override val x: Double,
            override val y: Double
        ) : MouseClick

    }

    public sealed interface MouseRelease : ScreenEvent {

        public val button: Int
        public val x: Double
        public val y: Double

        public data class Pre(
            override val screen: Screen,
            override val button: Int,
            override val x: Double,
            override val y: Double
        ) : MouseRelease, CancellableEvent {
            override var isCancelled: Boolean = false
        }

        public data class Post(
            override val screen: Screen,
            override val button: Int,
            override val x: Double,
            override val y: Double
        ) : MouseRelease

    }

}
