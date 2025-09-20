package dev.deftu.omnicore.internal.mixins;

import net.minecraft.util.math.Matrix3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix3f.class)
public interface Mixin_Matrix3fGetters {
    @Accessor("a00") public float getM00();
    @Accessor("a01") public float getM01();
    @Accessor("a02") public float getM02();
    @Accessor("a10") public float getM10();
    @Accessor("a11") public float getM11();
    @Accessor("a12") public float getM12();
    @Accessor("a20") public float getM20();
    @Accessor("a21") public float getM21();
    @Accessor("a22") public float getM22();
}
