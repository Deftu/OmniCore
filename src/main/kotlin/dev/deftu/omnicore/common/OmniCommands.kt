package dev.deftu.omnicore.common

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.tree.CommandNode

public object OmniCommands {

    @JvmStatic
    public fun <T> printTree(dispatcher: CommandDispatcher<T>) {
        printNode(dispatcher.root, 0)
    }

    @JvmStatic
    private fun <T> printNode(node: CommandNode<T>, depth: Int) {
        val indent = "  ".repeat(depth)
        println("$indent${node.javaClass.simpleName} ${node.name}")
        for (child in node.children) {
            printNode(child, depth + 1)
        }
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
