package dev.deftu.omnicore.internal.mixins;

import dev.deftu.omnicore.api.network.OmniNetworking;
import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
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

    @Inject(method = "onCustomPayload", at = @At("HEAD"))
    private void omnicore$captureCustomPayloads(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        CustomPayload payload = packet.comp_1647();
        //#if MC >= 1.20.6
        Identifier channel = payload.getId().comp_2242();
        //#else
        //$$ Identifier channel = payload.comp_1678();
        //#endif
        if (!OmniNetworking.isRegisteredChannel(channel)) {
            return;
        }

        if (!(payload instanceof UnknownPayloadDataSmuggler)) {
            return;
        }

        PacketByteBuf buf = ((UnknownPayloadDataSmuggler) payload).omnicore$getData();
        OmniNetworking.handle(channel, buf, player);
    }
}
