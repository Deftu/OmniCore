package dev.deftu.omnicore.internal.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.omnicore.api.chat.OmniChat
import dev.deftu.textile.Text
import net.minecraft.server.network.ServerPlayerEntity
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class ServerAudience(private val targets: Collection<ServerPlayerEntity>) : Audience {
    override fun sendChat(text: Text) {
        for (player in targets) {
            OmniChat.displayChatMessage(player, text)
        }
    }

    override fun sendActionBar(text: Text) {
        for (player in targets) {
            OmniChat.displayActionBar(player, text)
        }
    }

    override fun sendTitle(title: Text, subtitle: Text?, fadeIn: Int, stay: Int, fadeOut: Int) {
        for (player in targets) {
            OmniChat.displayTitle(player, title, subtitle, fadeIn, stay, fadeOut)
        }
    }
}
