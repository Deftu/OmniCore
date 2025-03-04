package dev.deftu.omnicore.common

import net.minecraft.util.math.Vec3d

public object OmniCommonDefaults {

    public val defaultVec3d: Vec3d by lazy {
        //#if MC >= 1.12.2
        Vec3d.ZERO
        //#else
        //$$ Vec3d(0.0, 0.0, 0.0)
        //#endif
    }

}
