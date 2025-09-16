package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyRenderState

public class OmniRenderState(
    @JvmField public var blendState: OmniBlendState,
    @JvmField public var depthState: OmniDepthState,
    @JvmField public var cullState: OmniCullState,
    @JvmField public var colorMaskState: OmniColorMask,
    @JvmField public var polygonOffsetState: OmniPolygonOffset,
    @JvmField public var legacyState: OmniLegacyRenderState,
) : TrackedState<OmniRenderState> {
    override var prevState: OmniRenderState? = null
        private set

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = OmniRenderStates.current
        }

        blendState.submit(saveLast)
        depthState.submit(saveLast)
        cullState.submit(saveLast)
        colorMaskState.submit(saveLast)
        polygonOffsetState.submit(saveLast)
    }

    public fun applyTo(other: OmniRenderState) {
        if (other.blendState != blendState) {
            other.setBlendState(blendState)
        }

        if (other.depthState != depthState) {
            other.setDepthState(depthState)
        }

        if (other.cullState != cullState) {
            other.setCullState(cullState)
        }

        if (other.colorMaskState != colorMaskState) {
            other.setColorMaskState(colorMaskState)
        }

        if (other.polygonOffsetState != polygonOffsetState) {
            other.setPolygonOffsetState(polygonOffsetState)
        }

        if (other.legacyState != legacyState) {
            other.setLegacyState(legacyState)
        }
    }

    public fun setBlendState(state: OmniBlendState): OmniRenderState {
        blendState = state
        state.submit(true)
        return this
    }

    public fun setDepthState(state: OmniDepthState): OmniRenderState {
        depthState = state
        state.submit(true)
        return this
    }

    public fun setCullState(state: OmniCullState): OmniRenderState {
        cullState = state
        state.submit(true)
        return this
    }

    public fun setColorMaskState(state: OmniColorMask): OmniRenderState {
        colorMaskState = state
        state.submit(true)
        return this
    }

    public fun setPolygonOffsetState(state: OmniPolygonOffset): OmniRenderState {
        polygonOffsetState = state
        state.submit(true)
        return this
    }

    public fun setLegacyState(state: OmniLegacyRenderState): OmniRenderState {
        legacyState = state
        state.submit(true)
        return this
    }
}
