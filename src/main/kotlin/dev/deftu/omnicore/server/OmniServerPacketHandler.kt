package dev.deftu.omnicore.server

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//$$ import dev.deftu.omnicore.common.OmniPacketHandler
//$$ import io.netty.buffer.ByteBuf
//$$ import io.netty.channel.ChannelHandlerContext
//$$ import net.minecraft.entity.player.EntityPlayerMP
//$$ import java.util.function.BiPredicate
//$$ import net.minecraft.network.Packet
//$$ import net.minecraft.util.ResourceLocation
//$$
//#if MC >= 1.12.2
//$$ import net.minecraft.network.play.client.CPacketCustomPayload
//#else
//$$ import net.minecraft.network.play.client.C17PacketCustomPayload
//#endif
//$$
//$$ public class OmniServerPacketHandler(
//$$     private val channeledPacketReceiverProvider: () -> Map<ResourceLocation, List<BiPredicate<EntityPlayerMP, ByteBuf>>>,
//$$     private val globalPacketReceiverProvider: () -> List<BiPredicate<EntityPlayerMP, ByteBuf>>,
//$$     private val player: EntityPlayerMP
//$$ ) : OmniPacketHandler() {
//$$
//$$     override fun handleRead(ctx: ChannelHandlerContext, packet: Packet<*>?): Boolean {
//$$         val channeledPacketReceivers = channeledPacketReceiverProvider()
//$$         val globalPacketReceivers = globalPacketReceiverProvider()
//$$
//#if MC >= 1.12.2
//$$         val customPayloadPacket = packet as? CPacketCustomPayload ?: return false
//#else
//$$         val customPayloadPacket = packet as? C17PacketCustomPayload ?: return false
//#endif
//$$         val channelName = customPayloadPacket.channelName
//$$         val buf = customPayloadPacket.bufferData
//$$
//$$         val channel = OmniIdentifier.create(channelName)
//$$         val channeledReceivers = channeledPacketReceivers[channel] ?: emptyList()
//$$         val receivers = channeledReceivers + globalPacketReceivers
//$$         for (receiver in channeledReceivers) {
//$$             if (receiver.test(player, buf)) {
//$$                 return true
//$$             }
//$$         }
//$$
//$$         return false
//$$     }
//$$
//$$ }
//#endif
