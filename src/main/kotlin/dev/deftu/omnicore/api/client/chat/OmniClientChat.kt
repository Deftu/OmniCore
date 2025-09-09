package dev.deftu.omnicore.api.client.chat

import dev.deftu.omnicore.api.chat.MessageSurface
import dev.deftu.omnicore.api.chat.OmniChat
import dev.deftu.omnicore.api.client.playerHud
import dev.deftu.omnicore.client.OmniClientPlayer
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextHolder

public object OmniClientChat {
    @JvmStatic
    public fun display(surface: MessageSurface) {
        when (surface) {
            is MessageSurface.ChatMessage -> displayChatMessage(surface.content)
            is MessageSurface.ErrorMessage -> displayErrorMessage(surface.content, surface.error, surface.isDetailed)
            is MessageSurface.ActionBar -> displayActionBar(surface.content)
            is MessageSurface.Title -> displayTitle(
                surface.title,
                surface.subtitle,
                surface.fadeIn,
                surface.stay,
                surface.fadeOut
            )
        }
    }

    @JvmStatic
    public fun displayChatMessage(text: MCTextHolder<*>) {
        val player = OmniClientPlayer.getInstance() ?: throw IllegalStateException("Player is null")
        player
            //#if MC >= 1.16.5
            .sendMessage(text.asVanilla(), false)
            //#else
            //$$ .sendMessage(text.asVanilla())
            //#endif
    }

    @JvmStatic
    public fun displayChatMessage(text: String) {
        displayChatMessage(MCSimpleTextHolder(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(content: MCTextHolder<*>, throwable: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(OmniChat.buildErrorMessage(content, throwable, isDetailed))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(error: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(OmniChat.buildErrorMessage(error, isDetailed))
    }

    @JvmStatic
    public fun displayActionBar(text: MCTextHolder<*>) {
        //#if MC >= 1.16.5
        val player = OmniClientPlayer.getInstance() ?: throw IllegalStateException("Player is null")
        player.sendMessage(text.asVanilla(), true)
        //#else
        //$$ networkHandler?.handleChat(S02PacketChat(text.asVanilla(), 2))
        //#endif
    }

    @JvmStatic
    public fun displayActionBar(text: String) {
        displayActionBar(MCSimpleTextHolder(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayTitle(
        title: MCTextHolder<*>,
        subtitle: MCTextHolder<*>? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    ) {
        //#if MC >= 1.17.1
        playerHud?.apply {
            clear()
            setTitleTicks(fadeIn, stay, fadeOut)
            subtitle?.let(MCTextHolder<*>::asVanilla).let(this::setSubtitle)
            setTitle(title.asVanilla())
        }
        //#else
        //$$ val hud = playerHud ?: return
        //$$ hud.setTitles(title.asVanilla(), subtitle?.asVanilla(), fadeIn, stay, fadeOut)
        //#endif
    }

    @JvmStatic
    @JvmOverloads
    public fun displayTitle(
        title: String,
        subtitle: String? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    ) {
        displayTitle(MCSimpleTextHolder(title), subtitle?.let(::MCSimpleTextHolder), fadeIn, stay, fadeOut)
    }
}
