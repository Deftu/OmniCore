package dev.deftu.omnicore.api.network

import dev.deftu.omnicore.api.network.codec.StreamCodec
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation

public data class PacketType<T : PacketPayload, B : FriendlyByteBuf>(
    val id: ResourceLocation,
    val codec: StreamCodec<T, B>
)
