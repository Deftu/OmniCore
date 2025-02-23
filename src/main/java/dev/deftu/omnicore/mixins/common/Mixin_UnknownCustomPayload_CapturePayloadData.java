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
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(DiscardedPayload.class)
//$$ public class Mixin_UnknownCustomPayload_CapturePayloadData implements OmniCustomPayloadDataHolder {
//$$
//$$     @Unique
//$$     private FriendlyByteBuf omnicore$payloadData = null;
//$$
//#if MC <= 1.20.4
//$$     @Inject(method = "write", at = @At("HEAD"))
//$$     private void omnicore$capturePayloadData(FriendlyByteBuf buf, CallbackInfo ci) {
//$$         this.omnicore$payloadData = new FriendlyByteBuf(buf.copy());
//$$     }
//#endif
//$$
//$$     @Override
//$$     public @Nullable FriendlyByteBuf omnicore$getData() {
//$$         return omnicore$payloadData;
//$$     }
//$$
//#if MC >= 1.20.6
//$$     @Override
//$$     public void omnicore$setData(@Nullable PacketByteBuf data) {
//$$         this.omnicore$payloadData = data;
//$$     }
//#endif
//$$
//$$ }
//#endif
