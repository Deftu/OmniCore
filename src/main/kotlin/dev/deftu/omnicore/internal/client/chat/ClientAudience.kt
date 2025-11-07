package dev.deftu.omnicore.internal.client.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.omnicore.api.chat.TitleInfo
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.sound.OmniClientSound
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.textile.Text
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object ClientAudience : Audience {
    override fun sendChat(text: Text) {
        OmniClientChat.displayChatMessage(text)
    }

    override fun sendActionBar(text: Text) {
        OmniClientChat.displayActionBar(text)
    }

    override fun sendTitle(titleInfo: TitleInfo) {
        OmniClientChat.displayTitle(titleInfo)
    }

    override fun playSound(sound: OmniSound, volume: Float, pitch: Float) {
        OmniClientSound.play(sound, volume, pitch)
    }
}
