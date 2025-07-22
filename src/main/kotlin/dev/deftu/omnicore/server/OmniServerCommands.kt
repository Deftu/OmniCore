package dev.deftu.omnicore.server

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Incubating
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.common.OmniCommands

//#if MC >= 1.16.5
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.command.CommandOutput
//#endif

//#if FABRIC && MC >= 1.16.5
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback

//#endif

//#if FORGE-LIKE && MC >= 1.18.2
//#if FORGE
//$$ import net.minecraftforge.common.MinecraftForge
//$$ import net.minecraftforge.event.RegisterCommandsEvent
//#else
//$$ import net.neoforged.neoforge.common.NeoForge
//$$ import net.neoforged.neoforge.event.RegisterCommandsEvent
//#endif
//#endif

//#if MC <= 1.12.2
//$$ import com.mojang.brigadier.exceptions.CommandSyntaxException
//$$ import com.mojang.brigadier.suggestion.Suggestion
//$$ import dev.deftu.omnicore.common.OmniCommandBridge
//$$ import dev.deftu.omnicore.common.OmniProfiler
//$$ import dev.deftu.textile.minecraft.MCTextFormat
//$$ import net.minecraft.command.ICommandSender
//$$ import net.minecraft.server.MinecraftServer
//$$ import org.apache.logging.log4j.LogManager
//#if FORGE
//$$ import net.minecraft.command.ServerCommandManager
//$$ import net.minecraftforge.fml.server.FMLServerHandler
//#else
//$$ import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
//$$ import net.minecraft.server.command.CommandManager
//#endif
//#endif

@Incubating
public object OmniServerCommands {

    private var isInitialized = false

    //#if MC <= 1.12.2
    //$$ private val logger = LogManager.getLogger()
    //#endif

