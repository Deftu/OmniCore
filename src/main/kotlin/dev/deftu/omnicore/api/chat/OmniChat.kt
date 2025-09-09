package dev.deftu.omnicore.api.chat

import dev.deftu.omnicore.internal.asReadableString
import dev.deftu.textile.minecraft.MCHoverEvent
import dev.deftu.textile.minecraft.MCSimpleMutableTextHolder
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextFormat
import dev.deftu.textile.minecraft.MCTextHolder
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.network.ServerPlayerEntity

public object OmniChat {
    @JvmStatic
    public fun display(player: ServerPlayerEntity, surface: MessageSurface) {
        when (surface) {
            is MessageSurface.ChatMessage -> displayChatMessage(player,surface.content)
            is MessageSurface.ErrorMessage -> displayErrorMessage(player,surface.content, surface.error, surface.isDetailed)
            is MessageSurface.ActionBar -> displayActionBar(player,surface.content)
            is MessageSurface.Title -> displayTitle(
                player,
                surface.title,
                surface.subtitle,
                surface.fadeIn,
                surface.stay,
                surface.fadeOut
            )
        }
    }

    @JvmStatic
    public fun displayChatMessage(player: ServerPlayerEntity, text: MCTextHolder<*>) {
        player
            //#if MC >= 1.16.5
            .sendMessage(text.asVanilla(), false)
            //#else
            //$$ .sendMessage(text.asVanilla())
            //#endif
    }

    @JvmStatic
    public fun displayChatMessage(player: ServerPlayerEntity, text: String) {
        displayChatMessage(player, MCSimpleTextHolder(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(player: ServerPlayerEntity, content: MCTextHolder<*>, throwable: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(player, buildErrorMessage(content, throwable, isDetailed))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(player: ServerPlayerEntity, error: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(player, buildErrorMessage(error, isDetailed))
    }

    @JvmStatic
    public fun displayActionBar(player: ServerPlayerEntity, text: MCTextHolder<*>) {
        //#if MC >= 1.16.5
        player.sendMessage(text.asVanilla(), true)
        //#else
        //$$ player.networkHandler?.handleChat(S02PacketChat(text.asVanilla(), 2))
        //#endif
    }

    @JvmStatic
    public fun displayActionBar(player: ServerPlayerEntity, text: String) {
        displayActionBar(player, MCSimpleTextHolder(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayTitle(
        player: ServerPlayerEntity,
        title: MCTextHolder<*>,
        subtitle: MCTextHolder<*>? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    ) {
        // TODO: Implement title display for server players
    }

    @JvmStatic
    @JvmOverloads
    public fun displayTitle(
        player: ServerPlayerEntity,
        title: String,
        subtitle: String? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    ) {
        displayTitle(player, MCSimpleTextHolder(title), subtitle?.let(::MCSimpleTextHolder), fadeIn, stay, fadeOut)
    }

    @JvmStatic
    public fun buildErrorMessage(throwable: Throwable, isDetailed: Boolean = true): MCTextHolder<*> {
        val text = MCSimpleMutableTextHolder(throwable.message ?: "An error occurred")
            .addFormatting(MCTextFormat.RED)
        if (isDetailed) {
            text.setHoverEvent(MCHoverEvent.ShowText(throwable.asReadableString))
        }

        return text
    }

    @JvmStatic
    public fun buildErrorMessage(content: MCTextHolder<*>, throwable: Throwable, isDetailed: Boolean = true): MCTextHolder<*> {
        val displayedText = MCSimpleMutableTextHolder("")
            .append(content)
        if (isDetailed) {
            displayedText.setHoverEvent(MCHoverEvent.ShowText(throwable.asReadableString))
        }

        return displayedText
    }
}
