package dev.deftu.omnicore.mixins.common;

//#if MC == 1.20.4
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
//$$ import net.minecraft.network.PacketByteBuf;
//$$ import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import net.minecraft.network.packet.UnknownCustomPayload;
//$$ import net.minecraft.util.Identifier;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$
//$$ @Mixin(CustomPayloadC2SPacket.class)
//$$ public abstract class Mixin_CustomPayloadC2SPacket_CapturePayloadData {
//$$
//$$     @Shadow
//$$     private static UnknownCustomPayload readUnknownPayload(Identifier arg, PacketByteBuf arg2) {
//$$         return null;
//$$     }
//$$
//$$     @Redirect(method = "readPayload*", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/common/CustomPayloadC2SPacket;readUnknownPayload(Lnet/minecraft/util/Identifier;Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/network/packet/UnknownCustomPayload;"))
//$$     private static UnknownCustomPayload omnicore$capturePayloadData(Identifier channel, PacketByteBuf buf) {
//$$         try {
//$$             PacketByteBuf copiedBuffer = new PacketByteBuf(buf.copy());
//$$             CustomPayload packet = readUnknownPayload(channel, buf);
//$$             if (packet instanceof OmniCustomPayloadDataHolder) {
//$$                 ((OmniCustomPayloadDataHolder) packet).omnicore$setData(copiedBuffer);
//$$             } else {
//$$                 // Discard the copied buffer if it's not used
//$$                 copiedBuffer.release();
//$$             }
//$$
//$$             return (UnknownCustomPayload) packet;
//$$         } catch (Throwable t) {
//$$             t.printStackTrace();
//$$             return null;
//$$         }
//$$     }
//$$
//$$ }
//#endif
