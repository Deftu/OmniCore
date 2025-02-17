package dev.deftu.omnicore.client

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder

//#if FABRIC && MC >= 1.19
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
//#endif

//#if FABRIC && MC >= 1.19 || FORGE-LIKE && MC >= 1.18.2
import java.util.concurrent.LinkedBlockingQueue
//#endif

//#if FORGE && MC >= 1.16.5 && MC <= 1.17.1
//$$ import com.mojang.brigadier.Command
//$$ import com.mojang.brigadier.StringReader
//$$ import com.mojang.brigadier.arguments.ArgumentType
//$$ import com.mojang.brigadier.builder.ArgumentBuilder
//$$ import com.mojang.brigadier.builder.RequiredArgumentBuilder
//$$ import com.mojang.brigadier.suggestion.SuggestionProvider
//$$ import com.mojang.brigadier.tree.ArgumentCommandNode
//$$ import com.mojang.brigadier.tree.CommandNode
//$$ import com.mojang.brigadier.tree.LiteralCommandNode
//$$ import com.mojang.brigadier.tree.RootCommandNode
//$$ import net.minecraft.commands.SharedSuggestionProvider
//$$ import net.minecraft.commands.synchronization.SuggestionProviders
//$$ import net.minecraft.commands.CommandSource
//$$ import net.minecraft.commands.CommandSourceStack
//$$ import java.util.*
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.common.OmniCommandBridge
//$$ import net.minecraftforge.client.ClientCommandHandler
//#endif

//#if FABRIC && MC <= 1.12.2 || FORGE && MC >= 1.16.5 || FORGE && MC <= 1.17.1
//$$ import dev.deftu.textile.minecraft.MinecraftTextFormat
//$$ import com.mojang.brigadier.suggestion.Suggestion
//#endif

//#if FORGE-LIKE && MC >= 1.18.2
//#if FORGE
//$$ import net.minecraftforge.client.event.RegisterClientCommandsEvent
//$$ import net.minecraftforge.common.MinecraftForge
//#else
//$$ import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent
//$$ import net.neoforged.neoforge.common.NeoForge
//#endif
//#endif

public typealias ClientCommandSource =
        //#if MC >= 1.16.5
        //#if FABRIC
        net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
        //#else
        //$$ net.minecraft.commands.CommandSourceStack
        //#endif
        //#else
        //$$ net.minecraft.command.ICommandSender
        //#endif

public object OmniClientCommands {

    private val dispatcher: CommandDispatcher<ClientCommandSource> = CommandDispatcher<ClientCommandSource>()

    //#if FABRIC && MC >= 1.19 || FORGE-LIKE && MC >= 1.18.2
    private val commandsToRegister = LinkedBlockingQueue<LiteralArgumentBuilder<ClientCommandSource>>()
    //#endif

