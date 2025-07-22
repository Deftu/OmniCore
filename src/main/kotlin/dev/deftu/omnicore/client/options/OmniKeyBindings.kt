package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.annotations.VersionedAbove
import dev.deftu.omnicore.client.keybindings.OmniKeyBinding
import dev.deftu.omnicore.client.keybindings.WrappedKeyBinding

@GameSide(Side.CLIENT)
public object OmniKeyBindings {

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val forward: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.forwardKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val left: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.leftKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val back: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.backKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val right: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.rightKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val jump: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.jumpKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val sneak: OmniKeyBinding by lazy {
        //#if MC >= 1.16.5
        WrappedKeyBinding(options.sneakKey)
        //#else
        //$$ WrappedKeyBinding(options.keyBindSneak)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val sprint: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.sprintKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val inventory: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.inventoryKey)
    }

    //#if MC >= 1.12.2
    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.12.2")
    public val swapHands: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.swapHandsKey)
    }
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val drop: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.dropKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val use: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.useKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val attack: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.attackKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val pickItem: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.pickItemKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chat: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.chatKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val playerList: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.playerListKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val command: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.commandKey)
    }

    //#if MC >= 1.19.2
    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.19.2")
    public val socialInteractions: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.socialInteractionsKey)
    }
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val screenshot: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.screenshotKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val togglePerspective: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.togglePerspectiveKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val smoothCamera: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.smoothCameraKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val fullscreen: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.fullscreenKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public val spectatorOutlines: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.spectatorOutlinesKey)
    }

    //#if MC >= 1.12.2
    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.12.2")
    public val advancements: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.advancementsKey)
    }
    //#endif

    //#if MC >= 1.21.6
    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.21.6")
    public val quickActions: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.quickActionsKey)
    }
    //#endif

    //#if MC >= 1.16.5
    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.16.5")
    public val saveToolbarActivator: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.saveToolbarActivatorKey)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.16.5")
    public val loadToolbarActivator: OmniKeyBinding by lazy {
        WrappedKeyBinding(options.loadToolbarActivatorKey)
    }
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getHotbarKeyBinding(slot: Int): OmniKeyBinding {
        require(slot in 0..8) { "Hotbar slot must be between 0 and 8, inclusive." }
        return WrappedKeyBinding(options.hotbarKeys[slot])
    }

}
