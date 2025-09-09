package dev.deftu.omnicore.internal.client.render.state

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.render.state.BlendEquation
import dev.deftu.omnicore.api.client.render.state.BlendFunction
import dev.deftu.omnicore.api.client.render.state.DstFactor
import dev.deftu.omnicore.api.client.render.state.SrcFactor
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

//#if MC <= 1.16.5 || MC >= 1.21.5
import org.lwjgl.opengl.GL14
//#endif

//#if MC >= 1.16.5 && MC < 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#endif

@ApiStatus.Internal
public object BlendStateInternals {
    @JvmStatic
    public val isEnabled: Boolean
        get() = GL11.glIsEnabled(GL11.GL_BLEND)

    @JvmStatic
    public val equation: BlendEquation
        get() {
            //#if MC >= 1.16.5
            val value = GL11.glGetInteger(GL20.GL_BLEND_EQUATION_RGB)
            //#else
            //$$ val value = GL11.glGetInteger(GL14.GL_BLEND_EQUATION)
            //#endif

            return BlendEquation.findOrThrow(value)
        }

    @JvmStatic
    public val srcColor: SrcFactor
        get() {
            return SrcFactor.findOrThrow(GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB))
        }

    @JvmStatic
    public val dstColor: DstFactor
        get() {
            return DstFactor.findOrThrow(GL11.glGetInteger(GL14.GL_BLEND_DST_RGB))
        }

    @JvmStatic
    public val srcAlpha: SrcFactor
        get() {
            return SrcFactor.findOrThrow(GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA))
        }

    @JvmStatic
    public val dstAlpha: DstFactor
        get() {
            return DstFactor.findOrThrow(GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA))
        }

    @JvmStatic
    public val function: BlendFunction
        get() {
            return BlendFunction(
                srcColor = srcColor,
                dstColor = dstColor,
                srcAlpha = srcAlpha,
                dstAlpha = dstAlpha
            )
        }

    @JvmStatic
    public fun enable() {
        GlStateManager._enableBlend()
    }

    @JvmStatic
    public fun disable() {
        GlStateManager._disableBlend()
    }

    @JvmStatic
    public fun equation(equation: Int) {
        //#if MC >= 1.21.5
        GL14.glBlendEquation(equation)
        //#elseif MC >= 1.16.5
        //$$ RenderSystem.blendEquation(equation)
        //#else
        //$$ GL14.glBlendEquation(equation)
        //#endif
    }

    @JvmStatic
    public fun func(srcFactor: Int, dstFactor: Int) {
        //#if MC >= 1.21.5
        GL11.glBlendFunc(srcFactor, dstFactor)
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.blendFunc(srcFactor, dstFactor)
        //#else
        //$$ GlStateManager.blendFunc(srcFactor, dstFactor)
        //#endif
    }

    @JvmStatic
    public fun func(srcFactor: SrcFactor, dstFactorAlpha: DstFactor) {
        func(srcFactor.const, dstFactorAlpha.const)
    }

    @JvmStatic
    public fun funcSeparate(srcFactor: Int, dstFactor: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) {
        //#if MC >= 1.21.5
        GlStateManager._blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#else
        //$$ GlStateManager.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#endif
    }

    @JvmStatic
    public fun funcSeparate(srcFactor: SrcFactor, dstFactor: DstFactor, srcFactorAlpha: SrcFactor, dstFactorAlpha: DstFactor) {
        funcSeparate(srcFactor.const, dstFactor.const, srcFactorAlpha.const, dstFactorAlpha.const)
    }
}
