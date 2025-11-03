package dev.deftu.omnicore.internal.mixins;

import dev.deftu.omnicore.api.network.OmniNetworking;
import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class Mixin_ForwardServerCustomPayload2EletricBoogaloo {
    @Shadow public ServerPlayer player;

    @Inject(method = "onCustomPayload", at = @At("HEAD"))
    private void omnicore$captureCustomPayloads(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
        CustomPacketPayload payload = packet.payload();
        //#if MC >= 1.20.6
        ResourceLocation channel = payload.type().id();
        //#else
        //$$ Identifier channel = payload.id();
        //#endif
        if (!OmniNetworking.isRegisteredChannel(channel)) {
            return;
        }

        if (!(payload instanceof UnknownPayloadDataSmuggler)) {
            return;
        }

        FriendlyByteBuf buf = ((UnknownPayloadDataSmuggler) payload).omnicore$getData();
        OmniNetworking.handle(channel, buf, player);
    }
}
