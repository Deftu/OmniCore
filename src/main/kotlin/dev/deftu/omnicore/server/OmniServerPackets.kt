package dev.deftu.omnicore.server

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.function.Consumer

//#if MC >= 1.20.6
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadImpl
//#endif

public object OmniServerPackets {

    public fun send(player: ServerPlayerEntity, id: Identifier, consumer: Consumer<ByteBuf>) {
        val networkHandler = player.networkHandler ?: return

        val buf = Unpooled.buffer()
        consumer.accept(buf)

        val packet = CustomPayloadS2CPacket(
            //#if MC <= 1.16.5
            //#if MC > 1.12.2
            //$$ id,
            //#else
            //$$ id.toString(),
            //#endif
            //#endif
            //#if MC >= 1.20.6
            //$$ OmniCustomPayloadImpl(id, consumer)
            //#else
            PacketByteBuf(buf)
            //#endif
        )

        //#if MC >= 1.20.6 && FORGE-LIKE
        //$$ networkHandler.send(packet)
        //#else
        networkHandler.sendPacket(packet)
        //#endif
    }

    public fun send(player: ServerPlayerEntity, id: Identifier, block: ByteBuf.() -> Unit) {
        send(player, id) { buf ->
            block(buf)
        }
    }

}
