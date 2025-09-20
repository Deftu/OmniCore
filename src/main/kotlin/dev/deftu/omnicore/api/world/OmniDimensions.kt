@file:JvmName("OmniDimensions")

package dev.deftu.omnicore.api.world

import dev.deftu.omnicore.internal.world.DimensionInternals
import net.minecraft.util.Identifier
import net.minecraft.world.World

public typealias DimensionId =
        //#if MC >= 1.16.5
        Identifier
        //#else
        //$$ Int
        //#endif

public val World.dimensionType: OmniDimension
    get() {
        //#if MC >= 1.16.5
        val id = registryKey.value
        val type = dimension.toString()
        //#elseif MC >= 1.12.2
        //#if FORGE
        //$$ val id = provider.dimension
        //#else
        //$$ val id = dimension.dimensionType.id
        //#endif
        //$$ val type = provider.dimensionType.toString()
        //#else
        //#if FORGE
        //$$ val id = provider.dimensionId
        //$$ val type = provider.dimensionName
        //#else
        //$$ val id = dimension.type
        //$$ val type = dimension.name
        //#endif
        //#endif
        return DimensionInternals.derive(id, type)
    }
