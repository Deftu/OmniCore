package dev.deftu.omnicore.internal.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.api.commands.OmniCommandSource
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.16.5
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.command.CommandOutput
//#endif

//#if FABRIC && MC >= 1.16.5
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
//#endif

//#if FORGE-LIKE && MC >= 1.18.2
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//#if FORGE
//$$ import net.minecraftforge.event.RegisterCommandsEvent
//#else
//$$ import net.neoforged.neoforge.event.RegisterCommandsEvent
//#endif
//#endif

//#if MC <= 1.12.2
//$$ import com.mojang.brigadier.exceptions.CommandSyntaxException
//$$ import com.mojang.brigadier.suggestion.Suggestion
//$$ import dev.deftu.omnicore.api.profiled
//$$ import net.minecraft.command.ICommandSender
//$$ import net.minecraft.server.MinecraftServer
//$$ import net.minecraft.util.text.TextFormatting
//#if FORGE
//$$ import net.minecraft.command.ServerCommandManager
//$$ import net.minecraftforge.fml.server.FMLServerHandler
//#else
//$$ import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
//$$ import net.minecraft.server.command.CommandManager
//#endif
//#endif

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
        //$$         OmniCommandSource(src.server, src.output, src.level)
        //$$     }
        //$$ }
        //#endif

        isInitialized = true
    }

    @JvmStatic
    public fun register(node: LiteralCommandNode<OmniCommandSource>) {
        dispatcher.root.addChild(node)

        //#if FORGE && MC <= 1.12.2
        //$$ val server = FMLServerHandler.instance().server
        //$$ val commandManager = server.commandManager as ServerCommandManager
        //$$ commandManager.registerCommand(LegacyCommandBridge(
        //$$     node = node,
        //$$     executor = { sender, command -> execute(server, sender, command) },
        //$$     completer = { sender, command -> retrieveAutoComplete(server, sender, command) }
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
    //$$     return server.profiled("omnicore_command___$command") {
    //$$         try {
    //$$             dispatcher.execute(results)
    //$$         } catch (e: CommandSyntaxException) {
    //$$             val isIgnored = CommandOps.isIgnoredException(e.type)
    //$$             val message = "Syntax exception for server-sided command '$command'"
    //$$
    //$$             if (!isIgnored) {
    //$$                 logger.warn(message, e)
    //$$                 context.replyError(e)
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
    //$$         .map { text -> TextFormatting.GRAY.toString() + (if (command.contains(" ")) "" else "/") + text + TextFormatting.RESET.toString() }
    //$$         .toMutableList()
    //$$ }
    //#endif
}
