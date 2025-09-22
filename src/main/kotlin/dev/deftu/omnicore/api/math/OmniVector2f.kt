package dev.deftu.omnicore.api.math

import kotlin.math.acos
import kotlin.math.sqrt

public data class OmniVector2f(
    public var x: Float,
    public var y: Float
) {
    public companion object {
        @JvmField public val ZERO: OmniVector2f = OmniVector2f(0f, 0f)
        @JvmField public val UNIT_X: OmniVector2f = OmniVector2f(1f, 0f)
        @JvmField public val UNIT_Y: OmniVector2f = OmniVector2f(0f, 1f)

        @JvmStatic
        public fun from(data: FloatArray): OmniVector2f {
            require(data.size >= 2) { "Array must have at least 2 elements" }
            return OmniVector2f(data[0], data[1])
        }
    }

    public val length: Float
        get() = sqrt(x * x + y * y)

    public val lengthSquared: Float
        get() = x * x + y * y

    public constructor(other: OmniVector2f) : this(other.x, other.y)

    public constructor(value: Float) : this(value, value)

    public constructor() : this(0f, 0f)

    public fun set(x: Float, y: Float): OmniVector2f {
        this.x = x
        this.y = y
        return this
    }

    public operator fun plus(other: OmniVector2f): OmniVector2f {
        return OmniVector2f(x + other.x, y + other.y)
    }

    public operator fun plus(value: Float): OmniVector2f {
        return OmniVector2f(x + value, y + value)
    }

    public operator fun minus(other: OmniVector2f): OmniVector2f {
        return OmniVector2f(x - other.x, y - other.y)
    }

    public operator fun minus(value: Float): OmniVector2f {
        return OmniVector2f(x - value, y - value)
    }

    public operator fun times(other: OmniVector2f): OmniVector2f {
        return OmniVector2f(x * other.x, y * other.y)
    }

    public operator fun times(scalar: Float): OmniVector2f {
        return OmniVector2f(x * scalar, y * scalar)
    }

    public operator fun div(other: OmniVector2f): OmniVector2f {
        require(other.x != 0f && other.y != 0f) { "Division by zero in one of the vector components" }
        return OmniVector2f(x / other.x, y / other.y)
    }

    public operator fun div(scalar: Float): OmniVector2f {
        require(scalar != 0f) { "Division by zero" }
        return OmniVector2f(x / scalar, y / scalar)
    }

    public operator fun unaryMinus(): OmniVector2f {
        return OmniVector2f(-x, -y)
    }

    public fun dot(other: OmniVector2f): Float {
        return x * other.x + y * other.y
    }

    public fun cross(other: OmniVector2f): Float {
        return x * other.y - y * other.x
    }

    public fun normalized(): OmniVector2f {
        val length = length
        require(length != 0f) { "Cannot normalize a zero-length vector." }
        val inv = 1f / length
        return OmniVector2f(x * inv, y * inv)
    }

    public fun withLength(length: Float): OmniVector2f {
        val currentLength = length
        if (currentLength == 0f) {
            return this
        }

        val scale = length / currentLength
        return this * scale
    }

    public fun clampLength(max: Float): OmniVector2f {
        require(max >= 0f) { "Max length must be non-negative." }
        val lengthSq = lengthSquared
        val maxSq = max * max
        if (lengthSq <= maxSq) {
            return this
        }

        val scale = max / sqrt(lengthSq)
        return this * scale
    }

    public fun lerp(target: OmniVector2f, delta: Float): OmniVector2f {
        return OmniVector2f(
            x = OmniMath.lerp(this.x, target.x, delta),
            y = OmniMath.lerp(this.y, target.y, delta)
        )
    }

    public fun project(other: OmniVector2f): OmniVector2f {
        val otherLengthSq = other.lengthSquared
        if (otherLengthSq == 0f) {
            return ZERO
        }
        val dotProduct = this.dot(other)
        val scale = dotProduct / otherLengthSq
        return other * scale
    }

    public fun reject(other: OmniVector2f): OmniVector2f {
        return this - this.project(other)
    }

    public fun reflect(normal: OmniVector2f): OmniVector2f {
        val k = 2f * this.dot(normal)
        return this - normal * k
    }

    public fun angleTo(other: OmniVector2f): Float {
        val thisLength = this.length
        val otherLength = other.length
        if (thisLength == 0f || otherLength == 0f) {
            return 0f
        }

        val cosTheta = (this.dot(other) / (thisLength * otherLength)).coerceIn(-1f, 1f)
        return acos(cosTheta)
    }

    public fun transform(matrix: OmniMatrix3f): OmniVector2f {
        val nx = matrix.m00 * x + matrix.m01 * y + matrix.m02
        val ny = matrix.m10 * x + matrix.m11 * y + matrix.m12
        return OmniVector2f(nx, ny)
    }

    public fun transformLinear(matrix: OmniMatrix4f): OmniVector2f {
        val nx = matrix.m00 * x + matrix.m01 * y
        val ny = matrix.m10 * x + matrix.m11 * y
        return OmniVector2f(nx, ny)
    }

    public fun deepCopy(): OmniVector2f {
        return OmniVector2f(this)
    }
}
