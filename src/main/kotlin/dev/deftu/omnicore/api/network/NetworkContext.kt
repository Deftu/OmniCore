package dev.deftu.omnicore.api.network

import dev.deftu.omnicore.api.Side
import dev.deftu.omnicore.api.client.network.OmniClientNetworking
import net.minecraft.world.entity.player.Player
import net.minecraft.network.Connection
import net.minecraft.server.level.ServerPlayer

public data class NetworkContext(
    val side: Side,
    val connection: Connection,
    val player: Player?
) {
    public fun reply(payload: PacketPayload) {
        when (side) {
            Side.CLIENT -> OmniClientNetworking.send(payload)
            Side.SERVER -> OmniNetworking.send(player as ServerPlayer, payload)
        }
    }
}
