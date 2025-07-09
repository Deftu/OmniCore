package dev.deftu.omnicore.server

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.common.OmniPacketReceiverContext
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet
import java.util.function.BiPredicate
import java.util.function.Consumer

//#if MC >= 1.20.4
import dev.deftu.omnicore.common.OmniCustomPayloadImpl
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.common.OmniPackets
//$$ import net.minecraftforge.common.MinecraftForge
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.fml.common.gameevent.PlayerEvent
//#endif

public object OmniServerPackets {

    //#if FORGE && MC <= 1.12.2
    //$$ private var isInitialized = false
    //#endif

    private val channeledPacketReceivers = ConcurrentHashMap<Identifier, CopyOnWriteArraySet<BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext>>>()
    private val globalPacketReceivers = CopyOnWriteArraySet<BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext>>()

    @JvmStatic
    @GameSide(Side.SERVER)
    public fun send(player: ServerPlayerEntity, id: Identifier, consumer: Consumer<ByteBuf>) {
        val networkHandler = player.networkHandler ?: return

        //#if FORGE && MC <= 1.12.2
        //$$ OmniPackets.setupCustomPacketHandler(
        //$$     networkHandler.networkManager,
        //$$     OmniServerPacketHandler(
        //$$         channeledPacketReceiverProvider = { channeledPacketReceivers },
        //$$         globalPacketReceiverProvider = { globalPacketReceivers },
        //$$         player = player
        //$$     )
        //$$ )
        //#endif

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
            OmniCustomPayloadImpl(id, consumer).also { it.write(buf) }
            //#else
            //$$ FriendlyByteBuf(buf)
            //#endif
        )

        //#if MC >= 1.20.6 && FORGE-LIKE
        //$$ networkHandler.send(packet)
        //#else
        networkHandler.sendPacket(packet)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.SERVER)
    public fun send(player: ServerPlayerEntity, id: Identifier, block: ByteBuf.() -> Unit) {
        send(player, id) { buf ->
            block(buf)
        }
    }

    @JvmStatic
    @GameSide(Side.SERVER)
    public fun createChanneledPacketReceiver(id: Identifier, block: (ServerPlayerEntity, OmniPacketReceiverContext) -> Boolean): () -> Unit {
        val list = channeledPacketReceivers.getOrPut(id) { CopyOnWriteArraySet() }
        val receiver = BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext> { player, buf -> block(player, buf) }
        list.add(receiver)

        //#if FORGE && MC <= 1.12.2
        //$$ setupForgeListener()
        //#endif

        return {
            list.remove(receiver)
        }
    }

    @JvmStatic
    @GameSide(Side.SERVER)
    public fun createGlobalPacketReceiver(block: (ServerPlayerEntity, OmniPacketReceiverContext) -> Boolean): () -> Unit {
        val receiver = BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext> { player, buf -> block(player, buf) }
        globalPacketReceivers.add(receiver)

        //#if FORGE && MC <= 1.12.2
        //$$ setupForgeListener()
        //#endif

        return {
            globalPacketReceivers.remove(receiver)
        }
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
    //$$ public fun onPlayerJoinedServer(event: PlayerEvent.PlayerLoggedInEvent) {
    //$$     val player = event.player as? EntityPlayerMP ?: return
    //$$     OmniPackets.setupCustomPacketHandler(
    //#if MC >= 1.12.2
    //$$         player.connection.networkManager,
    //#else
    //$$         player.playerNetServerHandler.networkManager,
    //#endif
    //$$         OmniServerPacketHandler(
    //$$             channeledPacketReceiverProvider = { channeledPacketReceivers },
    //$$             globalPacketReceiverProvider = { globalPacketReceivers },
    //$$             player = player
    //$$         )
    //$$     )
    //$$ }
    //#endif

    @JvmStatic
    internal fun getAllPacketReceivers(id: Identifier): Set<BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext>> {
        val channeled = channeledPacketReceivers[id] ?: emptySet()
        return channeled + globalPacketReceivers
    }

}
