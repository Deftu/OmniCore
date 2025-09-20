package dev.deftu.omnicore.internal.mixins;

import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix4f.class)
public interface Mixin_Matrix4fSetters {
    @Accessor("a00") void setM00(float v);
    @Accessor("a01") void setM01(float v);
    @Accessor("a02") void setM02(float v);
    @Accessor("a03") void setM03(float v);
    @Accessor("a10") void setM10(float v);
    @Accessor("a11") void setM11(float v);
    @Accessor("a12") void setM12(float v);
    @Accessor("a13") void setM13(float v);
    @Accessor("a20") void setM20(float v);
    @Accessor("a21") void setM21(float v);
    @Accessor("a22") void setM22(float v);
    @Accessor("a23") void setM23(float v);
    @Accessor("a30") void setM30(float v);
    @Accessor("a31") void setM31(float v);
    @Accessor("a32") void setM32(float v);
    @Accessor("a33") void setM33(float v);
}
