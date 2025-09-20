package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.events.input.InputEvent
import dev.deftu.omnicore.api.client.events.input.InputEventType
import dev.deftu.omnicore.api.client.events.input.InputState
import dev.deftu.omnicore.api.eventBus
import net.minecraft.client.option.GameOptions
import java.util.function.BiConsumer
import java.util.function.Consumer

public object OmniKeyBindings {
    internal inline val options: GameOptions
        get() = client.options

    @JvmStatic
    public val forward: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.forwardKey)
    }

    @JvmStatic
    public val left: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.leftKey)
    }

    @JvmStatic
    public val back: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.backKey)
    }

    @JvmStatic
    public val right: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.rightKey)
    }

    @JvmStatic
    public val jump: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.jumpKey)
    }

    @JvmStatic
    public val sneak: OmniKeyBinding by lazy {
        //#if MC >= 1.16.5
        WrappedKeyBinding(options.sneakKey)
        //#else
        //$$ WrappedKeyBinding(options.keyBindSneak)
        //#endif
    }

    @JvmStatic
    public val sprint: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.sprintKey)
    }

    @JvmStatic
    public val inventory: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.inventoryKey)
    }

    //#if MC >= 1.12.2
    @JvmStatic
    @VersionedAbove("1.12.2")
    public val swapHands: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.swapHandsKey)
    }
    //#endif

    @JvmStatic
    public val drop: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.dropKey)
    }

    @JvmStatic
    public val use: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.useKey)
    }

    @JvmStatic
    public val attack: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.attackKey)
    }

    @JvmStatic
    public val pickItem: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.pickItemKey)
    }

    @JvmStatic
    public val chat: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.chatKey)
    }

    @JvmStatic
    public val playerList: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.playerListKey)
    }

    @JvmStatic
    public val command: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.commandKey)
    }

    //#if MC >= 1.19.2
    @JvmStatic
    @VersionedAbove("1.19.2")
    public val socialInteractions: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.socialInteractionsKey)
    }
    //#endif

    @JvmStatic
    public val screenshot: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.screenshotKey)
    }

    @JvmStatic
    public val togglePerspective: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.togglePerspectiveKey)
    }

    @JvmStatic
    public val smoothCamera: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.smoothCameraKey)
    }

    @JvmStatic
    public val fullscreen: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.fullscreenKey)
    }

    @JvmStatic
    public val spectatorOutlines: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.spectatorOutlinesKey)
    }

    //#if MC >= 1.12.2
    @JvmStatic
    @VersionedAbove("1.12.2")
    public val advancements: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.advancementsKey)
    }
    //#endif

    //#if MC >= 1.21.6
    @JvmStatic
    @VersionedAbove("1.21.6")
    public val quickActions: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.quickActionsKey)
    }
    //#endif

    //#if MC >= 1.16.5
    @JvmStatic
    @VersionedAbove("1.16.5")
    public val saveToolbarActivator: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.saveToolbarActivatorKey)
    }

    @JvmStatic
    @VersionedAbove("1.16.5")
    public val loadToolbarActivator: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.loadToolbarActivatorKey)
    }
    //#endif

    @JvmStatic
    public fun on(keyBinding: OmniKeyBinding, action: BiConsumer<InputEventType, InputState>): () -> Unit {
        val keyEventCallback = eventBus.on<InputEvent.Key> {
            if (keyBinding.matchesKey(key.code, scanCode)) {
                action.accept(InputEventType.KEY, state)
            }
        }

        val mouseEventCallback = eventBus.on<InputEvent.MouseButton> {
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
    public fun onKey(keyBinding: OmniKeyBinding, action: Consumer<InputState>): () -> Unit {
        return eventBus.on<InputEvent.Key> {
            if (keyBinding.matchesKey(key.code, scanCode)) {
                action.accept(state)
            }
        }
    }

    @JvmStatic
    public fun onMouse(keyBinding: OmniKeyBinding, action: Consumer<InputState>): () -> Unit {
        return eventBus.on<InputEvent.MouseButton> {
            if (keyBinding.matchesMouse(button)) {
                action.accept(state)
            }
        }
    }

    @JvmStatic
    public fun getHotbarKeyBinding(slot: Int): OmniKeyBinding {
        require(slot in 0..8) { "Hotbar slot must be between 0 and 8, inclusive." }
        return WrappedKeyBinding(options.hotbarKeys[slot])
    }
}
