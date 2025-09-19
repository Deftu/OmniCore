package dev.deftu.omnicore.api.direction

public data class AxisSpace(
    public val axis: OmniDirectionalAxis,
    public val isPositive: Boolean,
) {
    public val isNegative: Boolean
        get() = !isPositive

    public val direction: OmniSpatialDirection
        get() = if (isPositive) axis.positiveDirection else axis.negativeDirection

    public val opposite: AxisSpace
        get() = AxisSpace(axis, !isPositive)

    public operator fun unaryMinus(): AxisSpace {
        return if (isNegative) this else this.copy(isPositive = false)
    }

    public operator fun unaryPlus(): AxisSpace {
        return if (isPositive) this else this.copy(isPositive = true)
    }
}
