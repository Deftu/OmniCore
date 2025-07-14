package dev.deftu.omnicore.server

import dev.deftu.textile.minecraft.*
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import java.util.function.Consumer

//#if MC >= 1.16.5 && MC <= 1.18.2
//$$ import java.util.UUID
//#endif

public class OmniServerCommandSource(
    public val server: MinecraftServer,
    public val output: CommandOutput,
    public val world: ServerWorld
) {

    //#if MC <= 1.12.2
    //$$ public companion object {
    //$$
    //$$     @JvmStatic
    //$$     public fun from(
    //$$         server: MinecraftServer,
    //$$         sender: ICommandSender,
    //$$     ): OmniServerCommandSource {
    //$$         return OmniServerCommandSource(
    //$$             server = server,
    //$$             output = sender,
    //$$             sender.entityWorld as WorldServer
    //$$         )
    //$$     }
    //$$
    //$$ }
    //#endif

    public val player: ServerPlayerEntity?
        get() = output as? ServerPlayerEntity

    public fun displayMessage(textHolder: MCTextHolder<*>) {
        output.sendMessage(
            textHolder.asVanilla(),
            //#if MC >= 1.16.5 && MC <= 1.18.2
            //$$ UUID(0, 0)
            //#endif
        )
    }

    public fun displayMessage(text: String) {
        displayMessage(MCSimpleTextHolder(text))
    }

    public fun displayError(error: Throwable, block: Consumer<MCMutableTextHolder<*>> = Consumer {  }) {
        val text = MCSimpleMutableTextHolder(error.message ?: "An error occurred")
            .addFormatting(MCTextFormat.RED)
        block.accept(text)
        displayMessage(text)
    }

    public fun displayError(textHolder: MCTextHolder<*>) {
        displayMessage(textHolder.withFormatting(MCTextFormat.RED))
    }

    public fun displayError(text: String) {
        displayError(MCSimpleTextHolder(text))
    }

}
