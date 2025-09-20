package dev.deftu.omnicore.api.client.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals

public object OmniClientCommands {
    public val dispatcher: CommandDispatcher<OmniClientCommandSource>
        get() = ClientCommandInternals.dispatcher

    @JvmStatic
    public fun register(node: LiteralCommandNode<OmniClientCommandSource>) {
        ClientCommandInternals.register(node)
    }

    @JvmStatic
    public fun register(builder: LiteralArgumentBuilder<OmniClientCommandSource>) {
        ClientCommandInternals.register(builder)
    }

    public fun literal(name: String): LiteralArgumentBuilder<OmniClientCommandSource> {
        return LiteralArgumentBuilder.literal(name)
    }

    public fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<OmniClientCommandSource, T> {
        return RequiredArgumentBuilder.argument(name, type)
    }
}
