package dev.deftu.omnicore.internal.client.networking

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.internal.networking.VanillaCustomPayload
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object ClientNetworkingInternals {
    private val logger = LogManager.getLogger(ClientNetworkingInternals::class.java)

    //#if FORGE && MC <= 1.12.2
    //$$ public var isInitialized = false
    //$$     private set
    //#endif

    @JvmStatic
    public fun send(id: Identifier, buf: PacketByteBuf) {
        val networkHandler = client.networkHandler
        if (networkHandler == null) {
            logger.warn("Attempted to send packet while network handler is null.")
            return
        }

        //#if FORGE && MC <= 1.12.2
        //$$ setupForgeListener()
        //#endif

        val packet = CustomPayloadC2SPacket(
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

        //#if MC >= 1.20.6 && FORGE-LIKE
        //$$ networkHandler.send(packet)
        //#else
        networkHandler.sendPacket(packet)
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @JvmStatic
    //$$ public fun setupForgeListener() {
    //$$     if (isInitialized) return
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
    //$$         OmniClientPacketHandler { buf ->
    //$$             val id = buf.readIdentifier()
    //$$             OmniClientNetworking.handle(id, buf, networkManager)
    //$$         }
    //$$     )
    //$$ }
    //#endif
}
