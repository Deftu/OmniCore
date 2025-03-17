package dev.deftu.omnicore.common

//#if MC <= 1.12.2
//$$ import com.mojang.brigadier.CommandDispatcher
//$$ import com.mojang.brigadier.suggestion.Suggestion
//$$ import com.mojang.brigadier.tree.CommandNode
//$$ import dev.deftu.omnicore.client.OmniChat
//$$ import dev.deftu.textile.minecraft.MCTextFormat
//$$ import net.minecraft.command.CommandBase
//$$ import net.minecraft.command.ICommandSender
//$$ import net.minecraft.server.MinecraftServer
//$$ import net.minecraft.util.math.BlockPos
//$$
//$$ internal class OmniCommandBridge<S>(
//$$     private val dispatcher: CommandDispatcher<S>,
//$$     private val node: CommandNode<S>,
//$$     private val contextFactory: (ICommandSender) -> S
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
//$$         val results = dispatcher.parse(text, contextFactory(sender))
//$$
//$$         try {
//$$             dispatcher.execute(results)
//$$         } catch (e: Exception) {
//$$             OmniChat.displayClientMessage("${MCTextFormat.RED}${e.message}")
//$$         }
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
//$$         val results = dispatcher.parse(text, contextFactory(sender))
//$$
//$$         return dispatcher.getCompletionSuggestions(results)
//$$             .join()
//$$             .list
//$$             .map(Suggestion::getText)
//$$             .toMutableList()
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
