package dev.deftu.omnicore.internal.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.exceptions.CommandExceptionType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.CommandNode
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CommandOps {
    @JvmStatic
    public fun isIgnoredException(type: CommandExceptionType): Boolean {
        val builtIns = CommandSyntaxException.BUILT_IN_EXCEPTIONS
        return type == builtIns.dispatcherUnknownCommand() || type == builtIns.dispatcherParseException()
    }

    @JvmStatic
    public fun <S, T> copyCommands(
        targetDispatcher: CommandDispatcher<T>,
        sourceDispatcher: CommandDispatcher<S>,
        source: S
    ) {
        val originalToCopy = mutableMapOf<CommandNode<S>, CommandNode<T>>()
        originalToCopy[sourceDispatcher.root] = targetDispatcher.root
        copyChildren(sourceDispatcher.root, targetDispatcher.root, source, originalToCopy)
    }

    @JvmStatic
    public fun <S, T> copyCommandsWithMapper(
        targetDispatcher: CommandDispatcher<T>,
        sourceDispatcher: CommandDispatcher<S>,
        sourceMapper: (T) -> S
    ) {
        val originalToCopy = mutableMapOf<CommandNode<S>, CommandNode<T>>()
        originalToCopy[sourceDispatcher.root] = targetDispatcher.root
        copyChildrenWithMapper(sourceDispatcher.root, targetDispatcher.root, sourceMapper, originalToCopy)
    }

    private fun <S, T> copyChildren(
        origin: CommandNode<S>,
        target: CommandNode<T>,
        source: S,
        originalToCopy: MutableMap<CommandNode<S>, CommandNode<T>>
    ) {
        for (child in origin.children) {
            if (!child.canUse(source)) {
                continue
            }

            val builder = child.createBuilder().withTargetType<S, T>()

            // Reset permissions since we already checked canUse
            builder.requires { true }

            if (builder.command != null) {
                builder.executes { 0 }
            }

            child.redirect?.let { originalRedirect ->
                originalToCopy[originalRedirect]?.let { builder.redirect(it) }
            }

            val result = builder.build()
            originalToCopy[child] = result
            target.addChild(result)

            if (child.children.isNotEmpty()) {
                copyChildren(child, result, source, originalToCopy)
            }
        }
    }

    private fun <S, T> copyChildrenWithMapper(
        origin: CommandNode<S>,
        target: CommandNode<T>,
        sourceMapper: (T) -> S,
        originalToCopy: MutableMap<CommandNode<S>, CommandNode<T>>
    ) {
        for (child in origin.children) {
            val builder = child.createBuilder().withTargetType<S, T>()

            var source: S? = null
            builder.command?.let { originalCommand ->
                builder.executes { originalCtx ->
                    source = sourceMapper(originalCtx.source)
                    originalCommand.run(originalCtx)
                }
            }

            builder.requires { child.canUse(source) }

            child.redirect?.let { originalRedirect ->
                originalToCopy[originalRedirect]?.let { builder.redirect(it) }
            }

            val result = builder.build()
            originalToCopy[child] = result
            target.addChild(result)

            if (child.children.isNotEmpty()) {
                copyChildrenWithMapper(child, result, sourceMapper, originalToCopy)
            }
        }
    }

    private fun <S, T> ArgumentBuilder<S, *>.withTargetType(): ArgumentBuilder<T, *> {
        @Suppress("UNCHECKED_CAST")
        return this as ArgumentBuilder<T, *>
    }
}
