package dev.deftu.omnicore.api.data.vec

import com.mojang.serialization.Codec
import dev.deftu.omnicore.api.direction.OmniDirectionalAxis
import dev.deftu.omnicore.api.math.OmniMath
import dev.deftu.omnicore.api.data.DistanceMetric
import dev.deftu.omnicore.api.serialization.OmniDataResult
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

public data class OmniVec3d(
    public val x: Double,
    public val y: Double,
    public val z: Double,
) {
    public companion object {
        @JvmField public val ZERO: OmniVec3d = OmniVec3d(0.0, 0.0, 0.0)
        @JvmField public val ONE: OmniVec3d = OmniVec3d(1.0, 1.0, 1.0)

        @JvmField
        public val CODEC: Codec<OmniVec3d> = Codec.DOUBLE
            .listOf()
            .comapFlatMap(
                {
                    OmniDataResult.decodeFixedLengthList(it, 3).map { list ->
                        OmniVec3d(list[0], list[1], list[2])
                    }
                },
                { listOf(it.x, it.y, it.z) },
            )
    }

    @get:JvmName("toVanilla")
    public val vanilla: Vec3d
        get() = Vec3d(x, y, z)

    @get:JvmName("toVec3i")
    public val vec3i: OmniVec3i
        get() = OmniVec3i(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())

    public val length: Double
        get() = sqrt(x * x + y * y + z * z)

    public val lengthSquared: Double
        get() = x * x + y * y + z * z

    public val horizontalLength: Double
        get() = sqrt(x * x + z * z)

    public val horizontalLengthSquared: Double
        get() = x * x + z * z

    public constructor(vanilla: Vec3d) : this(vanilla.x, vanilla.y, vanilla.z)

    public constructor(values: Triple<Double, Double, Double>) : this(values.first, values.second, values.third)

    public constructor(value: Double) : this(value, value, value)

    public fun floor(): OmniVec3d {
        return OmniVec3d(floor(x), floor(y), floor(z))
    }

    public fun normalize(): OmniVec3d {
        val length = sqrt(x * x + y * y + z * z)
        return if (length < 1.0E-4) {
            ZERO
        } else {
            OmniVec3d(x / length, y / length, z / length)
        }
    }

    public fun relativize(other: OmniVec3d): OmniVec3d {
        return OmniVec3d(
            other.x - this.x,
            other.y - this.y,
            other.z - this.z,
        )
    }

    public fun relativize(other: Vec3d): OmniVec3d {
        return OmniVec3d(
            other.x - this.x,
            other.y - this.y,
            other.z - this.z,
        )
    }

    public fun relativize(x: Double, y: Double, z: Double): OmniVec3d {
        return OmniVec3d(
            x - this.x,
            y - this.y,
            z - this.z,
        )
    }

    public fun distanceTo(other: OmniVec3d, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x, this.y, this.z,
            other.x, other.y, other.z,
        )
    }

    public fun distanceTo(other: Vec3d, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x, this.y, this.z,
            other.x, other.y, other.z,
        )
    }

    public fun distanceTo(x: Double, y: Double, z: Double, metric: DistanceMetric): Double {
        return metric.distance3D(
            this.x, this.y, this.z,
            x, y, z,
        )
    }

    public fun lerp(to: OmniVec3d, delta: Double): OmniVec3d {
        return OmniVec3d(OmniMath.lerp(this.x, to.x, delta), OmniMath.lerp(this.y, to.y, delta), OmniMath.lerp(this.z, to.z, delta))
    }

    public fun lerp(to: Vec3d, delta: Double): OmniVec3d {
        return OmniVec3d(OmniMath.lerp(this.x, to.x, delta), OmniMath.lerp(this.y, to.y, delta), OmniMath.lerp(this.z, to.z, delta))
    }

    public fun lerp(to: Double, delta: Double): OmniVec3d {
        return OmniVec3d(OmniMath.lerp(this.x, to, delta), OmniMath.lerp(this.y, to, delta), OmniMath.lerp(this.z, to, delta))
    }

    public fun rotateX(angle: Float): OmniVec3d {
        val f = cos(angle)
        val g = sin(angle)
        val d0 = this.x
        val d1 = this.y * f + this.z * g
        val d2 = this.z * f - this.y * g
        return OmniVec3d(d0, d1, d2)
    }

    public fun rotateY(angle: Float): OmniVec3d {
        val f = cos(angle)
        val g = sin(angle)
        val d0 = this.x * f - this.z * g
        val d1 = this.y
        val d2 = this.z * f + this.x * g
        return OmniVec3d(d0, d1, d2)
    }

    public fun rotateZ(angle: Float): OmniVec3d {
        val f = cos(angle)
        val g = sin(angle)
        val d0 = this.x * f + this.y * g
        val d1 = this.y * f - this.x * g
        val d2 = this.z
        return OmniVec3d(d0, d1, d2)
    }

    public operator fun get(axis: OmniDirectionalAxis): Double {
        return when (axis) {
            OmniDirectionalAxis.X -> x
            OmniDirectionalAxis.Y -> y
            OmniDirectionalAxis.Z -> z
        }
    }

    public operator fun plus(other: OmniVec3d): OmniVec3d {
        return OmniVec3d(x + other.x, y + other.y, z + other.z)
    }

    public operator fun plus(other: Vec3d): OmniVec3d {
        return OmniVec3d(x + other.x, y + other.y, z + other.z)
    }

    public operator fun plus(other: Double): OmniVec3d {
        return OmniVec3d(x + other, y + other, z + other)
    }

    public operator fun minus(other: OmniVec3d): OmniVec3d {
        return OmniVec3d(x - other.x, y - other.y, z - other.z)
    }

    public operator fun minus(other: Vec3d): OmniVec3d {
        return OmniVec3d(x - other.x, y - other.y, z - other.z)
    }

    public operator fun minus(other: Double): OmniVec3d {
        return OmniVec3d(x - other, y - other, z - other)
    }

    public operator fun times(other: OmniVec3d): OmniVec3d {
        return OmniVec3d(x * other.x, y * other.y, z * other.z)
    }

    public operator fun times(other: Vec3d): OmniVec3d {
        return OmniVec3d(x * other.x, y * other.y, z * other.z)
    }

    public operator fun times(other: Double): OmniVec3d {
        return OmniVec3d(x * other, y * other, z * other)
    }

    public operator fun div(other: OmniVec3d): OmniVec3d {
        return OmniVec3d(x / other.x, y / other.y, z / other.z)
    }

    public operator fun div(other: Vec3d): OmniVec3d {
        return OmniVec3d(x / other.x, y / other.y, z / other.z)
    }

    public operator fun div(other: Double): OmniVec3d {
        return OmniVec3d(x / other, y / other, z / other)
    }

    public fun dotProduct(other: OmniVec3d): OmniVec3d {
        return OmniVec3d(
            this.x * other.x,
            this.y * other.y,
            this.z * other.z,
        )
    }

    public fun dotProduct(other: Vec3d): OmniVec3d {
        return OmniVec3d(
            this.x * other.x,
            this.y * other.y,
            this.z * other.z,
        )
    }

    public fun dotProduct(other: Double): OmniVec3d {
        return OmniVec3d(
            this.x * other,
            this.y * other,
            this.z * other,
        )
    }

    public fun crossProduct(other: OmniVec3d): OmniVec3d {
        return OmniVec3d(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x,
        )
    }

    public fun crossProduct(other: Vec3d): OmniVec3d {
        return OmniVec3d(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x,
        )
    }

    public fun crossProduct(other: Double): OmniVec3d {
        return OmniVec3d(
            this.y * other - this.z * other,
            this.z * other - this.x * other,
            this.x * other - this.y * other,
        )
    }

    public fun withX(x: Double): OmniVec3d {
        return OmniVec3d(x, this.y, this.z)
    }

    public fun withY(y: Double): OmniVec3d {
        return OmniVec3d(this.x, y, this.z)
    }

    public fun withZ(z: Double): OmniVec3d {
        return OmniVec3d(this.x, this.y, z)
    }
}
