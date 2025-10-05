package dev.deftu.omnicore.api.chat

import dev.deftu.textile.Text

public sealed interface MessageSurface {
    public data class ChatMessage(val content: Text) : MessageSurface
    public data class ErrorMessage(
        val content: Text,
        val error: Throwable,
        val isDetailed: Boolean = true
    ) : MessageSurface
    public data class ActionBar(val content: Text) : MessageSurface
    public data class Title(
        val title: Text,
        val subtitle: Text? = null,
        val fadeIn: Int = 10,
        val stay: Int = 70,
        val fadeOut: Int = 20
    ) : MessageSurface
}
