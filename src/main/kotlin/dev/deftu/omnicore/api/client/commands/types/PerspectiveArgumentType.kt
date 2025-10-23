package dev.deftu.omnicore.api.client.commands.types

import dev.deftu.omnicore.api.client.options.OmniPerspective
import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType

public class PerspectiveArgumentType : EnumArgumentType<OmniPerspective>(OmniPerspective.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun perspective(): PerspectiveArgumentType {
            return PerspectiveArgumentType()
        }
    }
}
