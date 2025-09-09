package dev.deftu.omnicore.internal.mixins.client;

// We need to do all this fancy fandangling with Fabric 1.12.2
// because Remap tries to go from 1.16.5 Forge -> 1.12.2 Forge,
// but this is Fabric, so the mappings end up breaking

//#if FABRIC || MC >= 1.16.5
import dev.deftu.omnicore.api.client.network.OmniClientNetworking;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.16.5
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;

//#if MC >= 1.20.4
import dev.deftu.omnicore.internal.networking.VanillaCustomPayload;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.UnknownCustomPayload;
//#else
//$$ import net.minecraft.client.multiplayer.ClientPacketListener;
//#endif

//#if MC >= 1.20.4
@Mixin(ClientCommonNetworkHandler.class)
//#else
//$$ @Mixin(ClientPacketListener.class)
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
public class Mixin_ForwardClientCustomPayloads {
    @Shadow @Final protected ClientConnection connection;

    @Inject(
            //#if MC >= 1.20.4
            method = "onCustomPayload(Lnet/minecraft/network/packet/s2c/common/CustomPayloadS2CPacket;)V",
            //#elseif MC >= 1.16.5
            //$$ method = "handleCustomPayload",
            //#else
            //$$ method = "onCustomPayload",
            //#endif
            at = @At("HEAD")
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
        CustomPayload payload = packet.comp_1646();
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
        VanillaCustomPayload holder = (VanillaCustomPayload) payload;

        Identifier channel = discardedPayload.comp_1678();
        PacketByteBuf buf = holder.getData();
        //#elseif MC >= 1.16.5
        //$$ ResourceLocation channel = packet.getIdentifier();
        //$$ FriendlyByteBuf buf = packet.getData();
        //#else
        //$$ Identifier channel = OmniIdentifier.create(packet.getChannel());
        //$$ PacketByteBuf buf = packet.getPayload();
        //#endif
        OmniClientNetworking.handle(channel, buf, this.connection);
    }
}
//#endif
