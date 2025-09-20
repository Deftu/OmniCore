package dev.deftu.omnicore.api.data.vec

import com.mojang.serialization.Codec
import dev.deftu.omnicore.api.direction.OmniDirectionalAxis
import dev.deftu.omnicore.api.data.DistanceMetric
import dev.deftu.omnicore.api.serialization.OmniDataResult
import net.minecraft.util.math.Vec3i

public data class OmniVec3i(
    public val x: Int,
    public val y: Int,
    public val z: Int,
) {
    public companion object {
        @JvmField public val ZERO: OmniVec3i = OmniVec3i(0, 0, 0)
        @JvmField public val ONE: OmniVec3i = OmniVec3i(1, 1, 1)

        @JvmField
        public val CODEC: Codec<OmniVec3i> = Codec.INT
            .listOf()
            .comapFlatMap(
                {
                    OmniDataResult.decodeFixedLengthList(it, 3).map { list ->
                        OmniVec3i(list[0], list[1], list[2])
                    }
                },
                { listOf(it.x, it.y, it.z) },
            )
    }

    @get:JvmName("toVanilla")
    public val vanilla: Vec3i
        get() = Vec3i(x, y, z)

    @get:JvmName("toVec3d")
    public val vec3d: OmniVec3d
        get() = OmniVec3d(x.toDouble(), y.toDouble(), z.toDouble())

    public constructor(vanilla: Vec3i) : this(vanilla.x, vanilla.y, vanilla.z)

    public constructor(values: Triple<Int, Int, Int>) : this(values.first, values.second, values.third)

    public constructor(value: Int) : this(value, value, value)

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

    public operator fun get(axis: OmniDirectionalAxis): Int {
        return when (axis) {
            OmniDirectionalAxis.X -> x
            OmniDirectionalAxis.Y -> y
            OmniDirectionalAxis.Z -> z
        }
    }

    public operator fun plus(other: OmniVec3i): OmniVec3i {
        return OmniVec3i(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    public operator fun plus(other: Vec3i): OmniVec3i {
        return this.plus(OmniVec3i(other))
    }

    public operator fun plus(other: Int): OmniVec3i {
        return OmniVec3i(this.x + other, this.y + other, this.z + other)
    }

    public operator fun minus(other: OmniVec3i): OmniVec3i {
        return OmniVec3i(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    public operator fun minus(other: Vec3i): OmniVec3i {
        return this.minus(OmniVec3i(other))
    }

    public operator fun minus(other: Int): OmniVec3i {
        return OmniVec3i(this.x - other, this.y - other, this.z - other)
    }

    public operator fun times(other: OmniVec3i): OmniVec3i {
        return OmniVec3i(this.x * other.x, this.y * other.y, this.z * other.z)
    }

    public operator fun times(other: Vec3i): OmniVec3i {
        return OmniVec3i(this.x * other.x, this.y * other.y, this.z * other.z)
    }

    public operator fun times(other: Int): OmniVec3i {
        return OmniVec3i(this.x * other, this.y * other, this.z * other)
    }

    public operator fun div(other: OmniVec3i): OmniVec3i {
        return OmniVec3i(this.x / other.x, this.y / other.y, this.z / other.z)
    }

    public operator fun div(other: Vec3i): OmniVec3i {
        return OmniVec3i(this.x / other.x, this.y / other.y, this.z / other.z)
    }

    public operator fun div(other: Int): OmniVec3i {
        return OmniVec3i(this.x / other, this.y / other, this.z / other)
    }

    public fun withX(x: Int): OmniVec3i {
        return OmniVec3i(x, this.y, this.z)
    }

    public fun withY(y: Int): OmniVec3i {
        return OmniVec3i(this.x, y, this.z)
    }

    public fun withZ(z: Int): OmniVec3i {
        return OmniVec3i(this.x, this.y, z)
    }
}
