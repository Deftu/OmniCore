package dev.deftu.omnicore.common.world

import dev.deftu.omnicore.client.OmniClient
import net.minecraft.util.Identifier
import net.minecraft.world.World
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.16.5
import net.minecraft.world.dimension.DimensionTypes
//#endif

public typealias DimensionId =
        //#if MC >= 1.16.5
        Identifier
        //#else
        //$$ Int
        //#endif

public sealed interface OmniDimension {

    public companion object {

        @JvmStatic
        public val current: OmniDimension?
            get() {
                val world = OmniClient.currentWorld ?: return null
                return from(world.vanilla)
            }

        @JvmStatic
        public fun from(world: World): OmniDimension {
            //#if MC >= 1.16.5
            val id = world.registryKey.value
            val type = world.dimension.toString()
            //#elseif MC >= 1.12.2
            //#if FORGE
            //$$ val id = world.provider.dimension
            //#else
            //$$ val id = world.dimension.dimensionType.id
            //#endif
            //$$ val type = world.provider.dimensionType.toString()
            //#else
            //#if FORGE
            //$$ val id = world.provider.dimensionId
            //$$ val type = world.provider.dimensionName
            //#else
            //$$ val id = world.dimension.type
            //$$ val type = world.dimension.name
            //#endif
            //#endif
            return from(id, type)
        }

        @JvmStatic
        @ApiStatus.Internal
        public fun from(
            identifier: DimensionId,
            name: String
        ): OmniDimension {
            //#if MC >= 1.16.5
            return when (identifier) {
                DimensionTypes.OVERWORLD_ID -> Overworld(identifier)
                DimensionTypes.THE_NETHER_ID -> Nether(identifier)
                DimensionTypes.THE_END_ID -> End(identifier)
                else -> Custom(identifier, name)
            }
            //#else
            //$$ return when (identifier) {
            //$$     0 -> Overworld(identifier)
            //$$     -1 -> Nether(identifier)
            //$$     1 -> End(identifier)
            //$$     else -> Custom(identifier, name)
            //$$ }
            //#endif
        }

    }

    public val id: DimensionId

    public data class Overworld(override val id: DimensionId) : OmniDimension

    public data class Nether(override val id: DimensionId) : OmniDimension

    public data class End(override val id: DimensionId) : OmniDimension

    public data class Custom(override val id: DimensionId, public val name: String) : OmniDimension {

        init {
            require(name.isNotBlank()) { "Dimension name cannot be blank" }
        }

    }

}
