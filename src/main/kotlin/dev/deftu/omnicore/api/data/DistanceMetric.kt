package dev.deftu.omnicore.api.data

import dev.deftu.omnicore.api.data.vec.OmniVec3d
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

public enum class DistanceMetric {
    /** L2 norm. */
    EUCLIDEAN,
    /** L2 without sqrt; matches "squaredDistanceTo". */
    EUCLIDEAN_SQUARED,
    /** L1 norm; matches "manhattanDistance". */
    MANHATTAN,
    /** L∞ norm; max component distance. */
    CHEBYSHEV;

    public fun distance3D(
        ax: Double, ay: Double, az: Double,
        bx: Double, by: Double, bz: Double,
    ): Double {
        val dx = ax - bx
        val dy = ay - by
        val dz = az - bz

        return when (this) {
            EUCLIDEAN -> {
                sqrt(dx * dx + dy * dy + dz * dz)
            }

            EUCLIDEAN_SQUARED -> {
                dx * dx + dy * dy + dz * dz
            }

            MANHATTAN -> {
                abs(dx) + abs(dy) + abs(dz)
            }

            CHEBYSHEV -> {
                val adx = abs(dx)
                val ady = abs(dy)
                val adz = abs(dz)
                max(max(adx, ady), adz)
            }
        }
    }

    public fun distance3D(a: OmniVec3d, b: OmniVec3d): Double {
        return distance3D(a.x, a.y, a.z, b.x, b.y, b.z)
    }

    public fun distance2D(
        ax: Double, az: Double,
        bx: Double, bz: Double,
    ): Double {
        val dx = ax - bx
        val dz = az - bz

        return when (this) {
            EUCLIDEAN -> {
                sqrt(dx * dx + dz * dz)
            }

            EUCLIDEAN_SQUARED -> {
                dx * dx + dz * dz
            }

            MANHATTAN -> {
                abs(dx) + abs(dz)
            }

            CHEBYSHEV -> {
                val adx = abs(dx)
                val adz = abs(dz)
                max(adx, adz)
            }
        }
    }

    public fun distance2D(a: OmniVec3d, b: OmniVec3d): Double {
        return distance2D(a.x, a.z, b.x, b.z)
    }
}
