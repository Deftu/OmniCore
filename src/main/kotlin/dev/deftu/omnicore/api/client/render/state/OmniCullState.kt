package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.internal.client.render.state.CullStateInternals

public data class OmniCullState(
    public val isEnabled: Boolean,
    public val mode: CullFace,
) : TrackedState<OmniCullState> {
    override var prevState: OmniCullState? = null
        private set

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = OmniRenderStates.cull
        }

        if (isEnabled) {
            CullStateInternals.enable()
            CullStateInternals.face(mode.const)
        } else {
            CullStateInternals.disable()
        }
    }

    public fun asEnabled(): OmniCullState {
        return if (isEnabled) this else OmniCullState(true, mode)
    }

    public fun asDisabled(): OmniCullState {
        return if (!isEnabled) this else OmniCullState(false, mode)
    }

    public fun withMode(mode: CullFace): OmniCullState {
        return if (this.mode == mode) this else OmniCullState(isEnabled, mode)
    }
}
