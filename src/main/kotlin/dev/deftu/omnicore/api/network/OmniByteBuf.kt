package dev.deftu.omnicore.api.network

import net.minecraft.network.FriendlyByteBuf

public object OmniByteBuf {
    @JvmStatic
    public fun readString(buf: FriendlyByteBuf, maxLength: Int): String {
        return buf.readUtf(maxLength)
    }

    @JvmStatic
    public fun readString(buf: FriendlyByteBuf): String {
        return readString(buf, Short.MAX_VALUE.toInt())
    }
}
