package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.render.state.TrackedState

//#if MC <= 1.16.5
//$$ import dev.deftu.omnicore.internal.client.render.state.legacy.LightingStateInternals
//#endif

public data class OmniLegacyLightingState(
    public val isEnabled: Boolean,
) : TrackedState<OmniLegacyLightingState> {
    override var prevState: OmniLegacyLightingState? = null
        private set

    override fun submit(saveLast: Boolean) {
        //#if MC <= 1.16.5
        //$$ if (saveLast) {
        //$$     prevState = OmniLegacyRenderStates.lighting
        //$$ }
        //$$
        //$$ if (isEnabled) {
        //$$     LightingStateInternals.enable()
        //$$ } else {
        //$$     LightingStateInternals.disable()
        //$$ }
        //#endif
    }

    public fun asEnabled(): OmniLegacyLightingState {
        return if (isEnabled) this else copy(isEnabled = true)
    }

    public fun asDisabled(): OmniLegacyLightingState {
        return if (!isEnabled) this else copy(isEnabled = false)
    }

    public companion object {
        @JvmField public val DISABLED: OmniLegacyLightingState = OmniLegacyLightingState(false)
        @JvmField public val ENABLED: OmniLegacyLightingState = OmniLegacyLightingState(true)
    }
}
