package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.internal.client.render.state.AlphaStateInternals
import dev.deftu.omnicore.internal.client.render.state.BlendStateInternals
import dev.deftu.omnicore.internal.client.render.state.ColorMaskStateInternals
import dev.deftu.omnicore.internal.client.render.state.CullStateInternals
import dev.deftu.omnicore.internal.client.render.state.DepthStateInternals
import dev.deftu.omnicore.internal.client.render.state.PolygonOffsetStateInternals

public object OmniRenderStates {
    @JvmStatic
    public val alpha: OmniAlphaState
        get() {
            return OmniAlphaState(
                isEnabled = AlphaStateInternals.isEnabled,
                func = AlphaStateInternals.func,
                ref = AlphaStateInternals.ref,
            )
        }

    @JvmStatic
    public val blend: OmniBlendState
        get() {
            return OmniBlendState(
                isEnabled = BlendStateInternals.isEnabled,
                equation = BlendStateInternals.equation,
                function = BlendStateInternals.function,
            )
        }

    @JvmStatic
    public val depth: OmniDepthState
        get() {
            return OmniDepthState(
                isEnabled = DepthStateInternals.isEnabled,
                function = DepthStateInternals.func,
                mask = DepthStateInternals.mask,
            )
        }

    @JvmStatic
    public val cull: OmniCullState
        get() {
            return OmniCullState(
                isEnabled = CullStateInternals.isEnabled,
                mode = CullStateInternals.mode,
            )
        }

    @JvmStatic
    public val colorMask: OmniColorMask
        get() {
            val (rg, ba) = ColorMaskStateInternals.values
            val (red, green) = rg
            val (blue, alpha) = ba
            return OmniColorMask(
                red = red,
                green = green,
                blue = blue,
                alpha = alpha,
            )
        }

    @JvmStatic
    public val polygonOffset: OmniPolygonOffset
        get() {
            return OmniPolygonOffset(
                isEnabled = PolygonOffsetStateInternals.isEnabled,
                factor = PolygonOffsetStateInternals.factor,
                units = PolygonOffsetStateInternals.units,
            )
        }

    @JvmStatic
    public val current: OmniRenderState
        get() {
            return OmniRenderState(
                alphaState = alpha,
                blendState = blend,
                depthState = depth,
                cullState = cull,
                colorMaskState = colorMask,
                polygonOffsetState = polygonOffset,
            )
        }

    /** Forcibly syncs Minecraft's GL state tracker to match all global render states. */
    @JvmStatic
    public fun sync() {
        syncAlpha()
        syncBlend()
        syncDepth()
        syncCull()
        syncColorMask()
        syncPolygonOffset()
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global alpha state. */
    @JvmStatic
    public fun syncAlpha() {
        //#if MC <= 1.16.5
        //$$ val enabled = AlphaStateInternals.isEnabled
        //$$ if (enabled) {
        //$$     AlphaStateInternals.enableAlpha()
        //$$     val func = AlphaStateInternals.func
        //$$     val ref  = AlphaStateInternals.ref
        //$$     AlphaStateInternals.alphaFunc(func, ref)
        //$$ } else {
        //$$     AlphaStateInternals.disableAlpha()
        //$$ }
        //#endif
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global blend state. */
    @JvmStatic
    public fun syncBlend() {
        val enabled = BlendStateInternals.isEnabled
        if (enabled) {
            BlendStateInternals.enable()
            val equation = BlendStateInternals.equation
            BlendStateInternals.equation(equation.const)
            val function = BlendStateInternals.function
            if (function.isSeparate) {
                BlendStateInternals.funcSeparate(
                    function.srcColor.const,
                    function.dstColor.const,
                    function.srcAlpha.const,
                    function.dstAlpha.const
                )
            } else {
                BlendStateInternals.func(function.srcColor, function.dstAlpha)
            }
        } else {
            BlendStateInternals.disable()
        }
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global depth state. */
    @JvmStatic
    public fun syncDepth() {
        val enabled = DepthStateInternals.isEnabled
        if (enabled) {
            DepthStateInternals.enable()
            val func = DepthStateInternals.func
            DepthStateInternals.func(func.const)
            val mask = DepthStateInternals.mask
            DepthStateInternals.mask(mask)
        } else {
            DepthStateInternals.disable()
        }
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global cull state. */
    @JvmStatic
    public fun syncCull() {
        val enabled = CullStateInternals.isEnabled
        if (enabled) {
            CullStateInternals.enable()
            val mode = CullStateInternals.mode
            CullStateInternals.face(mode.const)
        } else {
            CullStateInternals.disable()
        }
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global color mask state. */
    @JvmStatic
    public fun syncColorMask() {
        val (rg, ba) = ColorMaskStateInternals.values
        val (red, green) = rg
        val (blue, alpha) = ba
        ColorMaskStateInternals.configure(red, green, blue, alpha)
    }

    /** Forcibly syncs Minecraft's GL state tracker to match the global polygon offset state. */
    @JvmStatic
    public fun syncPolygonOffset() {
        val enabled = PolygonOffsetStateInternals.isEnabled
        if (enabled) {
            PolygonOffsetStateInternals.enable()
            val factor = PolygonOffsetStateInternals.factor
            val units = PolygonOffsetStateInternals.units
            PolygonOffsetStateInternals.configure(factor, units)
        } else {
            PolygonOffsetStateInternals.disable()
        }
    }
}
