package dev.deftu.omnicore.api.chat

import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextHolder

public interface Audience {
    public fun sendChat(text: MCTextHolder<*>)
    public fun sendChat(text: String) {
        sendChat(MCSimpleTextHolder(text))
    }

    public fun sendActionBar(text: MCTextHolder<*>)
    public fun sendActionBar(text: String) {
        sendActionBar(MCSimpleTextHolder(text))
    }

    public fun sendTitle(
        title: MCTextHolder<*>,
        subtitle: MCTextHolder<*>? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    )

    public fun sendTitle(
        title: String,
        subtitle: String? = null,
        fadeIn: Int = 10,
        stay: Int = 70,
        fadeOut: Int = 20
    ) {
        sendTitle(
            MCSimpleTextHolder(title),
            subtitle?.let { MCSimpleTextHolder(it) },
            fadeIn,
            stay,
            fadeOut
        )
    }
}
