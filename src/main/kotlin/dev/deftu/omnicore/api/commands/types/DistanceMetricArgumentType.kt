package dev.deftu.omnicore.api.commands.types

import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType
import dev.deftu.omnicore.api.data.DistanceMetric

public class DistanceMetricArgumentType : EnumArgumentType<DistanceMetric>(DistanceMetric.ALL.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun distanceMetric(): DistanceMetricArgumentType {
            return DistanceMetricArgumentType()
        }
    }
}
