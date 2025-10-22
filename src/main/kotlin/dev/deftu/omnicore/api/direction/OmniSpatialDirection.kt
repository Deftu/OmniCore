package dev.deftu.omnicore.api.direction

import dev.deftu.omnicore.api.commands.types.enumerable.CommandCompletable
import net.minecraft.util.math.Direction

public enum class OmniSpatialDirection : CommandCompletable {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    UP,
    DOWN;

    public val isHorizontal: Boolean get() = this in HORIZONTAL
    public val isVertical: Boolean get() = this in VERTICAL

    public val opposite: OmniSpatialDirection
        get() = when (this) {
            NORTH -> SOUTH
            EAST -> WEST
            SOUTH -> NORTH
            WEST -> EAST
            UP -> DOWN
            DOWN -> UP
        }

    public val vanilla: Direction
        get() = VANILLA_MAPPINGS[this]
            ?: throw IllegalStateException("No vanilla mapping for $this")

    override val id: String
        get() = name.lowercase()

    public fun next(): OmniSpatialDirection {
        return ALL[(ordinal + 1) % ALL.size]
    }

    public fun previous(): OmniSpatialDirection {
        return ALL[(ordinal + ALL.size - 1) % ALL.size]
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniSpatialDirection> = values().toList()

        @JvmField
        public val HORIZONTAL: Set<OmniSpatialDirection> = setOf(NORTH, EAST, SOUTH, WEST)

        @JvmField
        public val VERTICAL: Set<OmniSpatialDirection> = setOf(UP, DOWN)

        @JvmField
        public val VANILLA_MAPPINGS: Map<OmniSpatialDirection, Direction> = mapOf(
            NORTH to Direction.NORTH,
            EAST to Direction.EAST,
            SOUTH to Direction.SOUTH,
            WEST to Direction.WEST,
            UP to Direction.UP,
            DOWN to Direction.DOWN
        )

        @JvmField
        public val VANILLA_MAPPINGS_INVERSE: Map<Direction, OmniSpatialDirection> = VANILLA_MAPPINGS.entries
            .associate { (key, value) -> value to key }

        @JvmStatic
        public fun from(direction: Direction): OmniSpatialDirection {
            return VANILLA_MAPPINGS_INVERSE[direction]
                ?: throw IllegalArgumentException("No OmniSpatialDirection for vanilla direction $direction")
        }
    }
}