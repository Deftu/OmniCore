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
}
