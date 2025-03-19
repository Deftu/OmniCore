package dev.deftu.omnicore.common.events

import net.minecraft.server.MinecraftServer

public sealed interface TickEvent {
    public sealed interface Client : TickEvent {
        public data object Pre : Client
        public data object Post : Client
    }

    public sealed interface Server : TickEvent {
        public val server: MinecraftServer

        public data class Pre(override val server: MinecraftServer) : Server
        public data class Post(override val server: MinecraftServer) : Server
    }
}