    private val dispatcher: CommandDispatcher<OmniServerCommandSource> = CommandDispatcher<OmniServerCommandSource>()

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
            OmniCommands.copyCommandsWithMapper(dispatcher, this@OmniServerCommands.dispatcher) { src ->
                OmniServerCommandSource(src.server, src.output, src.world)
            }
        }
        //#else
        //$$ ServerLifecycleEvents.SERVER_STARTING.register { server ->
        //$$     val commandManager = server.commandManager as CommandManager
        //$$     for (child in dispatcher.root.children) {
        //$$         commandManager.registerCommand(OmniCommandBridge(
        //$$             node = child,
        //$$             executor = { sender, command -> execute(server, sender, command) },
        //$$             completer = { sender, command -> retrieveAutoComplete(server, sender, command) }
        //$$         ))
        //$$     }
        //$$ }
        //#endif
        //#elseif FORGE-LIKE && MC >= 1.18.2
        //#if FORGE
        //$$ MinecraftForge.EVENT_BUS.addListener<RegisterCommandsEvent> { event ->
        //#else
        //$$ NeoForge.EVENT_BUS.addListener<RegisterCommandsEvent> { event ->
        //#endif
        //$$     OmniCommands.copyCommandsWithMapper(event.dispatcher, this.dispatcher) { src ->
        //$$         OmniServerCommandSource(src.server, src.output, src.level)
        //$$     }
        //$$ }
        //#endif

        isInitialized = true
    }

    @JvmStatic
    public fun register(node: LiteralCommandNode<OmniServerCommandSource>) {
        dispatcher.root.addChild(node)

        //#if FORGE && MC <= 1.12.2
        //$$ val server = FMLServerHandler.instance().server
        //$$ val commandManager = server.commandManager as ServerCommandManager
        //$$ commandManager.registerCommand(OmniCommandBridge(
        //$$     node = node,
        //$$     executor = { sender, command -> execute(server, sender, command) },
        //$$     completer = { sender, command -> retrieveAutoComplete(server, sender, command) }
        //$$ ))
        //#endif
    }

    @JvmStatic
    @Incubating
    public fun register(command: LiteralArgumentBuilder<OmniServerCommandSource>) {
        register(command.build())
    }

    @JvmStatic
    @Incubating
    public fun literal(name: String): LiteralArgumentBuilder<OmniServerCommandSource> {
        return LiteralArgumentBuilder.literal(name)
    }

    @JvmStatic
    @Incubating
    public fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<OmniServerCommandSource, T> {
        return RequiredArgumentBuilder.argument(name, type)
    }

    //#if MC <= 1.12.2
    //$$ private fun execute(server: MinecraftServer, sender: ICommandSender, command: String) {
    //$$     val context = OmniServerCommandSource.from(server, sender)
    //$$     val results = dispatcher.parse(command, context)
    //$$
    //$$     OmniProfiler.start("omnicore_command___$command")
    //$$
    //$$     try {
    //$$         dispatcher.execute(results)
    //$$     } catch (e: CommandSyntaxException) {
    //$$         val isIgnored = OmniCommands.isIgnoredException(e.type)
    //$$         val message = "Syntax exception for server-sided command '$command'"
    //$$
    //$$         if (!isIgnored) {
    //$$             logger.warn(message, e)
    //$$             context.displayError(e)
    //$$        } else {
    //$$            logger.debug(message, e)
    //$$       }
    //$$     } finally {
    //$$         OmniProfiler.end()
    //$$     }
    //$$ }
    //$$
    //$$ private fun retrieveAutoComplete(server: MinecraftServer, sender: ICommandSender, command: String): MutableList<String> {
    //$$     val context = OmniServerCommandSource.from(server, sender)
    //$$     val results = dispatcher.parse(command, context)
    //$$     return dispatcher.getCompletionSuggestions(results)
    //$$         .join()
    //$$         .list
    //$$         .map(Suggestion::getText)
    //$$         .map { text -> MCTextFormat.GRAY + (if (command.contains(" ")) "" else "/") + text + MCTextFormat.RESET }
    //$$         .toMutableList()
    //$$ }
    //#endif

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

    // Kotlin DSL

    @GameSide(Side.SERVER)
    public fun LiteralArgumentBuilder<OmniServerCommandSource>.register(): LiteralCommandNode<OmniServerCommandSource> {
        val node = this.build()
        register(node)
        return node
    }

    @GameSide(Side.SERVER)
    public fun OmniServerCommands.command(name: String, block: LiteralArgumentBuilder<OmniServerCommandSource>.() -> Unit): LiteralArgumentBuilder<OmniServerCommandSource> {
        val command = literal(name)
        command.block()
        return command
    }

    @GameSide(Side.SERVER)
    public fun LiteralArgumentBuilder<OmniServerCommandSource>.does(block: CommandContext<OmniServerCommandSource>.() -> Int): LiteralArgumentBuilder<OmniServerCommandSource> {
        this.executes { ctx ->
            block(ctx)
        }

        return this
    }

    @GameSide(Side.SERVER)
    public fun LiteralArgumentBuilder<OmniServerCommandSource>.command(
        name: String,
        block: LiteralArgumentBuilder<OmniServerCommandSource>.() -> Unit
    ): LiteralArgumentBuilder<OmniServerCommandSource> {
        val command = literal(name)
        command.block()
        this.then(command)
        return command
    }

    @GameSide(Side.SERVER)
    public fun <T> LiteralArgumentBuilder<OmniServerCommandSource>.argument(
        name: String,
        type: ArgumentType<T>,
        block: RequiredArgumentBuilder<OmniServerCommandSource, *>.() -> Unit
    ): RequiredArgumentBuilder<OmniServerCommandSource, *> {
        val argument = argument(name, type)
        argument.block()
        this.then(argument)
        return argument
    }

    @GameSide(Side.SERVER)
    public fun <T> RequiredArgumentBuilder<OmniServerCommandSource, T>.does(block: CommandContext<OmniServerCommandSource>.() -> Int): RequiredArgumentBuilder<OmniServerCommandSource, T> {
        this.executes { ctx ->
            block(ctx)
        }

        return this
    }

}
