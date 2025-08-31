package dev.deftu.omnicore.api.client.events

public sealed interface ClientTickEvent {
    public data object Pre : ClientTickEvent
    public data object Post : ClientTickEvent
}
