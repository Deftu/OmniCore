package dev.deftu.omnicore.common

//#if MC <= 1.12.2
//$$ import com.mojang.brigadier.tree.CommandNode
//$$ import net.minecraft.command.CommandBase
//$$ import net.minecraft.command.ICommandSender
//$$ import net.minecraft.server.MinecraftServer
//$$ import net.minecraft.util.math.BlockPos
//$$
//$$ internal class OmniCommandBridge<S>(
//$$     private val node: CommandNode<S>,
//$$     private val executor: (ICommandSender, String) -> Unit,
//$$     private val completer: (ICommandSender, String) -> MutableList<String>,
//$$ ) : CommandBase() {
//$$
//$$     override fun getName(): String = node.name
//$$
//$$     override fun getUsage(sender: ICommandSender): String {
//$$         return ""
//$$     }
//$$
//$$     override fun execute(
//#if MC >= 1.12.2
//$$         server: MinecraftServer,
//#endif
//$$         sender: ICommandSender,
//$$         args: Array<out String>
//$$     ) {
//$$         val text = args.getText()
//$$         executor(sender, text)
//$$     }
//$$
//$$     override fun getTabCompletions(
//#if MC >= 1.12.2
//$$         server: MinecraftServer,
//#endif
//$$         sender: ICommandSender,
//$$         args: Array<out String>,
//$$         targetPos: BlockPos?
//$$     ): MutableList<String> {
//$$         val text = args.getText()
//$$         return completer(sender, text)
//$$     }
//$$
//$$     override fun checkPermission(
//#if MC >= 1.12.2
//$$         server: MinecraftServer,
//#endif
//$$         sender: ICommandSender
//$$     ): Boolean {
//$$         return true
//$$     }
//$$
//$$     private fun Array<out String>.getText(): String {
//$$         return "${node.name} ${this.joinToString(" ")}".trim()
//$$     }
//$$
//$$ }
//#endif
