package dev.deftu.omnicore.internal.mixins;

import com.mojang.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix4f.class)
public interface Mixin_Matrix4fSetters {
    @Accessor("m00") void setM00(float v);
    @Accessor("m01") void setM01(float v);
    @Accessor("m02") void setM02(float v);
    @Accessor("m03") void setM03(float v);
    @Accessor("m10") void setM10(float v);
    @Accessor("m11") void setM11(float v);
    @Accessor("m12") void setM12(float v);
    @Accessor("m13") void setM13(float v);
    @Accessor("m20") void setM20(float v);
    @Accessor("m21") void setM21(float v);
    @Accessor("m22") void setM22(float v);
    @Accessor("m23") void setM23(float v);
    @Accessor("m30") void setM30(float v);
    @Accessor("m31") void setM31(float v);
    @Accessor("m32") void setM32(float v);
    @Accessor("m33") void setM33(float v);
}
