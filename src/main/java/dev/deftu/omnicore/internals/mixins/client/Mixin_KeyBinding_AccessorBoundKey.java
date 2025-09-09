package dev.deftu.omnicore.internals.mixins.client;

//#if MC >= 1.16.5
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface Mixin_KeyBinding_AccessorBoundKey {
    @Accessor InputUtil.Key getBoundKey();
}
//#endif
