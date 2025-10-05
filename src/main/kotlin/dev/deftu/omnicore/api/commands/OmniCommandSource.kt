package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.Command
import dev.deftu.omnicore.api.chat.OmniChat
import dev.deftu.omnicore.api.network.OmniNetworking
import dev.deftu.omnicore.api.network.PacketPayload
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCText
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

public class OmniCommandSource(
    public val server: MinecraftServer,
    public val output: CommandOutput,
    public val world: ServerWorld
) {
    public inline val player: ServerPlayerEntity?
        get() = output as? ServerPlayerEntity

    //#if MC <= 1.12.2
    //$$ internal constructor(server: MinecraftServer, output: ICommandSender) : this(
    //$$     server,
    //$$     output,
    //$$     output.entityWorld as WorldServer
    //$$ )
    //#endif

    public fun replyChat(text: Text): Int {
        output.sendMessage(
            MCText.convert(text),
            //#if MC >= 1.16.5 && MC < 1.19.2
            //$$ OmniChat.NULL_UUID
            //#endif
        )

        return Command.SINGLE_SUCCESS
    }

    public fun replyChat(text: String): Int {
        return replyChat(Text.literal(text))
    }

    @JvmOverloads
    public fun replyError(content: Text, throwable: Throwable, isDetailed: Boolean = true): Int {
        output.sendMessage(
            MCText.convert(OmniChat.buildErrorMessage(content, throwable, isDetailed)),
            //#if MC >= 1.16.5 && MC < 1.19.2
            //$$ OmniChat.NULL_UUID
            //#endif
        )

        return Command.SINGLE_SUCCESS
    }

    @JvmOverloads
    public fun replyError(error: Throwable, isDetailed: Boolean = true): Int {
        output.sendMessage(
            MCText.convert(OmniChat.buildErrorMessage(error, isDetailed)),
            //#if MC >= 1.16.5 && MC < 1.19.2
            //$$ OmniChat.NULL_UUID
            //#endif
        )

        return Command.SINGLE_SUCCESS
    }

    public fun replyActionBar(text: Text): Int {
        val player = player ?: return 0
        OmniChat.displayActionBar(player, text)
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
        val player = player ?: return 0
        OmniChat.displayTitle(player, title, subtitle, fadeIn, stay, fadeOut)
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
        val player = player ?: return 0 // failure
        OmniNetworking.send(player, payload)
        return Command.SINGLE_SUCCESS
    }
}
