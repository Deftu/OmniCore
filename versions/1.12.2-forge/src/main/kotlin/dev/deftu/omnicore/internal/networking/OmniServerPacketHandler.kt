package dev.deftu.omnicore.internal.networking

//#if FORGE
import io.netty.channel.ChannelHandlerContext
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.Packet
import net.minecraft.network.PacketBuffer

//#if MC >= 1.12.2
import net.minecraft.network.play.client.CPacketCustomPayload
//#else
//$$ import net.minecraft.network.play.client.C17PacketCustomPayload
//#endif

public class OmniServerPacketHandler(
    private val player: EntityPlayerMP,
    private val handler: (PacketBuffer) -> Unit,
) : OmniPacketHandler() {
    override fun read(ctx: ChannelHandlerContext, packet: Packet<*>?) {
        //#if MC >= 1.12.2
        val customPayloadPacket = packet as? CPacketCustomPayload ?: return
        //#else
        //$$ val customPayloadPacket = packet as? C17PacketCustomPayload ?: return
        //#endif
        val channelName = customPayloadPacket.channelName
        val buf = customPayloadPacket.bufferData

    }
}
//#endif
