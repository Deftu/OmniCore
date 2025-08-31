package dev.deftu.omnicore.internal.client.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.textile.minecraft.MCTextHolder
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object ClientAudience : Audience {
    override fun sendChat(text: MCTextHolder<*>) {
        OmniClientChat.displayChatMessage(text)
    }

    override fun sendActionBar(text: MCTextHolder<*>) {
        OmniClientChat.displayActionBar(text)
    }

    override fun sendTitle(title: MCTextHolder<*>, subtitle: MCTextHolder<*>?, fadeIn: Int, stay: Int, fadeOut: Int) {
        OmniClientChat.displayTitle(title, subtitle, fadeIn, stay, fadeOut)
    }
}
