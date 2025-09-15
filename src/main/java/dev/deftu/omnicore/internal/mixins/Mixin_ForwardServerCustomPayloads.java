package dev.deftu.omnicore.internal.mixins;

//#if FABRIC || MC >= 1.16.5
import dev.deftu.omnicore.api.network.OmniNetworking;
import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.16.5
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

//#if MC >= 1.20.4
import com.mojang.authlib.GameProfile;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
//#else
//$$ import net.minecraft.server.network.ServerGamePacketListenerImpl;
//#endif

//#if MC >= 1.20.4
@Mixin(ServerCommonNetworkHandler.class)
//#else
//$$ @Mixin(ServerGamePacketListenerImpl.class)
//#endif
//#else
//$$ import dev.deftu.omnicore.api.OmniIdentifier;
//$$ import net.minecraft.entity.player.ServerPlayerEntity;
//$$ import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
//$$ import net.minecraft.server.network.ServerPlayNetworkHandler;
//$$ import net.minecraft.util.Identifier;
//$$ import net.minecraft.util.PacketByteBuf;
//$$
//$$ @Mixin(ServerPlayNetworkHandler.class)
//#endif
public class Mixin_ForwardServerCustomPayloads {
    //#if MC >= 1.20.4
    @Shadow @Final protected MinecraftServer server;
    //#endif

    @Inject(
            //#if MC >= 1.16.5
            method = "onCustomPayload",
            //#else
            //$$ method = "onCustomPayload",
            //#endif
            at = @At("HEAD"),
            cancellable = true
    )
    private void omnicore$captureCustomPayloads(
            //#if MC >= 1.16.5
            CustomPayloadC2SPacket packet,
            //#else
            //$$ CustomPayloadC2SPacket packet,
            //#endif
            CallbackInfo ci
    ) {
        //#if MC >= 1.20.4
        GameProfile playerProfile = ((ServerCommonNetworkHandler) (Object) this).getDebugProfile();
        ServerPlayerEntity player = this.server.getPlayerManager().getPlayer(playerProfile.getId());

        CustomPayload payload = packet.comp_1647();
        if (!(payload instanceof UnknownPayloadDataSmuggler)) {
            return;
        }

        //#if MC >= 1.20.6
        Identifier channel = payload.getId().comp_2242();
        //#else
        //$$ Identifier channel = payload.comp_1678();
        //#endif
        if (!OmniNetworking.isRegisteredChannel(channel)) {
            return;
        }

        PacketByteBuf buf = ((UnknownPayloadDataSmuggler) payload).omnicore$getData();
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
        //$$ Identifier channel = accessor.getChannel();
        //$$ if (!OmniNetworking.isRegisteredChannel(channel)) {
        //$$     return;
        //$$ }
        //$$
        //$$ PacketByteBuf packetData = accessor.getData();
        //$$ PacketByteBuf buf = new PacketByteBuf(packetData.copy());
        //$$ buf.readerIndex(0);
        //#endif
        //#else
        //$$ ServerPlayerEntity player = ((ServerPlayNetworkHandler) (Object) this).player;
        //$$ Identifier channel = OmniIdentifier.createOrNull(packet.getChannel());
        //$$ System.out.println("Received custom payload on channel: " + channel);
        //$$ if (channel == null || !OmniNetworking.isRegisteredChannel(channel)) {
        //$$     return;
        //$$ }
        //$$
        //$$ PacketByteBuf packetData = packet.getPayload();
        //$$ PacketByteBuf buf = new PacketByteBuf(packetData.copy());
        //$$ buf.readerIndex(0);
        //$$ System.out.println("Payload: " + buf);
        //#endif
        OmniNetworking.handle(channel, buf, player);
        ci.cancel();
    }
}
//#endif
