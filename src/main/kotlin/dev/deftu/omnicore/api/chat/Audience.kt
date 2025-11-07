package dev.deftu.omnicore.api.chat

import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.textile.Text

public interface Audience {
    public fun filter(predicate: (Audience) -> Boolean): Audience {
        return if (predicate(this)) {
            this
        } else {
            Audiences.EMPTY
        }
    }

    public fun forEach(action: (Audience) -> Unit) {
        action(this)
    }

    public fun sendChat(text: Text)
    public fun sendChat(text: String) {
        sendChat(Text.literal(text))
    }

    public fun sendActionBar(text: Text)
    public fun sendActionBar(text: String) {
        sendActionBar(Text.literal(text))
    }

    public fun sendTitle(titleInfo: TitleInfo)
    public fun playSound(sound: OmniSound, volume: Float, pitch: Float)
}
