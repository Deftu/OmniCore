package dev.deftu.omnicore.common

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.util.math.Direction

public enum class OmniSpatialDirection {

    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN;

    @GameSide(Side.BOTH)
    public val isHorizontal: Boolean get() = this in HORIZONTAL

    @GameSide(Side.BOTH)
    public val isVertical: Boolean get() = this in VERTICAL

    @GameSide(Side.BOTH)
    public val opposite: OmniSpatialDirection
        get() = when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
            UP -> DOWN
            DOWN -> UP
        }

    @GameSide(Side.BOTH)
    public val vanilla: Direction
        get() = VANILLA_MAPPINGS[this]
            ?: throw IllegalStateException("No vanilla mapping for $this")

    @GameSide(Side.BOTH)
    public fun next(): OmniSpatialDirection {
        return ALL[(ordinal + 1) % ALL.size]
    }

    @GameSide(Side.BOTH)
    public fun previous(): OmniSpatialDirection {
        return ALL[(ordinal + ALL.size - 1) % ALL.size]
    }

    public companion object {

        @JvmField
        @GameSide(Side.BOTH)
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniSpatialDirection> = OmniSpatialDirection.values().toList()

        @JvmField
        @GameSide(Side.BOTH)
        public val HORIZONTAL: Set<OmniSpatialDirection> = setOf(NORTH, EAST, SOUTH, WEST)

        @JvmField
        @GameSide(Side.BOTH)
        public val VERTICAL: Set<OmniSpatialDirection> = setOf(UP, DOWN)

        @JvmField
        @GameSide(Side.BOTH)
        public val VANILLA_MAPPINGS: Map<OmniSpatialDirection, Direction> = mapOf(
            NORTH to Direction.NORTH,
            EAST to Direction.EAST,
            SOUTH to Direction.SOUTH,
            WEST to Direction.WEST,
            UP to Direction.UP,
            DOWN to Direction.DOWN
        )

        @JvmField
        @GameSide(Side.BOTH)
        public val VANILLA_MAPPINGS_INVERSE: Map<Direction, OmniSpatialDirection> = VANILLA_MAPPINGS.entries
            .associate { (key, value) -> value to key }

        @JvmStatic
        @GameSide(Side.BOTH)
        public fun from(direction: Direction): OmniSpatialDirection {
            return VANILLA_MAPPINGS_INVERSE[direction]
                ?: throw IllegalArgumentException("No OmniSpatialDirection for vanilla direction $direction")
        }

    }

}
