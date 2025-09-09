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

    public companion object {
        @JvmField public val DISABLED: OmniDepthState = OmniDepthState(false, DepthFunction.LESS, false)

        @JvmStatic
        @JvmOverloads
        public fun asEnabled(
            function: DepthFunction = DepthStateInternals.func,
            mask: Boolean = true
        ): OmniDepthState {
            return OmniDepthState(true, function, mask)
        }
    }
}
