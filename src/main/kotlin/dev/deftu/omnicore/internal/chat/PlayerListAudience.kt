package dev.deftu.omnicore.internal.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.omnicore.api.chat.OmniChat
import dev.deftu.omnicore.api.chat.TitleInfo
import dev.deftu.omnicore.api.sound.OmniServerSounds
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.textile.Text
import net.minecraft.server.level.ServerPlayer
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class PlayerListAudience(private val targets: Collection<ServerPlayer>) : Audience {
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

    override fun sendTitle(titleInfo: TitleInfo) {
        for (player in targets) {
            OmniChat.displayTitle(player, titleInfo)
        }
    }

    override fun playSound(sound: OmniSound, volume: Float, pitch: Float) {
        for (player in targets) {
            OmniServerSounds.play(player, sound, volume, pitch)
        }
    }
}
