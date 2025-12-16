package dev.deftu.omnicore.internal.mixins.client;

//#if FABRIC
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface Mixin_AccessDefaultResourcePack {
    @Accessor DefaultResourcePack getMcDefaultResourcePack();
}
//#endif
