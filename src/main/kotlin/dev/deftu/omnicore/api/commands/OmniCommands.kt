package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.internal.commands.CommandInternals

public object OmniCommands {
    @JvmStatic
    public val dispatcher: CommandDispatcher<OmniCommandSource>
        get() = CommandInternals.dispatcher

    @JvmStatic
    public fun register(node: LiteralCommandNode<OmniCommandSource>) {
        CommandInternals.register(node)
    }

    @JvmStatic
    public fun register(builder: LiteralArgumentBuilder<OmniCommandSource>) {
        CommandInternals.register(builder)
    }

    @JvmStatic
    public fun literal(name: String): LiteralArgumentBuilder<OmniCommandSource> {
        return LiteralArgumentBuilder.literal(name)
    }

    @JvmStatic
    public fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<OmniCommandSource, T> {
        return RequiredArgumentBuilder.argument(name, type)
    }
}
