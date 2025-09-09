package dev.deftu.omnicore.internal.mixins.client;

import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NativeImage.class)
public interface Mixin_NativeImageAllocation {
    @Invoker void invokeCheckAllocated();
}
