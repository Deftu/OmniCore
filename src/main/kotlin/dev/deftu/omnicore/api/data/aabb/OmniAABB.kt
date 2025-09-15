package dev.deftu.omnicore.api.data.aabb

import com.mojang.serialization.Codec
import dev.deftu.omnicore.api.OmniDirectionalAxis
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import dev.deftu.omnicore.api.serialization.OmniDataResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d

public data class OmniAABB(
    public val minX: Double,
    public val minY: Double,
    public val minZ: Double,
    public val maxX: Double,
    public val maxY: Double,
    public val maxZ: Double,
) {
    public companion object {
        @JvmField public val ZERO: OmniAABB = OmniAABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        @JvmField public val UNIT: OmniAABB = OmniAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)

        @JvmField
        public val CODEC: Codec<OmniAABB> = Codec.DOUBLE
            .listOf()
            .comapFlatMap(
                {
                    OmniDataResult.decodeFixedLengthList(it, 6).map { list ->
                        OmniAABB(list[0], list[1], list[2], list[3], list[4], list[5])
                    }
                },
                { listOf(it.minX, it.minY, it.minZ, it.maxX, it.maxY, it.maxZ) },
            )

        @JvmStatic
        public fun enclosing(first: BlockPos, second: BlockPos): OmniAABB {
            val minX = minOf(first.x, second.x).toDouble()
            val minY = minOf(first.y, second.y).toDouble()
            val minZ = minOf(first.z, second.z).toDouble()
            val maxX = (maxOf(first.x, second.x) + 1).toDouble()
            val maxY = (maxOf(first.y, second.y) + 1).toDouble()
            val maxZ = (maxOf(first.z, second.z) + 1).toDouble()
            return OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
        }

        @JvmStatic
        public fun enclosing(first: OmniAABB, second: OmniAABB): OmniAABB {
            val minX = minOf(first.minX, second.minX)
            val minY = minOf(first.minY, second.minY)
            val minZ = minOf(first.minZ, second.minZ)
            val maxX = maxOf(first.maxX, second.maxX)
            val maxY = maxOf(first.maxY, second.maxY)
            val maxZ = maxOf(first.maxZ, second.maxZ)
            return OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
        }
    }

    @get:JvmName("toVanilla")
    public val vanilla: Box
        get() = Box(minX, minY, minZ, maxX, maxY, maxZ)

    public val lengthX: Double
        get() = maxX - minX

    public val lengthY: Double
        get() = maxY - minY

    public val lengthZ: Double
        get() = maxZ - minZ

    public val averageSideLength: Double
        get() = (lengthX + lengthY + lengthZ) / 3.0

    public val min: OmniVec3d
        get() = OmniVec3d(minX, minY, minZ)

    public val max: OmniVec3d
        get() = OmniVec3d(maxX, maxY, maxZ)

    public constructor(aabb: Box) : this(
        aabb.minX, aabb.minY, aabb.minZ,
        aabb.maxX, aabb.maxY, aabb.maxZ,
    )

    public constructor(
        min: Triple<Double, Double, Double>,
        max: Triple<Double, Double, Double>,
    ) : this(
        min.first, min.second, min.third,
        max.first, max.second, max.third,
    )

    public constructor(
        min: OmniVec3d,
        max: OmniVec3d,
    ) : this(
        min.x, min.y, min.z,
        max.x, max.y, max.z,
    )

    public constructor(
        min: Vec3d,
        max: Vec3d,
    ) : this(
        min.x, min.y, min.z,
        max.x, max.y, max.z,
    )

    public constructor(other: OmniVec3d) : this(
        other.x, other.y, other.z,
        other.x, other.y, other.z,
    )

    public constructor(other: Vec3d) : this(
        other.x, other.y, other.z,
        other.x, other.y, other.z,
    )

    public constructor(min: Double, max: Double) : this(
        min, min, min,
        max, max, max,
    )

    public constructor(pos: BlockPos) : this(
        pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
        pos.x + 1.0, pos.y + 1.0, pos.z + 1.0,
    )

    public fun shrink(x: Double, y: Double, z: Double): OmniAABB {
        var minX = this.minX
        var minY = this.minY
        var minZ = this.minZ
        var maxX = this.maxX
        var maxY = this.maxY
        var maxZ = this.maxZ

        if (x < 0.0) {
            minX -= x
        } else if (x > 0.0) {
            maxX -= x
        }

        if (y < 0.0) {
            minY -= y
        } else if (y > 0.0) {
            maxY -= y
        }

        if (z < 0.0) {
            minZ -= z
        } else if (z > 0.0) {
            maxZ -= z
        }

        return OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    public fun shrink(amount: Double): OmniAABB {
        return shrink(amount, amount, amount)
    }

    public fun shrink(vec: OmniVec3d): OmniAABB {
        return shrink(vec.x, vec.y, vec.z)
    }

    public fun shrink(vec: Vec3d): OmniAABB {
        return shrink(vec.x, vec.y, vec.z)
    }

    public fun stretch(x: Double, y: Double, z: Double): OmniAABB {
        var minX = this.minX
        var minY = this.minY
        var minZ = this.minZ
        var maxX = this.maxX
        var maxY = this.maxY
        var maxZ = this.maxZ

        if (x < 0.0) {
            maxX -= x
        } else if (x > 0.0) {
            minX -= x
        }

        if (y < 0.0) {
            maxY -= y
        } else if (y > 0.0) {
            minY -= y
        }

        if (z < 0.0) {
            maxZ -= z
        } else if (z > 0.0) {
            minZ -= z
        }

        return OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    public fun stretch(amount: Double): OmniAABB {
        return stretch(amount, amount, amount)
    }

    public fun stretch(vec: OmniVec3d): OmniAABB {
        return stretch(vec.x, vec.y, vec.z)
    }

    public fun stretch(vec: Vec3d): OmniAABB {
        return stretch(vec.x, vec.y, vec.z)
    }

    public fun expand(x: Double, y: Double, z: Double): OmniAABB {
        return OmniAABB(
            this.minX - x, this.minY - y, this.minZ - z,
            this.maxX + x, this.maxY + y, this.maxZ + z
        )
    }

    public fun expand(amount: Double): OmniAABB {
        return expand(amount, amount, amount)
    }

    public fun expand(vec: OmniVec3d): OmniAABB {
        return expand(vec.x, vec.y, vec.z)
    }

    public fun expand(vec: Vec3d): OmniAABB {
        return expand(vec.x, vec.y, vec.z)
    }

    public fun contract(x: Double, y: Double, z: Double): OmniAABB {
        return expand(-x, -y, -z)
    }

    public fun contract(amount: Double): OmniAABB {
        return contract(amount, amount, amount)
    }

    public fun contract(vec: OmniVec3d): OmniAABB {
        return contract(vec.x, vec.y, vec.z)
    }

    public fun contract(vec: Vec3d): OmniAABB {
        return contract(vec.x, vec.y, vec.z)
    }

    public fun offset(x: Double, y: Double, z: Double): OmniAABB {
        return OmniAABB(
            this.minX + x, this.minY + y, this.minZ + z,
            this.maxX + x, this.maxY + y, this.maxZ + z
        )
    }

    public fun offset(amount: Double): OmniAABB {
        return offset(amount, amount, amount)
    }

    public fun offset(vec: OmniVec3d): OmniAABB {
        return offset(vec.x, vec.y, vec.z)
    }

    public fun offset(vec: Vec3d): OmniAABB {
        return offset(vec.x, vec.y, vec.z)
    }

    public fun intersection(other: OmniAABB): OmniAABB {
        val minX = maxOf(this.minX, other.minX)
        val minY = maxOf(this.minY, other.minY)
        val minZ = maxOf(this.minZ, other.minZ)
        val maxX = minOf(this.maxX, other.maxX)
        val maxY = minOf(this.maxY, other.maxY)
        val maxZ = minOf(this.maxZ, other.maxZ)
        return OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    public fun intersection(other: Box): OmniAABB {
        return intersection(OmniAABB(other))
    }

    public fun union(other: OmniAABB): OmniAABB {
        val minX = minOf(this.minX, other.minX)
        val minY = minOf(this.minY, other.minY)
        val minZ = minOf(this.minZ, other.minZ)
        val maxX = maxOf(this.maxX, other.maxX)
        val maxY = maxOf(this.maxY, other.maxY)
        val maxZ = maxOf(this.maxZ, other.maxZ)
        return OmniAABB(minX, minY, minZ, maxX, maxY, maxZ)
    }

    public fun union(other: Box): OmniAABB {
        return union(OmniAABB(other))
    }

    public fun min(axis: OmniDirectionalAxis): Double {
        return axis.chooseFrom(minX, minY, minZ)
    }

    public fun max(axis: OmniDirectionalAxis): Double {
        return axis.chooseFrom(maxX, maxY, maxZ)
    }

    public fun isIntersecting(
        minX: Double, minY: Double, minZ: Double,
        maxX: Double, maxY: Double, maxZ: Double
    ): Boolean {
        return this.maxX > minX && this.minX < maxX &&
               this.maxY > minY && this.minY < maxY &&
               this.maxZ > minZ && this.minZ < maxZ
    }

    public fun isIntersecting(other: OmniAABB): Boolean {
        return isIntersecting(
            other.minX, other.minY, other.minZ,
            other.maxX, other.maxY, other.maxZ
        )
    }

    public fun isIntersecting(other: Box): Boolean {
        return isIntersecting(OmniAABB(other))
    }

    public fun isIntersecting(other: OmniVec3d): Boolean {
        return isIntersecting(OmniAABB(other))
    }

    public fun isIntersecting(other: Vec3d): Boolean {
        return isIntersecting(OmniAABB(other))
    }

    public fun isIntersecting(first: OmniVec3d, second: OmniVec3d): Boolean {
        return isIntersecting(OmniAABB(first, second))
    }

    public fun isIntersecting(first: Vec3d, second: Vec3d): Boolean {
        return isIntersecting(OmniAABB(first, second))
    }

    public fun contains(
        minX: Double, minY: Double, minZ: Double,
        maxX: Double, maxY: Double, maxZ: Double
    ): Boolean {
        return this.minX <= minX && this.maxX >= maxX &&
               this.minY <= minY && this.maxY >= maxY &&
               this.minZ <= minZ && this.maxZ >= maxZ
    }

    public operator fun contains(other: OmniAABB): Boolean {
        return contains(
            other.minX, other.minY, other.minZ,
            other.maxX, other.maxY, other.maxZ
        )
    }

    public operator fun contains(other: Box): Boolean {
        return contains(OmniAABB(other))
    }

    public operator fun contains(other: OmniVec3d): Boolean {
        return contains(OmniAABB(other))
    }

    public operator fun contains(other: Vec3d): Boolean {
        return contains(OmniAABB(other))
    }

    public operator fun contains(pos: BlockPos): Boolean {
        return isIntersecting(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), pos.x + 1.0, pos.y + 1.0, pos.z + 1.0)
    }

    public fun withMinX(minX: Double): OmniAABB {
        return OmniAABB(minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ)
    }

    public fun withMinY(minY: Double): OmniAABB {
        return OmniAABB(this.minX, minY, this.minZ, this.maxX, this.maxY, this.maxZ)
    }

    public fun withMinZ(minZ: Double): OmniAABB {
        return OmniAABB(this.minX, this.minY, minZ, this.maxX, this.maxY, this.maxZ)
    }

    public fun withMaxX(maxX: Double): OmniAABB {
        return OmniAABB(this.minX, this.minY, this.minZ, maxX, this.maxY, this.maxZ)
    }

    public fun withMaxY(maxY: Double): OmniAABB {
        return OmniAABB(this.minX, this.minY, this.minZ, this.maxX, maxY, this.maxZ)
    }

    public fun withMaxZ(maxZ: Double): OmniAABB {
        return OmniAABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, maxZ)
    }
}
