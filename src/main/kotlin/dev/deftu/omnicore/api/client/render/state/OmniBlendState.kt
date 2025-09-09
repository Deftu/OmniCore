package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.internal.client.render.state.BlendStateInternals

//#if MC >= 1.17 && MC <= 1.21.1
//$$ import net.minecraft.client.gl.GlBlendState
//#endif

//#if MC <= 1.16.5 || MC >= 1.21.5

//#endif

//#if MC >= 1.16.5 && MC < 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#endif

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
public data class OmniBlendState(
    public val isEnabled: Boolean,
    public val equation: BlendEquation,
    public val function: BlendFunction
) : TrackedState<OmniBlendState> {
    //#if MC >= 1.17.1 && MC <= 1.21.1
    //$$ private inner class VanillaBlendState : GlBlendState {
    //$$     constructor() : super()
    //$$
    //$$     constructor(
    //$$         srcRgb: Int,
    //$$         dstRgb: Int,
    //$$         func: Int
    //$$     ) : super(srcRgb, dstRgb, func)
    //$$
    //$$     constructor(
    //$$         srcRgb: Int,
    //$$         dstRgb: Int,
    //$$         srcAlpha: Int,
    //$$         dstAlpha: Int,
    //$$         func: Int
    //$$     ) : super(srcRgb, dstRgb, srcAlpha, dstAlpha, func)
    //$$
    //$$     override fun enable() {
    //$$         super.enable()
    //$$         this@OmniManagedBlendState.applyGlobally()
    //$$     }
    //$$ }
    //$$
    //$$ public val vanilla: GlBlendState = if (isEnabled) {
    //$$     if (function.isSeparate) {
    //$$         VanillaBlendState(function.srcColor.value, function.dstColor.value, function.srcAlpha.value, function.dstAlpha.value, equation.value)
    //$$     } else {
    //$$         VanillaBlendState(function.srcColor.value, function.dstColor.value, equation.value)
    //$$     }
    //$$ } else {
    //$$     VanillaBlendState()
    //$$ }
    //#endif

    override var prevState: OmniBlendState? = null
        private set

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = OmniRenderStates.blend
        }

        //#if MC >= 1.17.1 && MC <= 1.21.1
        //$$ vanilla.enable()
        //#else
        applyGlobally()
        //#endif
    }

    private fun applyGlobally() {
        if (isEnabled) {
            BlendStateInternals.enable()
            BlendStateInternals.equation(equation.const)
            BlendStateInternals.funcSeparate(function.srcColor, function.dstColor, function.srcAlpha, function.dstAlpha)
        } else {
            BlendStateInternals.disable()
        }
    }

    public companion object {
        @JvmField public val DISABLED: OmniBlendState = OmniBlendState(false, BlendEquation.ADD, BlendFunction.DISABLED)
        @JvmField public val NORMAL: OmniBlendState = OmniBlendState(true, BlendEquation.ADD, BlendFunction.DEFAULT)
        @JvmField public val ALPHA: OmniBlendState = OmniBlendState(true, BlendEquation.ADD, BlendFunction.ALPHA)
        @JvmField public val ALPHA_OVERWRITE: OmniBlendState = OmniBlendState(true, BlendEquation.ADD, BlendFunction.ALPHA_OVERWRITE)
        @JvmField public val PREMULTIPLIED: OmniBlendState = OmniBlendState(true, BlendEquation.ADD, BlendFunction.PREMULTIPLIED)

        @JvmStatic
        public fun asEnabled(
            equation: BlendEquation,
            function: BlendFunction
        ): OmniBlendState {
            return OmniBlendState(
                isEnabled = true,
                equation = equation,
                function = function
            )
        }
    }
}
