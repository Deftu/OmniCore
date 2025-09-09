package dev.deftu.omnicore.internal.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.api.commands.OmniCommandSource
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.command.ServerCommandSource
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CommandInternals {
    private val logger = LogManager.getLogger(CommandInternals::class.java)

    //#if FABRIC && MC >= 1.16.5
    private val ServerCommandSource.output: CommandOutput
        get() {
            val field = ServerCommandSource::class.java.getDeclaredField("output")
            field.isAccessible = true
            return field.get(this) as CommandOutput
        }
    //#elseif FORGE-LIKE && MC >= 1.18.2
    //$$ private val CommandSourceStack.output: CommandSource
    //$$     get() {
    //$$         val field = CommandSourceStack::class.java.getDeclaredField("source")
    //$$         field.isAccessible = true
    //$$         return field.get(this) as CommandSource
    //$$     }
    //#endif

    @JvmStatic
    public var isInitialized: Boolean = false
        private set

    @JvmStatic
    public val dispatcher: CommandDispatcher<OmniCommandSource> = CommandDispatcher()

    @JvmStatic
    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC
        //#if MC >= 1.16.5
        CommandRegistrationCallback.EVENT.register {
                dispatcher,
                _,
                //#if MC >= 1.19.2
                _
            //#endif
            ->
            CommandOps.copyCommandsWithMapper(dispatcher, this.dispatcher) { src ->
                OmniCommandSource(src.server, src.output, src.world)
            }
        }
        //#else
        //$$ ServerLifecycleEvents.SERVER_STARTING.register { server ->
        //$$     val commandManager = server.commandManager as CommandManager
        //$$     for (child in dispatcher.root.children) {
        //$$         commandManager.registerCommand(LegacyCommandBridge(
        //$$             node = child,
        //$$             executor = { sender, command -> execute(server, sender, command) },
        //$$             completer = { sender, command -> retrieveAutoComplete(server, sender, command) }
        //$$         ))
        //$$     }
        //$$ }
        //#endif
        //#elseif FORGE-LIKE && MC >= 1.18.2
        //$$ forgeEventBus.addListener<RegisterCommandsEvent> { event ->
        //$$     CommandOps.copyCommandsWithMapper(event.dispatcher, this.dispatcher) { src ->
        //$$         OmniServerCommandSource(src.server, src.output, src.level)
        //$$     }
        //$$ }
        //#endif

        isInitialized = true
    }

    @JvmStatic
    public fun register(node: LiteralCommandNode<OmniCommandSource>) {
        dispatcher.root.addChild(node)

        //#if FORGE && MC <= 1.12.2
        //$$ ClientCommandHandler.instance.registerCommand(LegacyCommandBridge(
        //$$     node = node,
        //$$     executor = { _, command -> execute(command) },
        //$$     completer = { _, command -> retrieveAutoComplete(command).toMutableList() }
        //$$ ))
        //#endif
    }

    @JvmStatic
    public fun register(command: LiteralArgumentBuilder<OmniCommandSource>) {
        register(command.build())
    }

    //#if MC <= 1.12.2
    //$$ private fun execute(server: MinecraftServer, sender: ICommandSender, command: String) {
    //$$     val context = OmniCommandSource(server, sender)
    //$$     val results = dispatcher.parse(command, context)
    //$$
    //$$     return profiled("omnicore_command___$command") {
    //$$         try {
    //$$             dispatcher.execute(results)
    //$$         } catch (e: CommandSyntaxException) {
    //$$             val isIgnored = CommandOps.isIgnoredException(e.type)
    //$$             val message = "Syntax exception for server-sided command '$command'"
    //$$
    //$$             if (!isIgnored) {
    //$$                 logger.warn(message, e)
    //$$                 context.displayError(e)
    //$$            } else {
    //$$                logger.debug(message, e)
    //$$           }
    //$$         }
    //$$     }
    //$$ }
    //$$
    //$$ private fun retrieveAutoComplete(server: MinecraftServer, sender: ICommandSender, command: String): MutableList<String> {
    //$$     val context = OmniCommandSource(server, sender)
    //$$     val results = dispatcher.parse(command, context)
    //$$     return dispatcher.getCompletionSuggestions(results)
    //$$         .join()
    //$$         .list
    //$$         .map(Suggestion::getText)
    //$$         .map { text -> MCTextFormat.GRAY + (if (command.contains(" ")) "" else "/") + text + MCTextFormat.RESET }
    //$$         .toMutableList()
    //$$ }
    //#endif
}
