package dev.deftu.omnicore.common

import io.netty.buffer.ByteBuf
import java.util.function.BiConsumer

public object OmniPackets {

    public fun writeVarInt(buf: ByteBuf, value: Int) {
        var currentValue = value
        while (currentValue and -128 != 0) {
            buf.writeByte(currentValue and 127 or 128)
            currentValue = currentValue ushr 7
        }

        buf.writeByte(currentValue)
    }

    public fun writeString(buf: ByteBuf, string: String) {
        val bytes = string.toByteArray(Charsets.UTF_8)
        if (bytes.size > Short.MAX_VALUE) {
            throw IllegalArgumentException("String too big (was ${bytes.size} bytes, max ${Short.MAX_VALUE})")
        }

        buf.writeVarInt(bytes.size)
        buf.writeBytes(bytes)
    }

    public fun <T> writeCollection(buf: ByteBuf, collection: Collection<T>, writer: BiConsumer<ByteBuf, T>) {
        buf.writeVarInt(collection.size)
        for (item in collection) {
            writer.accept(buf, item)
        }
    }

}

public fun ByteBuf.writeVarInt(value: Int) {
    OmniPackets.writeVarInt(this, value)
}

public fun ByteBuf.writeString(string: String) {
    OmniPackets.writeString(this, string)
}

public fun <T> ByteBuf.writeCollection(collection: Collection<T>, writer: (ByteBuf, T) -> Unit) {
    OmniPackets.writeCollection(this, collection, writer)
}
