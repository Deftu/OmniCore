package dev.deftu.omnicore.server

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.deftu.omnicore.annotations.Incubating
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
//$$ import dev.deftu.omnicore.common.OmniCommandBridge
//#if FORGE
//$$ import net.minecraftforge.client.ClientCommandHandler
//$$ import net.minecraft.command.ServerCommandManager
//$$ import net.minecraftforge.fml.server.FMLServerHandler
//#else
//$$ import net.legacyfabric.fabric.api.registry.CommandRegistry
//#endif
//#endif

@Incubating
public object OmniServerCommands {

    private var isInitialized = false

    private val dispatcher: CommandDispatcher<OmniServerCommandSource> = CommandDispatcher<OmniServerCommandSource>()

    @JvmStatic
    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if FABRIC && MC >= 1.16.5
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
    @Incubating
    public fun register(command: LiteralArgumentBuilder<OmniServerCommandSource>) {
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
        //#endif
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

}
