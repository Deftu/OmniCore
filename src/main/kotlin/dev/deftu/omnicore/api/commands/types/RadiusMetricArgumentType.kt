package dev.deftu.omnicore.api.commands.types

import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.data.DistanceMetric
import dev.deftu.omnicore.api.data.RadiusMetric

public class RadiusMetricArgumentType : EnumArgumentType<RadiusMetric>(RadiusMetric.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun radiusMetric(): RadiusMetricArgumentType {
            return RadiusMetricArgumentType()
        }
    }
}
