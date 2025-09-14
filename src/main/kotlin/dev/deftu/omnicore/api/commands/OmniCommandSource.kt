package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.Command
import dev.deftu.omnicore.api.chat.OmniChat
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextHolder
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

    public fun replyChat(text: MCTextHolder<*>): Int {
        output.sendMessage(
            text.asVanilla(),
            //#if MC >= 1.16.5 && MC < 1.19.2
            //$$ OmniChat.NULL_UUID
            //#endif
        )

        return Command.SINGLE_SUCCESS
    }

    public fun replyChat(text: String): Int {
        return replyChat(MCSimpleTextHolder(text))
    }

    @JvmOverloads
    public fun replyError(content: MCTextHolder<*>, throwable: Throwable, isDetailed: Boolean = true): Int {
        output.sendMessage(
            OmniChat.buildErrorMessage(content, throwable, isDetailed).asVanilla(),
            //#if MC >= 1.16.5 && MC < 1.19.2
            //$$ OmniChat.NULL_UUID
            //#endif
        )

        return Command.SINGLE_SUCCESS
    }

    @JvmOverloads
    public fun replyError(error: Throwable, isDetailed: Boolean = true): Int {
        output.sendMessage(
            OmniChat.buildErrorMessage(error, isDetailed).asVanilla(),
            //#if MC >= 1.16.5 && MC < 1.19.2
            //$$ OmniChat.NULL_UUID
            //#endif
        )

        return Command.SINGLE_SUCCESS
    }

    public fun replyActionBar(text: MCTextHolder<*>): Int {
        val player = player ?: return 0
        OmniChat.displayActionBar(player, text)
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
            MCSimpleTextHolder(title),
            subtitle?.let { MCSimpleTextHolder(it) },
            fadeIn,
            stay,
            fadeOut
        )
    }
}
