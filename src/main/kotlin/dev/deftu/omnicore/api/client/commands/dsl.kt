package dev.deftu.omnicore.api.client.commands

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

public fun OmniClientCommands.command(name: String, block: CommandBuilder.() -> Unit): CommandBuilder {
    val rootNode = literal(name)
    val builder = CommandBuilder(rootNode)
    builder.block()
    return builder
}

public inline fun <reified T> CommandContext<OmniClientCommandSource>.argument(name: String): T {
    return this.getArgument(name, T::class.java)
}

public class CommandBuilder internal constructor(internal val builder: LiteralArgumentBuilder<OmniClientCommandSource>) {
    private val aliases = mutableListOf<String>()

    public fun alias(vararg names: String): CommandBuilder {
        aliases.addAll(names)
        return this
    }

    public fun requires(requirement: (OmniClientCommandSource) -> Boolean): CommandBuilder {
        builder.requires(requirement)
        return this
    }

    public fun runs(command: (CommandContext<OmniClientCommandSource>) -> Int): CommandBuilder {
        builder.executes(command)
        return this
    }

    public fun then(name: String, block: CommandBuilder.() -> Unit): CommandBuilder {
        val childNode = LiteralArgumentBuilder.literal<OmniClientCommandSource>(name)
        val childBuilder = CommandBuilder(childNode)
        childBuilder.block()
        builder.then(childNode)
        return this
    }

    public fun <T> argument(name: String, type: ArgumentType<T>, block: ArgumentBuilder<T>.() -> Unit): CommandBuilder {
        val argNode = RequiredArgumentBuilder.argument<OmniClientCommandSource, T>(name, type)
        val argBuilder = ArgumentBuilder(argNode)
        argBuilder.block()
        builder.then(argNode)
        return this
    }

    public fun register() {
        val node = builder.build()
        OmniClientCommands.register(node)
        for (alias in aliases) {
            val aliasNode = LiteralArgumentBuilder.literal<OmniClientCommandSource>(alias)
                .requires(node.requirement)
                .executes(node.command)
                .redirect(node)
                .build()
            OmniClientCommands.register(aliasNode)
        }
    }
}

public class ArgumentBuilder<T> internal constructor(internal val builder: RequiredArgumentBuilder<OmniClientCommandSource, T>) {
    public fun suggest(suggestions: (CommandContext<OmniClientCommandSource>, SuggestionsBuilder) -> CompletableFuture<Suggestions>): ArgumentBuilder<T> {
        builder.suggests(suggestions)
        return this
    }

    public fun runs(command: (CommandContext<OmniClientCommandSource>) -> Int): ArgumentBuilder<T> {
        builder.executes(command)
        return this
    }

    public fun then(name: String, block: CommandBuilder.() -> Unit): ArgumentBuilder<T> {
        val childNode = LiteralArgumentBuilder.literal<OmniClientCommandSource>(name)
        val childBuilder = CommandBuilder(childNode)
        childBuilder.block()
        builder.then(childNode)
        return this
    }

    public fun <T2> argument(name: String, type: ArgumentType<T2>): ArgumentBuilder<T2> {
        val argNode = RequiredArgumentBuilder.argument<OmniClientCommandSource, T2>(name, type)
        builder.then(argNode)
        return ArgumentBuilder(argNode)
    }
}
