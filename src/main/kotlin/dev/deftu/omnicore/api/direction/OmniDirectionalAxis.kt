package dev.deftu.omnicore.api.direction

import dev.deftu.omnicore.api.commands.CommandCompletable
import net.minecraft.util.math.Direction

public enum class OmniDirectionalAxis(override val id: String) : CommandCompletable {
    X("x"),
    Y("y"),
    Z("z");

    public val isHorizontal: Boolean get() = this in HORIZONTAL
    public val isVertical: Boolean get() = this in VERTICAL

    public val positiveDirection: OmniSpatialDirection
        get() {
            return when (this) {
                X -> OmniSpatialDirection.EAST
                Y -> OmniSpatialDirection.UP
                Z -> OmniSpatialDirection.SOUTH
            }
        }

    public val negativeDirection: OmniSpatialDirection
        get() {
            return when (this) {
                X -> OmniSpatialDirection.WEST
                Y -> OmniSpatialDirection.DOWN
                Z -> OmniSpatialDirection.NORTH
            }
        }

    public val vanilla: Direction.Axis
        get() = VANILLA_MAPPINGS[this]
            ?: throw IllegalStateException("No vanilla mapping for $this")

    public fun chooseFrom(x: Int, y: Int, z: Int): Int {
        return when (this) {
            X -> x
            Y -> y
            Z -> z
        }
    }

    public fun chooseFrom(x: Double, y: Double, z: Double): Double {
        return when (this) {
            X -> x
            Y -> y
            Z -> z
        }
    }

    public fun chooseFrom(x: Boolean, y: Boolean, z: Boolean): Boolean {
        return when (this) {
            X -> x
            Y -> y
            Z -> z
        }
    }

    public operator fun unaryMinus(): AxisSpace {
        return AxisSpace(this, isPositive = false)
    }

    public operator fun unaryPlus(): AxisSpace {
        return AxisSpace(this, isPositive = true)
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniDirectionalAxis> = values().toList()

        @JvmField
        public val HORIZONTAL: Set<OmniDirectionalAxis> = setOf(X, Z)

        @JvmField
        public val VERTICAL: Set<OmniDirectionalAxis> = setOf(Y)

        @JvmField
        public val VANILLA_MAPPINGS: Map<OmniDirectionalAxis, Direction.Axis> = mapOf(
            X to Direction.Axis.X,
            Y to Direction.Axis.Y,
            Z to Direction.Axis.Z,
        )

        @JvmField
        public val VANILLA_MAPPINGS_INVERSE: Map<Direction.Axis, OmniDirectionalAxis> = VANILLA_MAPPINGS.entries
            .associate { (key, value) -> value to key }

        @JvmStatic
        public fun from(id: String): OmniDirectionalAxis? {
            return ALL.firstOrNull { it.id == id }
        }

        @JvmStatic
        public fun from(vanilla: Direction.Axis): OmniDirectionalAxis {
            return VANILLA_MAPPINGS_INVERSE[vanilla]
                ?: throw IllegalArgumentException("No OmniDirectionalAxis for vanilla axis $vanilla")
        }
    }
}
