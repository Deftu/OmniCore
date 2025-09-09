package dev.deftu.omnicore.api.commands

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

public fun command(name: String, block: CommandBuilder.() -> Unit): CommandBuilder {
    val rootNode = LiteralArgumentBuilder.literal<OmniCommandSource>(name)
    val builder = CommandBuilder(rootNode)
    builder.block()
    return builder
}

public inline fun <reified T> CommandContext<OmniCommandSource>.argument(name: String): T {
    return this.getArgument(name, T::class.java)
}

public class CommandBuilder internal constructor(internal val builder: LiteralArgumentBuilder<OmniCommandSource>) {
    private val aliases = mutableListOf<String>()

    public fun alias(vararg names: String): CommandBuilder {
        aliases.addAll(names)
        return this
    }

    public fun requires(requirement: (OmniCommandSource) -> Boolean): CommandBuilder {
        builder.requires(requirement)
        return this
    }

    public fun runs(command: (CommandContext<OmniCommandSource>) -> Int): CommandBuilder {
        builder.executes(command)
        return this
    }

    public fun then(name: String, block: CommandBuilder.() -> Unit): CommandBuilder {
        val childNode = LiteralArgumentBuilder.literal<OmniCommandSource>(name)
        val childBuilder = CommandBuilder(childNode)
        childBuilder.block()
        builder.then(childNode)
        return this
    }

    public fun <T> argument(name: String, type: ArgumentType<T>, block: ArgumentBuilder<T>.() -> Unit): CommandBuilder {
        val argNode = RequiredArgumentBuilder.argument<OmniCommandSource, T>(name, type)
        val argBuilder = ArgumentBuilder(argNode)
        argBuilder.block()
        builder.then(argNode)
        return this
    }

    public fun register() {
        val node = builder.build()
        OmniCommands.register(node)
        for (alias in aliases) {
            val aliasNode = LiteralArgumentBuilder.literal<OmniCommandSource>(alias)
                .requires(node.requirement)
                .redirect(node)
                .build()
            OmniCommands.register(aliasNode)
        }
    }
}

public class ArgumentBuilder<T> internal constructor(internal val builder: RequiredArgumentBuilder<OmniCommandSource, T>) {
    public fun suggest(suggestions: (CommandContext<OmniCommandSource>, SuggestionsBuilder) -> CompletableFuture<Suggestions>): ArgumentBuilder<T> {
        builder.suggests(suggestions)
        return this
    }

    public fun runs(command: (CommandContext<OmniCommandSource>) -> Int): ArgumentBuilder<T> {
        builder.executes(command)
        return this
    }

    public fun then(name: String, block: CommandBuilder.() -> Unit): ArgumentBuilder<T> {
        val childNode = LiteralArgumentBuilder.literal<OmniCommandSource>(name)
        val childBuilder = CommandBuilder(childNode)
        childBuilder.block()
        builder.then(childNode)
        return this
    }

    public fun <T2> argument(name: String, type: ArgumentType<T2>): ArgumentBuilder<T2> {
        val argNode = RequiredArgumentBuilder.argument<OmniCommandSource, T2>(name, type)
        builder.then(argNode)
        return ArgumentBuilder(argNode)
    }
}
