package dev.deftu.omnicore.internal.mixins;

//#if MC >= 1.20.6
import dev.deftu.omnicore.internal.networking.VanillaCustomPacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.protocol.common.custom.CustomPacketPayload$1")
public class Mixin_WriteCustomPacketBuffers<B extends FriendlyByteBuf> {
    @Inject(
            //#if FORGE-LIKE
            method = "encode(Lnet/minecraft/network/FriendlyByteBuf;Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V",
            //#else
            //$$ method = "encode(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/packet/CustomPayload;)V",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void omnicore$bypass(B buf, CustomPacketPayload customPayload, CallbackInfo ci) {
        if (!(customPayload instanceof VanillaCustomPacketPayload)) {
            return;
        }

        VanillaCustomPacketPayload payload = (VanillaCustomPacketPayload) customPayload;
        buf.writeResourceLocation(payload.getIdentifier());
        payload.write(buf);
        ci.cancel();
    }
}
//#endif
