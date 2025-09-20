package dev.deftu.omnicore.internal.mixins.client;

//#if MC >= 1.16.5
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface Mixin_AccessBoundKey {
    @Accessor InputUtil.Key getBoundKey();
}
//#endif
