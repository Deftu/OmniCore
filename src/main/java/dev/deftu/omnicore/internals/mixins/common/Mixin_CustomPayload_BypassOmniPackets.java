package dev.deftu.omnicore.internals.mixins.common;

//#if MC >= 1.20.6
import dev.deftu.omnicore.common.OmniCustomPayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.packet.CustomPayload$1")
public class Mixin_CustomPayload_BypassOmniPackets<B extends PacketByteBuf> {

    @Inject(
//#if FORGE-LIKE
//$$         method = "encode(Lnet/minecraft/network/FriendlyByteBuf;Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V",
//#else
        method = "encode(Lnet/minecraft/network/PacketByteBuf;Lnet/minecraft/network/packet/CustomPayload;)V",
//#endif
        at = @At("HEAD"),
        cancellable = true
    )
    private void omnicore$bypass(B buf, CustomPayload customPayload, CallbackInfo ci) {
        if (!(customPayload instanceof OmniCustomPayload)) {
            return;
        }

        OmniCustomPayload payload = (OmniCustomPayload) customPayload;
        buf.writeIdentifier(payload.getChannel());
        payload.write(buf);
        ci.cancel();
    }

}
//#endif
