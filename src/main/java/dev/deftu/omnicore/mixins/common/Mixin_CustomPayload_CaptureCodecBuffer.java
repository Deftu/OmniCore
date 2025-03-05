package dev.deftu.omnicore.mixins.common;

//#if MC >= 1.20.6
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//$$ import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
//$$ import com.llamalad7.mixinextras.sugar.Local;
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
//$$ import net.minecraft.network.PacketByteBuf;
//$$ import net.minecraft.network.codec.PacketDecoder;
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import net.minecraft.util.Identifier;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Coerce;
//$$
//$$ @Mixin(targets = "net.minecraft.network.packet/CustomPayload$1")
//$$ public class Mixin_CustomPayload_CaptureCodecBuffer<B extends PacketByteBuf> {
//$$
//$$     @WrapOperation(
//$$             method = "decode(Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/network/packet/CustomPayload;",
//$$             at = @At(
//$$                     value = "INVOKE",
//$$                     target = "Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;"
//$$             )
//$$     )
//$$     private Object omnicore$capturePayloadData(
//$$             @Coerce PacketDecoder<B, CustomPayload> instance,
//$$             Object buf,
//$$             Operation<Object> original,
//$$             @Local(ordinal = 0) Identifier channel
//$$     ) {
//$$         try {
//$$             PacketByteBuf copiedBuffer = new PacketByteBuf(((PacketByteBuf) buf).copy());
//$$             CustomPayload packet = (CustomPayload) original.call(instance, buf);
//$$             if (packet instanceof OmniCustomPayloadDataHolder) {
//$$                 ((OmniCustomPayloadDataHolder) packet).omnicore$setData(copiedBuffer);
//$$             } else {
//$$                 // Discard the copied buffer if it's not used
//$$                 copiedBuffer.release();
//$$             }
//$$
//$$             return packet;
//$$         } catch (Throwable t) {
//$$             t.printStackTrace();
//$$             return null;
//$$         }
//$$     }
//$$
//$$ }
//#endif
