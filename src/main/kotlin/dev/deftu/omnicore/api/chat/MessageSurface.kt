package dev.deftu.omnicore.api.chat

import dev.deftu.textile.minecraft.MCTextHolder

public sealed interface MessageSurface {
    public data class ChatMessage(val content: MCTextHolder<*>) : MessageSurface
    public data class ErrorMessage(
        val content: MCTextHolder<*>,
        val error: Throwable,
        val isDetailed: Boolean = true
    ) : MessageSurface
    public data class ActionBar(val content: MCTextHolder<*>) : MessageSurface
    public data class Title(
        val title: MCTextHolder<*>,
        val subtitle: MCTextHolder<*>? = null,
        val fadeIn: Int = 10,
        val stay: Int = 70,
        val fadeOut: Int = 20
    ) : MessageSurface
}
