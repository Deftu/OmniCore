package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.context.CommandContext
import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.data.RadiusMetric

public class RadiusMetricArgumentType : EnumArgumentType<RadiusMetric>(RadiusMetric.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun create(): RadiusMetricArgumentType {
            return RadiusMetricArgumentType()
        }

        @JvmStatic
        public fun <T> get(context: CommandContext<T>, name: String): RadiusMetric {
            return context.getArgument(name, RadiusMetric::class.java)
        }
    }
}
