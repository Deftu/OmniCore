package dev.deftu.omnicore.client.keybindings

import dev.deftu.eventbus.on
import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.events.InputEvent
import dev.deftu.omnicore.client.events.InputEventType
import dev.deftu.omnicore.client.events.InputState
import java.util.function.BiConsumer
import java.util.function.Consumer

public object OmniKeyBindings {
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun on(keyBinding: OmniKeyBinding, action: BiConsumer<InputEventType, InputState>): () -> Unit {
        val keyEventCallback = OmniCore.eventBus.on<InputEvent.Key> {
            if (keyBinding.matchesKey(keyCode, scanCode)) {
                action.accept(InputEventType.KEY, state)
            }
        }

        val mouseEventCallback = OmniCore.eventBus.on<InputEvent.MouseButton> {
            if (keyBinding.matchesMouse(button)) {
                action.accept(InputEventType.MOUSE, state)
            }
        }

        return {
            keyEventCallback()
            mouseEventCallback()
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun onKey(keyBinding: OmniKeyBinding, action: Consumer<InputState>): () -> Unit {
        return OmniCore.eventBus.on<InputEvent.Key> {
            if (keyBinding.matchesKey(keyCode, scanCode)) {
                action.accept(state)
            }
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun onMouse(keyBinding: OmniKeyBinding, action: Consumer<InputState>): () -> Unit {
        return OmniCore.eventBus.on<InputEvent.MouseButton> {
            if (keyBinding.matchesMouse(button)) {
                action.accept(state)
            }
        }
    }
}
