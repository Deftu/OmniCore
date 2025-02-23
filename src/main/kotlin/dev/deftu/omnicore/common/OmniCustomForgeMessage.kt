package dev.deftu.omnicore.common

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.client.OmniClientPackets
//$$ import dev.deftu.omnicore.server.OmniServerPackets
//$$ import net.minecraftforge.fml.common.network.simpleimpl.IMessage
//$$ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
//$$ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
//$$ import net.minecraft.util.ResourceLocation
//$$ import io.netty.buffer.ByteBuf
//$$ import net.minecraftforge.fml.relauncher.Side
//$$
//$$ internal class OmniCustomForgeMessage(internal var data: ByteBuf? = null) : IMessage {
//$$
//$$     /**
//$$      * Receive
//$$      */
//$$     override fun fromBytes(buf: ByteBuf?) {
//$$         this.data = buf
//$$     }
//$$
//$$     /**
//$$      * Send
//$$      */
//$$     override fun toBytes(buf: ByteBuf?) {
//$$         buf?.writeBytes(data)
//$$     }
//$$
//$$     internal class Handler(private val id: ResourceLocation) : IMessageHandler<OmniCustomForgeMessage, IMessage> {
//$$
//$$         override fun onMessage(packet: OmniCustomForgeMessage, ctx: MessageContext): IMessage? {
//$$             val data = packet.data ?: return null
//$$             when (ctx.side) {
//$$                 Side.CLIENT -> {
//$$                     for (receiver in OmniClientPackets.getAllPacketReceivers(id)) {
//$$                         if (receiver.test(data)) {
//$$                             return null
//$$                         }
//$$                     }
//$$                 }
//$$
//$$                 Side.SERVER -> {
//#if MC >= 1.12.2
//$$                     val player = ctx.serverHandler.player
//#else
//$$                     val player = ctx.serverHandler.playerEntity
//#endif
//$$                     for (receiver in OmniServerPackets.getAllPacketReceivers(id)) {
//$$                         if (receiver.test(player, data)) {
//$$                             return null
//$$                         }
//$$                     }
//$$                 }
//$$
//$$                 else -> return null
//$$             }
//$$
//$$             return null
//$$         }
//$$
//$$     }
//$$
//$$ }
//#endif
