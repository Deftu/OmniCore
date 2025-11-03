package dev.deftu.omnicore.internal.mixins;

//#if MC == 1.16.5
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundCustomPayloadPacket.class)
public interface Mixin_CustomPayloadDataAccessor {
    @Accessor ResourceLocation getIdentifier();
    @Accessor FriendlyByteBuf getData();
}
//#endif
