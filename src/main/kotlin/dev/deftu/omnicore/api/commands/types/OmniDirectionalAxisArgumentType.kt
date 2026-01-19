package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.context.CommandContext
import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.direction.OmniDirectionalAxis

public class OmniDirectionalAxisArgumentType : EnumArgumentType<OmniDirectionalAxis>(OmniDirectionalAxis.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun create(): OmniDirectionalAxisArgumentType {
            return OmniDirectionalAxisArgumentType()
        }

        @JvmStatic
        public fun <T> get(context: CommandContext<T>, name: String): OmniDirectionalAxis {
            return context.getArgument(name, OmniDirectionalAxis::class.java)
        }
    }
}
