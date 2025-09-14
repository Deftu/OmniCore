package com.test

import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.omnicore.api.network.PacketPayload
import dev.deftu.omnicore.api.network.PacketType
import dev.deftu.omnicore.api.network.codec.StreamCodec
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

data class TestPacketPayload(val message: String) : PacketPayload {
    companion object {
        @JvmField
        val ID = identifierOrThrow("testmod", "test_packet")

        @JvmField
        val CODEC = StreamCodec.ofMember(TestPacketPayload::write, ::TestPacketPayload)

        @JvmField
        val TYPE = PacketType(ID, CODEC)
    }

    private constructor(buf: PacketByteBuf) : this(
        //#if MC >= 1.16.5
        buf.readString()
        //#else
        //$$ buf.readString(32767)
        //#endif
    )

    fun write(buf: PacketByteBuf) {
        buf.writeString(message)
    }

    override val id: Identifier = ID
}
