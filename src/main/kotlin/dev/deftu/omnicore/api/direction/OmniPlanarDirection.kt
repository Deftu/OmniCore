package dev.deftu.omnicore.api.direction

import dev.deftu.omnicore.api.commands.CommandCompletable

public enum class OmniPlanarDirection(
    public val fullName: String,
    public val abbreviatedName: String
) : CommandCompletable {
    NORTH("North", "N"),
    NORTH_EAST("North East", "NE"),
    EAST("East", "E"),
    SOUTH_EAST("South East", "SE"),
    SOUTH("South", "S"),
    SOUTH_WEST("South West", "SW"),
    WEST("West", "W"),
    NORTH_WEST("North West", "NW");

    public val isNorth: Boolean get() = this in NORTH_DIRECTIONS
    public val isSouth: Boolean get() = this in SOUTH_DIRECTIONS
    public val isWest: Boolean get() = this in WEST_DIRECTIONS
    public val isEast: Boolean get() = this in EAST_DIRECTIONS

    public val isCardinal: Boolean get() = this.ordinal % 2 == 0
    public val isOrdinal: Boolean get() = this.ordinal % 2 == 1

    public val dominantAxis: OmniSpatialDirection
        get() {
            return when (this) {
                NORTH, NORTH_WEST, NORTH_EAST -> OmniSpatialDirection.NORTH
                SOUTH, SOUTH_WEST, SOUTH_EAST -> OmniSpatialDirection.SOUTH
                EAST -> OmniSpatialDirection.EAST
                WEST -> OmniSpatialDirection.WEST
            }
        }

    public val opposite: OmniPlanarDirection
        get() = when (this) {
            NORTH -> SOUTH
            NORTH_EAST -> SOUTH_WEST
            EAST -> WEST
            SOUTH_EAST -> NORTH_WEST
            SOUTH -> NORTH
            SOUTH_WEST -> NORTH_EAST
            WEST -> EAST
            NORTH_WEST -> SOUTH_EAST
        }

    override val id: String
        get() = fullName.lowercase().replace(" ", "_")

    public fun next(): OmniPlanarDirection {
        return ALL[(ordinal + 1) % ALL.size]
    }

    public fun previous(): OmniPlanarDirection {
        return ALL[(ordinal + ALL.size - 1) % ALL.size]
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniPlanarDirection> = values().toList()

        @JvmField
        public val NORTH_DIRECTIONS: Set<OmniPlanarDirection> = setOf(NORTH, NORTH_WEST, NORTH_EAST)

        @JvmField
        public val SOUTH_DIRECTIONS: Set<OmniPlanarDirection> = setOf(SOUTH, SOUTH_WEST, SOUTH_EAST)

        @JvmField
        public val WEST_DIRECTIONS: Set<OmniPlanarDirection> = setOf(WEST, NORTH_WEST, SOUTH_WEST)

        @JvmField
        public val EAST_DIRECTIONS: Set<OmniPlanarDirection> = setOf(EAST, NORTH_EAST, SOUTH_EAST)

        @JvmStatic
        public fun from(yaw: Float, isExact: Boolean = false): OmniPlanarDirection {
            return if (isExact) {
                parseExact(yaw)
            } else {
                parseLenient(yaw)
            }
        }

        @JvmStatic
        public fun parseLenient(yaw: Float): OmniPlanarDirection {
            val rotationYaw = wrapDegrees(yaw)

            return when {
                rotationYaw <= -165f || rotationYaw > 165f -> NORTH
                rotationYaw in -165f..-105f -> NORTH_EAST
                rotationYaw in -105f..-75f -> EAST
                rotationYaw in -75f..-15f -> SOUTH_EAST
                rotationYaw in -15f..15f -> SOUTH
                rotationYaw in 15f..75f -> SOUTH_WEST
                rotationYaw in 75f..105f -> WEST
                rotationYaw in 105f..165f -> NORTH_WEST
                else -> NORTH // fallback, shouldn't happen
            }
        }

        @JvmStatic
        public fun parseExact(yaw: Float): OmniPlanarDirection {
            val rotationYaw = wrapDegrees(yaw)

            return when (rotationYaw) {
                -180f, 180f -> NORTH
                -90f -> EAST
                0f -> SOUTH
                90f -> WEST
                in -180f..-90f -> NORTH_EAST
                in -90f..0f -> SOUTH_EAST
                in 0f..90f -> SOUTH_WEST
                in 90f..180f -> NORTH_WEST
                else -> NORTH // fallback, shouldn't happen
            }
        }

        private fun wrapDegrees(value: Float): Float {
            val new = value % 360.0f
            if (new <= -180.0f) {
                return new + 360.0f
            }

            if (new > 180.0f) {
                return new - 360.0f
            }

            return new
        }
    }
}