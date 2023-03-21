package xyz.deftu.multi

//#if MC>=11500
import com.mojang.blaze3d.systems.RenderSystem
//#endif

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14
import com.mojang.blaze3d.platform.GlStateManager

object MultiGlStateManager {
    @JvmStatic fun color4f(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        //#if MC>11700
        RenderSystem.setShaderColor(red, green, blue, alpha)
        //#elseif MC>11502
        //$$ RenderSystem.color4f(red, green, blue, alpha)
        //#elseif MC>=11500
        //$$ GlStateManager.color4f(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.color(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic fun color3f(
        red: Float,
        green: Float,
        blue: Float
    ) {
        //#if MC>=11700
        color4f(red, green, blue, 1f)
        //#else
        //#if MC>=11500
        //$$ RenderSystem.color3f(red, green, blue)
        //#else
        //$$ GlStateManager.color(red, green, blue)
        //#endif
        //#endif
    }

    @JvmStatic fun enableTexture2D() {
        //#if MC>=11904
        // no-op
        //#elseif MC>=11700
        //$$ RenderSystem.enableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
    }

    @JvmStatic fun disableTexture2D() {
        //#if MC>=11904
        // no-op
        //#elseif MC>=11700
        //$$ RenderSystem.disableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
    }

    @JvmStatic fun toggleTexture2D(enable: Boolean) {
        if (enable) enableTexture2D() else disableTexture2D()
    }

    @JvmStatic fun enableBasicTexture2D() {
        //#if MC>=11904
        // no-op
        //#elseif MC>=11700
        //$$ GlStateManager._enableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
    }

    @JvmStatic fun disableBasicTexture2D() {
        //#if MC>=11904
        // no-op
        //#elseif MC>=11700
        //$$ GlStateManager._disableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
    }

    @JvmStatic fun toggleBasicTexture2D(enable: Boolean) {
        if (enable) enableBasicTexture2D() else disableBasicTexture2D()
    }

    @JvmStatic fun enableCull() {
        //#if MC>=11700
        RenderSystem.enableCull()
        //#else
        //$$ GlStateManager.enableCull()
        //#endif
    }

    @JvmStatic fun disableCull() {
        //#if MC>=11700
        RenderSystem.disableCull()
        //#else
        //$$ GlStateManager.disableCull()
        //#endif
    }

    @JvmStatic fun toggleCull(enable: Boolean) {
        if (enable) enableCull() else disableCull()
    }

    @JvmStatic fun enableBlend() {
        //#if MC>=11700
        RenderSystem.enableBlend()
        //#else
        //$$ GlStateManager.enableBlend()
        //#endif
    }

    @JvmStatic fun disableBlend() {
        //#if MC>=11700
        RenderSystem.disableBlend()
        //#else
        //$$ GlStateManager.disableBlend()
        //#endif
    }

    @JvmStatic fun toggleBlend(enable: Boolean) {
        if (enable) enableBlend() else disableBlend()
    }

    @JvmStatic fun blendFunc(srcFactor: Int, dstFactor: Int) {
        //#if MC>=11700
        RenderSystem.blendFunc(srcFactor, dstFactor)
        //#else
        //$$ GlStateManager.blendFunc(srcFactor, dstFactor)
        //#endif
    }

    @JvmStatic fun blendFunc(srcFactor: SrcFactor, dstFactorAlpha: DstFactor) {
        blendFunc(srcFactor.value, dstFactorAlpha.value)
    }

    @JvmStatic fun blendFuncSeparate(srcFactor: Int, dstFactor: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) {
        //#if MC>=11700
        RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#elseif MC>=11400
        //$$ GlStateManager.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#else
        //$$ GlStateManager.tryBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#endif
    }

    @JvmStatic fun blendFuncSeparate(srcFactor: SrcFactor, dstFactor: DstFactor, srcFactorAlpha: SrcFactor, dstFactorAlpha: DstFactor) {
        blendFuncSeparate(srcFactor.value, dstFactor.value, srcFactorAlpha.value, dstFactorAlpha.value)
    }

    @JvmStatic fun defaultBlendFunc() {
        //#if MC>=11700
        RenderSystem.defaultBlendFunc()
        //#else
        //$$ blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO)
        //#endif
    }

