package dev.deftu.omnicore.internal.data.shape

import dev.deftu.omnicore.api.data.aabb.OmniAABB
import dev.deftu.omnicore.api.data.shape.OmniVoxelShape
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import java.util.function.Consumer

public data class OmniVoxelShapeImpl(override val vanilla: VoxelShape) : OmniVoxelShape {
    public companion object {
        @JvmField public val EMPTY: OmniVoxelShape = OmniVoxelShapeImpl(VoxelShapes.empty())

        @JvmStatic
        public fun cuboid(aabb: OmniAABB): OmniVoxelShape {
            return OmniVoxelShapeImpl(VoxelShapes.cuboid(aabb.vanilla))
        }

        @JvmStatic
        public fun fullCube(): OmniVoxelShape {
            return OmniVoxelShapeImpl(VoxelShapes.fullCube())
        }
    }

    override val isEmpty: Boolean
        get() = vanilla.isEmpty

    override val isFullCube: Boolean
        get() = areShapesEqual(vanilla, VoxelShapes.fullCube())

    override val size: Int
        get() {
            var count = 0
            vanilla.forEachBox { _, _, _, _, _, _ -> count++ }
            return count
        }

    override val boxes: List<OmniAABB>
        get() {
            return buildList {
                vanilla.forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
                    add(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
                }
            }
        }

    override val edges: List<OmniAABB>
        get() {
            return buildList {
                vanilla.forEachEdge { minX, minY, minZ, maxX, maxY, maxZ ->
                    add(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
                }
            }
        }

    override fun forEachBox(consumer: Consumer<OmniAABB>) {
        vanilla.forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            consumer.accept(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
        }
    }

    override fun forEachEdge(consumer: Consumer<OmniAABB>) {
        vanilla.forEachEdge { minX, minY, minZ, maxX, maxY, maxZ ->
            consumer.accept(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
        }
    }

    override fun offset(
        x: Double,
        y: Double,
        z: Double
    ): OmniVoxelShape {
        return OmniVoxelShapeImpl(vanilla.offset(x, y, z))
    }

    override fun union(other: OmniVoxelShape): OmniVoxelShape {
        return OmniVoxelShapeImpl(VoxelShapes.union(vanilla, other.vanilla))
    }

    override fun union(box: OmniAABB): OmniVoxelShape {
        return OmniVoxelShapeImpl(VoxelShapes.union(vanilla, VoxelShapes.cuboid(box.vanilla)))
    }

    override fun intersect(other: OmniVoxelShape): OmniVoxelShape {
        return OmniVoxelShapeImpl(VoxelShapes.combineAndSimplify(vanilla, other.vanilla, BooleanBiFunction.AND))
    }

    override fun intersect(box: OmniAABB): OmniVoxelShape {
        return OmniVoxelShapeImpl(VoxelShapes.combineAndSimplify(vanilla, VoxelShapes.cuboid(box.vanilla), BooleanBiFunction.AND))
    }

    override fun subtract(other: OmniVoxelShape): OmniVoxelShape {
        return OmniVoxelShapeImpl(VoxelShapes.combineAndSimplify(vanilla, other.vanilla, BooleanBiFunction.ONLY_FIRST))
    }

    override fun subtract(box: OmniAABB): OmniVoxelShape {
        return OmniVoxelShapeImpl(VoxelShapes.combineAndSimplify(vanilla, VoxelShapes.cuboid(box.vanilla), BooleanBiFunction.ONLY_FIRST))
    }

    override fun simplify(): OmniVoxelShape {
        return OmniVoxelShapeImpl(vanilla.simplify())
    }

    override fun contains(x: Double, y: Double, z: Double): Boolean {
        var result = false
        vanilla.forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            if (x in minX..maxX && y in minY..maxY && z in minZ..maxZ) {
                result = true
            }
        }

        return result
    }

    override fun intersects(box: OmniAABB): Boolean {
        var result = false
        vanilla.forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            if (box.maxX > minX && box.minX < maxX &&
                box.maxY > minY && box.minY < maxY &&
                box.maxZ > minZ && box.minZ < maxZ
            ) {
                result = true
            }
        }

        return result
    }

    override fun raycast(
        from: OmniVec3d,
        to: OmniVec3d
    ): Boolean {
        var result = false
        vanilla.forEachBox { minX, minY, minZ, maxX, maxY, maxZ ->
            val current = OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
            result = current.vanilla.raycast(from.vanilla, to.vanilla).isPresent
        }

        return result
    }

    private fun areShapesEqual(first: VoxelShape, second: VoxelShape): Boolean {
        return !VoxelShapes.matchesAnywhere(first, second, BooleanBiFunction.NOT_SAME)
    }
}