    public fun initialize() {
        //#if FABRIC && MC >= 1.19
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            while (commandsToRegister.isNotEmpty()) {
                dispatcher.register(commandsToRegister.poll())
            }
        }
        //#elseif FORGE-LIKE && MC >= 1.18.2
        //#if FORGE
        //$$ MinecraftForge.EVENT_BUS.addListener<RegisterClientCommandsEvent> { event ->
        //#else
        //$$ NeoForge.EVENT_BUS.addListener<RegisterClientCommandsEvent> { event ->
        //#endif
        //$$     println("Registering commands with $event:\n${commandsToRegister.joinToString("\n")}")
        //$$     for (command in commandsToRegister) {
        //$$         event.dispatcher.register(command)
        //$$     }
        //$$ }
        //#endif
    }

    public fun register(command: LiteralArgumentBuilder<ClientCommandSource>) {
        //#if MC <= 1.12.2
        //$$ val node =
        //#endif
        dispatcher.register(command)

        //#if FORGE && MC <= 1.12.2
        //$$ ClientCommandHandler.instance.registerCommand(OmniCommandBridge(dispatcher, node))
        //#elseif FABRIC && MC >= 1.19 || FORGE-LIKE && MC >= 1.18.2
        commandsToRegister.add(command)
        //#endif
    }

    public fun literal(name: String): LiteralArgumentBuilder<ClientCommandSource> {
        return LiteralArgumentBuilder.literal(name)
    }

    //#if FABRIC && MC <= 1.12.2 || FORGE && MC >= 1.16.5 && MC <= 1.17.1
    //#if MC >= 1.16.5
    //$$ // Adapted from MinecraftForge under LGPL-2.1
    //$$ // https://github.com/MinecraftForge/MinecraftForge/blob/1.18.x/LICENSE.txt
    //$$
    //$$ @JvmStatic
    //$$ internal fun mergeServerCommands(serverCommands: CommandDispatcher<SharedSuggestionProvider>): CommandDispatcher<SharedSuggestionProvider> {
    //$$     val customDispatcherCopy = CommandDispatcher<ClientCommandSource>()
    //$$     dispatcher.root.copyTo(customDispatcherCopy.root)
    //$$
    //$$     // Copies the server commands into another RootCommandNode so that redirects can't be used with client commands
    //$$     val serverCommandsRoot = serverCommands.root
    //$$     val newServerCommands = CommandDispatcher<SharedSuggestionProvider>()
    //$$     serverCommandsRoot.copyTo(newServerCommands.root)
    //$$
    //$$     // Copies the server side commands into a temporary server side commands root node to be used later without the client commands
    //$$     val serverCommandsCopy = RootCommandNode<SharedSuggestionProvider>()
    //$$     mergeCommandNode(
    //$$         newServerCommands.root, serverCommandsCopy, IdentityHashMap(),
    //$$         OmniClient.getInstance().connection!!.suggestionsProvider, { 0 }, { null }
    //$$     )
    //$$
    //$$     // Copies the client side commands into the server side commands to be used for suggestions
    //$$     mergeCommandNode(
    //$$         customDispatcherCopy.root, newServerCommands.root, IdentityHashMap(), obtainCommandSource(), { 0 }, { suggestions ->
    //$$             var suggestionProvider = SuggestionProviders.safelySwap(suggestions as SuggestionProvider<SharedSuggestionProvider>?)
    //$$             if (suggestionProvider == SuggestionProviders.ASK_SERVER) {
    //$$                 suggestionProvider = SuggestionProvider { context, builder ->
    //$$                     val source = obtainCommandSource()
    //$$                     val reader = StringReader(context.input)
    //$$                     if (reader.canRead() && reader.peek() == '/') {
    //$$                         reader.skip()
    //$$                     }
    //$$                     val parse = dispatcher.parse(reader, source)
    //$$                     dispatcher.getCompletionSuggestions(parse)
    //$$                 }
    //$$             }
    //$$             suggestionProvider
    //$$         }
    //$$     )
    //$$
    //$$     // Copies the server side commands into the client side commands so that they can be sent to the server as a chat message
    //$$     mergeCommandNode(
    //$$         serverCommandsCopy, dispatcher.root, IdentityHashMap(),
    //$$         OmniClient.getInstance().connection!!.suggestionsProvider,
    //$$         { context ->
    //$$             OmniChat.showChatMessage((if (context.input.startsWith("/")) "" else "/") + context.input)
    //$$             0
    //$$         },
    //$$         { null }
    //$$     )
    //$$
    //$$     return newServerCommands
    //$$ }
    //$$
    //$$ private fun <S> CommandNode<S>.copyTo(destNode: CommandNode<S>) {
    //$$     val newNodes = IdentityHashMap<CommandNode<S>, CommandNode<S>>()
    //$$     newNodes[this] = destNode
    //$$     for (child in this.children) {
    //$$         val copiedNode = newNodes.computeIfAbsent(child) { innerChild ->
    //$$             val argBuilder = innerChild.createBuilder()
    //$$             val innerChildCopy = argBuilder.build()
    //$$             innerChild.copyTo(innerChildCopy)
    //$$             innerChildCopy
    //$$         }
    //$$
    //$$         destNode.addChild(copiedNode)
    //$$     }
    //$$ }
    //$$
    //$$ private fun <S, T> mergeCommandNode(
    //$$     sourceNode: CommandNode<S>,
    //$$     resultNode: CommandNode<T>,
    //$$     sourceToResult: MutableMap<CommandNode<S>, CommandNode<T>>,
    //$$     canUse: S,
    //$$     execute: Command<T>,
    //$$     sourceToResultSuggestion: (SuggestionProvider<S>) -> SuggestionProvider<T>?
    //$$ ) {
    //$$     sourceToResult[sourceNode] = resultNode
    //$$     for (sourceChild in sourceNode.children) {
    //$$         if (sourceChild.canUse(canUse)) {
    //$$             resultNode.addChild(toResult<S, T>(sourceChild, sourceToResult, canUse, execute, sourceToResultSuggestion))
    //$$         }
    //$$     }
    //$$ }
    //$$
    //$$ private fun <S, T> toResult(
    //$$     sourceNode: CommandNode<S>,
    //$$     sourceToResult: MutableMap<CommandNode<S>, CommandNode<T>>,
    //$$     canUse: S,
    //$$     execute: Command<T>,
    //$$     sourceToResultSuggestion: (SuggestionProvider<S>) -> SuggestionProvider<T>?
    //$$ ): CommandNode<T> {
    //$$     sourceToResult[sourceNode]?.let { return it }
    //$$
    //$$     val resultBuilder: ArgumentBuilder<T, *> = when (sourceNode) {
    //$$         is ArgumentCommandNode<*, *> -> {
    //$$             val sourceArgument = sourceNode as ArgumentCommandNode<S, Any>
    //$$             RequiredArgumentBuilder.argument<T, Any>(sourceArgument.name, sourceArgument.type as ArgumentType<Any>).apply {
    //$$                 sourceArgument.customSuggestions?.let { suggests(sourceToResultSuggestion(it)) }
    //$$             }
    //$$         }
    //$$         is LiteralCommandNode<*> -> {
    //$$             val sourceLiteral = sourceNode as LiteralCommandNode<S>
    //$$             LiteralArgumentBuilder.literal(sourceLiteral.literal)
    //$$         }
    //$$         is RootCommandNode<S> -> {
    //$$             val resultNode = RootCommandNode<T>()
    //$$             mergeCommandNode<S, T>(sourceNode, resultNode, sourceToResult, canUse, execute, sourceToResultSuggestion)
    //$$             return resultNode
    //$$         }
    //$$         else -> throw IllegalStateException("Node type $sourceNode is not a standard node type")
    //$$     }
    //$$
    //$$     sourceNode.command?.let { resultBuilder.executes(execute) }
    //$$     sourceNode.redirect?.let { resultBuilder.redirect(toResult(it, sourceToResult, canUse, execute, sourceToResultSuggestion)) }
    //$$
    //$$     val resultNode = resultBuilder.build()
    //$$     mergeCommandNode(sourceNode, resultNode, sourceToResult, canUse, execute, sourceToResultSuggestion)
    //$$     return resultNode
    //$$ }
    //#else
    //$$ @JvmStatic
    //$$ internal fun retrieveAutoComplete(command: String): Set<String> {
    //$$     val results = dispatcher.parse(command, obtainCommandSource())
    //$$     return dispatcher.getCompletionSuggestions(results)
    //$$         .join()
    //$$         .list
    //$$         .map(Suggestion::getText)
    //#if MC <= 1.12.2
    //$$         .map { text -> MinecraftTextFormat.GRAY + (if (command.contains(" ")) "" else "/") + text + MinecraftTextFormat.RESET }
    //#endif
    //$$         .toSet()
    //$$ }
    //#endif
    //$$
    //$$ @JvmStatic
    //$$ internal fun execute(command: String): Int {
    //$$     val results = dispatcher.parse(command, obtainCommandSource())
    //$$     println(results)
    //$$     return try {
    //$$         dispatcher.execute(results)
    //$$     } catch (e: Exception) {
    //$$         e.printStackTrace()
    //$$         0
    //$$     }
    //$$ }
    //$$
    //$$ private fun obtainCommandSource(): ClientCommandSource {
    //#if MC >= 1.16.5
    //$$     val player = OmniClient.player ?: throw IllegalStateException("Player is null")
    //$$     return CommandSourceStack(
    //$$         CommandSource.NULL,
    //$$         player.position(),
    //$$         player.rotationVector,
    //$$         null,
    //$$         2,
    //$$         player.name.string,
    //$$         player.name,
    //$$         null,
    //$$         player
    //$$     )
    //#else
    //$$     return OmniClient.player ?: throw IllegalStateException("Player is null")
    //#endif
    //$$ }
    //#endif

}
