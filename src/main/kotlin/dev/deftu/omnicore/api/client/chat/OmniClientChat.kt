package dev.deftu.omnicore.api.client.chat

import dev.deftu.omnicore.api.chat.MessageSurface
import dev.deftu.omnicore.api.chat.OmniChat
import dev.deftu.omnicore.api.chat.TitleInfo
import dev.deftu.omnicore.api.client.player
import dev.deftu.omnicore.api.client.playerHud
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCText

//#if MC <= 1.12.2
//$$ import dev.deftu.omnicore.api.client.client
//$$ import dev.deftu.textile.CollapseMode
//$$ import net.minecraft.network.play.server.SPacketChat
//#endif

public object OmniClientChat {
    @JvmStatic
    public fun display(surface: MessageSurface) {
        when (surface) {
            is MessageSurface.ChatMessage -> displayChatMessage(surface.content)
            is MessageSurface.ErrorMessage -> displayErrorMessage(surface.content, surface.error, surface.isDetailed)
            is MessageSurface.ActionBar -> displayActionBar(surface.content)
            is MessageSurface.Title -> displayTitle(surface.title)
        }
    }

    @JvmStatic
    public fun displayChatMessage(text: Text) {
        player
            //#if MC >= 1.16.5
            ?.displayClientMessage(MCText.convert(text), false)
            //#else
            //$$ ?.sendMessage(MCText.convert(text))
            //#endif
    }

    @JvmStatic
    public fun displayChatMessage(text: String) {
        displayChatMessage(Text.literal(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(content: Text, throwable: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(OmniChat.buildErrorMessage(content, throwable, isDetailed))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(error: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(OmniChat.buildErrorMessage(error, isDetailed))
    }

    @JvmStatic
    public fun displayActionBar(text: Text) {
        //#if MC >= 1.16.5
        player?.displayClientMessage(MCText.convert(text), true)
        //#elseif MC >= 1.12.2
        //$$ player?.sendStatusMessage(MCText.convert(text), true)
        //#else
        //$$ client.netHandler?.handleChat(S02PacketChat(MCText.convert(text), 2))
        //#endif
    }

    @JvmStatic
    public fun displayActionBar(text: String) {
        displayActionBar(Text.literal(text))
    }

    @JvmStatic
    public fun displayTitle(titleInfo: TitleInfo) {
        with(titleInfo) {
            //#if MC >= 1.17.1
            playerHud?.apply {
                clearTitles()
                setTimes(timings.fadeIn, timings.stay, timings.fadeOut)
                subtitle?.let(MCText::convert)?.let(this::setSubtitle)
                setTitle(MCText.convert(title))
            }
            //#else
            //$$ val hud = playerHud ?: return
            //#if MC >= 1.16.5
            //$$ hud.setTitles(MCText.convert(title), subtitle?.let(MCText::convert), timings.fadeIn, timings.stay, timings.fadeOut)
            //#else
            //$$ hud.displayTitle(title.collapseToString(CollapseMode.SCOPED), subtitle?.collapseToString(CollapseMode.SCOPED), timings.fadeIn, timings.stay, timings.fadeOut)
            //#endif
            //#endif
        }
    }
}
