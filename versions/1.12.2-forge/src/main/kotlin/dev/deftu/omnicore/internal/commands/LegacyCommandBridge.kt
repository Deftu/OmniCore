package dev.deftu.omnicore.internal.commands

import com.mojang.brigadier.tree.CommandNode
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos

internal class LegacyCommandBridge<S>(
    private val node: CommandNode<S>,
    private val executor: (ICommandSender, String) -> Unit,
    private val completer: (ICommandSender, String) -> MutableList<String>,
) : CommandBase() {
    private val Array<out String>.fullText: String
        get() {
            return "${node.name} ${this.joinToString(" ")}".trim()
        }

    override fun getName(): String {
        return node.name
    }

    override fun getUsage(sender: ICommandSender): String {
        return ""
    }

    override fun execute(
        //#if MC >= 1.12.2
        server: MinecraftServer,
        //#endif
        sender: ICommandSender,
        args: Array<out String>
    ) {
        val text = args.fullText
        executor(sender, text)
    }

    override fun getTabCompletions(
        //#if MC >= 1.12.2
        server: MinecraftServer,
        //#endif
        sender: ICommandSender,
        args: Array<out String>,
        targetPos: BlockPos?
    ): MutableList<String> {
        val text = args.fullText
        return completer(sender, text)
    }

    override fun checkPermission(
        //#if MC >= 1.12.2
        server: MinecraftServer,
        //#endif
        sender: ICommandSender
    ): Boolean {
        return true
    }
}
