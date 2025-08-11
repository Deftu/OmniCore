package dev.deftu.omnicore.mixins.client;

//#if MC >= 1.16.5
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NativeImage.class)
public interface Mixin_NativeImage_Accessor {
    @Accessor long getPointer();
    @Invoker void invokeCheckAllocated();
}
//#endif
