package dev.deftu.omnicore.mixins.common;

//#if MC >= 1.20.6
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
//$$ import net.minecraft.network.PacketByteBuf;
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(targets = "net/minecraft/network/packet/CustomPayload$1")
//$$ public class Mixin_CustomPayload_EncodePayloadDataForCapture<B extends PacketByteBuf> {
//$$
//$$     @Inject(
//#if FORGE-LIKE
//$$             method = "encode(Lnet/minecraft/network/FriendlyByteBuf;Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V",
//#else
//$$             method = "encode(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/packet/CustomPayload;)V",
//#endif
//$$             at = @At("HEAD"),
//$$             cancellable = true
//$$     )
//$$     private void omnicore$bypass(B buf, CustomPayload customPayload, CallbackInfo ci) {
//$$         if (!(customPayload instanceof OmniCustomPayloadDataHolder)) {
//$$             return;
//$$         }
//$$
//$$         OmniCustomPayloadDataHolder payloadDataHolder = (OmniCustomPayloadDataHolder) customPayload;
//$$         PacketByteBuf data = payloadDataHolder.omnicore$getData();
//$$         if (data != null) {
//$$             return;
//$$         }
//$$
//$$         payloadDataHolder.omnicore$setData(buf);
//$$     }
//$$
//$$ }
//#endif
