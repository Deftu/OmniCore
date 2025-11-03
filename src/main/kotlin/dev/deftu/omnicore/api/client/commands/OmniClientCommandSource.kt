package dev.deftu.omnicore.api.client.commands

import com.mojang.brigadier.Command
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.network.OmniClientNetworking
import dev.deftu.omnicore.api.network.PacketPayload
import dev.deftu.omnicore.api.scheduling.TickSchedulers
import dev.deftu.textile.Text
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer

public object OmniClientCommandSource {
    public inline val client: Minecraft
        get() = Minecraft.getInstance()

    public inline val player: LocalPlayer?
        get() = client.player

    public inline val world: ClientLevel?
        get() = client.level

    public fun openScreen(screen: Screen): Int {
        TickSchedulers.client.post {
            client.setScreen(screen)
        }

        return Command.SINGLE_SUCCESS
    }

    public fun replyChat(text: Text): Int {
        OmniClientChat.displayChatMessage(text)
        return Command.SINGLE_SUCCESS
    }

    public fun replyChat(text: String): Int {
        return replyChat(Text.literal(text))
    }

    @JvmOverloads
    public fun replyError(content: Text, throwable: Throwable, isDetailed: Boolean = true): Int {
        OmniClientChat.displayErrorMessage(content, throwable, isDetailed)
        return Command.SINGLE_SUCCESS
    }

    @JvmOverloads
    public fun replyError(error: Throwable, isDetailed: Boolean = true): Int {
        OmniClientChat.displayErrorMessage(error, isDetailed)

        return Command.SINGLE_SUCCESS
    }

    public fun replyActionBar(text: Text): Int {
        OmniClientChat.displayActionBar(text)
        return Command.SINGLE_SUCCESS
    }

    public fun replyActionBar(text: String): Int {
        return replyActionBar(Text.literal(text))
    }

    public fun replyTitle(
        title: Text,
        subtitle: Text? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    ): Int {
        OmniClientChat.displayTitle(title, subtitle, fadeIn, stay, fadeOut)
        return Command.SINGLE_SUCCESS
    }

    public fun replyTitle(
        title: String,
        subtitle: String? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    ): Int {
        return replyTitle(
            Text.literal(title),
            subtitle?.let(Text::literal),
            fadeIn,
            stay,
            fadeOut
        )
    }

    public fun replyPacket(payload: PacketPayload): Int {
        OmniClientNetworking.send(payload)
        return Command.SINGLE_SUCCESS
    }
}
