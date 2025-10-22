package dev.deftu.omnicore.api.commands.types

import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.direction.OmniSpatialDirection

public class OmniSpatialDirectionArgumentType : EnumArgumentType<OmniSpatialDirection>(OmniSpatialDirection.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun spatialDirection(): OmniSpatialDirectionArgumentType {
            return OmniSpatialDirectionArgumentType()
        }
    }
}
