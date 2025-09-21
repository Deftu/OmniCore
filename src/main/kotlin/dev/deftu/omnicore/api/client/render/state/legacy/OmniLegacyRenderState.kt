package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.render.state.TrackedState

public data class OmniLegacyRenderState(
    @JvmField public var alphaState: OmniLegacyAlphaState,
    @JvmField public var lightingState: OmniLegacyLightingState,
    @JvmField public var textureStates: List<OmniLegacyTextureState>,
) : TrackedState<OmniLegacyRenderState> {
    override var prevState: OmniLegacyRenderState? = null
        private set

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = OmniLegacyRenderStates.current
        }

        alphaState.submit(saveLast)
        lightingState.submit(saveLast)
        textureStates.forEach { it.submit(saveLast) }
    }

    public fun applyTo(other: OmniLegacyRenderState) {
        if (other.alphaState != alphaState) {
            other.setAlphaState(alphaState)
        }

        if (other.lightingState != lightingState) {
            other.setLightingState(lightingState)
        }

        if (other.textureStates != textureStates) {
            other.setTextureStates(textureStates)
        }
    }

    public fun setAlphaState(state: OmniLegacyAlphaState): OmniLegacyRenderState {
        alphaState = state
        state.submit(true)
        return this
    }

    public fun setLightingState(state: OmniLegacyLightingState): OmniLegacyRenderState {
        lightingState = state
        state.submit(true)
        return this
    }

    public fun setTextureStates(states: List<OmniLegacyTextureState>): OmniLegacyRenderState {
        textureStates = states
        states.forEach { it.submit(true) }
        return this
    }
}
