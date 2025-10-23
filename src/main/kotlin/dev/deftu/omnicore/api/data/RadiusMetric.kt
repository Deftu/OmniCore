package dev.deftu.omnicore.api.data

import dev.deftu.omnicore.api.commands.types.enumerable.CommandCompletable
import kotlin.math.abs
import kotlin.math.max

public enum class RadiusMetric(override val id: String) : CommandCompletable {
    /** L1: |dx| + |dy| + |dz| <= r (diamond/octahedron) */
    MANHATTAN("manhattan"),
    /** L∞: max(|dx|, |dy|, |dz|) <= r (cube / square) */
    CHEBYSHEV("chebyshev"),
    /** L2: (squared): dx² + dy² + dz² <= r (sphere/circle). avoids sqrt */
    EUCLIDEAN("euclidean");

    @JvmOverloads
    public fun isInside2D(
        dx: Int, dz: Int,
        radius: Int, radiusSq: Int = radius * radius,
    ): Boolean {
        return when (this) {
            MANHATTAN -> abs(dx) + abs(dz) <= radius
            CHEBYSHEV -> max(abs(dx), abs(dz)) <= radius
            EUCLIDEAN -> (dx * dx) + (dz * dz) <= radiusSq
        }
    }

    @JvmOverloads
    public fun isInside3D(
        dx: Int, dy: Int, dz: Int,
        radius: Int, radiusSq: Int = radius * radius,
    ): Boolean {
        return when (this) {
            MANHATTAN -> abs(dx) + abs(dy) + abs(dz) <= radius
            CHEBYSHEV -> max(max(abs(dx), abs(dy)), abs(dz)) <= radius
            EUCLIDEAN -> (dx * dx) + (dy * dy) + (dz * dz) <= radiusSq
        }
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<RadiusMetric> = RadiusMetric.values().toList()
    }
}
