package dev.deftu.omnicore.client

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket
import net.minecraft.util.Identifier
import java.util.function.Consumer
import java.util.function.Predicate

//#if MC >= 1.20.4
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadImpl
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.common.OmniCustomForgeMessage
//$$ import dev.deftu.omnicore.common.OmniPackets
//#endif

public object OmniClientPackets {

    private val packetReceivers = mutableMapOf<Identifier, MutableList<Predicate<ByteBuf>>>()

    @JvmStatic
    public fun sendVanilla(id: Identifier, consumer: Consumer<ByteBuf>) {
        val networkHandler = OmniClient.networkHandler ?: return

        val buf = Unpooled.buffer()
        consumer.accept(buf)

        val packet = CustomPayloadC2SPacket(
            //#if MC <= 1.16.5
            //#if MC > 1.12.2
            //$$ id,
            //#else
            //$$ id.toString(),
            //#endif
            //#endif
            //#if MC >= 1.20.4
            //$$ OmniCustomPayloadImpl(id, consumer).also { it.write(buf) }
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

    public fun sendVanilla(id: Identifier, block: ByteBuf.() -> Unit) {
        sendVanilla(id) { buf ->
            block(buf)
        }
    }

    public fun send(id: Identifier, consumer: Consumer<ByteBuf>) {
        //#if FORGE && MC <= 1.12.2
        //$$ val channel = OmniPackets.getChannel(id)
        //$$ val buf = Unpooled.buffer()
        //$$ consumer.accept(buf)
        //$$ channel.sendToServer(OmniCustomForgeMessage(buf))
        //#else
        sendVanilla(id, consumer)
        //#endif
    }

    public fun send(id: Identifier, block: ByteBuf.() -> Unit) {
        send(id) { buf ->
            block(buf)
        }
    }

    public fun createPacketReceiver(id: Identifier, receiver: Predicate<ByteBuf>): Runnable {
        val list = packetReceivers.getOrPut(id) { mutableListOf() }
        list.add(receiver)

        return Runnable {
            list.remove(receiver)
        }
    }

    public fun createPacketReceiver(id: Identifier, block: ByteBuf.() -> Boolean): () -> Unit {
        val fn = createPacketReceiver(id) { buf -> block(buf) }
        return { fn.run() }
    }

    @JvmStatic
    internal fun getAllPacketReceivers(id: Identifier): List<Predicate<ByteBuf>> {
        return packetReceivers[id] ?: return emptyList()
    }

}
