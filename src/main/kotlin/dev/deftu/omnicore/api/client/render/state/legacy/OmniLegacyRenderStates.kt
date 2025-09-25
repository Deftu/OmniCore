package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.internal.client.render.state.legacy.AlphaStateInternals
import dev.deftu.omnicore.internal.client.render.state.legacy.LightingStateInternals
import dev.deftu.omnicore.internal.client.textures.TextureInternals

//#if MC <= 1.16.5
//$$ import org.lwjgl.opengl.GL11
//#endif

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
    public val lineStipple: OmniLegacyLineStippleState
        get() {
            //#if MC >= 1.17.1
            return OmniLegacyLineStippleState(
                isEnabled = false,
                factor = 1,
                pattern = 0xFFFF.toShort(),
            )
            //#else
            //$$ val enabled = GL11.glIsEnabled(GL11.GL_LINE_STIPPLE)
            //$$ val factor = GL11.glGetInteger(GL11.GL_LINE_STIPPLE_REPEAT)
            //$$ val pattern = GL11.glGetInteger(GL11.GL_LINE_STIPPLE_PATTERN).toShort()
            //$$ return OmniLegacyLineStippleState(
            //$$     isEnabled = enabled,
            //$$     factor = factor,
            //$$     pattern = pattern,
            //$$ )
            //#endif
        }

    @JvmStatic
    public val shadeModel: OmniLegacyShadeModelState
        get() {
            //#if MC >= 1.17.1
            return OmniLegacyShadeModelState.SMOOTH
            //#else
            //$$ val model = ShadeModel.findOrThrow(GL11.glGetInteger(GL11.GL_SHADE_MODEL))
            //$$ return OmniLegacyShadeModelState(model)
            //#endif
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
            lineStippleState = lineStipple,
            shadeModelState = shadeModel,
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

    public fun texture(unit: OmniTextureUnit): OmniLegacyTextureState {
        return OmniLegacyTextureState(
            unit = unit,
            isEnabled = TextureInternals.isEnabled(unit),
        )
    }
}
