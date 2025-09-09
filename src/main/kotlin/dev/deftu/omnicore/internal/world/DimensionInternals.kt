package dev.deftu.omnicore.internal.world

import dev.deftu.omnicore.api.world.DimensionId
import dev.deftu.omnicore.api.world.OmniDimension
import net.minecraft.world.dimension.DimensionTypes
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object DimensionInternals {
    @JvmField
    public val OVERWORLD: DimensionId =
        //#if MC >= 1.16.5
        DimensionTypes.OVERWORLD_ID
        //#else
        //$$ 0
        //#endif

    @JvmField
    public val NETHER: DimensionId =
        //#if MC >= 1.16.5
        DimensionTypes.THE_NETHER_ID
        //#else
        //$$ -1
        //#endif

    @JvmField
    public val END: DimensionId =
        //#if MC >= 1.16.5
        DimensionTypes.THE_END_ID
        //#else
        //$$ 1
        //#endif

    public fun derive(
        identifier: DimensionId,
        name: String
    ): OmniDimension {
        return when (identifier) {
            OVERWORLD -> OmniDimension.Overworld
            NETHER -> OmniDimension.Nether
            END -> OmniDimension.End
            else -> OmniDimension.Custom(identifier, name)
        }
    }
}
