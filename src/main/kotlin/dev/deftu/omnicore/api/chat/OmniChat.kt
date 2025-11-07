package dev.deftu.omnicore.api.chat

import dev.deftu.omnicore.internal.asReadableString
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.HoverEvent
import dev.deftu.textile.minecraft.MCText
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

//#if MC >= 1.17.1
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket
//#else
//$$ import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket
//#endif

//#if MC >= 1.12.2 && MC < 1.19.2
//$$ import net.minecraft.network.chat.ChatType
//#endif

//#if MC <= 1.12.2
//$$ import net.minecraft.network.play.server.SPacketChat
//#endif

public object OmniChat {
    //#if MC >= 1.16.5 && MC <= 1.18.2
    //$$ private val NULL_UUID = java.util.UUID(0L, 0L)
    //#endif

    @JvmStatic
    public fun display(server: MinecraftServer, surface: MessageSurface) {
        when (surface) {
            is MessageSurface.ChatMessage -> broadcastChatMessage(server, surface.content)
            is MessageSurface.ErrorMessage -> broadcastErrorMessage(server, surface.content, surface.error, surface.isDetailed)
            is MessageSurface.ActionBar -> broadcastActionBar(server, surface.content)
            is MessageSurface.Title -> broadcastTitle(server, surface.title)
        }
    }

    @JvmStatic
    public fun broadcastChatMessage(server: MinecraftServer, text: Text) {
        //#if MC >= 1.19.2
        server.playerList.broadcastSystemMessage(MCText.convert(text), false)
        //#elseif MC >= 1.16.5
        //$$ server.playerList.broadcastMessage(MCText.convert(text), ChatType.SYSTEM, NULL_UUID)
        //#else
        //$$ server.playerList.sendMessage(MCText.convert(text), true)
        //#endif
    }

    @JvmStatic
    public fun broadcastChatMessage(server: MinecraftServer, text: String) {
        broadcastChatMessage(server, Text.literal(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun broadcastErrorMessage(server: MinecraftServer, content: Text, throwable: Throwable, isDetailed: Boolean = true) {
        broadcastChatMessage(server, buildErrorMessage(content, throwable, isDetailed))
    }

    @JvmStatic
    @JvmOverloads
    public fun broadcastErrorMessage(server: MinecraftServer, error: Throwable, isDetailed: Boolean = true) {
        broadcastChatMessage(server, buildErrorMessage(error, isDetailed))
    }

    @JvmStatic
    public fun broadcastActionBar(server: MinecraftServer, text: Text) {
        //#if MC >= 1.19.2
        server.playerList.broadcastSystemMessage(MCText.convert(text), true)
        //#elseif MC >= 1.16.5
        //$$ server.playerList.broadcastMessage(MCText.convert(text), ChatType.GAME_INFO, NULL_UUID)
        //#else
        //$$ server.playerList.sendPacketToAllPlayers(SPacketChat(
        //$$     MCText.convert(text),
            //#if MC >= 1.12.2
            //$$ ChatType.GAME_INFO
            //#else
            //$$ 2.toByte()
            //#endif
        //$$ ))
        //#endif
    }

    @JvmStatic
    public fun broadcastActionBar(server: MinecraftServer, text: String) {
        broadcastActionBar(server, Text.literal(text))
    }

    @JvmStatic
    public fun broadcastTitle(server: MinecraftServer, titleInfo: TitleInfo) {
        with(titleInfo) {
            //#if MC >= 1.17.1
            server.playerList.broadcastAll(ClientboundSetTitlesAnimationPacket(timings.fadeIn, timings.stay, timings.fadeOut))
            server.playerList.broadcastAll(ClientboundSetTitleTextPacket(MCText.convert(title)))
            if (subtitle != null) {
                server.playerList.broadcastAll(ClientboundSetSubtitleTextPacket(MCText.convert(subtitle)))
            }
            //#else
            //$$ // Set timings
            //$$ server.playerList.broadcastAll(ClientboundSetTitlesPacket(timings.fadeIn, timings.stay, timings.fadeOut))
            //$$
            //$$ // Set title text
            //$$ server.playerList.broadcastAll(ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.TITLE, MCText.convert(title)))
            //$$
            //$$ // Set subtitle text
            //$$ if (subtitle != null) {
            //$$     server.playerList.broadcastAll(ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.SUBTITLE, MCText.convert(subtitle)))
            //$$ }
            //#endif
        }
    }

    @JvmStatic
    public fun display(player: ServerPlayer, surface: MessageSurface) {
        when (surface) {
            is MessageSurface.ChatMessage -> displayChatMessage(player,surface.content)
            is MessageSurface.ErrorMessage -> displayErrorMessage(player,surface.content, surface.error, surface.isDetailed)
            is MessageSurface.ActionBar -> displayActionBar(player,surface.content)
            is MessageSurface.Title -> displayTitle(player, surface.title)
        }
    }

    @JvmStatic
    public fun displayChatMessage(player: ServerPlayer, text: Text) {
        player
            //#if MC >= 1.16.5
            .displayClientMessage(MCText.convert(text), false)
            //#else
            //$$ .sendMessage(MCText.convert(text))
            //#endif
    }

    @JvmStatic
    public fun displayChatMessage(player: ServerPlayer, text: String) {
        displayChatMessage(player, Text.literal(text))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(player: ServerPlayer, content: Text, throwable: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(player, buildErrorMessage(content, throwable, isDetailed))
    }

    @JvmStatic
    @JvmOverloads
    public fun displayErrorMessage(player: ServerPlayer, error: Throwable, isDetailed: Boolean = true) {
        displayChatMessage(player, buildErrorMessage(error, isDetailed))
    }

    @JvmStatic
    public fun displayActionBar(player: ServerPlayer, text: Text) {
        //#if MC >= 1.19.2
        player.displayClientMessage(MCText.convert(text), true)
        //#elseif MC >= 1.12.2
        //$$ player.displayClientMessage(MCText.convert(text), true)
        //#else
        //$$ val packet = S02PacketChat(MCText.convert(text) , 2.toByte())
        //$$ player.playerNetServerHandler.sendPacket(packet)
        //#endif
    }

    @JvmStatic
    public fun displayActionBar(player: ServerPlayer, text: String) {
        displayActionBar(player, Text.literal(text))
    }

    @JvmStatic
    public fun displayTitle(player: ServerPlayer, titleInfo: TitleInfo) {
        with(titleInfo) {
            with(player.connection) {
                //#if MC >= 1.17.1
                send(ClientboundSetTitlesAnimationPacket(timings.fadeIn, timings.stay, timings.fadeOut))
                send(ClientboundSetTitleTextPacket(MCText.convert(title)))
                if (subtitle != null) {
                    send(ClientboundSetSubtitleTextPacket(MCText.convert(subtitle)))
                }
                //#else
                //$$ // Set timings
                //$$ send(ClientboundSetTitlesPacket(timings.fadeIn, timings.stay, timings.fadeOut))
                //$$
                //$$ // Set title text
                //$$ send(ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.TITLE, MCText.convert(title)))
                //$$
                //$$ // Set subtitle text
                //$$ if (subtitle != null) {
                //$$     send(ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.SUBTITLE, MCText.convert(subtitle)))
                //$$ }
                //#endif
            }
        }
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
