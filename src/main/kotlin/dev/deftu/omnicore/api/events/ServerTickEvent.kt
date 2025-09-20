package dev.deftu.omnicore.api.events

import net.minecraft.server.MinecraftServer

public sealed interface ServerTickEvent {
    public val server: MinecraftServer

    public data class Pre(override val server: MinecraftServer) : ServerTickEvent
    public data class Post(override val server: MinecraftServer) : ServerTickEvent
}
