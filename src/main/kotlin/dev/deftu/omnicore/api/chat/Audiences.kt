package dev.deftu.omnicore.api.chat

import dev.deftu.omnicore.internal.chat.ConsoleAudience
import dev.deftu.omnicore.internal.chat.EmptyAudience
import dev.deftu.omnicore.internal.chat.PlayerListAudience
import dev.deftu.omnicore.internal.chat.ServerAudience
import dev.deftu.omnicore.internal.client.chat.ClientAudience
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

public object Audiences {
    /**
     * An audience which does nothing when sent data.
     */
    @JvmField public val EMPTY: Audience = EmptyAudience
    /**
     * An audience which broadcasts semi-formatted debug messages to a Log4j logger (the console).
     */
    @JvmField public val CONSOLE: Audience = ConsoleAudience
    /**
     * An audience which broadcasts to the client-side receiver.
     */
    @JvmField public val CLIENT: Audience = ClientAudience

    @JvmStatic
    public fun of(vararg audiences: Audience): Audience {
        return when (audiences.size) {
            0 -> EMPTY
            1 -> audiences[0]
            else -> DelegatingAudience { audiences.asList() }
        }
    }

    @JvmStatic
    public fun of(audiences: Collection<Audience>): Audience {
        return when (audiences.size) {
            0 -> EMPTY
            1 -> audiences.first()
            else -> DelegatingAudience { audiences }
        }
    }

    /**
     * Creates an audience which broadcasts to a single player.
     */
    @JvmStatic
    public fun single(player: ServerPlayer): Audience {
        return PlayerListAudience(listOf(player))
    }

    /**
     * Creates an audience which broadcasts to the given players.
     */
    @JvmStatic
    public fun forAll(vararg players: ServerPlayer): Audience {
        return PlayerListAudience(players.toList())
    }

    /**
     * Creates an audience which broadcasts to the given collection of players.
     */
    @JvmStatic
    public fun forAll(players: Collection<ServerPlayer>): Audience {
        return PlayerListAudience(players)
    }

    /**
     * Creates an audience which broadcasts to all players on the given server.
     */
    @JvmStatic
    public fun broadcasting(server: MinecraftServer): Audience {
        return ServerAudience(server)
    }
}
