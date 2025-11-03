package dev.deftu.omnicore.api.data.shape

import dev.deftu.omnicore.api.data.aabb.OmniAABB
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer

//#if MC >= 1.16.5
import net.minecraft.world.phys.shapes.VoxelShape
//#endif

@ApiStatus.Experimental
public interface OmniVoxelShape {
    //#if MC >= 1.16.5
    public val vanilla: VoxelShape
    //#endif

    public val isEmpty: Boolean
    public val isFullCube: Boolean
    public val size: Int
    public val boxes: List<OmniAABB>
    public val edges: List<OmniAABB>

    public fun forEachBox(consumer: Consumer<OmniAABB>)
    public fun forEachEdge(consumer: Consumer<OmniAABB>)

    public fun offset(x: Double, y: Double, z: Double): OmniVoxelShape
    public fun union(other: OmniVoxelShape): OmniVoxelShape
    public fun union(box: OmniAABB): OmniVoxelShape
    public fun intersect(other: OmniVoxelShape): OmniVoxelShape
    public fun intersect(box: OmniAABB): OmniVoxelShape
    public fun subtract(other: OmniVoxelShape): OmniVoxelShape
    public fun subtract(box: OmniAABB): OmniVoxelShape
    public fun simplify(): OmniVoxelShape
    public fun contains(x: Double, y: Double, z: Double): Boolean
    public fun intersects(box: OmniAABB): Boolean
    public fun raycast(from: OmniVec3d, to: OmniVec3d): Boolean

    public operator fun contains(point: OmniVec3d): Boolean {
        return contains(point.x, point.y, point.z)
    }
}
