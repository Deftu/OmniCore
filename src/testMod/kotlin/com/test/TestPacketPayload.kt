package com.test

import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.api.network.OmniByteBuf
import dev.deftu.omnicore.api.network.PacketPayload
import dev.deftu.omnicore.api.network.PacketType
import dev.deftu.omnicore.api.network.codec.StreamCodec
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation

data class TestPacketPayload(val message: String) : PacketPayload {
    companion object {
        @JvmField
        val ID = locationOrThrow("testmod", "test_packet")

        @JvmField
        val CODEC = StreamCodec.ofMember(TestPacketPayload::write, ::TestPacketPayload)

        @JvmField
        val TYPE = PacketType(ID, CODEC)
    }

    private constructor(buf: FriendlyByteBuf) : this(OmniByteBuf.readString(buf))

    fun write(buf: FriendlyByteBuf) {
        buf.writeUtf(message)
    }

    override val id: ResourceLocation = ID
}
