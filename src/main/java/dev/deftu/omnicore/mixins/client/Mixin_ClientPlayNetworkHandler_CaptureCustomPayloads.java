package dev.deftu.omnicore.mixins.client;

// We need to do all this fancy fandangling with Fabric 1.12.2
// because Remap tries to go from 1.16.5 Forge -> 1.12.2 Forge,
// but this is Fabric, so the mappings end up breaking

//#if FABRIC || MC >= 1.16.5
import dev.deftu.omnicore.client.OmniClientPackets;
import dev.deftu.omnicore.common.OmniPacketReceiverContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Predicate;

//#if MC >= 1.16.5
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

//#if MC >= 1.20.4
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
//$$ import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//$$ import net.minecraft.network.protocol.common.custom.DiscardedPayload;
//#else
import net.minecraft.client.network.ClientPlayNetworkHandler;
//#endif

//#if MC >= 1.20.4
//$$ @Mixin(ClientCommonPacketListenerImpl.class)
//#else
@Mixin(ClientPlayNetworkHandler.class)
//#endif
//#else
//$$ import dev.deftu.omnicore.common.OmniIdentifier;
//$$ import net.minecraft.util.Identifier;
//$$ import net.minecraft.util.PacketByteBuf;
//$$ import net.minecraft.client.network.ClientPlayNetworkHandler;
//$$ import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
//$$
//$$ @Mixin(ClientPlayNetworkHandler.class)
//#endif
public class Mixin_ClientPlayNetworkHandler_CaptureCustomPayloads {

    @Inject(
            //#if MC >= 1.20.4
            //$$ method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
            //#elseif MC >= 1.16.5
            method = "onCustomPayload",
            //#else
            //$$ method = "onCustomPayload",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void omnicore$captureCustomPayloads(
            //#if MC >= 1.16.5
            CustomPayloadS2CPacket packet,
            //#else
            //$$ CustomPayloadS2CPacket packet,
            //#endif
            CallbackInfo ci
    ) {
        //#if MC >= 1.20.4
        //$$ CustomPacketPayload payload = packet.payload();
        //$$ if (!(payload instanceof DiscardedPayload)) {
        //$$     return;
        //$$ }
        //$$
        //$$ @SuppressWarnings("PatternVariableCanBeUsed")
        //$$ DiscardedPayload discardedPayload = (DiscardedPayload) payload;
        //$$ //noinspection ConstantValue
        //$$ if (!(payload instanceof OmniCustomPayloadDataHolder)) {
        //$$     return;
        //$$ }
        //$$
        //$$ @SuppressWarnings("PatternVariableCanBeUsed")
        //$$ OmniCustomPayloadDataHolder holder = (OmniCustomPayloadDataHolder) payload;
        //$$
        //$$ ResourceLocation channel = discardedPayload.id();
        //$$ FriendlyByteBuf buf = holder.omnicore$getData();
        //#elseif MC >= 1.16.5
        Identifier channel = packet.getChannel();
        PacketByteBuf buf = packet.getData();
        //#else
        //$$ Identifier channel = OmniIdentifier.create(packet.getChannel());
        //$$ PacketByteBuf buf = packet.getPayload();
        //#endif
        Set<Predicate<OmniPacketReceiverContext>> receivers = OmniClientPackets.getAllPacketReceivers$OmniCore(channel);
        OmniPacketReceiverContext context = new OmniPacketReceiverContext(channel, buf);
        boolean anyHandled = receivers.stream().anyMatch(receiver -> receiver.test(context));
        if (anyHandled) {
            ci.cancel();
        }
    }

}
//#endif
