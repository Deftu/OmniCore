package dev.deftu.omnicore.api.data.shape

import dev.deftu.omnicore.api.data.aabb.OmniAABB
import dev.deftu.omnicore.internal.data.shape.OmniVoxelShapeImpl
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public object OmniVoxelShapes {
    @JvmStatic
    public fun empty(): OmniVoxelShape {
        return OmniVoxelShapeImpl.EMPTY
    }

    @JvmStatic
    public fun cuboid(aabb: OmniAABB): OmniVoxelShape {
        return OmniVoxelShapeImpl.cuboid(aabb)
    }

    @JvmStatic
    public fun fullCube(): OmniVoxelShape {
        return OmniVoxelShapeImpl.fullCube()
    }
}
