package dev.deftu.omnicore.api.network

import dev.deftu.omnicore.api.network.codec.StreamCodec
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

public data class PacketType<T : PacketPayload, B : PacketByteBuf>(
    val id: Identifier,
    val codec: StreamCodec<T, B>
)
