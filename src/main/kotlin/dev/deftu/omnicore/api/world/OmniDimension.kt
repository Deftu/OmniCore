package dev.deftu.omnicore.api.world

import dev.deftu.omnicore.internal.world.DimensionInternals

public sealed interface OmniDimension {
    public val id: DimensionId

    public data object Overworld : OmniDimension {
        override val id: DimensionId = DimensionInternals.OVERWORLD;
    }

    public data object Nether : OmniDimension {
        override val id: DimensionId = DimensionInternals.NETHER;
    }

    public data object End : OmniDimension {
        override val id: DimensionId = DimensionInternals.END;
    }

    public data class Custom(override val id: DimensionId, public val name: String) : OmniDimension {
        init {
            require(name.isNotBlank()) { "Dimension name cannot be blank" }
        }
    }
}
