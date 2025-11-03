package dev.deftu.omnicore.api.chat

import dev.deftu.omnicore.internal.chat.ServerAudience
import dev.deftu.omnicore.internal.client.chat.ClientAudience
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

public object Audiences {
    @JvmStatic
    public val client: Audience
        get() = ClientAudience

    @JvmStatic
    public fun create(player: ServerPlayer): Audience {
        return ServerAudience(listOf(player))
    }

    @JvmStatic
    public fun create(vararg players: ServerPlayer): Audience {
        return ServerAudience(players.toList())
    }

    @JvmStatic
    public fun create(players: Collection<ServerPlayer>): Audience {
        return ServerAudience(players)
    }

    @JvmStatic
    public fun all(server: MinecraftServer): Audience {
        return ServerAudience(server.playerList.players)
    }
}
