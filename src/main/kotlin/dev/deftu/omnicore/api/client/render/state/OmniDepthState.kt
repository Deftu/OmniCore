package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.internal.client.render.state.DepthStateInternals

public data class OmniDepthState(
    public val isEnabled: Boolean,
    public val function: DepthFunction,
    public val mask: Boolean
) : TrackedState<OmniDepthState> {
    override var prevState: OmniDepthState? = null
        private set

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = OmniRenderStates.depth
        }

        if (isEnabled) {
            DepthStateInternals.enable()
            DepthStateInternals.func(function.const)
            DepthStateInternals.mask(mask)
        } else {
            DepthStateInternals.disable()
        }
    }

    public fun asEnabled(): OmniDepthState {
        return if (isEnabled) this else copy(isEnabled = true)
    }

    public fun asDisabled(): OmniDepthState {
        return if (!isEnabled) this else copy(isEnabled = false)
    }

    public fun withFunction(function: DepthFunction): OmniDepthState {
        return if (this.function == function) this else copy(function = function)
    }

    public fun withMask(mask: Boolean): OmniDepthState {
        return if (this.mask == mask) this else copy(mask = mask)
    }

    public companion object {
        @JvmField public val DISABLED: OmniDepthState = OmniDepthState(false, DepthFunction.LESS, false)
    }
}
