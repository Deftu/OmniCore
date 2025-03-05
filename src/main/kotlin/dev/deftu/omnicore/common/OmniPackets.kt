package dev.deftu.omnicore.common

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import io.netty.buffer.ByteBuf
import java.util.function.BiConsumer
import java.util.function.Function
import kotlin.experimental.and

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

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun writeVarInt(buf: ByteBuf, value: Int): ByteBuf {
        var currentValue = value
        while (currentValue and -128 != 0) {
            buf.writeByte(currentValue and 127 or 128)
            currentValue = currentValue ushr 7
        }

        buf.writeByte(currentValue)
        return buf
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun readVarInt(buf: ByteBuf): Int {
        var result = 0
        var shift = 0
        var byteValue: Byte

        do {
            byteValue = buf.readByte()
            result = result or ((byteValue.toInt() and 127) shl shift++ * 7)
            if (shift > 5) {
                throw RuntimeException("VarInt too big")
            }
        } while (shouldContinueReadingVarInt(byteValue))

        return result
    }

    private fun shouldContinueReadingVarInt(byte: Byte): Boolean {
        return (byte and 128.toByte()) == 128.toByte()
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun writeString(buf: ByteBuf, string: String): ByteBuf {
        val bytes = string.toByteArray(Charsets.UTF_8)
        if (bytes.size > Short.MAX_VALUE) {
            throw IllegalArgumentException("String too big (was ${bytes.size} bytes, max ${Short.MAX_VALUE})")
        }

        buf.writeVarInt(bytes.size)
        buf.writeBytes(bytes)
        return buf
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun readString(buf: ByteBuf): String {
        val length = buf.readVarInt()
        val bytes = ByteArray(length)
        buf.readBytes(bytes)
        return String(bytes, Charsets.UTF_8)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun <T> writeCollection(buf: ByteBuf, collection: Collection<T>, writer: BiConsumer<ByteBuf, T>): ByteBuf {
        buf.writeVarInt(collection.size)
        for (item in collection) {
            writer.accept(buf, item)
        }

        return buf
    }

    @JvmStatic
    @GameSide(Side.BOTH)
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

@GameSide(Side.BOTH)
public fun ByteBuf.writeVarInt(value: Int) {
    OmniPackets.writeVarInt(this, value)
}

@GameSide(Side.BOTH)
public fun ByteBuf.readVarInt(): Int {
    return OmniPackets.readVarInt(this)
}

@GameSide(Side.BOTH)
public fun ByteBuf.writeString(string: String) {
    OmniPackets.writeString(this, string)
}

@GameSide(Side.BOTH)
public fun ByteBuf.readString(): String {
    return OmniPackets.readString(this)
}

@GameSide(Side.BOTH)
public fun <T> ByteBuf.writeCollection(collection: Collection<T>, writer: (ByteBuf, T) -> Unit) {
    OmniPackets.writeCollection(this, collection, writer)
}

@GameSide(Side.BOTH)
public fun <T> ByteBuf.readCollection(reader: (ByteBuf) -> T): List<T> {
    return OmniPackets.readCollection(this, reader)
}
