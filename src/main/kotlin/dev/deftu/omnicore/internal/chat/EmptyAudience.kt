package dev.deftu.omnicore.internal.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.omnicore.api.chat.TitleInfo
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.textile.Text
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object EmptyAudience : Audience {
    override fun sendChat(text: Text) {
        // no-op
    }

    override fun sendActionBar(text: Text) {
        // no-op
    }

    override fun sendTitle(titleInfo: TitleInfo) {
        // no-op
    }

    override fun playSound(sound: OmniSound, volume: Float, pitch: Float) {
        // no-op
    }
}
