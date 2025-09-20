package dev.deftu.omnicore.internal.client.networking

import dev.deftu.omnicore.api.client.client
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.20.4
import dev.deftu.omnicore.internal.networking.VanillaCustomPayload
//#endif

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.api.client.network.OmniClientNetworking
//$$ import dev.deftu.omnicore.internal.forgeEventBus
//$$ import dev.deftu.omnicore.internal.networking.NetworkingInternals
//$$ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//$$ import net.minecraftforge.fml.common.network.FMLNetworkEvent
//#endif

@ApiStatus.Internal
public object ClientNetworkingInternals {
    private val logger = LogManager.getLogger(ClientNetworkingInternals::class.java)
    private var isInitialized: Boolean = false

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
    public fun send(id: Identifier, buf: PacketByteBuf) {
        val networkHandler = client.networkHandler
        if (networkHandler == null) {
            logger.warn("Attempted to send packet while network handler is null.")
            return
        }

        //#if FORGE && MC <= 1.12.2
        //$$ initialize()
        //#endif

        val packet = CustomPayloadC2SPacket(
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

        //#if MC >= 1.20.6 && FORGE-LIKE
        //$$ networkHandler.send(packet)
        //#else
        networkHandler.sendPacket(packet)
        //#endif
    }

    //#if FORGE && MC <= 1.12.2
    //$$ @SubscribeEvent
    //$$ public fun onClientConnectedToServer(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
    //$$     val networkManager = event.manager
    //$$     NetworkingInternals.setupCustomPacketHandler(
    //$$         networkManager,
    //$$         OmniClientPacketHandler(networkManager)
    //$$     )
    //$$ }
    //#endif
}
