package dev.deftu.omnicore.api.math

import dev.deftu.omnicore.api.direction.OmniDirectionalAxis
import org.joml.Vector3f
import kotlin.math.acos
import kotlin.math.sqrt

public data class OmniVector3f(
    public val x: Float,
    public val y: Float,
    public val z: Float
) {
    public companion object {
        @JvmField public val ZERO: OmniVector3f = OmniVector3f(0f, 0f, 0f)
        @JvmField public val UNIT_X: OmniVector3f = OmniVector3f(1f, 0f, 0f)
        @JvmField public val UNIT_Y: OmniVector3f = OmniVector3f(0f, 1f, 0f)
        @JvmField public val UNIT_Z: OmniVector3f = OmniVector3f(0f, 0f, 1f)

        @JvmStatic
        public fun from(data: FloatArray): OmniVector3f {
            require(data.size >= 3) { "Array must have at least 3 elements" }
            return OmniVector3f(data[0], data[1], data[2])
        }
    }

    public val length: Float
        get() = sqrt(x * x + y * y + z * z)

    public val lengthSquared: Float
        get() = x * x + y * y + z * z

    public constructor(other: OmniVector3f) : this(other.x, other.y, other.z)

    public constructor(other: Vector3f) : this(
        //#if MC >= 1.19.4
        other.x,
        other.y,
        other.z
        //#elseif MC >= 1.16.5
        //$$ other.x(),
        //$$ other.y(),
        //$$ other.z()
        //#else
        //$$ other.x,
        //$$ other.y,
        //$$ other.z
        //#endif
    )

    public operator fun get(axis: OmniDirectionalAxis): Float {
        return when (axis) {
            OmniDirectionalAxis.X -> x
            OmniDirectionalAxis.Y -> y
            OmniDirectionalAxis.Z -> z
        }
    }

    public operator fun plus(other: OmniVector3f): OmniVector3f {
        return OmniVector3f(x + other.x, y + other.y, z + other.z)
    }

    public operator fun plus(value: Float): OmniVector3f {
        return OmniVector3f(x + value, y + value, z + value)
    }

    public operator fun minus(other: OmniVector3f): OmniVector3f {
        return OmniVector3f(x - other.x, y - other.y, z - other.z)
    }

    public operator fun minus(value: Float): OmniVector3f {
        return OmniVector3f(x - value, y - value, z - value)
    }

    public operator fun times(other: OmniVector3f): OmniVector3f {
        return OmniVector3f(x * other.x, y * other.y, z * other.z)
    }

    public operator fun times(scalar: Float): OmniVector3f {
        return OmniVector3f(x * scalar, y * scalar, z * scalar)
    }

    public operator fun div(other: OmniVector3f): OmniVector3f {
        require(other.x != 0f && other.y != 0f && other.z != 0f) { "Division by zero is not allowed." }
        return OmniVector3f(x / other.x, y / other.y, z / other.z)
    }

    public operator fun div(scalar: Float): OmniVector3f {
        require(scalar != 0f) { "Division by zero is not allowed." }
        return OmniVector3f(x / scalar, y / scalar, z / scalar)
    }

    public operator fun unaryMinus(): OmniVector3f {
        return OmniVector3f(-x, -y, -z)
    }

    public fun dot(other: OmniVector3f): Float {
        return x * other.x + y * other.y + z * other.z
    }

    public fun cross(other: OmniVector3f): OmniVector3f {
        return OmniVector3f(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    public fun normalized(): OmniVector3f {
        val length = length
        require(length != 0f) { "Cannot normalize a zero-length vector." }
        val inv = 1f / length
        return OmniVector3f(x * inv, y * inv, z * inv)
    }

    public fun withLength(length: Float): OmniVector3f {
        val currentLength = this.length
        if (currentLength == 0f) {
            return this
        }

        val scale = length / currentLength
        return this * scale
    }

    public fun clampLength(max: Float): OmniVector3f {
        require(max >= 0f) { "Max length must be non-negative." }
        val lengthSq = lengthSquared
        val maxSq = max * max
        if (lengthSq <= maxSq) {
            return this
        }

        val scale = max / sqrt(lengthSq)
        return this * scale
    }

    public fun lerp(target: OmniVector3f, delta: Float): OmniVector3f {
        return OmniVector3f(
            x = OmniMath.lerp(this.x, target.x, delta),
            y = OmniMath.lerp(this.y, target.y, delta),
            z = OmniMath.lerp(this.z, target.z, delta)
        )
    }

    public fun project(onto: OmniVector3f): OmniVector3f {
        val ontoLengthSq = onto.lengthSquared
        if (ontoLengthSq == 0f) {
            return ZERO
        }
        val dotProduct = this.dot(onto)
        val scale = dotProduct / ontoLengthSq
        return onto * scale
    }

    public fun reject(onto: OmniVector3f): OmniVector3f {
        return this - this.project(onto)
    }

    public fun reflect(normal: OmniVector3f): OmniVector3f {
        val k = 2f * this.dot(normal)
        return this - normal * k
    }

    public fun angleTo(other: OmniVector3f): Float {
        val thisLength = this.length
        val otherLength = other.length
        if (thisLength == 0f || otherLength == 0f) {
            return 0f
        }

        val cosTheta = (this.dot(other) / (thisLength * otherLength)).coerceIn(-1f, 1f)
        return acos(cosTheta)
    }

    public fun transform(matrix: OmniMatrix3f): OmniVector3f {
        val data = matrix.toArray()
        val nx = data[0] * x + data[3] * y + data[6] * z
        val ny = data[1] * x + data[4] * y + data[7] * z
        val nz = data[2] * x + data[5] * y + data[8] * z
        return OmniVector3f(nx, ny, nz)
    }

    public fun transformLinear(matrix: OmniMatrix4f): OmniVector3f {
        val data = matrix.toArray()
        val nx = data[0] * x + data[4] * y + data[8] * z
        val ny = data[1] * x + data[5] * y + data[9] * z
        val nz = data[2] * x + data[6] * y + data[10] * z
        return OmniVector3f(nx, ny, nz)
    }

    public fun toArray(): FloatArray {
        return floatArrayOf(x, y, z)
    }
}
