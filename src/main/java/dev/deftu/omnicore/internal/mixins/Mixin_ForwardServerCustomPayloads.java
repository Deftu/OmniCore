package dev.deftu.omnicore.internal.mixins;

//#if FABRIC || MC >= 1.16.5
import dev.deftu.omnicore.api.network.OmniNetworking;
import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.16.5
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

//#if MC >= 1.20.4
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
//#else
//$$ import net.minecraft.server.network.ServerGamePacketListenerImpl;
//#endif

//#if MC >= 1.20.4
@Mixin(ServerCommonPacketListenerImpl.class)
//#else
//$$ @Mixin(ServerGamePacketListenerImpl.class)
//#endif
//#else
//$$ import dev.deftu.omnicore.api.OmniResourceLocation;
//$$ import net.minecraft.entity.player.EntityPlayerMP;
//$$ import net.minecraft.network.NetHandlerPlayServer;
//$$ import net.minecraft.network.PacketBuffer;
//$$ import net.minecraft.network.play.client.CPacketCustomPayload;
//$$ import net.minecraft.util.ResourceLocation;
//$$
//$$ @Mixin(NetHandlerPlayServer.class)
//#endif
public class Mixin_ForwardServerCustomPayloads {
    //#if MC >= 1.20.4
    @Shadow @Final protected MinecraftServer server;
    //#endif

    @Inject(
            //#if MC >= 1.16.5
            method = "handleCustomPayload",
            //#else
            //$$ method = "processCustomPayload",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void omnicore$captureCustomPayloads(
            //#if MC >= 1.16.5
            ServerboundCustomPayloadPacket packet,
            //#else
            //$$ CPacketCustomPayload packet,
            //#endif
            CallbackInfo ci
    ) {
        //#if MC >= 1.20.4
        GameProfile playerProfile = ((ServerCommonPacketListenerImpl) (Object) this).getOwner();
        ServerPlayer player = this.server.getPlayerList().getPlayer(playerProfile.id());

        CustomPacketPayload payload = packet.payload();
        if (!(payload instanceof UnknownPayloadDataSmuggler)) {
            return;
        }

        //#if MC >= 1.20.6
        ResourceLocation channel = payload.type().id();
        //#else
        //$$ ResourceLocation channel = payload.id();
        //#endif
        if (!OmniNetworking.isRegisteredChannel(channel)) {
            return;
        }

        FriendlyByteBuf buf = ((UnknownPayloadDataSmuggler) payload).omnicore$getData();
        //#elseif MC >= 1.16.5
        //$$ ServerPlayer player = ((ServerGamePacketListenerImpl) (Object) this).player;
        //#if MC >= 1.17.1
        //$$ ResourceLocation channel = packet.getIdentifier();
        //$$ if (!OmniNetworking.isRegisteredChannel(channel)) {
        //$$     return;
        //$$ }
        //$$
        //$$ FriendlyByteBuf packetData = packet.getData();
        //$$ FriendlyByteBuf buf = new FriendlyByteBuf(packetData.copy());
        //$$ buf.readerIndex(0);
        //#elseif MC >= 1.16.5
        //$$ Mixin_CustomPayloadDataAccessor accessor = (Mixin_CustomPayloadDataAccessor) packet;
        //$$ ResourceLocation channel = accessor.getIdentifier();
        //$$ if (!OmniNetworking.isRegisteredChannel(channel)) {
        //$$     return;
        //$$ }
        //$$
        //$$ FriendlyByteBuf packetData = accessor.getData();
        //$$ FriendlyByteBuf buf = new FriendlyByteBuf(packetData.copy());
        //$$ buf.readerIndex(0);
        //#endif
        //#else
        //$$ EntityPlayerMP player = ((NetHandlerPlayServer) (Object) this).player;
        //$$ ResourceLocation channel = OmniResourceLocation.createOrNull(packet.getChannelName());
        //$$ if (channel == null || !OmniNetworking.isRegisteredChannel(channel)) {
        //$$     return;
        //$$ }
        //$$
        //$$ PacketBuffer packetData = packet.getBufferData();
        //$$ PacketBuffer buf = new PacketBuffer(packetData.copy());
        //$$ buf.readerIndex(0);
        //#endif
        OmniNetworking.handle(channel, buf, player);
        ci.cancel();
    }
}
//#endif
