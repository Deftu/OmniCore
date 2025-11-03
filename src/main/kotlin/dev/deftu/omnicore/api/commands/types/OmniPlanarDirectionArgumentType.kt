package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.context.CommandContext
import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.direction.OmniPlanarDirection

public class OmniPlanarDirectionArgumentType : EnumArgumentType<OmniPlanarDirection>(OmniPlanarDirection.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun planarDirection(): OmniPlanarDirectionArgumentType {
            return OmniPlanarDirectionArgumentType()
        }

        @JvmStatic
        public fun <T> get(context: CommandContext<T>, name: String): OmniPlanarDirection {
            return context.getArgument(name, OmniPlanarDirection::class.java)
        }
    }
}
