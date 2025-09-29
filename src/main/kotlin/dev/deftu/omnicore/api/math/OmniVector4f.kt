package dev.deftu.omnicore.api.math

import org.joml.Vector4f
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

public data class OmniVector4f(
    public val x: Float,
    public val y: Float,
    public val z: Float,
    public val w: Float,
) {
    public companion object {
        @JvmField public val ZERO: OmniVector4f = OmniVector4f(0f, 0f, 0f, 0f)
        @JvmField public val UNIT_X: OmniVector4f = OmniVector4f(1f, 0f, 0f, 0f)
        @JvmField public val UNIT_Y: OmniVector4f = OmniVector4f(0f, 1f, 0f, 0f)
        @JvmField public val UNIT_Z: OmniVector4f = OmniVector4f(0f, 0f, 1f, 0f)

        @JvmStatic
        public fun from(data: FloatArray): OmniVector4f {
            require(data.size >= 4) { "Array must have at least 4 elements" }
            return OmniVector4f(data[0], data[1], data[2], data[3])
        }
    }

    public val length: Float
        get() = sqrt(x * x + y * y + z * z + w * w)

    public val lengthSquared: Float
        get() = x * x + y * y + z * z + w * w

    public constructor(other: OmniVector4f) : this(other.x, other.y, other.z, other.w)

    public constructor(other: Vector4f) : this(
        //#if MC >= 1.19.4
        other.x,
        other.y,
        other.z,
        other.w,
        //#elseif MC >= 1.16.5
        //$$ other.x(),
        //$$ other.y(),
        //$$ other.z(),
        //$$ other.w(),
        //#else
        //$$ other.x,
        //$$ other.y,
        //$$ other.z,
        //$$ other.w,
        //#endif
    )

    public operator fun plus(other: OmniVector4f): OmniVector4f {
        return OmniVector4f(x + other.x, y + other.y, z + other.z, w + other.w)
    }

    public operator fun plus(value: Float): OmniVector4f {
        return OmniVector4f(x + value, y + value, z + value, w + value)
    }

    public operator fun minus(other: OmniVector4f): OmniVector4f {
        return OmniVector4f(x - other.x, y - other.y, z - other.z, w - other.w)
    }

    public operator fun minus(value: Float): OmniVector4f {
        return OmniVector4f(x - value, y - value, z - value, w - value)
    }

    public operator fun times(other: OmniVector4f): OmniVector4f {
        return OmniVector4f(x * other.x, y * other.y, z * other.z, w * other.w)
    }

    public operator fun times(scalar: Float): OmniVector4f {
        return OmniVector4f(x * scalar, y * scalar, z * scalar, w * scalar)
    }

    public operator fun div(other: OmniVector4f): OmniVector4f {
        require(other.x != 0f && other.y != 0f && other.z != 0f && other.w != 0f) { "Division by zero is not allowed." }
        return OmniVector4f(x / other.x, y / other.y, z / other.z, w / other.w)
    }

    public operator fun div(scalar: Float): OmniVector4f {
        require(scalar != 0f) { "Division by zero is not allowed." }
        return OmniVector4f(x / scalar, y / scalar, z / scalar, w / scalar)
    }

    public operator fun unaryMinus(): OmniVector4f {
        return OmniVector4f(-x, -y, -z, -w)
    }

    public fun dot(other: OmniVector4f): Float {
        return x * other.x + y * other.y + z * other.z + w * other.w
    }

    public fun normalized(): OmniVector4f {
        val length = length
        require(length != 0f) { "Cannot normalize a zero-length vector." }
        val inv = 1f / length
        return OmniVector4f(x * inv, y * inv, z * inv, w * inv)
    }

    public fun withLength(length: Float): OmniVector4f {
        val currentLength = this.length
        if (currentLength == 0f) {
            return this
        }

        val scale = length / currentLength
        return this * scale
    }

    public fun clampLength(max: Float): OmniVector4f {
        require(max >= 0f) { "Max length must be non-negative." }
        val lengthSq = lengthSquared
        val maxSq = max * max
        if (lengthSq <= maxSq) {
            return this
        }

        val scale = max / sqrt(lengthSq)
        return this * scale
    }

    public fun lerp(target: OmniVector4f, delta: Float): OmniVector4f {
        return OmniVector4f(
            x = OmniMath.lerp(this.x, target.x, delta),
            y = OmniMath.lerp(this.y, target.y, delta),
            z = OmniMath.lerp(this.z, target.z, delta),
            w = OmniMath.lerp(this.w, target.w, delta),
        )
    }

    public fun project(onto: OmniVector4f): OmniVector4f {
        val ontoLengthSq = onto.lengthSquared
        if (ontoLengthSq == 0f) {
            return ZERO
        }
        val dotProduct = this.dot(onto)
        val scale = dotProduct / ontoLengthSq
        return onto * scale
    }

    public fun reject(onto: OmniVector4f): OmniVector4f {
        return this - this.project(onto)
    }

    public fun reflect(normal: OmniVector4f): OmniVector4f {
        val k = 2f * this.dot(normal)
        return this - normal * k
    }

    public fun angleTo(other: OmniVector4f): Float {
        val thisLength = this.length
        val otherLength = other.length
        if (thisLength == 0f || otherLength == 0f) {
            return 0f
        }

        val cosTheta = (this.dot(other) / (thisLength * otherLength)).coerceIn(-1f, 1f)
        return acos(cosTheta)
    }

    public fun perspectiveDivide(eps: Float = 1e-6f): OmniVector4f {
        return if (abs(w) <= eps) this
        else OmniVector4f(x / w, y / w, z / w, 1f)
    }

    public fun toArray(): FloatArray {
        return floatArrayOf(x, y, z, w)
    }
}
