package dev.deftu.omnicore.internal.world

import dev.deftu.omnicore.api.world.DimensionId
import dev.deftu.omnicore.api.world.OmniDimension
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.19.2
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
//#elseif MC >= 1.16.5
//$$ import net.minecraft.world.level.dimension.DimensionType
//#endif

@ApiStatus.Internal
public object DimensionInternals {
    @JvmField
    public val OVERWORLD: DimensionId =
        //#if MC >= 1.21.11
        //$$ BuiltinDimensionTypes.OVERWORLD.identifier()
        //#elseif MC >= 1.19.2
        BuiltinDimensionTypes.OVERWORLD_EFFECTS
        //#elseif MC >= 1.16.5
        //$$ DimensionType.OVERWORLD_EFFECTS
        //#else
        //$$ 0
        //#endif

    @JvmField
    public val NETHER: DimensionId =
        //#if MC >= 1.21.11
        //$$ BuiltinDimensionTypes.NETHER.identifier()
        //#elseif MC >= 1.19.2
        BuiltinDimensionTypes.NETHER_EFFECTS
        //#elseif MC >= 1.16.5
        //$$ DimensionType.NETHER_EFFECTS
        //#else
        //$$ -1
        //#endif

    @JvmField
    public val END: DimensionId =
        //#if MC >= 1.21.11
        //$$ BuiltinDimensionTypes.END.identifier()
        //#elseif MC >= 1.19.2
        BuiltinDimensionTypes.END_EFFECTS
        //#elseif MC >= 1.16.5
        //$$ DimensionType.END_EFFECTS
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
