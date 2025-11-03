package dev.deftu.omnicore.api.client.input.keybindings

import dev.deftu.eventbus.on
import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.events.input.InputEvent
import dev.deftu.omnicore.api.client.events.input.InputEventType
import dev.deftu.omnicore.api.client.events.input.InputState
import dev.deftu.omnicore.api.eventBus
import net.minecraft.client.Options
import java.util.function.BiConsumer
import java.util.function.Consumer

public object OmniKeyBindings {
    internal inline val options: Options
        get() = client.options

    @JvmStatic
    public val forward: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyUp)
    }

    @JvmStatic
    public val left: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyLeft)
    }

    @JvmStatic
    public val back: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyDown)
    }

    @JvmStatic
    public val right: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyRight)
    }

    @JvmStatic
    public val jump: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyJump)
    }

    @JvmStatic
    public val sneak: OmniKeyBinding by lazy {
        //#if MC >= 1.16.5
        OmniKeyBinding.wrap(options.keyShift)
        //#else
        //$$ OmniKeyBinding.wrap(options.keyBindSneak)
        //#endif
    }

    @JvmStatic
    public val sprint: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keySprint)
    }

    @JvmStatic
    public val inventory: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyInventory)
    }

    //#if MC >= 1.12.2
    @JvmStatic
    @VersionedAbove("1.12.2")
    public val swapHands: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keySwapOffhand)
    }
    //#endif

    @JvmStatic
    public val drop: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyDrop)
    }

    @JvmStatic
    public val use: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyUse)
    }

    @JvmStatic
    public val attack: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyAttack)
    }

    @JvmStatic
    public val pickItem: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyPickItem)
    }

    @JvmStatic
    public val chat: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyChat)
    }

    @JvmStatic
    public val playerList: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyPlayerList)
    }

    @JvmStatic
    public val command: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyCommand)
    }

    //#if MC >= 1.19.2
    @JvmStatic
    @VersionedAbove("1.19.2")
    public val socialInteractions: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keySocialInteractions)
    }
    //#endif

    @JvmStatic
    public val screenshot: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyScreenshot)
    }

    @JvmStatic
    public val togglePerspective: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyTogglePerspective)
    }

    @JvmStatic
    public val smoothCamera: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keySmoothCamera)
    }

    @JvmStatic
    public val fullscreen: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyFullscreen)
    }

    @JvmStatic
    public val spectatorOutlines: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keySpectatorOutlines)
    }

    //#if MC >= 1.12.2
    @JvmStatic
    @VersionedAbove("1.12.2")
    public val advancements: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyAdvancements)
    }
    //#endif

    //#if MC >= 1.21.6
    @JvmStatic
    @VersionedAbove("1.21.6")
    public val quickActions: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyQuickActions)
    }
    //#endif

    //#if MC >= 1.16.5
    @JvmStatic
    @VersionedAbove("1.16.5")
    public val saveToolbarActivator: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keySaveHotbarActivator)
    }

    @JvmStatic
    @VersionedAbove("1.16.5")
    public val loadToolbarActivator: OmniKeyBinding by lazy {
        OmniKeyBinding.wrap(options.keyLoadHotbarActivator)
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
        return OmniKeyBinding.wrap(options.keyHotbarSlots[slot])
    }
}
