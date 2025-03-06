package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
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
//$$ import dev.deftu.omnicore.common.OmniPackets
//$$ import net.minecraftforge.common.MinecraftForge
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.fml.common.network.FMLNetworkEvent
//#endif

public object OmniClientPackets {

    //#if FORGE && MC <= 1.12.2
    //$$ private var isInitialized = false
    //#endif

    private val channeledPacketReceivers = mutableMapOf<Identifier, MutableList<Predicate<ByteBuf>>>()
    private val globalPacketReceivers = mutableListOf<Predicate<ByteBuf>>()

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun send(id: Identifier, consumer: Consumer<ByteBuf>) {
        val networkHandler = OmniClient.networkHandler ?: return

        //#if FORGE && MC <= 1.12.2
        //$$ setupForgeListener()
        //#endif

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

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun send(id: Identifier, block: ByteBuf.() -> Unit) {
        send(id) { buf ->
            block(buf)
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun createChanneledPacketReceiver(id: Identifier, receiver: Predicate<ByteBuf>): Runnable {
        val list = channeledPacketReceivers.getOrPut(id) { mutableListOf() }
        list.add(receiver)

        //#if FORGE && MC <= 1.12.2
        //$$ setupForgeListener()
        //#endif

        return Runnable {
            list.remove(receiver)
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun createChanneledPacketReceiver(id: Identifier, block: ByteBuf.() -> Boolean): () -> Unit {
        val fn = createChanneledPacketReceiver(id) { buf -> block(buf) }
        return { fn.run() }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun createGlobalPacketReceiver(receiver: Predicate<ByteBuf>): Runnable {
        globalPacketReceivers.add(receiver)

        //#if FORGE && MC <= 1.12.2
        //$$ setupForgeListener()
        //#endif

        return Runnable {
            globalPacketReceivers.remove(receiver)
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun createGlobalPacketReceiver(block: ByteBuf.() -> Boolean): () -> Unit {
        val fn = createGlobalPacketReceiver { buf -> block(buf) }
        return { fn.run() }
    }

    //#if FORGE && MC <= 1.12.2
    //$$ private fun setupForgeListener() {
    //$$     if (isInitialized) {
    //$$         return
    //$$     }
    //$$
    //$$     MinecraftForge.EVENT_BUS.register(this)
    //$$     isInitialized = true
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ public fun onClientConnectedToServer(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
    //$$     val networkManager = event.manager
    //$$     OmniPackets.setupCustomPacketHandler(
    //$$         networkManager,
    //$$         OmniClientPacketHandler(
    //$$             channeledPacketReceiverProvider = { channeledPacketReceivers },
    //$$             globalPacketReceiverProvider = { globalPacketReceivers }
    //$$         )
    //$$     )
    //$$ }
    //#endif

    @JvmStatic
    internal fun getAllPacketReceivers(id: Identifier): List<Predicate<ByteBuf>> {
        return (channeledPacketReceivers[id] ?: return emptyList()) + globalPacketReceivers
    }

}
