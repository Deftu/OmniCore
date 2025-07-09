package dev.deftu.omnicore.common

//#if MC >= 1.20.4
import io.netty.buffer.ByteBuf
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.util.function.Consumer

internal class OmniCustomPayloadImpl(
    override val channel: Identifier,
    val consumer: Consumer<ByteBuf>
) : OmniCustomPayload, OmniCustomPayloadDataHolder, CustomPayload {

    private var data: ByteBuf? = null

//#if MC >= 1.20.6
    override fun getId(): CustomPayload.Id<out CustomPayload> {
        return CustomPayload.Id(channel)
    }
//#else
//$$     override fun comp_1678(): Identifier {
//$$         return channel
//$$     }
//#endif

//#if MC <= 1.20.4
//$$     override fun write(buf: PacketByteBuf) {
//$$         this.data = buf.copy()
//$$         consumer.accept(buf)
//$$     }
//#endif

    override fun write(buf: ByteBuf) {
        this.data = buf.copy()
        consumer.accept(buf)
    }

    override fun `omnicore$getData`(): PacketByteBuf? {
        return data?.let { PacketByteBuf(it) }
    }

    override fun `omnicore$setData`(data: PacketByteBuf) {
        this.data = data
    }

}
//#endif
