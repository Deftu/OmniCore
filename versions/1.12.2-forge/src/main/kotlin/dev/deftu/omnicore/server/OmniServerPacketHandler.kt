package dev.deftu.omnicore.server

//#if FORGE
import dev.deftu.omnicore.common.OmniIdentifier
import dev.deftu.omnicore.common.OmniPacketHandler
import dev.deftu.omnicore.common.OmniPacketReceiverContext
import io.netty.channel.ChannelHandlerContext
import net.minecraft.entity.player.EntityPlayerMP
import java.util.function.BiPredicate
import net.minecraft.network.Packet
import net.minecraft.util.ResourceLocation

//#if MC >= 1.12.2
import net.minecraft.network.play.client.CPacketCustomPayload
//#else
//$$ import net.minecraft.network.play.client.C17PacketCustomPayload
//#endif

public class OmniServerPacketHandler(
    private val channeledPacketReceiverProvider: () -> Map<ResourceLocation, Set<BiPredicate<EntityPlayerMP, OmniPacketReceiverContext>>>,
    private val globalPacketReceiverProvider: () -> Set<BiPredicate<EntityPlayerMP, OmniPacketReceiverContext>>,
    private val player: EntityPlayerMP
) : OmniPacketHandler() {
    override fun handleRead(ctx: ChannelHandlerContext, packet: Packet<*>?): Boolean {
        val channeledPacketReceivers = channeledPacketReceiverProvider()
        val globalPacketReceivers = globalPacketReceiverProvider()

        //#if MC >= 1.12.2
        val customPayloadPacket = packet as? CPacketCustomPayload ?: return false
        //#else
        //$$ val customPayloadPacket = packet as? C17PacketCustomPayload ?: return false
        //#endif
        val channelName = customPayloadPacket.channelName
        val buf = customPayloadPacket.bufferData

        val channel = OmniIdentifier.create(channelName)
        val channeledReceivers = channeledPacketReceivers[channel] ?: emptyList()
        val receivers = channeledReceivers + globalPacketReceivers
        val context = OmniPacketReceiverContext(channel, buf)
        for (receiver in receivers) {
            if (receiver.test(player, context)) {
                return true
            }
        }

        return false
    }
}
//#endif
