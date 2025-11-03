package dev.deftu.omnicore.internal.mixins.client;

// We need to do all this fancy fandangling with Fabric 1.12.2
// because Remap tries to go from 1.16.5 Forge -> 1.12.2 Forge,
// but this is Fabric, so the mappings end up breaking

//#if FABRIC || MC >= 1.16.5
//#if MC >= 1.16.5
import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import net.minecraft.network.Connection;
//#else
//$$ import net.minecraft.network.NetworkManager;
//#endif

import dev.deftu.omnicore.api.client.network.OmniClientNetworking;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.16.5
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;

//#if MC >= 1.20.4
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
//#else
//$$ import net.minecraft.client.multiplayer.ClientPacketListener;
//#endif

//#if MC >= 1.20.4
@Mixin(ClientCommonPacketListenerImpl.class)
//#else
//$$ @Mixin(ClientPacketListener.class)
//#endif
//#else
//$$ import dev.deftu.omnicore.api.OmniResourceLocation;
//$$ import net.minecraft.client.network.NetHandlerPlayClient;
//$$ import net.minecraft.network.PacketBuffer;
//$$ import net.minecraft.network.play.server.SPacketCustomPayload;
//$$ import net.minecraft.util.ResourceLocation;
//$$
//$$ @Mixin(NetHandlerPlayClient.class)
//#endif
public class Mixin_ForwardClientCustomPayloads {
    //#if MC >= 1.16.5
    @Shadow @Final protected Connection connection;
    //#else
    //$$ @Shadow @Final private NetworkManager netManager;
    //#endif

    @Inject(
            //#if MC >= 1.20.4
            method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
            //#else
            //$$ method = "handleCustomPayload",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void omnicore$captureCustomPayloads(
            //#if MC >= 1.16.5
            ClientboundCustomPayloadPacket packet,
            //#else
            //$$ SPacketCustomPayload packet,
            //#endif
            CallbackInfo ci
    ) {
        //#if MC >= 1.20.4
        CustomPacketPayload payload = packet.payload();
        //#if MC >= 1.20.6
        ResourceLocation channel = payload.type().id();
        //#else
        //$$ ResourceLocation channel = payload.id();
        //#endif
        if (!OmniClientNetworking.isChannelRegistered(channel)) {
            return;
        }

        // Guard against stealing other mods' packets... Weird that they've taken up the same channel, though.
        if (!(payload instanceof DiscardedPayload)) {
            return;
        }

        FriendlyByteBuf buf = ((UnknownPayloadDataSmuggler) payload).omnicore$getData();
        //#elseif MC >= 1.16.5
        //$$ ResourceLocation channel = packet.getIdentifier();
        //$$ if (!OmniClientNetworking.isChannelRegistered(channel)) {
        //$$     return;
        //$$ }
        //$$
        //$$ FriendlyByteBuf packetData = packet.getData();
        //$$ FriendlyByteBuf buf = new FriendlyByteBuf(packetData.copy());
        //#else
        //$$ ResourceLocation channel = OmniResourceLocation.createOrNull(packet.getChannelName());
        //$$ if (channel == null || !OmniClientNetworking.isChannelRegistered(channel)) {
        //$$     return;
        //$$ }
        //$$
        //$$ PacketBuffer packetData = packet.getBufferData();
        //$$ PacketBuffer buf = new PacketBuffer(packetData.copy());
        //#endif

        OmniClientNetworking.handle(
                channel,
                buf,
                //#if MC >= 1.16.5
                this.connection
                //#else
                //$$ this.netManager
                //#endif
        );

        ci.cancel();
    }
}
//#endif
