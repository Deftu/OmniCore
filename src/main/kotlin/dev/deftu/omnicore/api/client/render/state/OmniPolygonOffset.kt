package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.internal.client.render.state.PolygonOffsetStateInternals

public data class OmniPolygonOffset(
    public val isEnabled: Boolean,
    public val factor: Float,
    public val units: Float,
) : TrackedState<OmniPolygonOffset> {
    override var prevState: OmniPolygonOffset? = null
        private set

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = OmniRenderStates.polygonOffset
        }

        if (isEnabled) {
            PolygonOffsetStateInternals.enable()
            PolygonOffsetStateInternals.configure(factor, units)
        } else {
            PolygonOffsetStateInternals.disable()
        }
    }

    public fun asEnabled(): OmniPolygonOffset {
        return if (isEnabled) this else copy(isEnabled = true)
    }

    public fun asDisabled(): OmniPolygonOffset {
        return if (!isEnabled) this else copy(isEnabled = false)
    }

    public fun withFactor(factor: Float): OmniPolygonOffset {
        return if (this.factor == factor) this else copy(factor = factor)
    }

    public fun withUnits(units: Float): OmniPolygonOffset {
        return if (this.units == units) this else copy(units = units)
    }

    public companion object {
        @JvmField public val DEFAULT: OmniPolygonOffset = OmniPolygonOffset(false, 0f, 0f)
        @JvmField public val DISABLED: OmniPolygonOffset = DEFAULT

        @JvmStatic
        public fun asEnabled(factor: Float, units: Float): OmniPolygonOffset {
            return OmniPolygonOffset(true, factor, units)
        }
    }
}
