package dev.deftu.omnicore.client.render.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

//#if MC >= 1.17 && MC <= 1.21.1
import net.minecraft.client.gl.GlBlendState
//#endif

//#if MC <= 1.16.5 || MC >= 1.21.5
//$$ import org.lwjgl.opengl.GL14
//#endif

//#if MC >= 1.16.5 && MC < 1.21.5
import com.mojang.blaze3d.systems.RenderSystem
//#endif

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
@GameSide(Side.CLIENT)
public data class OmniManagedBlendState(
    public val isEnabled: Boolean,
    public val equation: BlendEquation,
    public val function: BlendFunction
) {

    //#if MC >= 1.17.1 && MC <= 1.21.1
    @GameSide(Side.CLIENT)
    private inner class VanillaBlendState : GlBlendState {

        constructor() : super()

        constructor(
            srcRgb: Int,
            dstRgb: Int,
            func: Int
        ) : super(srcRgb, dstRgb, func)

        constructor(
            srcRgb: Int,
            dstRgb: Int,
            srcAlpha: Int,
            dstAlpha: Int,
            func: Int
        ) : super(srcRgb, dstRgb, srcAlpha, dstAlpha, func)

        override fun enable() {
            super.enable()
            this@OmniManagedBlendState.applyGlobally()
        }

    }

    @GameSide(Side.CLIENT)
    public val vanilla: GlBlendState = if (isEnabled) {
        if (function.isSeparate) {
            VanillaBlendState(function.srcColor.value, function.dstColor.value, function.srcAlpha.value, function.dstAlpha.value, equation.value)
        } else {
            VanillaBlendState(function.srcColor.value, function.dstColor.value, equation.value)
        }
    } else {
        VanillaBlendState()
    }
    //#endif

    public fun activate() {
        //#if MC >= 1.17.1 && MC <= 1.21.1
        vanilla.enable()
        //#else
        //$$ applyGlobally()
        //#endif
    }

    private fun applyGlobally() {
        if (isEnabled) {
            enableBlend()
            blendEquation(equation.value)
            blendFuncSeparate(function.srcColor, function.dstColor, function.srcAlpha, function.dstAlpha)
        } else {
            disableBlend()
        }
    }

    public companion object {

        @JvmField
        @GameSide(Side.CLIENT)
        public val DISABLED: OmniManagedBlendState = OmniManagedBlendState(false, BlendEquation.ADD, BlendFunction.DISABLED)

        @JvmField
        @GameSide(Side.CLIENT)
        public val NORMAL: OmniManagedBlendState = OmniManagedBlendState(true, BlendEquation.ADD, BlendFunction.DEFAULT)

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun active(): OmniManagedBlendState {
            return OmniManagedBlendState(
                GL11.glGetBoolean(GL11.GL_BLEND),
                BlendEquation.active(),
                BlendFunction.active()
            )
        }

        @JvmStatic
        public fun asEnabled(
            equation: BlendEquation,
            function: BlendFunction
        ): OmniManagedBlendState {
            return OmniManagedBlendState(
                isEnabled = true,
                equation = equation,
                function = function
            )
        }

        @JvmStatic
        public fun enable(
            equation: BlendEquation,
            function: BlendFunction
        ): OmniManagedBlendState {
            return asEnabled(equation, function).also(OmniManagedBlendState::activate)
        }

        @JvmStatic
        public fun disable(): OmniManagedBlendState {
            return DISABLED.also(OmniManagedBlendState::activate)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun enableBlend() {
            GlStateManager._enableBlend()
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun disableBlend() {
            GlStateManager._disableBlend()
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun blendEquation(equation: Int) {
            //#if MC >= 1.21.5
            //$$ GL14.glBlendEquation(equation)
            //#elseif MC >= 1.16.5
            RenderSystem.blendEquation(equation)
            //#else
            //$$ GL14.glBlendEquation(equation)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun blendFunc(srcFactor: Int, dstFactor: Int) {
            //#if MC >= 1.21.5
            //$$ GL11.glBlendFunc(srcFactor, dstFactor)
            //#elseif MC >= 1.17.1
            RenderSystem.blendFunc(srcFactor, dstFactor)
            //#else
            //$$ GlStateManager.blendFunc(srcFactor, dstFactor)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun blendFunc(srcFactor: SrcFactor, dstFactorAlpha: DstFactor) {
            blendFunc(srcFactor.value, dstFactorAlpha.value)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun blendFuncSeparate(srcFactor: Int, dstFactor: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) {
            //#if MC >= 1.21.5
            //$$ GlStateManager._blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
            //#elseif MC >= 1.17.1
            RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
            //#else
            //$$ GlStateManager.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun blendFuncSeparate(srcFactor: SrcFactor, dstFactor: DstFactor, srcFactorAlpha: SrcFactor, dstFactorAlpha: DstFactor) {
            blendFuncSeparate(srcFactor.value, dstFactor.value, srcFactorAlpha.value, dstFactorAlpha.value)
        }

    }

}
