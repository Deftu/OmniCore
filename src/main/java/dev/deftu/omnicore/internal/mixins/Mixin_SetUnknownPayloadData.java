package dev.deftu.omnicore.internal.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Mixin(targets = "net.minecraft.network.protocol.common.custom.CustomPacketPayload$1")
public class Mixin_SetUnknownPayloadData<B extends FriendlyByteBuf> {
    @WrapOperation(
            method = "decode(Lnet/minecraft/network/PacketByteBuf;)Lnet/minecraft/network/packet/CustomPayload;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/codec/PacketCodec;decode(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private Object omnicore$capturePayloadData(
            @Coerce StreamDecoder<B, CustomPacketPayload> instance,
            Object buf,
            Operation<Object> original,
            @Local(ordinal = 0) ResourceLocation channel
    ) {
        try {
            FriendlyByteBuf copiedBuffer = new FriendlyByteBuf(((FriendlyByteBuf) buf).copy());
            CustomPacketPayload packet = (CustomPacketPayload) original.call(instance, buf);
            if (packet instanceof UnknownPayloadDataSmuggler) {
                ((UnknownPayloadDataSmuggler) packet).omnicore$setData(copiedBuffer);
            } else {
                // Discard the copied buffer if it's not used
                copiedBuffer.release();
            }

            return packet;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
