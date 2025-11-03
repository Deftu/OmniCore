package dev.deftu.omnicore.internal.networking

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import org.jetbrains.annotations.ApiStatus

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.api.network.OmniNetworking
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//$$ import net.minecraft.network.NetworkManager
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.fml.common.gameevent.PlayerEvent
//#endif

//#if MC <= 1.8.9
//$$ import dev.deftu.omnicore.api.locationOrThrow
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
        player: ServerPlayer,
        id: ResourceLocation,
        buf: FriendlyByteBuf
    ) {
        //#if MC <= 1.12.2
        //$$ initialize()
        //#endif

        val packet = ClientboundCustomPayloadPacket(
            //#if MC >= 1.20.4
            VanillaCustomPacketPayload(id, buf),
            //#elseif MC >= 1.16.5
            //$$ id,
            //$$ buf,
            //#else
            //$$ id.toString(),
            //$$ buf,
            //#endif
        )

        player.connection.send(packet)
    }

    @JvmStatic
    public fun readResourceLocation(buf: FriendlyByteBuf): ResourceLocation {
        //#if MC >= 1.12.2
        return buf.readResourceLocation()
        //#else
        //$$ return locationOrThrow(buf.readStringFromBuffer(Short.MAX_VALUE.toInt()))
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
