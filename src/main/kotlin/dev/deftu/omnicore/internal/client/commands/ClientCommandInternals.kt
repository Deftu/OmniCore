package dev.deftu.omnicore.internal.client.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.tree.LiteralCommandNode
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.commands.OmniClientCommandSource
import dev.deftu.omnicore.api.client.profiled
import dev.deftu.omnicore.internal.commands.CommandOps
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

//#if FABRIC && MC >= 1.19.2
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.internal.commands.LegacyCommandBridge
//$$ import net.minecraftforge.client.ClientCommandHandler
//#endif

//#if MC <= 1.12.2
//$$ import com.mojang.brigadier.suggestion.Suggestion
//$$ import net.minecraft.util.text.TextFormatting
//#endif

//#if FORGE-LIKE && MC >= 1.18.2
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//#if FORGE
//$$ import net.minecraftforge.client.event.RegisterClientCommandsEvent
//#else
//$$ import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent
//#endif
//#endif

@ApiStatus.Internal
public object ClientCommandInternals {
    private val logger = LogManager.getLogger(ClientCommandInternals::class.java)

    @JvmStatic
    public var isInitialized: Boolean = false
        private set

    @JvmStatic
    public val dispatcher: CommandDispatcher<OmniClientCommandSource> = CommandDispatcher()

    @JvmStatic
    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC && MC >= 1.19.2
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            copyClientCommands(dispatcher)
        }
        //#elseif FORGE-LIKE && MC >= 1.18.2
        //$$ forgeEventBus.addListener<RegisterClientCommandsEvent> { event ->
        //$$     copyClientCommands(event.dispatcher)
        //$$ }
        //#endif

        isInitialized = true
    }

    @JvmStatic
    public fun register(node: LiteralCommandNode<OmniClientCommandSource>) {
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
    public fun register(command: LiteralArgumentBuilder<OmniClientCommandSource>) {
        register(command.build())
    }

    @JvmStatic
    public fun execute(command: String): Boolean {
        val results = dispatcher.parse(command, OmniClientCommandSource)
        return client.profiled<Boolean>("omnicore_command___$command") {
            try {
                dispatcher.execute(results)
                true
            } catch (e: CommandSyntaxException) {
                val isIgnored = CommandOps.isIgnoredException(e.type)
                val message = "Syntax exception for client-sided command '$command'"

                if (!isIgnored) {
                    logger.warn(message, e)
                    OmniClientChat.displayErrorMessage(e)
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
    //$$     val results = dispatcher.parse(command, OmniClientCommandSource)
    //$$     return dispatcher.getCompletionSuggestions(results)
    //$$         .join()
    //$$         .list
    //$$         .map(Suggestion::getText)
    //$$         .map { text -> TextFormatting.GRAY.toString() + (if (command.contains(" ")) "" else "/") + text + TextFormatting.RESET.toString() }
    //$$         .toSet()
    //$$ }
    //#endif

    /**
     * !!! NOTE !!!
     *
     * This does not actually copy command EXECUTION, only command structuring for auto-completion purposes.
     *
     * Please see [execute] usages for actual command execution implementation.
     */
    @JvmStatic
    @Suppress("MemberVisibilityCanBePrivate")
    public fun <T> copyClientCommands(targetDispatcher: CommandDispatcher<T>) {
        CommandOps.copyCommands(
            targetDispatcher,
            this.dispatcher,
            OmniClientCommandSource
        )
    }
}
