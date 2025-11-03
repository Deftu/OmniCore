package dev.deftu.omnicore.internal.mixins;

import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DiscardedPayload.class)
public class Mixin_SmuggleUnknownPayloadData implements UnknownPayloadDataSmuggler {
    private FriendlyByteBuf omnicore$data;

    public @NotNull FriendlyByteBuf omnicore$getData() {
        return this.omnicore$data;
    }

    public void omnicore$setData(@NotNull FriendlyByteBuf data) {
        this.omnicore$data = data;
    }
}
