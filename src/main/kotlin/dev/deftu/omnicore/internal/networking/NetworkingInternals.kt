package dev.deftu.omnicore.internal.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.fml.common.gameevent.PlayerEvent
//#endif

@ApiStatus.Internal
public object NetworkingInternals {
    //#if FORGE && MC <= 1.12.2
    //$$ private var isInitialized = false
    //$$
    //$$ @JvmStatic
    //$$ public fun initialize() {
    //$$     if (isInitialized) {
    //$$         return
    //$$     }
    //$$
    //$$     forgeEventBus.register(this)
    //$$ }
    //#endif

    @JvmStatic
    public fun send(
        player: ServerPlayerEntity,
        id: Identifier,
        buf: PacketByteBuf
    ) {
        val packet = CustomPayloadS2CPacket(
            //#if MC >= 1.20.4
            VanillaCustomPayload(id, buf),
            //#elseif MC == 1.16.5
            //$$ id,
            //#elseif MC <= 1.12.2
            //$$ id.toString(),
            //#else
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

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onPlayerJoinedServer(event: PlayerEvent.PlayerLoggedInEvent) {
    //$$     val player = event.player as? EntityPlayerMP ?: return
    //$$     val networkManager =
    //#if MC >= 1.12.2
    //$$         player.connection.networkManager
    //#else
    //$$         player.playerNetServerHandler.netManager
    //#endif
    //$$     setupCustomPacketHandler(
    //$$         networkManager = networkManager,
    //$$         handler = OmniServerPacketHandler { buf ->
    //$$             val id = buf.readIdentifier()
    //$$             OmniNetwork.handle(id, buf, player)
    //$$         }
    //$$     )
    //$$ }
    //$$
    //$$ @JvmStatic
    //$$ public fun setupCustomPacketHandler(
    //$$     networkManager: NetworkManager,
    //$$     packetHandler: OmniPacketHandler
    //$$ ) {
    //$$     val pipeline = networkManager.channel().pipeline()
    //$$     if (pipeline.any { (_, handler) -> handler is OmniPacketHandler })
    //$$         return
    //$$     }
    //$$
    //$$     pipeline.addFirst(packetHandler)
    //$$ }
    //#endif

}
