package dev.deftu.omnicore.api.chat

import dev.deftu.textile.Text

public interface Audience {
    public fun sendChat(text: Text)
    public fun sendChat(text: String) {
        sendChat(Text.literal(text))
    }

    public fun sendActionBar(text: Text)
    public fun sendActionBar(text: String) {
        sendActionBar(Text.literal(text))
    }

    public fun sendTitle(
        title: Text,
        subtitle: Text? = null,
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
            Text.literal(title),
            subtitle?.let(Text::literal),
            fadeIn,
            stay,
            fadeOut
        )
    }
}
