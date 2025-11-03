package dev.deftu.omnicore.internal.mixins;

import com.mojang.math.Matrix3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix3f.class)
public interface Mixin_Matrix3fGetters {
    @Accessor("m00") float getM00();
    @Accessor("m01") float getM01();
    @Accessor("m02") float getM02();
    @Accessor("m10") float getM10();
    @Accessor("m11") float getM11();
    @Accessor("m12") float getM12();
    @Accessor("m20") float getM20();
    @Accessor("m21") float getM21();
    @Accessor("m22") float getM22();
}
