package dev.deftu.omnicore.common

//#if MC >= 1.20.4
//$$ import io.netty.buffer.ByteBuf
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload
//$$ import net.minecraft.network.FriendlyByteBuf
//$$ import net.minecraft.resources.ResourceLocation
//$$ import java.util.function.Consumer
//$$
//$$ internal class OmniCustomPayloadImpl(
//$$     override val channel: ResourceLocation,
//$$     val consumer: Consumer<ByteBuf>
//$$ ) : OmniCustomPayload, OmniCustomPayloadDataHolder, CustomPacketPayload {
//$$
//$$     private var data: ByteBuf? = null
//$$
//#if MC >= 1.20.6
//$$     override fun getId(): CustomPayload.Id<out CustomPayload> {
//$$         return CustomPayload.Id(channel)
//$$     }
//#else
//$$     override fun id(): ResourceLocation {
//$$         return channel
//$$     }
//#endif
//$$
//#if MC <= 1.20.4
//$$     override fun write(buf: FriendlyByteBuf) {
//$$         this.data = buf.copy()
//$$         consumer.accept(buf)
//$$     }
//#endif
//$$
//$$     override fun write(buf: ByteBuf) {
//$$         this.data = buf.copy()
//$$         consumer.accept(buf)
//$$     }
//$$
//$$     override fun `omnicore$getData`(): FriendlyByteBuf? {
//$$         return data?.let { FriendlyByteBuf(it) }
//$$     }
//$$
//$$     override fun `omnicore$setData`(data: FriendlyByteBuf) {
//$$         this.data = data
//$$     }
//$$
//$$ }
//#endif
