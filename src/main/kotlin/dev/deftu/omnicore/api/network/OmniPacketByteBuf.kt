package dev.deftu.omnicore.api.network

import net.minecraft.network.PacketByteBuf

public object OmniPacketByteBuf {
    @JvmStatic
    public fun readString(buf: PacketByteBuf, maxLength: Int): String {
        return buf.readString(maxLength)
    }

    @JvmStatic
    public fun readString(buf: PacketByteBuf): String {
        return readString(buf, Short.MAX_VALUE.toInt())
    }
}
