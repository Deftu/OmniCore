package dev.deftu.omnicore.internal.client.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.omnicore.api.client.chat.OmniClientChat
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

    override fun sendTitle(title: Text, subtitle: Text?, fadeIn: Int, stay: Int, fadeOut: Int) {
        OmniClientChat.displayTitle(title, subtitle, fadeIn, stay, fadeOut)
    }
}
