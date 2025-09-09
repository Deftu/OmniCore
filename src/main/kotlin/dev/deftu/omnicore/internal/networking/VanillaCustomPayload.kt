package dev.deftu.omnicore.internal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class VanillaCustomPayload(public val id: Identifier, public val data: PacketByteBuf) : CustomPayload {
    public companion object {
        @JvmField
        public val CODEC: PacketCodec<PacketByteBuf, VanillaCustomPayload> =
            CustomPayload.codecOf(VanillaCustomPayload::write) { buf ->
                val id = buf.readIdentifier()
                VanillaCustomPayload(id, buf)
            }
    }

    //#if MC >= 1.20.6
    private
    //#else
    //$$ override
    //#endif
    fun write(buf: PacketByteBuf) {
        // write our id first
        this.data.writeIdentifier(this.id)

        // copy the data from our buf to the provided buf
        val bytes = ByteArray(this.data.readableBytes())
        this.data.readBytes(bytes)
        buf.writeBytes(bytes)
    }

    //#if MC >= 1.20.6
    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return CustomPayload.Id(id)
    }
    //#else
    //$$ override fun comp_1678(): Identifier {
    //$$     return id
    //$$ }
    //#endif
}
