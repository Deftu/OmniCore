@file:JvmName("OmniDimensions")

package dev.deftu.omnicore.api.world

import dev.deftu.omnicore.internal.world.DimensionInternals
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level

public typealias DimensionId =
        //#if MC >= 1.16.5
        ResourceLocation
        //#else
        //$$ Int
        //#endif

public val Level.dimensionType: OmniDimension
    get() {
        //#if MC >= 1.16.5
        val id = dimension().location()
        val type = dimensionType().toString()
        //#elseif MC >= 1.12.2
        //#if FORGE
        //$$ val id = provider.dimension
        //#else
        //$$ val id = provider.dimensionType.id
        //#endif
        //$$ val type = provider.dimensionType.toString()
        //#else
        //#if FORGE
        //$$ val id = provider.dimensionId
        //$$ val type = provider.dimensionName
        //#else
        //$$ val id = provider.dimensionId
        //$$ val type = provider.dimensionName
        //#endif
        //#endif
        return DimensionInternals.derive(id, type)
    }
