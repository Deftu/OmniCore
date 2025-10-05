package dev.deftu.omnicore.api.chat

import dev.deftu.omnicore.internal.asReadableString
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.HoverEvent
import dev.deftu.textile.minecraft.MCText
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.minecraft.server.network.ServerPlayerEntity

//#if MC <= 1.8.9
//$$ import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket
//#endif

public object OmniChat {
    //#if MC >= 1.16.5 && MC < 1.19.2
    //$$ internal val NULL_UUID = java.util.UUID(0, 0)
    //#endif

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
    public fun displayChatMessage(player: ServerPlayerEntity, text: Text) {
        player
            //#if MC >= 1.19.2
            .sendMessage(MCText.convert(text), false)
            //#elseif MC >= 1.16.5
            //$$ .sendSystemMessage(MCText.convert(text), NULL_UUID)
            //#else
            //$$ .sendMessage(MCText.convert(text))
            //#endif
    }

    @JvmStatic
    public fun displayChatMessage(player: ServerPlayerEntity, text: String) {
        displayChatMessage(player, Text.literal(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(player: ServerPlayerEntity, content: Text, throwable: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(player, buildErrorMessage(content, throwable, isDetailed))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(player: ServerPlayerEntity, error: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(player, buildErrorMessage(error, isDetailed))
    }

    @JvmStatic
    public fun displayActionBar(player: ServerPlayerEntity, text: Text) {
        //#if MC >= 1.19.2
        player.sendMessage(MCText.convert(text), true)
        //#elseif MC >= 1.12.2
        //$$ player.sendMessage(MCText.convert(text), true)
        //#else
        //$$ val packet = ChatMessageS2CPacket(MCText.convert(text) , 2.toByte())
        //$$ player.networkHandler.sendPacket(packet)
        //#endif
    }

    @JvmStatic
    public fun displayActionBar(player: ServerPlayerEntity, text: String) {
        displayActionBar(player, Text.literal(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayTitle(
        player: ServerPlayerEntity,
        title: Text,
        subtitle: Text? = null,
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
        displayTitle(player, Text.literal(title), subtitle?.let(Text::literal), fadeIn, stay, fadeOut)
    }

    @JvmStatic
    public fun buildErrorMessage(throwable: Throwable, isDetailed: Boolean = true): Text {
        val text = Text.literal(throwable.message ?: "An error occurred")
            .setStyle(MCTextStyle(color = TextColors.RED).build())
        if (isDetailed) {
            text.fillStyle(MCTextStyle(hoverEvent = HoverEvent.ShowText(Text.literal(throwable.asReadableString))).build())
        }

        return text
    }

    @JvmStatic
    public fun buildErrorMessage(content: Text, throwable: Throwable, isDetailed: Boolean = true): Text {
        val displayedText = Text.empty()
            .append(content)
        if (isDetailed) {
            displayedText.fillStyle(MCTextStyle(hoverEvent = HoverEvent.ShowText(Text.literal(throwable.asReadableString))).build())
        }

        return displayedText
    }
}
