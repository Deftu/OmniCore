package dev.deftu.omnicore.internal.client.networking

//#if FORGE
import dev.deftu.omnicore.api.client.OmniClientRuntime
import dev.deftu.omnicore.api.client.network.OmniClientNetworking
import dev.deftu.omnicore.api.locationOrNull
import dev.deftu.omnicore.internal.networking.OmniPacketHandler
import io.netty.channel.ChannelHandlerContext
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.PacketBuffer
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.12.2
import net.minecraft.network.play.server.SPacketCustomPayload
//#else
//$$ import net.minecraft.network.play.server.S3FPacketCustomPayload
//#endif

@ApiStatus.Internal
public class OmniClientPacketHandler(private val networkManager: NetworkManager) : OmniPacketHandler() {
    override fun read(ctx: ChannelHandlerContext, packet: Packet<*>?) {
        //#if MC >= 1.12.2
        val customPayloadPacket = packet as? SPacketCustomPayload ?: return
        //#else
        //$$ val customPayloadPacket = packet as? S3FPacketCustomPayload ?: return
        //#endif
        val channelName = customPayloadPacket.channelName
        val channel = locationOrNull(channelName) ?: return
        if (!OmniClientNetworking.isChannelRegistered(channel)) {
            return
        }

        val packetData = customPayloadPacket.bufferData
        val buf = PacketBuffer(packetData.copy())
        buf.readerIndex(0)

        OmniClientRuntime.runOnMain {
            OmniClientNetworking.handle(channel, buf, networkManager)
        }
    }
}
//#endif
