package dev.deftu.omnicore.server

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder

//#if FABRIC && MC >= 1.16.5
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
//#endif

//#if FABRIC && MC >= 1.16.5 || FORGE-LIKE && MC >= 1.18.2
import java.util.concurrent.LinkedBlockingQueue
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
//$$ import dev.deftu.omnicore.common.OmniCommandBridge
//#if FORGE
//$$ import net.minecraftforge.client.ClientCommandHandler
//$$ import net.minecraft.command.ServerCommandManager
//$$ import net.minecraftforge.fml.server.FMLServerHandler
//#else
//$$ import net.legacyfabric.fabric.api.registry.CommandRegistry
//#endif
//#endif

public typealias ServerCommandSource =
        //#if MC >= 1.16.5
        net.minecraft.server.command.ServerCommandSource
        //#else
        //$$ net.minecraft.command.ICommandSender
        //#endif

public object OmniServerCommands {

    private val dispatcher: CommandDispatcher<ServerCommandSource> = CommandDispatcher<ServerCommandSource>()
    //#if FABRIC && MC >= 1.16.5 || FORGE-LIKE && MC >= 1.18.2
    private val commandsToRegister = LinkedBlockingQueue<LiteralArgumentBuilder<ServerCommandSource>>()
    //#endif

    public fun initialize() {
        //#if FABRIC && MC >= 1.16.5
        CommandRegistrationCallback.EVENT.register {
                                                    dispatcher,
                                                    _,
                                                    //#if MC >= 1.19.2
                                                    env
                                                    //#endif
                                                    ->
            while (commandsToRegister.isNotEmpty()) {
                dispatcher.register(commandsToRegister.poll())
            }
        }
        //#elseif FORGE-LIKE && MC >= 1.18.2
        //#if FORGE
        //$$ MinecraftForge.EVENT_BUS.addListener<RegisterCommandsEvent> { event ->
        //#else
        //$$ NeoForge.EVENT_BUS.addListener<RegisterCommandsEvent> { event ->
        //#endif
        //$$     for (command in commandsToRegister) {
        //$$         event.dispatcher.register(command)
        //$$     }
        //$$ }
        //#endif
    }

    public fun register(command: LiteralArgumentBuilder<ServerCommandSource>) {
        //#if MC <= 1.12.2
        //$$ val node =
        //#endif
        dispatcher.register(command)

        //#if MC <= 1.12.2
        //#if FORGE
        //$$ val commandManager = FMLServerHandler.instance().server.commandManager as ServerCommandManager
        //$$ commandManager.registerCommand(OmniCommandBridge(dispatcher, node))
        //#else
        //$$ CommandRegistry.INSTANCE.register(OmniCommandBridge(dispatcher, node))
        //#endif
        //#elseif FABRIC || FORGE-LIKE && MC >= 1.18.2
        commandsToRegister.add(command)
        //#endif
    }

    public fun literal(name: String): LiteralArgumentBuilder<ServerCommandSource> {
        return LiteralArgumentBuilder.literal(name)
    }

}
