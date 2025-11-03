package dev.deftu.omnicore.internal.data.shape

import dev.deftu.omnicore.api.data.aabb.OmniAABB
import dev.deftu.omnicore.api.data.shape.OmniVoxelShape
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.world.phys.shapes.Shapes
import java.util.function.Consumer

public data class OmniVoxelShapeImpl(override val vanilla: VoxelShape) : OmniVoxelShape {
    public companion object {
        @JvmField public val EMPTY: OmniVoxelShape = OmniVoxelShapeImpl(Shapes.empty())

        @JvmStatic
        public fun cuboid(aabb: OmniAABB): OmniVoxelShape {
            return OmniVoxelShapeImpl(Shapes.create(aabb.vanilla))
        }

        @JvmStatic
        public fun fullCube(): OmniVoxelShape {
            return OmniVoxelShapeImpl(Shapes.block())
        }
    }

    override val isEmpty: Boolean
        get() = vanilla.isEmpty

    override val isFullCube: Boolean
        get() = areShapesEqual(vanilla, Shapes.block())

    override val size: Int
        get() {
            var count = 0
            vanilla.forAllBoxes { _, _, _, _, _, _ -> count++ }
            return count
        }

    override val boxes: List<OmniAABB>
        get() {
            return buildList {
                vanilla.forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
                    add(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
                }
            }
        }

    override val edges: List<OmniAABB>
        get() {
            return buildList {
                vanilla.forAllEdges { minX, minY, minZ, maxX, maxY, maxZ ->
                    add(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
                }
            }
        }

    override fun forEachBox(consumer: Consumer<OmniAABB>) {
        vanilla.forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
            consumer.accept(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
        }
    }

    override fun forEachEdge(consumer: Consumer<OmniAABB>) {
        vanilla.forAllEdges { minX, minY, minZ, maxX, maxY, maxZ ->
            consumer.accept(OmniAABB(minX, minY, minZ, maxX, maxY, maxZ))
        }
    }

    override fun offset(
        x: Double,
        y: Double,
        z: Double
    ): OmniVoxelShape {
        return OmniVoxelShapeImpl(vanilla.move(x, y, z))
    }

    override fun union(other: OmniVoxelShape): OmniVoxelShape {
        return OmniVoxelShapeImpl(Shapes.or(vanilla, other.vanilla))
    }

    override fun union(box: OmniAABB): OmniVoxelShape {
        return OmniVoxelShapeImpl(Shapes.or(vanilla, Shapes.create(box.vanilla)))
    }

    override fun intersect(other: OmniVoxelShape): OmniVoxelShape {
        return OmniVoxelShapeImpl(Shapes.join(vanilla, other.vanilla, BooleanOp.AND))
    }

    override fun intersect(box: OmniAABB): OmniVoxelShape {
        return OmniVoxelShapeImpl(Shapes.join(vanilla, Shapes.create(box.vanilla), BooleanOp.AND))
    }

    override fun subtract(other: OmniVoxelShape): OmniVoxelShape {
        return OmniVoxelShapeImpl(Shapes.join(vanilla, other.vanilla, BooleanOp.ONLY_FIRST))
    }

    override fun subtract(box: OmniAABB): OmniVoxelShape {
        return OmniVoxelShapeImpl(Shapes.join(vanilla, Shapes.create(box.vanilla), BooleanOp.ONLY_FIRST))
    }

    override fun simplify(): OmniVoxelShape {
        return OmniVoxelShapeImpl(vanilla.optimize())
    }

    override fun contains(x: Double, y: Double, z: Double): Boolean {
        var result = false
        vanilla.forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
            if (x in minX..maxX && y in minY..maxY && z in minZ..maxZ) {
                result = true
            }
        }

        return result
    }

    override fun intersects(box: OmniAABB): Boolean {
        var result = false
        vanilla.forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
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
        vanilla.forAllBoxes { minX, minY, minZ, maxX, maxY, maxZ ->
            val current = OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
            result = current.vanilla.clip(from.vanilla, to.vanilla).isPresent
        }

        return result
    }

    private fun areShapesEqual(first: VoxelShape, second: VoxelShape): Boolean {
        return !Shapes.joinIsNotEmpty(first, second, BooleanOp.NOT_SAME)
    }
}
