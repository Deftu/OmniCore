package dev.deftu.omnicore.api.data.pos

import com.mojang.serialization.Codec
import dev.deftu.omnicore.api.direction.OmniDirectionalAxis
import dev.deftu.omnicore.api.direction.OmniSpatialDirection
import dev.deftu.omnicore.api.data.DistanceMetric
import dev.deftu.omnicore.api.data.RadiusMetric
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import dev.deftu.omnicore.api.data.vec.OmniVec3i
import dev.deftu.omnicore.api.serialization.OmniDataResult
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i

public data class OmniBlockPos(
    public val x: Int,
    public val y: Int,
    public val z: Int,
) {
    public companion object {
        @JvmField public val ZERO: OmniBlockPos = OmniBlockPos(0, 0, 0)
        @JvmField public val ONE: OmniBlockPos = OmniBlockPos(1, 1, 1)

        @JvmField
        public val CODEC: Codec<OmniBlockPos> = Codec.INT
            .listOf()
            .comapFlatMap(
                {
                    OmniDataResult.decodeFixedLengthList(it, 3).map { list ->
                        OmniBlockPos(list[0], list[1], list[2])
                    }
                },
                { listOf(it.x, it.y, it.z) },
            )

        @JvmStatic
        public fun unpack(value: Long): OmniBlockPos {
            return OmniBlockPos(BlockPos.of(value))
        }
    }

    @get:JvmName("toVanilla")
    public val vanilla: BlockPos
        get() = BlockPos(x, y, z)

    @get:JvmName("center")
    public val centered: OmniVec3d
        get() = OmniVec3d(x + 0.5, y + 0.5, z + 0.5)

    public val neighbors: Sequence<OmniBlockPos>
        get() = sequence {
            for (direction in OmniSpatialDirection.ALL) {
                yield(offset(direction))
            }
        }

    public constructor(vanilla: BlockPos) : this(vanilla.x, vanilla.y, vanilla.z)

    public constructor(vec: OmniVec3i) : this(vec.x, vec.y, vec.z)

    public constructor(vec: Vec3i) : this(vec.x, vec.y, vec.z)

    public constructor(values: Triple<Int, Int, Int>) : this(values.first, values.second, values.third)

    public constructor(value: Int) : this(value, value, value)

    public fun distanceTo(other: OmniBlockPos, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x.toDouble(), this.y.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.y.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(other: BlockPos, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x.toDouble(), this.y.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.y.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(other: OmniVec3i, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x.toDouble(), this.y.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.y.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(other: Vec3i, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x.toDouble(), this.y.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.y.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(x: Int, y: Int, z: Int, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x.toDouble(), this.y.toDouble(), this.z.toDouble(),
            x.toDouble(), y.toDouble(), z.toDouble(),
        )
    }

    @JvmOverloads
    public fun offset(direction: OmniSpatialDirection, distance: Int = 1): OmniBlockPos {
        return OmniBlockPos(vanilla.relative(direction.vanilla, distance))
    }

    @JvmOverloads
    public fun radius2D(
        radius: Int,
        metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
        y: Int = this.y,
        includeCenter: Boolean = true,
    ): Sequence<OmniBlockPos> {
        return sequence {
            require(radius >= 0) { "radius must be >= 0" }

            val radiusSq = radius * radius
            val (cx, cz) = intArrayOf(this@OmniBlockPos.x, this@OmniBlockPos.z)
            for (dz in -radius..radius) {
                for (dx in -radius..radius) {
                    if (
                        !metric.isInside2D(dx, dz, radius, radiusSq) ||
                        (!includeCenter && dx == 0 && dz == 0 && y == this@OmniBlockPos.y)
                    ) {
                        continue
                    }

                    yield(OmniBlockPos(cx + dx, y, cz + dz))
                }
            }
        }
    }

    @JvmOverloads
    public fun radius3D(
        radius: Int,
        metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
        includeCenter: Boolean = true,
    ): Sequence<OmniBlockPos> {
        return sequence {
            require(radius >= 0) { "radius must be >= 0" }

            val radiusSq = radius * radius
            val (cx, cy, cz) = intArrayOf(this@OmniBlockPos.x, this@OmniBlockPos.y, this@OmniBlockPos.z)
            for (dz in -radius..radius) {
                for (dy in -radius..radius) {
                    for (dx in -radius..radius) {
                        if (
                            !metric.isInside3D(dx, dy, dz, radius, radiusSq) ||
                            (!includeCenter && dx == 0 && dy == 0 && dz == 0)
                        ) {
                            continue
                        }

                        yield(OmniBlockPos(cx + dx, cy + dy, cz + dz))
                    }
                }
            }
        }
    }

    public operator fun get(axis: OmniDirectionalAxis): Int {
        return when (axis) {
            OmniDirectionalAxis.X -> x
            OmniDirectionalAxis.Y -> y
            OmniDirectionalAxis.Z -> z
        }
    }

    public operator fun plus(other: OmniBlockPos): OmniBlockPos {
        return OmniBlockPos(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    public operator fun plus(other: BlockPos): OmniBlockPos {
        return this.plus(OmniBlockPos(other))
    }

    public operator fun plus(other: OmniVec3i): OmniBlockPos {
        return OmniBlockPos(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    public operator fun plus(other: Vec3i): OmniBlockPos {
        return this.plus(OmniVec3i(other))
    }

    public operator fun plus(other: Int): OmniBlockPos {
        return OmniBlockPos(this.x + other, this.y + other, this.z + other)
    }

    public operator fun minus(other: OmniBlockPos): OmniBlockPos {
        return OmniBlockPos(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    public operator fun minus(other: BlockPos): OmniBlockPos {
        return this.minus(OmniBlockPos(other))
    }

    public operator fun minus(other: OmniVec3i): OmniBlockPos {
        return OmniBlockPos(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    public operator fun minus(other: Vec3i): OmniBlockPos {
        return this.minus(OmniVec3i(other))
    }

    public operator fun minus(other: Int): OmniBlockPos {
        return OmniBlockPos(this.x - other, this.y - other, this.z - other)
    }

    public operator fun times(other: OmniBlockPos): OmniBlockPos {
        return OmniBlockPos(this.x * other.x, this.y * other.y, this.z * other.z)
    }

    public operator fun times(other: BlockPos): OmniBlockPos {
        return this.times(OmniBlockPos(other))
    }

    public operator fun times(other: OmniVec3i): OmniBlockPos {
        return OmniBlockPos(this.x * other.x, this.y * other.y, this.z * other.z)
    }

    public operator fun times(other: Vec3i): OmniBlockPos {
        return OmniBlockPos(this.x * other.x, this.y * other.y, this.z * other.z)
    }

    public operator fun times(other: Int): OmniBlockPos {
        return OmniBlockPos(this.x * other, this.y * other, this.z * other)
    }

    public operator fun div(other: OmniBlockPos): OmniBlockPos {
        return OmniBlockPos(this.x / other.x, this.y / other.y, this.z / other.z)
    }

    public operator fun div(other: BlockPos): OmniBlockPos {
        return this.div(OmniBlockPos(other))
    }

    public operator fun div(other: OmniVec3i): OmniBlockPos {
        return OmniBlockPos(this.x / other.x, this.y / other.y, this.z / other.z)
    }

    public operator fun div(other: Vec3i): OmniBlockPos {
        return OmniBlockPos(this.x / other.x, this.y / other.y, this.z / other.z)
    }

    public operator fun div(other: Int): OmniBlockPos {
        return OmniBlockPos(this.x / other, this.y / other, this.z / other)
    }

    public fun withX(x: Int): OmniBlockPos {
        return OmniBlockPos(x, this.y, this.z)
    }

    public fun withY(y: Int): OmniBlockPos {
        return OmniBlockPos(this.x, y, this.z)
    }

    public fun withZ(z: Int): OmniBlockPos {
        return OmniBlockPos(this.x, this.y, z)
    }

    public fun pack(): Long {
        return vanilla.asLong()
    }
}
