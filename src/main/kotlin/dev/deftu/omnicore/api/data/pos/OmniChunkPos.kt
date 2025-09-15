package dev.deftu.omnicore.api.data.pos

import com.mojang.serialization.Codec
import dev.deftu.omnicore.api.OmniDirectionalAxis
import dev.deftu.omnicore.api.data.DistanceMetric
import dev.deftu.omnicore.api.data.RadiusMetric
import dev.deftu.omnicore.api.data.vec.OmniVec3i
import dev.deftu.omnicore.api.serialization.OmniDataResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3i

public data class OmniChunkPos(
    public val x: Int,
    public val z: Int,
) {
    public companion object {
        public const val CHUNK_SIZE: Int = 16

        @JvmField public val ZERO: OmniChunkPos = OmniChunkPos(0, 0)
        @JvmField public val ONE: OmniChunkPos = OmniChunkPos(1, 1)

        @JvmField
        public val CODEC: Codec<OmniChunkPos> = Codec.INT
            .listOf()
            .comapFlatMap(
                {
                    OmniDataResult.decodeFixedLengthList(it, 2).map { list ->
                        OmniChunkPos(list[0], list[1])
                    }
                },
                { listOf(it.x, it.z) },
            )

        @JvmStatic
        public fun unpack(value: Long): OmniChunkPos {
            //#if MC >= 1.16.5
            return OmniChunkPos(ChunkPos(value))
            //#else
            //$$ val x = value.toInt()
            //$$ val z = (value ushr 32).toInt()
            //$$ return OmniChunkPos(ChunkPos(x, z))
            //#endif
        }

        @JvmStatic
        public fun getSectionCoord(coord: Int): Int {
            return coord shr 4
        }

        @JvmStatic
        public fun getBlockCoord(sectionCoord: Int): Int {
            return sectionCoord shl 4
        }

        @JvmStatic
        public fun getOffset(sectionCoord: Int, offset: Int): Int {
            return getBlockCoord(sectionCoord) + offset
        }
    }

    @get:JvmName("toVanilla")
    public val vanilla: ChunkPos
        get() = ChunkPos(x, z)

    public val centerX: Int
        get() = getOffsetX(CHUNK_SIZE / 2)

   public val centerZ: Int
        get() = getOffsetZ(CHUNK_SIZE / 2)

    public val startX: Int
        get() = getBlockCoord(x)

    public val startZ: Int
        get() = getBlockCoord(z)

    public val endX: Int
        get() = getOffsetX(CHUNK_SIZE - 1)

    public val endZ: Int
        get() = getOffsetZ(CHUNK_SIZE - 1)

    public val neighbors: Sequence<OmniChunkPos>
        get() = sequence {
            val cx = this@OmniChunkPos.x
            val cz = this@OmniChunkPos.z
            for (dz in -1..1) {
                for (dx in -1..1) {
                    if (dx == 0 && dz == 0) continue
                    yield(OmniChunkPos(cx + dx, cz + dz))
                }
            }
        }

    public constructor(vanilla: ChunkPos) : this(vanilla.x, vanilla.z)

    public constructor(pos: OmniBlockPos) : this(getSectionCoord(pos.x), getSectionCoord(pos.z))

    public constructor(pos: BlockPos) : this(getSectionCoord(pos.x), getSectionCoord(pos.z))

    public constructor(vec: OmniVec3i) : this(getSectionCoord(vec.x), getSectionCoord(vec.z))

    public constructor(vec: Vec3i) : this(getSectionCoord(vec.x), getSectionCoord(vec.z))

    public constructor(values: Pair<Int, Int>) : this(values.first, values.second)

    public constructor(value: Int) : this(value, value)

    public fun distanceTo(other: OmniChunkPos, metric: DistanceMetric): Double {
        return metric.distance2D(
            this.x.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(other: ChunkPos, metric: DistanceMetric): Double {
        return metric.distance2D(
            this.x.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(other: OmniVec3i, metric: DistanceMetric): Double {
        return metric.distance2D(
            this.x.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(other: Vec3i, metric: DistanceMetric): Double {
        return metric.distance2D(
            this.x.toDouble(), this.z.toDouble(),
            other.x.toDouble(), other.z.toDouble(),
        )
    }

    public fun distanceTo(x: Int, z: Int, metric: DistanceMetric): Double {
        return metric.distance2D(
            this.x.toDouble(), this.z.toDouble(),
            x.toDouble(), z.toDouble(),
        )
    }

    @JvmOverloads
    public fun radius(
        radius: Int,
        metric: RadiusMetric = RadiusMetric.CHEBYSHEV,
        includeCenter: Boolean = true,
    ): Sequence<OmniChunkPos> {
        return sequence {
            require(radius >= 0) { "radius must be >= 0" }

            val cx = this@OmniChunkPos.x
            val cz = this@OmniChunkPos.z
            val r2 = radius * radius
            for (dz in -radius..radius) {
                for (dx in -radius..radius) {
                    if (!metric.isInside2D(dx, dz, radius, r2)) continue
                    if (!includeCenter && dx == 0 && dz == 0) continue
                    yield(OmniChunkPos(cx + dx, cz + dz))
                }
            }
        }
    }

    public operator fun get(localX: Int, y: Int, localZ: Int): OmniBlockPos {
        return OmniBlockPos(getOffsetX(localX), y, getOffsetZ(localZ))
    }

    public operator fun get(axis: OmniDirectionalAxis): Int {
        return when (axis) {
            OmniDirectionalAxis.X -> x
            OmniDirectionalAxis.Z -> z
            else -> throw IllegalArgumentException("Cannot get ${axis.name} from ChunkPos")
        }
    }

    public operator fun plus(other: OmniChunkPos): OmniChunkPos {
        return OmniChunkPos(this.x + other.x, this.z + other.z)
    }

    public operator fun plus(other: ChunkPos): OmniChunkPos {
        return this.plus(OmniChunkPos(other))
    }

    public operator fun plus(other: OmniVec3i): OmniChunkPos {
        return OmniChunkPos(this.x + other.x, this.z + other.z)
    }

    public operator fun plus(other: Vec3i): OmniChunkPos {
        return this.plus(OmniVec3i(other))
    }

    public operator fun plus(other: Int): OmniChunkPos {
        return OmniChunkPos(this.x + other, this.z + other)
    }

    public operator fun minus(other: OmniChunkPos): OmniChunkPos {
        return OmniChunkPos(this.x - other.x, this.z - other.z)
    }

    public operator fun minus(other: ChunkPos): OmniChunkPos {
        return this.minus(OmniChunkPos(other))
    }

    public operator fun minus(other: OmniVec3i): OmniChunkPos {
        return OmniChunkPos(this.x - other.x, this.z - other.z)
    }

    public operator fun minus(other: Vec3i): OmniChunkPos {
        return this.minus(OmniVec3i(other))
    }

    public operator fun minus(other: Int): OmniChunkPos {
        return OmniChunkPos(this.x - other, this.z - other)
    }

    public operator fun times(other: OmniChunkPos): OmniChunkPos {
        return OmniChunkPos(this.x * other.x, this.z * other.z)
    }

    public operator fun times(other: ChunkPos): OmniChunkPos {
        return this.times(OmniChunkPos(other))
    }

    public operator fun times(other: OmniVec3i): OmniChunkPos {
        return OmniChunkPos(this.x * other.x, this.z * other.z)
    }

    public operator fun times(other: Vec3i): OmniChunkPos {
        return this.times(OmniVec3i(other))
    }

    public operator fun times(other: Int): OmniChunkPos {
        return OmniChunkPos(this.x * other, this.z * other)
    }

    public operator fun div(other: OmniChunkPos): OmniChunkPos {
        return OmniChunkPos(this.x / other.x, this.z / other.z)
    }

    public operator fun div(other: ChunkPos): OmniChunkPos {
        return this.div(OmniChunkPos(other))
    }

    public operator fun div(other: OmniVec3i): OmniChunkPos {
        return OmniChunkPos(this.x / other.x, this.z / other.z)
    }

    public operator fun div(other: Vec3i): OmniChunkPos {
        return this.div(OmniVec3i(other))
    }

    public operator fun div(other: Int): OmniChunkPos {
        return OmniChunkPos(this.x / other, this.z / other)
    }

    public fun getOffsetX(offset: Int): Int {
        return getOffset(this.x, offset)
    }

    public fun getOffsetZ(offset: Int): Int {
        return getOffset(this.z, offset)
    }

    public fun withX(x: Int): OmniChunkPos {
        return OmniChunkPos(x, this.z)
    }

    public fun withZ(z: Int): OmniChunkPos {
        return OmniChunkPos(this.x, z)
    }

    public fun pack(): Long {
        return ChunkPos.toLong(this.x, this.z)
    }
}
