package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.internal.client.render.state.legacy.AlphaStateInternals
import dev.deftu.omnicore.internal.client.render.state.legacy.LightingStateInternals
import dev.deftu.omnicore.internal.client.textures.TextureInternals

public object OmniLegacyRenderStates {
    @JvmStatic
    public val alpha: OmniLegacyAlphaState
        get() {
            return OmniLegacyAlphaState(
                isEnabled = AlphaStateInternals.isEnabled,
                func = AlphaStateInternals.func,
                ref = AlphaStateInternals.ref,
            )
        }

    @JvmStatic
    public val lighting: OmniLegacyLightingState
        get() {
            return OmniLegacyLightingState(isEnabled = LightingStateInternals.isEnabled,)
        }

    @JvmStatic
    public val activeTexture: OmniLegacyTextureState
        get() {
            return OmniLegacyTextureState(
                unit = TextureInternals.activeUnit,
                isEnabled = TextureInternals.isEnabled(TextureInternals.activeUnit),
            )
        }

    @JvmStatic
    public val textures: List<OmniLegacyTextureState>
        get() {
            return TextureInternals.states.map { (unit, enabled) ->
                OmniLegacyTextureState(
                    unit = unit,
                    isEnabled = enabled,
                )
            }
        }

    @JvmStatic
    public val current: OmniLegacyRenderState
        get() = OmniLegacyRenderState(
            alphaState = alpha,
            lightingState = lighting,
            textureStates = textures,
        )

    /** Forcibly syncs Minecraft's GL state tracker to match all global render states. */
    @JvmStatic
    public fun sync() {
        syncAlpha()
        syncLighting()
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global alpha state. */
    @JvmStatic
    public fun syncAlpha() {
        //#if MC <= 1.16.5
        //$$ val enabled = AlphaStateInternals.isEnabled
        //$$ if (enabled) {
        //$$     AlphaStateInternals.enable()
        //$$     val func = AlphaStateInternals.func
        //$$     val ref  = AlphaStateInternals.ref
        //$$     AlphaStateInternals.configure(func, ref)
        //$$ } else {
        //$$     AlphaStateInternals.disable()
        //$$ }
        //#endif
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global lighting state. */
    @JvmStatic
    public fun syncLighting() {
        //#if MC <= 1.16.5
        //$$ val enabled = LightingStateInternals.isEnabled
        //$$ if (enabled) {
        //$$     LightingStateInternals.enable()
        //$$ } else {
        //$$     LightingStateInternals.disable()
        //$$ }
        //#endif
    }
}
