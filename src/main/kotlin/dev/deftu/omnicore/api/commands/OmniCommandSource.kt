package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.Command
import dev.deftu.omnicore.api.chat.OmniChat
import dev.deftu.omnicore.api.network.OmniNetworking
import dev.deftu.omnicore.api.network.PacketPayload
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCText
import net.minecraft.server.MinecraftServer
import net.minecraft.commands.CommandSource
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.ServerLevel

public class OmniCommandSource(
    public val server: MinecraftServer,
    public val output: CommandSource,
    public val world: ServerLevel
) {
    //#if MC >= 1.16.5 && MC < 1.19.2
    //$$ private val NULL_UUID = java.util.UUID(0L, 0L)
    //#endif

    public inline val player: ServerPlayer?
        get() = output as? ServerPlayer

    //#if MC <= 1.12.2
    //$$ internal constructor(server: MinecraftServer, output: ICommandSender) : this(
    //$$     server,
    //$$     output,
    //$$     output.entityWorld as WorldServer
    //$$ )
    //#endif

    public fun replyChat(text: Text): Int {
        message(text)
        return Command.SINGLE_SUCCESS
    }

    public fun replyChat(text: String): Int {
        return replyChat(Text.literal(text))
    }

    @JvmOverloads
    public fun replyError(content: Text, throwable: Throwable, isDetailed: Boolean = true): Int {
        message(OmniChat.buildErrorMessage(content, throwable, isDetailed))
        return Command.SINGLE_SUCCESS
    }

    @JvmOverloads
    public fun replyError(error: Throwable, isDetailed: Boolean = true): Int {
        message(OmniChat.buildErrorMessage(error, isDetailed))
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

    private fun message(text: Text) {
        //#if MC >= 1.16.5 && MC < 1.19.2
        //$$ output.sendMessage(MCText.convert(text), NULL_UUID)
        //#elseif MC >= 1.19.2
        output.sendSystemMessage(MCText.convert(text))
        //#else
        //$$ output.sendMessage(MCText.convert(text))
        //#endif
    }
}
