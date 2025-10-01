package dev.deftu.omnicore.api.math

import org.joml.Quaternionf
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

public data class OmniQuaternion(
    public var x: Float,
    public var y: Float,
    public var z: Float,
    public var w: Float
) {
    public companion object {
        @JvmStatic
        public fun identity(): OmniQuaternion {
            return OmniQuaternion(0f, 0f, 0f, 1f)
        }

        @JvmStatic
        public fun fromAxisAngle(ax: Float, ay: Float, az: Float, angleRad: Float, dest: OmniQuaternion = OmniQuaternion()): OmniQuaternion {
            val len2 = ax * ax + ay * ay + az * az
            if (len2 == 0f) return dest.set(identity())
            val invLen = 1f / sqrt(len2)
            val hx = ax * invLen
            val hy = ay * invLen
            val hz = az * invLen
            val half = 0.5f * angleRad
            val s = sin(half)
            val c = cos(half)
            return dest.set(hx * s, hy * s, hz * s, c)
        }
    }

    public val length: Float
        get() = sqrt(x * x + y * y + z * z + w * w)

    public val lengthSquared: Float
        get() = x * x + y * y + z * z + w * w

    public constructor() : this(0f, 0f, 0f, 0f) {
        set(identity())
    }

    public constructor(vanilla: Quaternionf) : this(
        //#if MC >= 1.19.4
        vanilla.x,
        vanilla.y,
        vanilla.z,
        vanilla.w
        //#elseif MC >= 1.16.5
        //$$ vanilla.i(),
        //$$ vanilla.j(),
        //$$ vanilla.k(),
        //$$ vanilla.r(),
        //#else
        //$$ vanilla.x,
        //$$ vanilla.y,
        //$$ vanilla.z,
        //$$ vanilla.w
        //#endif
    )

    @JvmOverloads
    public fun normalize(dest: OmniQuaternion = this): OmniQuaternion {
        val length = length
        if (length == 0f) {
            return dest.set(identity())
        }

        val invLength = 1f / length
        return dest.scale(invLength, dest)
    }

    @JvmOverloads
    public fun add(x: Float, y: Float, z: Float, w: Float, dest: OmniQuaternion = this): OmniQuaternion {
        return dest.set(this.x + x, this.y + y, this.z + z, this.w + w)
    }

    @JvmOverloads
    public fun add(other: OmniQuaternion, dest: OmniQuaternion = this): OmniQuaternion {
        return add(other.x, other.y, other.z, other.w, dest)
    }

    public operator fun plus(other: OmniQuaternion): OmniQuaternion {
        return add(other)
    }

    @JvmOverloads
    public fun subtract(x: Float, y: Float, z: Float, w: Float, dest: OmniQuaternion = this): OmniQuaternion {
        return dest.set(this.x - x, this.y - y, this.z - z, this.w - w)
    }

    @JvmOverloads
    public fun subtract(other: OmniQuaternion, dest: OmniQuaternion = this): OmniQuaternion {
        return subtract(other.x, other.y, other.z, other.w, dest)
    }

    public operator fun minus(other: OmniQuaternion): OmniQuaternion {
        return subtract(other)
    }

    @JvmOverloads
    public fun multiply(other: OmniQuaternion, dest: OmniQuaternion = this): OmniQuaternion {
        val x = this.x
        val y = this.y
        val z = this.z
        val w = this.w
        return dest.set(
            w * other.x + x * other.w + y * other.z - z * other.y,
            w * other.y - x * other.z + y * other.w + z * other.x,
            w * other.z + x * other.y - y * other.x + z * other.w,
            w * other.w - x * other.x - y * other.y - z * other.z
        )
    }

    public operator fun times(other: OmniQuaternion): OmniQuaternion {
        return multiply(other)
    }

    @JvmOverloads
    public fun scale(factor: Float, dest: OmniQuaternion = this): OmniQuaternion {
        return dest.set(x * factor, y * factor, z * factor, w * factor)
    }

    public operator fun times(factor: Float): OmniQuaternion {
        return scale(factor)
    }

    @JvmOverloads
    public fun inverse(dest: OmniQuaternion = this): OmniQuaternion {
        val lengthSq = lengthSquared
        if (lengthSq == 0f) {
            return dest.set(identity())
        }

        val invLengthSq = 1f / lengthSq
        return dest.set(-x * invLengthSq, -y * invLengthSq, -z * invLengthSq, w * invLengthSq)
    }

    @JvmOverloads
    public fun conjugate(dest: OmniQuaternion = this): OmniQuaternion {
        return dest.set(-x, -y, -z, w)
    }

    @JvmOverloads
    public fun slerp(other: OmniQuaternion, t: Float, dest: OmniQuaternion = this): OmniQuaternion {
        var ox = other.x
        var oy = other.y
        var oz = other.z
        var ow = other.w
        var dot = dot(other)
        if (dot < 0f) {
            dot = -dot
            ox = -ox
            oy = -oy
            oz = -oz
            ow = -ow
        }

        if (1f - dot < 1E-6f) {
            return dest.set(
                x + (ox - x) * t,
                y + (oy - y) * t,
                z + (oz - z) * t,
                w + (ow - w) * t,
            ).normalize(dest)
        }

        val theta = acos(dot.coerceIn(-1f, 1f))
        val sinTheta = sin(theta)
        val a = sin((1f - t) * theta) / sinTheta
        val b = sin(t * theta) / sinTheta
        return dest.set(
            x * a + ox * b,
            y * a + oy * b,
            z * a + oz * b,
            w * a + ow * b,
        )
    }

    public fun set(x: Float, y: Float, z: Float, w: Float): OmniQuaternion {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
        return this
    }

    public fun set(other: OmniQuaternion): OmniQuaternion {
        return set(other.x, other.y, other.z, other.w)
    }

    public fun dot(other: OmniQuaternion): Float {
        return x * other.x + y * other.y + z * other.z + w * other.w
    }

    public fun angle(): Float {
        return (2f * acos(w)).let { if (it < 0f) -it else it }
    }
}
