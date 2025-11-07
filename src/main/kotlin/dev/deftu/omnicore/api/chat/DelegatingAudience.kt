package dev.deftu.omnicore.api.chat

import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.textile.Text
import org.jetbrains.annotations.ApiStatus

public fun interface DelegatingAudience : Audience {
    @ApiStatus.OverrideOnly public fun children(): Iterable<Audience>

    override fun filter(predicate: (Audience) -> Boolean): Audience {
        val filteredChildren = children().mapNotNull { child ->
            val filteredChild = child.filter(predicate)
            if (filteredChild === Audiences.EMPTY) {
                null
            } else {
                filteredChild
            }
        }

        return Audiences.of(filteredChildren)
    }

    override fun forEach(action: (Audience) -> Unit) {
        for (child in children()) {
            child.forEach(action)
        }
    }

    override fun sendChat(text: Text) {
        for (child in children()) {
            child.sendChat(text)
        }
    }

    override fun sendActionBar(text: Text) {
        for (child in children()) {
            child.sendActionBar(text)
        }
    }

    override fun sendTitle(titleInfo: TitleInfo) {
        for (child in children()) {
            child.sendTitle(titleInfo)
        }
    }

    override fun playSound(sound: OmniSound, volume: Float, pitch: Float) {
        for (child in children()) {
            child.playSound(sound, volume, pitch)
        }
    }
}
