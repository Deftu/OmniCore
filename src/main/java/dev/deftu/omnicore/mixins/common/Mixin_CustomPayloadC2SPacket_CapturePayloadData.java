package dev.deftu.omnicore.mixins.common;

//#if MC == 1.20.4
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
//$$ import net.minecraft.network.FriendlyByteBuf;
//$$ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//$$ import net.minecraft.network.protocol.common.custom.DiscardedPayload;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//$$
//$$ @Mixin(ServerboundCustomPayloadPacket.class)
//$$ public abstract class Mixin_CustomPayloadC2SPacket_CapturePayloadData {
//$$
//$$     @Shadow
//$$     private static DiscardedPayload readUnknownPayload(ResourceLocation arg, FriendlyByteBuf arg2) {
//$$         return null;
//$$     }
//$$
//$$     @Redirect(method = "readPayload*", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;readUnknownPayload(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/network/protocol/common/custom/DiscardedPayload;"))
//$$     private static DiscardedPayload omnicore$capturePayloadData(ResourceLocation channel, FriendlyByteBuf buf) {
//$$         try {
//$$             FriendlyByteBuf copiedBuffer = new FriendlyByteBuf(buf.copy());
//$$             CustomPacketPayload packet = readUnknownPayload(channel, buf);
//$$             if (packet instanceof OmniCustomPayloadDataHolder) {
//$$                 ((OmniCustomPayloadDataHolder) packet).omnicore$setData(copiedBuffer);
//$$             } else {
//$$                 // Discard the copied buffer if it's not used
//$$                 copiedBuffer.release();
//$$             }
//$$
//$$             return (DiscardedPayload) packet;
//$$         } catch (Throwable t) {
//$$             t.printStackTrace();
//$$             return null;
//$$         }
//$$     }
//$$
//$$ }
//#endif
