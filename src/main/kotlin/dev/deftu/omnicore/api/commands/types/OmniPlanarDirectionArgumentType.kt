package dev.deftu.omnicore.api.commands.types

import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.direction.OmniPlanarDirection

public class OmniPlanarDirectionArgumentType : EnumArgumentType<OmniPlanarDirection>(OmniPlanarDirection.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun planarDirection(): OmniPlanarDirectionArgumentType {
            return OmniPlanarDirectionArgumentType()
        }
    }
}
