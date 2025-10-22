package dev.deftu.omnicore.api.commands.types

import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.direction.OmniDirectionalAxis

public class OmniDirectionalAxisArgumentType : EnumArgumentType<OmniDirectionalAxis>(OmniDirectionalAxis.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun directionalAxis(): OmniDirectionalAxisArgumentType {
            return OmniDirectionalAxisArgumentType()
        }
    }
}
