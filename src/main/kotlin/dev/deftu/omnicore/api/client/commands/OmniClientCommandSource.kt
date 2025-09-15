package dev.deftu.omnicore.api.client.commands

import com.mojang.brigadier.Command
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.network.OmniClientNetworking
import dev.deftu.omnicore.api.network.PacketPayload
import dev.deftu.omnicore.api.scheduling.TickSchedulers
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextHolder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld

public object OmniClientCommandSource {
    public inline val client: MinecraftClient
        get() = MinecraftClient.getInstance()

    public inline val player: ClientPlayerEntity?
        get() = client.player

    public inline val world: ClientWorld?
        get() = client.world

    public fun openScreen(screen: Screen): Int {
        TickSchedulers.client.post {
            client.setScreen(screen)
        }

        return Command.SINGLE_SUCCESS
    }

    public fun replyChat(text: MCTextHolder<*>): Int {
        OmniClientChat.displayChatMessage(text)
        return Command.SINGLE_SUCCESS
    }

    public fun replyChat(text: String): Int {
        return replyChat(MCSimpleTextHolder(text))
    }

    @JvmOverloads
    public fun replyError(content: MCTextHolder<*>, throwable: Throwable, isDetailed: Boolean = true): Int {
        OmniClientChat.displayErrorMessage(content, throwable, isDetailed)
        return Command.SINGLE_SUCCESS
    }

    @JvmOverloads
    public fun replyError(error: Throwable, isDetailed: Boolean = true): Int {
        OmniClientChat.displayErrorMessage(error, isDetailed)

        return Command.SINGLE_SUCCESS
    }

    public fun replyActionBar(text: MCTextHolder<*>): Int {
        OmniClientChat.displayActionBar(text)
        return Command.SINGLE_SUCCESS
    }

    public fun replyActionBar(text: String): Int {
        return replyActionBar(MCSimpleTextHolder(text))
    }

    public fun replyTitle(
        title: MCTextHolder<*>,
        subtitle: MCTextHolder<*>? = null,
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
            MCSimpleTextHolder(title),
            subtitle?.let { MCSimpleTextHolder(it) },
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
