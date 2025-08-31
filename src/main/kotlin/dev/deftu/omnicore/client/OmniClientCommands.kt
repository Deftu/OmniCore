package dev.deftu.omnicore.client

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import dev.deftu.omnicore.common.OmniCommands
import dev.deftu.omnicore.common.profile
import org.apache.logging.log4j.LogManager

//#if FABRIC && MC >= 1.19
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
//#endif

//#if FORGE-LIKE && MC >= 1.16.5
//#if MC <= 1.17.1
//$$ import net.minecraft.commands.SharedSuggestionProvider;
//#endif
//$$
//$$ import net.minecraft.commands.CommandSourceStack
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.common.OmniCommandBridge
//$$ import net.minecraftforge.client.ClientCommandHandler
//#endif

//#if MC <= 1.12.2
//$$ import dev.deftu.textile.minecraft.MCTextFormat
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

@GameSide(Side.CLIENT)
public object OmniClientCommands {

    private var isInitialized = false

    private val logger = LogManager.getLogger()

    private val dispatcher: CommandDispatcher<OmniClientCommandSource> = CommandDispatcher<OmniClientCommandSource>()

    /**
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC && MC >= 1.19
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            copyClientCommands(dispatcher)
        }
        //#elseif FORGE-LIKE && MC >= 1.18.2
        //#if FORGE
        //$$ MinecraftForge.EVENT_BUS.addListener<RegisterClientCommandsEvent> { event ->
        //#else
        //$$ NeoForge.EVENT_BUS.addListener<RegisterClientCommandsEvent> { event ->
        //#endif
        //$$     copyClientCommands(event.dispatcher)
        //$$ }
        //#endif

        isInitialized = true
    }

    /**
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun register(node: LiteralCommandNode<OmniClientCommandSource>) {
        dispatcher.root.addChild(node)

        //#if FORGE && MC <= 1.12.2
        //$$ ClientCommandHandler.instance.registerCommand(OmniCommandBridge(
        //$$     node = node,
        //$$     executor = { _, command -> execute(command) },
        //$$     completer = { _, command -> retrieveAutoComplete(command).toMutableList() }
        //$$ ))
        //#endif
    }

    /**
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun register(command: LiteralArgumentBuilder<OmniClientCommandSource>) {
        register(command.build())
    }

    /**
     * Helper function for creating a literal argument builder without the need to specify the type.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun literal(name: String): LiteralArgumentBuilder<OmniClientCommandSource> {
        return LiteralArgumentBuilder.literal(name)
    }

    /**
     * Helper function for creating a required argument builder without the need to specify the type.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<OmniClientCommandSource, T> {
        return RequiredArgumentBuilder.argument(name, type)
    }

    /**
     * !!! NOTE !!!
     *
     * This does not actually copy command EXECUTION, only command structuring for auto-completion purposes.
     *
     * Please see [execute] usages for actual command execution implementation.
     */
    @JvmStatic
    @Suppress("MemberVisibilityCanBePrivate")
    internal fun <T> copyClientCommands(targetDispatcher: CommandDispatcher<T>) {
        OmniCommands.copyCommands(
            targetDispatcher,
            this.dispatcher,
            OmniClientCommandSource.UNIT
        )
    }

    @JvmStatic
    public fun execute(command: String): Boolean {
        val results = dispatcher.parse(command, OmniClientCommandSource.UNIT)

        return profile<Boolean>("omnicore_command___$command") {
            try {
                dispatcher.execute(results)
                true
            } catch (e: CommandSyntaxException) {
                val isIgnored = OmniCommands.isIgnoredException(e.type)
                val message = "Syntax exception for client-sided command '$command'"

                if (!isIgnored) {
                    logger.warn(message, e)
                    OmniClientCommandSource.UNIT.displayError(e)
                } else {
                    logger.debug(message, e)
                }

                !isIgnored
            }
        }
    }

    //#if MC <= 1.12.2
    //$$ @JvmStatic
    //$$ public fun retrieveAutoComplete(command: String): Set<String> {
    //$$     val results = dispatcher.parse(command, OmniClientCommandSource.UNIT)
    //$$     return dispatcher.getCompletionSuggestions(results)
    //$$         .join()
    //$$         .list
    //$$         .map(Suggestion::getText)
    //$$         .map { text -> MCTextFormat.GRAY + (if (command.contains(" ")) "" else "/") + text + MCTextFormat.RESET }
    //$$         .toSet()
    //$$ }
    //#endif

    // Kotlin DSL

    @GameSide(Side.CLIENT)
    public fun LiteralArgumentBuilder<OmniClientCommandSource>.register(): LiteralCommandNode<OmniClientCommandSource> {
        val node = this.build()
        register(node)
        return node
    }

    @GameSide(Side.CLIENT)
    public fun OmniClientCommands.command(name: String, block: LiteralArgumentBuilder<OmniClientCommandSource>.() -> Unit): LiteralArgumentBuilder<OmniClientCommandSource> {
        val command = literal(name)
        command.block()
        return command
    }

    @GameSide(Side.CLIENT)
    public fun LiteralArgumentBuilder<OmniClientCommandSource>.does(block: CommandContext<OmniClientCommandSource>.() -> Int): LiteralArgumentBuilder<OmniClientCommandSource> {
        this.executes { ctx ->
            block(ctx)
        }

        return this
    }

    @GameSide(Side.CLIENT)
    public fun LiteralArgumentBuilder<OmniClientCommandSource>.command(
        name: String,
        block: LiteralArgumentBuilder<OmniClientCommandSource>.() -> Unit
    ): LiteralArgumentBuilder<OmniClientCommandSource> {
        val command = literal(name)
        command.block()
        this.then(command)
        return command
    }

    @GameSide(Side.CLIENT)
    public fun <T> LiteralArgumentBuilder<OmniClientCommandSource>.argument(
        name: String,
        type: ArgumentType<T>,
        block: RequiredArgumentBuilder<OmniClientCommandSource, *>.() -> Unit
    ): RequiredArgumentBuilder<OmniClientCommandSource, *> {
        val argument = argument(name, type)
        argument.block()
        this.then(argument)
        return argument
    }

    @GameSide(Side.CLIENT)
    public fun <T> RequiredArgumentBuilder<OmniClientCommandSource, T>.does(block: CommandContext<OmniClientCommandSource>.() -> Int): RequiredArgumentBuilder<OmniClientCommandSource, T> {
        this.executes { ctx ->
            block(ctx)
        }

        return this
    }

}
