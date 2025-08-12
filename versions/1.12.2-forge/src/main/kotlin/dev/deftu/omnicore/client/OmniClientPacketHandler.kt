package dev.deftu.omnicore.client

//#if FORGE
import dev.deftu.omnicore.common.OmniIdentifier
import dev.deftu.omnicore.common.OmniPacketHandler
import dev.deftu.omnicore.common.OmniPacketReceiverContext
import io.netty.channel.ChannelHandlerContext
import java.util.function.Predicate
import net.minecraft.network.Packet
import net.minecraft.util.ResourceLocation

//#if MC >= 1.12.2
import net.minecraft.network.play.server.SPacketCustomPayload
//#else
//$$ import net.minecraft.network.play.server.S3FPacketCustomPayload
//#endif

public class OmniClientPacketHandler(
    private val channeledPacketReceiverProvider: () -> Map<ResourceLocation, Set<Predicate<OmniPacketReceiverContext>>>,
    private val globalPacketReceiverProvider: () -> Set<Predicate<OmniPacketReceiverContext>>,
) : OmniPacketHandler() {
    override fun handleRead(ctx: ChannelHandlerContext, packet: Packet<*>?): Boolean {
        val channeledPacketReceivers = channeledPacketReceiverProvider()
        val globalPacketReceivers = globalPacketReceiverProvider()

        //#if MC >= 1.12.2
        val customPayloadPacket = packet as? SPacketCustomPayload ?: return false
        //#else
        //$$ val customPayloadPacket = packet as? S3FPacketCustomPayload ?: return false
        //#endif
        val channelName = customPayloadPacket.channelName
        val buf = customPayloadPacket.bufferData

        val channel = OmniIdentifier.create(channelName)
        val channeledReceivers = channeledPacketReceivers[channel] ?: emptyList()
        val receivers = channeledReceivers + globalPacketReceivers
        val context = OmniPacketReceiverContext(channel, buf)
        for (receiver in receivers) {
            if (receiver.test(context)) {
                return true
            }
        }

        return false
    }
}
//#endif
