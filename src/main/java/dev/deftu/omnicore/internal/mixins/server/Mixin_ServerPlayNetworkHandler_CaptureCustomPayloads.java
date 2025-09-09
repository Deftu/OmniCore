package dev.deftu.omnicore.internal.mixins.server;

//#if FABRIC || MC >= 1.16.5
import dev.deftu.omnicore.common.OmniPacketReceiverContext;
import dev.deftu.omnicore.server.OmniServerPackets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.BiPredicate;

//#if MC >= 1.16.5
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

//#if MC >= 1.20.4
import com.mojang.authlib.GameProfile;
import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
import dev.deftu.omnicore.common.OmniCustomPayloadImpl;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.UnknownCustomPayload;
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
//$$ import dev.deftu.omnicore.common.OmniIdentifier;
//$$ import dev.deftu.omnicore.server.OmniServerPackets;
//$$ import net.minecraft.entity.player.ServerPlayerEntity;
//$$ import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
//$$ import net.minecraft.server.network.ServerPlayNetworkHandler;
//$$ import net.minecraft.util.Identifier;
//$$ import net.minecraft.util.PacketByteBuf;
//$$
//$$ @Mixin(ServerPlayNetworkHandler.class)
//#endif
public class Mixin_ServerPlayNetworkHandler_CaptureCustomPayloads {

    //#if MC >= 1.20.4
    @Shadow
    @Final
    protected MinecraftServer server;
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

        Identifier channel;
        PacketByteBuf buf;

        if (payload instanceof UnknownCustomPayload && payload instanceof OmniCustomPayloadDataHolder) {
            UnknownCustomPayload discardedPayload = (UnknownCustomPayload) payload;
            OmniCustomPayloadDataHolder dataHolder = (OmniCustomPayloadDataHolder) payload;

            channel = discardedPayload.comp_1678();
            buf = dataHolder.omnicore$getData();
        } else if (payload instanceof OmniCustomPayloadImpl) {
            OmniCustomPayloadImpl omniPayload = (OmniCustomPayloadImpl) payload;
            channel = omniPayload.getChannel();
            buf = omniPayload.omnicore$getData();
        } else {
            return;
        }

        Set<BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext>> receivers = OmniServerPackets.getAllPacketReceivers$OmniCore(channel);
        //#elseif MC >= 1.16.5
        //$$ ServerPlayer player = ((ServerGamePacketListenerImpl) (Object) this).player;
        //#if MC >= 1.17.1
        //$$ ResourceLocation channel = packet.getIdentifier();
        //$$ FriendlyByteBuf buf = packet.getData();
        //#elseif MC >= 1.16.5
        //$$ Mixin_CustomPayloadC2SPacket_FieldAccessor accessor = (Mixin_CustomPayloadC2SPacket_FieldAccessor) packet;
        //$$ Identifier channel = accessor.getChannel();
        //$$ PacketByteBuf buf = accessor.getData();
        //#endif
        //$$ Set<BiPredicate<ServerPlayer, OmniPacketReceiverContext>> receivers = OmniServerPackets.getAllPacketReceivers$OmniCore(channel);
        //#else
        //$$ ServerPlayerEntity player = ((ServerPlayNetworkHandler) (Object) this).player;
        //$$ Identifier channel = OmniIdentifier.create(packet.getChannel());
        //$$ PacketByteBuf buf = packet.getPayload();
        //$$ Set<BiPredicate<ServerPlayerEntity, OmniPacketReceiverContext>> receivers = OmniServerPackets.getAllPacketReceivers$OmniCore(channel);
        //#endif
        OmniPacketReceiverContext context = new OmniPacketReceiverContext(channel, buf);
        boolean anyHandled = receivers.stream().anyMatch(receiver -> receiver.test(player, context));
        if (anyHandled) {
            ci.cancel();
        }
    }

}
//#endif
