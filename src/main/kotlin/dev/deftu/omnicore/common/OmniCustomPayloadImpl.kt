package dev.deftu.omnicore.common

//#if MC >= 1.20.6
//$$ import io.netty.buffer.ByteBuf
//$$ import net.minecraft.network.packet.CustomPayload
//$$ import net.minecraft.util.Identifier
//$$ import java.util.function.Consumer
//$$
//$$ internal class OmniCustomPayloadImpl(
//$$     override val channel: Identifier,
//$$     val consumer: Consumer<ByteBuf>
//$$ ) : OmniCustomPayload, CustomPayload {
//$$
//$$     override fun getId(): CustomPayload.Id<out CustomPayload> {
//$$         return CustomPayload.Id(channel)
//$$     }
//$$
//$$     override fun write(buf: ByteBuf) {
//$$         consumer.accept(buf)
//$$     }
//$$
//$$ }
//#endif
