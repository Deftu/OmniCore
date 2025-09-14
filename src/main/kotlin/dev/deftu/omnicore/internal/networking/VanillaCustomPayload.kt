package dev.deftu.omnicore.internal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.20.6
import net.minecraft.network.codec.PacketCodec
//#endif

@ApiStatus.Internal
public class VanillaCustomPayload(public val identifier: Identifier, public val data: PacketByteBuf) : CustomPayload {
    //#if MC >= 1.20.6
    public companion object {
        @JvmField
        public val CODEC: PacketCodec<PacketByteBuf, VanillaCustomPayload> =
            CustomPayload.codecOf(VanillaCustomPayload::write) { buf ->
                val id = buf.readIdentifier()
                VanillaCustomPayload(id, buf)
            }
    }
    //#endif

    //#if MC >= 1.20.6
    internal
    //#else
    //$$ override
    //#endif
    fun write(buf: PacketByteBuf) {
        println("Writing packet with id $identifier and ${data.readableBytes()} bytes of data")

        // write our id first
        this.data.writeIdentifier(this.identifier)

        // copy the data from our buf to the provided buf
        val bytes = ByteArray(this.data.readableBytes())
        this.data.readBytes(bytes)
        buf.writeBytes(bytes)
    }

    //#if MC >= 1.20.6
    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return CustomPayload.Id(identifier)
    }
    //#else
    //$$ override fun comp_1678(): Identifier {
    //$$     return id
    //$$ }
    //#endif
}
