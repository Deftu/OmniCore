package dev.deftu.omnicore.client

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.exceptions.CommandExceptionType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.common.OmniCommands
import dev.deftu.omnicore.common.OmniProfiler
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

//#if FABRIC && MC <= 1.12.2
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
        //$$ ClientCommandHandler.instance.registerCommand(OmniCommandBridge(dispatcher, node) { _ -> OmniClientCommandSource.UNIT })
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
    internal fun execute(command: String): Boolean {
        val results = dispatcher.parse(command, OmniClientCommandSource.UNIT)

        OmniProfiler.start("omnicore_command___$command")

        try {
            dispatcher.execute(results)
            return true
        } catch (e: CommandSyntaxException) {
            val isIgnored = isIgnoredException(e.type)
            val message = "Syntax exception for client-sided command '$command'"

            if (!isIgnored) {
                logger.warn(message, e)
            } else {
                logger.debug(message, e)
            }

            return !isIgnored
        } finally {
            OmniProfiler.end()
        }
    }

    private fun isIgnoredException(type: CommandExceptionType): Boolean {
        val builtIns = CommandSyntaxException.BUILT_IN_EXCEPTIONS
        return type == builtIns.dispatcherUnknownCommand() || type == builtIns.dispatcherParseException()
    }

    //#if FABRIC && MC <= 1.12.2
    //$$ @JvmStatic
    //$$ internal fun retrieveAutoComplete(command: String): Set<String> {
    //$$     val results = dispatcher.parse(command, OmniClientCommandSource.UNIT)
    //$$     return dispatcher.getCompletionSuggestions(results)
    //$$         .join()
    //$$         .list
    //$$         .map(Suggestion::getText)
    //$$         .map { text -> MCTextFormat.GRAY + (if (command.contains(" ")) "" else "/") + text + MCTextFormat.RESET }
    //$$         .toSet()
    //$$ }
    //#endif

}
