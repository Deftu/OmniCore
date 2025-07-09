package dev.deftu.omnicore.mixins.server;

//#if MC >= 1.20.6
import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
import dev.deftu.omnicore.common.OmniPacketReceiverContext;
import dev.deftu.omnicore.server.OmniServerPackets;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.BiPredicate;

@Mixin(ServerPlayNetworkHandler.class)
public class Mixin_ServerPlayNetworkHandler_CaptureOverridenCustomPayloads {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void omnicore$captureCustomPayloads(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        CustomPayload payload = packet.comp_1647();
        if (!(payload instanceof UnknownCustomPayload)) {
            return;
        }

        @SuppressWarnings("PatternVariableCanBeUsed")
        UnknownCustomPayload discardedPayload = (UnknownCustomPayload) payload;
        //noinspection ConstantValue
        if (!(payload instanceof OmniCustomPayloadDataHolder)) {
            return;
        }

        @SuppressWarnings("PatternVariableCanBeUsed")
        OmniCustomPayloadDataHolder holder = (OmniCustomPayloadDataHolder) payload;

        Identifier channel = discardedPayload.comp_1678();
        PacketByteBuf buf = holder.omnicore$getData();
        Set<BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext>> receivers = OmniServerPackets.getAllPacketReceivers$OmniCore(channel);
        OmniPacketReceiverContext context = new OmniPacketReceiverContext(channel, buf);
        boolean anyHandled = receivers.stream().anyMatch(receiver -> receiver.test(this.player, context));
        if (anyHandled) {
            ci.cancel();
        }
    }

}
//#endif
