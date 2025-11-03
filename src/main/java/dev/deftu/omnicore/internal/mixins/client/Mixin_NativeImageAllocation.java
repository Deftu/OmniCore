package dev.deftu.omnicore.internal.mixins.client;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//#if MC < 1.21.5
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//#endif

@Mixin(NativeImage.class)
public interface Mixin_NativeImageAllocation {
    //#if MC < 1.21.5
    //$$ @Accessor long getPixels();
    //#endif

    @Invoker void invokeCheckAllocated();
}
