package dev.deftu.omnicore.server

import dev.deftu.textile.MutableTextHolder
import dev.deftu.textile.SimpleMutableTextHolder
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder
import dev.deftu.textile.minecraft.MinecraftTextFormat
import dev.deftu.textile.minecraft.toVanilla
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

    public val player: ServerPlayerEntity?
        get() = output as? ServerPlayerEntity

    public fun showMessage(textHolder: TextHolder) {
        output.sendMessage(
            textHolder.toVanilla(),
            //#if MC >= 1.16.5 && MC <= 1.18.2
            //$$ UUID(0, 0)
            //#endif
        )
    }

    public fun showMessage(text: String) {
        showMessage(SimpleTextHolder(text))
    }

    public fun showError(error: Throwable, block: Consumer<MutableTextHolder> = Consumer {  }) {
        val text = SimpleMutableTextHolder(error.message ?: "An error occurred")
            .format(MinecraftTextFormat.RED)
        block.accept(text)
        showMessage(text)
    }

    public fun showError(textHolder: TextHolder) {
        showMessage(textHolder.formatted(MinecraftTextFormat.RED))
    }

    public fun showError(text: String) {
        showError(SimpleTextHolder(text))
    }

}
