package dev.deftu.omnicore.internal.networking

//#if FORGE
import dev.deftu.omnicore.api.locationOrNull
import dev.deftu.omnicore.api.network.OmniNetworking
import io.netty.channel.ChannelHandlerContext
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.Packet
import net.minecraft.network.PacketBuffer

//#if MC >= 1.12.2
import net.minecraft.network.play.client.CPacketCustomPayload
//#else
//$$ import net.minecraft.network.play.client.C17PacketCustomPayload
//#endif

public class OmniServerPacketHandler(private val player: EntityPlayerMP) : OmniPacketHandler() {
    override fun read(ctx: ChannelHandlerContext, packet: Packet<*>?) {
        //#if MC >= 1.12.2
        val customPayloadPacket = packet as? CPacketCustomPayload ?: return
        //#else
        //$$ val customPayloadPacket = packet as? C17PacketCustomPayload ?: return
        //#endif
        val channelName = customPayloadPacket.channelName
        val channel = locationOrNull(channelName) ?: return
        if (!OmniNetworking.isRegisteredChannel(channel)) {
            return
        }

        val packetData = customPayloadPacket.bufferData
        val buf = PacketBuffer(packetData.copy())
        buf.readerIndex(0)

        //#if MC >= 1.12.2
        player.server.callFromMainThread {
        //#else
        //$$ player.serverForPlayer.minecraftServer.callFromMainThread {
        //#endif
            OmniNetworking.handle(channel, buf, player)
        }
    }
}
//#endif
