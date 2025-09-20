package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.state.TrackedState

//#if MC <= 1.19.2
//$$ import dev.deftu.omnicore.internal.client.textures.TextureInternals
//#endif

public data class OmniLegacyTextureState(
    public val unit: OmniTextureUnit,
    public val isEnabled: Boolean,
) : TrackedState<OmniLegacyTextureState> {
    override var prevState: OmniLegacyTextureState? = null
        private set

    override fun submit(saveLast: Boolean) {
        //#if MC <= 1.19.2
        //$$ if (saveLast) {
        //$$     prevState = OmniLegacyRenderStates.textures.first { it.unit == unit }
        //$$ }
        //$$
        //$$ if (isEnabled) {
        //$$     TextureInternals.enable(unit)
        //$$ } else {
        //$$     TextureInternals.disable(unit)
        //$$ }
        //#endif
    }

    public fun asEnabled(): OmniLegacyTextureState {
        return if (isEnabled) this else copy(isEnabled = true)
    }

    public fun asDisabled(): OmniLegacyTextureState {
        return if (!isEnabled) this else copy(isEnabled = false)
    }

    public companion object {
        @JvmField public val ENABLED: OmniLegacyTextureState = OmniLegacyTextureState(OmniTextureUnit.TEXTURE0, true)
        @JvmField public val DISABLED: OmniLegacyTextureState = OmniLegacyTextureState(OmniTextureUnit.TEXTURE0, false)

        @JvmStatic
        public fun enabled(unit: OmniTextureUnit): OmniLegacyTextureState {
            return OmniLegacyTextureState(unit, true)
        }

        @JvmStatic
        public fun disabled(unit: OmniTextureUnit): OmniLegacyTextureState {
            return OmniLegacyTextureState(unit, false)
        }
    }
}
