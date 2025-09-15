package dev.deftu.omnicore.internal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.api.network.OmniNetworking
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//$$ import net.minecraft.network.NetworkManager
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.fml.common.gameevent.PlayerEvent
//#endif

//#if MC <= 1.8.9
//$$ import dev.deftu.omnicore.api.identifierOrThrow
//#endif

@ApiStatus.Internal
public object NetworkingInternals {
    private var isInitialized = false

    @JvmStatic
    public fun initialize() {
        if (isInitialized) {
            return
        }

        //#if MC <= 1.12.2 && FORGE
        //$$ forgeEventBus.register(this)
        //#endif

        isInitialized = true
    }

    @JvmStatic
    public fun send(
        player: ServerPlayerEntity,
        id: Identifier,
        buf: PacketByteBuf
    ) {
        //#if MC <= 1.12.2
        //$$ initialize()
        //#endif

        val packet = CustomPayloadS2CPacket(
            //#if MC >= 1.20.4
            VanillaCustomPayload(id, buf),
            //#elseif MC >= 1.16.5
            //$$ id,
            //$$ buf,
            //#else
            //$$ id.toString(),
            //$$ buf,
            //#endif
        )

        val networkHandler = player.networkHandler
        //#if MC >= 1.20.6 && FORGE-LIKE
        //$$ networkHandler.send(packet)
        //#else
        networkHandler.sendPacket(packet)
        //#endif
    }

    @JvmStatic
    public fun readIdentifier(buf: PacketByteBuf): Identifier {
        //#if MC >= 1.12.2
        return buf.readIdentifier()
        //#else
        //$$ return identifierOrThrow(buf.readString(Short.MAX_VALUE.toInt()))
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onPlayerJoinedServer(event: PlayerEvent.PlayerLoggedInEvent) {
    //$$     val player = event.player as? EntityPlayerMP ?: return
    //$$     val networkManager =
            //#if MC >= 1.12.2
            //$$ player.connection.networkManager
            //#else
            //$$ player.playerNetServerHandler.netManager
            //#endif
    //$$     setupCustomPacketHandler(
    //$$         networkManager = networkManager,
    //$$         packetHandler = OmniServerPacketHandler(player)
    //$$     )
    //$$ }
    //$$
    //$$ @JvmStatic
    //$$ public fun setupCustomPacketHandler(
    //$$     networkManager: NetworkManager,
    //$$     packetHandler: OmniPacketHandler
    //$$ ) {
    //$$     val pipeline = networkManager.channel().pipeline()
    //$$     if (pipeline.any { (_, handler) -> handler is OmniPacketHandler }) {
    //$$         return
    //$$     }
    //$$
    //$$     pipeline.addFirst(packetHandler)
    //$$ }
    //#endif

}