    @JvmStatic fun blendEquation(equation: Int) {
        //#if MC>=11700
        RenderSystem.blendEquation(equation)
        //#else
        //$$ GL14.glBlendEquation(equation)
        //#endif
    }

    @JvmStatic fun enableDepth() {
        //#if MC>=11700
        RenderSystem.enableDepthTest()
        //#elseif MC>=11400
        //$$ GlStateManager.enableDepthTest()
        //#else
        //$$ GlStateManager.enableDepth()
        //#endif
    }

    @JvmStatic fun disableDepth() {
        //#if MC>=11700
        RenderSystem.disableDepthTest()
        //#elseif MC>=11400
        //$$ GlStateManager.disableDepthTest()
        //#else
        //$$ GlStateManager.disableDepth()
        //#endif
    }

    @JvmStatic fun toggleDepth(enable: Boolean) {
        if (enable) enableDepth() else disableDepth()
    }

    @JvmStatic fun depthFunc(func: Int) {
        //#if MC>=11700
        RenderSystem.depthFunc(func)
        //#else
        //$$ GlStateManager.depthFunc(func)
        //#endif
    }

    @JvmStatic fun depthFunc(state: DepthState) {
        depthFunc(state.value)
    }

    @JvmStatic fun enableLighting() {
        //#if MC<11700
        //$$ GlStateManager.enableLighting()
        //#endif
    }

    @JvmStatic fun disableLighting() {
        //#if MC<11700
        //$$ GlStateManager.disableLighting()
        //#endif
    }

    @JvmStatic fun toggleLighting(enable: Boolean) {
        //#if MC<11700
        //$$ if (enable) enableLighting() else disableLighting()
        //#endif
    }

    enum class SrcFactor(
        val value: Int
    ) {
        CONSTANT_ALPHA(GL11.GL_SRC_ALPHA),
        CONSTANT_COLOR(GL11.GL_SRC_COLOR),
        DST_ALPHA(GL11.GL_DST_ALPHA),
        DST_COLOR(GL11.GL_DST_COLOR),
        ONE(GL11.GL_ONE),
        ONE_MINUS_CONSTANT_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
        ONE_MINUS_CONSTANT_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR),
        ONE_MINUS_DST_ALPHA(GL11.GL_ONE_MINUS_DST_ALPHA),
        ONE_MINUS_DST_COLOR(GL11.GL_ONE_MINUS_DST_COLOR),
        ONE_MINUS_SRC_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
        ONE_MINUS_SRC_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR),
        SRC_ALPHA(GL11.GL_SRC_ALPHA),
        SRC_ALPHA_SATURATE(GL11.GL_SRC_ALPHA_SATURATE),
        SRC_COLOR(GL11.GL_SRC_COLOR),
        ZERO(GL11.GL_ZERO)
    }

    enum class DstFactor(
        val value: Int
    ) {
        CONSTANT_ALPHA(GL11.GL_SRC_ALPHA),
        CONSTANT_COLOR(GL11.GL_SRC_COLOR),
        DST_ALPHA(GL11.GL_DST_ALPHA),
        DST_COLOR(GL11.GL_DST_COLOR),
        ONE(GL11.GL_ONE),
        ONE_MINUS_CONSTANT_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
        ONE_MINUS_CONSTANT_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR),
        ONE_MINUS_DST_ALPHA(GL11.GL_ONE_MINUS_DST_ALPHA),
        ONE_MINUS_DST_COLOR(GL11.GL_ONE_MINUS_DST_COLOR),
        ONE_MINUS_SRC_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA),
        ONE_MINUS_SRC_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR),
        SRC_ALPHA(GL11.GL_SRC_ALPHA),
        SRC_COLOR(GL11.GL_SRC_COLOR),
        ZERO(GL11.GL_ZERO)
    }

    enum class DepthState(
        val value: Int
    ) {
        ALWAYS(GL11.GL_ALWAYS),
        EQUAL(GL11.GL_EQUAL),
        GEQUAL(GL11.GL_GEQUAL),
        GREATER(GL11.GL_GREATER),
        LEQUAL(GL11.GL_LEQUAL),
        LESS(GL11.GL_LESS),
        NEVER(GL11.GL_NEVER),
        NOTEQUAL(GL11.GL_NOTEQUAL)
    }
}
