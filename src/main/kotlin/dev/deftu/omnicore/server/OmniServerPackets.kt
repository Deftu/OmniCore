package dev.deftu.omnicore.server

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.function.BiPredicate
import java.util.function.Consumer

//#if MC >= 1.20.4
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadImpl
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.common.OmniCustomForgeMessage
//$$ import dev.deftu.omnicore.common.OmniPackets
//#endif

public object OmniServerPackets {

    private val packetReceivers = mutableMapOf<Identifier, MutableList<BiPredicate<ServerPlayerEntity, ByteBuf>>>()

    @JvmStatic
    public fun sendVanilla(player: ServerPlayerEntity, id: Identifier, consumer: Consumer<ByteBuf>) {
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
            //#if MC >= 1.20.4
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

    @JvmStatic
    public fun sendVanilla(player: ServerPlayerEntity, id: Identifier, block: ByteBuf.() -> Unit) {
        sendVanilla(player, id) { buf ->
            block(buf)
        }
    }

    @JvmStatic
    public fun send(player: ServerPlayerEntity, id: Identifier, consumer: Consumer<ByteBuf>) {
        //#if FORGE && MC <= 1.12.2
        //$$ val channel = OmniPackets.getChannel(id)
        //$$ val buf = Unpooled.buffer()
        //$$ consumer.accept(buf)
        //$$ channel.sendTo(OmniCustomForgeMessage(buf), player)
        //#else
        sendVanilla(player, id, consumer)
        //#endif
    }

    @JvmStatic
    public fun send(player: ServerPlayerEntity, id: Identifier, block: ByteBuf.() -> Unit) {
        send(player, id) { buf ->
            block(buf)
        }
    }

    public fun createPacketReceiver(id: Identifier, receiver: BiPredicate<ServerPlayerEntity, ByteBuf>): Runnable {
        val list = packetReceivers.getOrPut(id) { mutableListOf() }
        list.add(receiver)

        return Runnable {
            list.remove(receiver)
        }
    }

    public fun createPacketReceiver(id: Identifier, block: (ServerPlayerEntity, ByteBuf) -> Boolean): () -> Unit {
        val fn = createPacketReceiver(id, receiver = { player, buf -> block(player, buf) })
        return { fn.run() }
    }

    @JvmStatic
    internal fun getAllPacketReceivers(id: Identifier): List<BiPredicate<ServerPlayerEntity, ByteBuf>> {
        return packetReceivers[id] ?: return emptyList()
    }

}
