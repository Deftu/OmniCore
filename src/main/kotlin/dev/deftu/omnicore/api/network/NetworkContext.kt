package dev.deftu.omnicore.api.network

import dev.deftu.omnicore.api.Side
import dev.deftu.omnicore.api.client.network.OmniClientNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.ClientConnection
import net.minecraft.server.network.ServerPlayerEntity

public data class NetworkContext(
    val side: Side,
    val connection: ClientConnection,
    val player: PlayerEntity?
) {
    public fun reply(payload: PacketPayload) {
        when (side) {
            Side.CLIENT -> OmniClientNetworking.send(payload)
            Side.SERVER -> OmniNetworking.send(player as ServerPlayerEntity, payload)
        }
    }
}
