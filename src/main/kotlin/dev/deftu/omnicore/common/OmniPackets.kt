package dev.deftu.omnicore.common

import io.netty.buffer.ByteBuf
import java.util.function.BiConsumer
import java.util.function.Function

//#if FORGE && MC <= 1.12.2
//$$ import net.minecraftforge.fml.common.network.NetworkRegistry
//$$ import net.minecraftforge.fml.common.network.simpleimpl.IMessage
//$$ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
//$$ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
//$$ import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
//$$ import net.minecraftforge.fml.relauncher.Side
//$$ import net.minecraft.util.ResourceLocation
//#endif

public object OmniPackets {

    //#if FORGE && MC <= 1.12.2
    //$$ private val channels = mutableMapOf<ResourceLocation, SimpleNetworkWrapper>()
    //#endif

    public fun writeVarInt(buf: ByteBuf, value: Int) {
        var currentValue = value
        while (currentValue and -128 != 0) {
            buf.writeByte(currentValue and 127 or 128)
            currentValue = currentValue ushr 7
        }

        buf.writeByte(currentValue)
    }

    public fun readVarInt(buf: ByteBuf): Int {
        var result = 0
        var shift = 0
        var byteValue: Int

        do {
            if (shift >= 32) {
                throw RuntimeException("VarInt is too large")
            }

            byteValue = buf.readByte().toInt()
            result = result or ((byteValue and 127) shl shift)
            shift += 7
        } while (byteValue and 128 != 0)

        return result
    }


    public fun writeString(buf: ByteBuf, string: String) {
        val bytes = string.toByteArray(Charsets.UTF_8)
        if (bytes.size > Short.MAX_VALUE) {
            throw IllegalArgumentException("String too big (was ${bytes.size} bytes, max ${Short.MAX_VALUE})")
        }

        buf.writeVarInt(bytes.size)
        buf.writeBytes(bytes)
    }

    public fun readString(buf: ByteBuf): String {
        val length = buf.readVarInt()
        val bytes = ByteArray(length)
        buf.readBytes(bytes)
        return String(bytes, Charsets.UTF_8)
    }

    public fun <T> writeCollection(buf: ByteBuf, collection: Collection<T>, writer: BiConsumer<ByteBuf, T>) {
        buf.writeVarInt(collection.size)
        for (item in collection) {
            writer.accept(buf, item)
        }
    }

    public fun <T> readCollection(buf: ByteBuf, reader: Function<ByteBuf, T>): List<T> {
        val size = buf.readVarInt()
        return List(size) {
            reader.apply(buf)
        }
    }

    //#if FORGE && MC <= 1.12.2
    //$$ public fun getChannel(id: ResourceLocation): SimpleNetworkWrapper {
    //$$     return channels.computeIfAbsent(id) {
    //$$         val value = NetworkRegistry.INSTANCE.newSimpleChannel(id.toString())
    //$$
    //$$         var discriminator = 0
    //$$         value.registerMessage(OmniCustomForgeMessage.Handler(id), OmniCustomForgeMessage::class.java, discriminator++, Side.CLIENT)
    //$$         value.registerMessage(OmniCustomForgeMessage.Handler(id), OmniCustomForgeMessage::class.java, discriminator, Side.SERVER)
    //$$
    //$$         value
    //$$     }
    //$$ }
    //#endif

}

public fun ByteBuf.writeVarInt(value: Int) {
    OmniPackets.writeVarInt(this, value)
}

public fun ByteBuf.readVarInt(): Int {
    return OmniPackets.readVarInt(this)
}

public fun ByteBuf.writeString(string: String) {
    OmniPackets.writeString(this, string)
}

public fun ByteBuf.readString(): String {
    return OmniPackets.readString(this)
}

public fun <T> ByteBuf.writeCollection(collection: Collection<T>, writer: (ByteBuf, T) -> Unit) {
    OmniPackets.writeCollection(this, collection, writer)
}

public fun <T> ByteBuf.readCollection(reader: (ByteBuf) -> T): List<T> {
    return OmniPackets.readCollection(this, reader)
}
