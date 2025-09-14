package dev.deftu.omnicore.internal.mixins;

import dev.deftu.omnicore.api.network.OmniNetworking;
import dev.deftu.omnicore.internal.networking.VanillaCustomPayload;
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

@Mixin(ServerPlayNetworkHandler.class)
public class Mixin_ForwardServerCustomPayload2EletricBoogaloo {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void omnicore$captureCustomPayloads(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        CustomPayload payload = packet.comp_1647();
        if (!(payload instanceof UnknownCustomPayload)) {
            return;
        }

        @SuppressWarnings("PatternVariableCanBeUsed")
        UnknownCustomPayload discardedPayload = (UnknownCustomPayload) payload;
        //noinspection ConstantValue
        if (!(payload instanceof VanillaCustomPayload)) {
            return;
        }

        @SuppressWarnings("PatternVariableCanBeUsed")
        VanillaCustomPayload customPayload = (VanillaCustomPayload) payload;

        Identifier channel = discardedPayload.comp_1678();
        PacketByteBuf buf = customPayload.getData();
        OmniNetworking.handle(channel, buf, player);
    }
}
