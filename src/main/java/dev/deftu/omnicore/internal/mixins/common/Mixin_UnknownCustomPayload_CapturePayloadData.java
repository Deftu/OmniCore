package dev.deftu.omnicore.internal.mixins.common;

//#if MC >= 1.20.4
import dev.deftu.omnicore.common.OmniCustomPayloadDataHolder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.UnknownCustomPayload;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(UnknownCustomPayload.class)
public class Mixin_UnknownCustomPayload_CapturePayloadData implements OmniCustomPayloadDataHolder {

    @Unique
    private PacketByteBuf omnicore$payloadData = null;

    @Override
    public @Nullable PacketByteBuf omnicore$getData() {
        return omnicore$payloadData;
    }

    @Override
    public void omnicore$setData(@Nullable PacketByteBuf data) {
        this.omnicore$payloadData = data;
    }

}
//#endif
