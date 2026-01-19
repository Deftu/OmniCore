package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.context.CommandContext
import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.direction.OmniSpatialDirection

public class OmniSpatialDirectionArgumentType : EnumArgumentType<OmniSpatialDirection>(OmniSpatialDirection.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun create(): OmniSpatialDirectionArgumentType {
            return OmniSpatialDirectionArgumentType()
        }

        @JvmStatic
        public fun <T> get(context: CommandContext<T>, name: String): OmniSpatialDirection {
            return context.getArgument(name, OmniSpatialDirection::class.java)
        }
    }
}
