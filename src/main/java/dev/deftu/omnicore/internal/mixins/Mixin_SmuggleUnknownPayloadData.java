package dev.deftu.omnicore.internal.mixins;

import dev.deftu.omnicore.internal.networking.UnknownPayloadDataSmuggler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.UnknownCustomPayload;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UnknownCustomPayload.class)
public class Mixin_SmuggleUnknownPayloadData implements UnknownPayloadDataSmuggler {
    private PacketByteBuf omnicore$data;

    public @NotNull PacketByteBuf omnicore$getData() {
        return this.omnicore$data;
    }

    public void omnicore$setData(@NotNull PacketByteBuf data) {
        this.omnicore$data = data;
    }
}
