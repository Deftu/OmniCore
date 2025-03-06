package dev.deftu.omnicore.mixins.common;

//#if MC >= 1.20.4
//$$ import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
//$$ import net.minecraft.network.FriendlyByteBuf;
//$$ import net.minecraft.network.protocol.common.custom.DiscardedPayload;
//$$ import org.jetbrains.annotations.Nullable;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Unique;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$
//#if MC >= 1.20.6
//$$ import net.minecraft.network.packet.CustomPayload;
//$$ import net.minecraft.util.Identifier;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif
//$$
//$$ @Mixin(DiscardedPayload.class)
//$$ public class Mixin_UnknownCustomPayload_CapturePayloadData implements OmniCustomPayloadDataHolder {
//$$
//$$     @Unique
//$$     private FriendlyByteBuf omnicore$payloadData = null;
//$$
//$$     @Override
//$$     public @Nullable FriendlyByteBuf omnicore$getData() {
//$$         return omnicore$payloadData;
//$$     }
//$$
//$$     @Override
//$$     public void omnicore$setData(@Nullable FriendlyByteBuf data) {
//$$         this.omnicore$payloadData = data;
//$$     }
//$$
//$$ }
//#endif
