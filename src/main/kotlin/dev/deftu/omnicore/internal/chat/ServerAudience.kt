package dev.deftu.omnicore.internal.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.textile.minecraft.MCTextHolder
import net.minecraft.server.network.ServerPlayerEntity
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class ServerAudience(private val targets: Collection<ServerPlayerEntity>) : Audience {
    override fun sendChat(text: MCTextHolder<*>) {
        for (player in targets) {
            //#if MC >= 1.19.2
            player.sendMessage(text.asVanilla(), false)
            //#else
            //$$ player.sendMessage(text.asVanilla())
            //#endif
        }
    }

    override fun sendActionBar(text: MCTextHolder<*>) {
        for (player in targets) {
            //#if MC >= 1.16.5
            player.sendMessage(text.asVanilla(), true)
            //#else
            //$$ player.sendMessage(text.asVanilla())
            //#endif
        }
    }

    override fun sendTitle(title: MCTextHolder<*>, subtitle: MCTextHolder<*>?, fadeIn: Int, stay: Int, fadeOut: Int) {
        for (player in targets) {
            // TODO
        }
    }
}
