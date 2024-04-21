@file:Suppress("UNUSED_PARAMETER")

package dev.deftu.omnicore

//#if MC >= 1.15
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC <= 1.16.5
//$$ import org.lwjgl.opengl.GL14
//#endif

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

public object MultiGlStateManager {
    @JvmStatic public fun getErrorCode(): Int =
        GL11.glGetError()

    @JvmStatic public fun getError(): GlError =
        GlError.values().firstOrNull { it.value == getErrorCode() } ?: GlError.NO_ERROR

    @JvmStatic public fun color4f(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        //#if MC > 1.17
        RenderSystem.setShaderColor(red, green, blue, alpha)
        //#elseif MC > 1.15.2
        //$$ RenderSystem.color4f(red, green, blue, alpha)
        //#elseif MC >= 1.15
        //$$ GlStateManager.color4f(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.color(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic public fun color3f(
        red: Float,
        green: Float,
        blue: Float
    ) {
        //#if MC >= 1.17
        color4f(red, green, blue, 1f)
        //#else
        //#if MC >= 1.15
        //$$ RenderSystem.color3f(red, green, blue)
        //#else
        //$$ GlStateManager.color(red, green, blue)
        //#endif
        //#endif
    }

    @JvmStatic public fun colorMask(
        red: Boolean,
        green: Boolean,
        blue: Boolean,
        alpha: Boolean
    ) {
        //#if MC >= 1.15
        RenderSystem.colorMask(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.colorMask(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic public fun clearColor(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        //#if MC >= 1.17
        RenderSystem.clearColor(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.clearColor(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic public fun clear(mask: Int) {
        //#if MC >= 1.17
        RenderSystem.clear(mask, false)
        //#elseif MC >= 1.16
        //$$ GlStateManager.clear(mask, false)
        //#else
        //$$ GlStateManager.clear(mask)
        //#endif
    }

    @JvmStatic public fun clear(vararg mask: ClearMask) {
        val maskInt = mask.fold(0) { acc, clearMask -> acc or clearMask.value }
        clear(maskInt)
    }

    @JvmStatic public fun viewport(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) {
        //#if MC >= 1.17
        GlStateManager._viewport(x, y, width, height)
        //#else
        //$$ GlStateManager.viewport(x, y, width, height)
        //#endif
    }

    @JvmStatic public fun enableTexture2D() {
        //#if MC>=1.19.4
        // no-op
        //#elseif MC >= 1.17
        //$$ RenderSystem.enableTexture()
        //#elseif MC >= 1.14
        //$$ GlStateManager.enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
    }

    @JvmStatic public fun disableTexture2D() {
        //#if MC >= 1.19.4
        // no-op
        //#elseif MC >= 1.17
        //$$ RenderSystem.disableTexture()
        //#elseif MC >= 1.14
        //$$ GlStateManager.disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
    }

    @JvmStatic public fun toggleTexture2D(enable: Boolean) {
        if (enable) enableTexture2D() else disableTexture2D()
    }

    @JvmStatic public fun enableBasicTexture2D() {
        //#if MC >= 1.19.4
        // no-op
        //#elseif MC >= 1.17
        //$$ GlStateManager._enableTexture()
        //#elseif MC >= 1.14
        //$$ GlStateManager.enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
    }

    @JvmStatic public fun disableBasicTexture2D() {
        //#if MC >= 1.19.4
        // no-op
        //#elseif MC >= 1.17
        //$$ GlStateManager._disableTexture()
        //#elseif MC >= 1.14
        //$$ GlStateManager.disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
    }

    @JvmStatic public fun toggleBasicTexture2D(enable: Boolean) {
        if (enable) enableBasicTexture2D() else disableBasicTexture2D()
    }

    @JvmStatic public fun enableCull() {
        //#if MC >= 1.17
        RenderSystem.enableCull()
        //#else
        //$$ GlStateManager.enableCull()
        //#endif
    }

    @JvmStatic public fun disableCull() {
        //#if MC >= 1.17
        RenderSystem.disableCull()
        //#else
        //$$ GlStateManager.disableCull()
        //#endif
    }

    @JvmStatic public fun toggleCull(enable: Boolean) {
        if (enable) enableCull() else disableCull()
    }

    @JvmStatic public fun enableBlend() {
        //#if MC >= 1.17
        RenderSystem.enableBlend()
        //#else
        //$$ GlStateManager.enableBlend()
        //#endif
    }

    @JvmStatic public fun disableBlend() {
        //#if MC >= 1.17
        RenderSystem.disableBlend()
        //#else
        //$$ GlStateManager.disableBlend()
        //#endif
    }

    @JvmStatic public fun toggleBlend(enable: Boolean) {
        if (enable) enableBlend() else disableBlend()
    }

    @JvmStatic public fun blendFunc(srcFactor: Int, dstFactor: Int) {
        //#if MC >= 1.17
        RenderSystem.blendFunc(srcFactor, dstFactor)
        //#else
        //$$ GlStateManager.blendFunc(srcFactor, dstFactor)
        //#endif
    }

    @JvmStatic public fun blendFunc(srcFactor: SrcFactor, dstFactorAlpha: DstFactor) {
        blendFunc(srcFactor.value, dstFactorAlpha.value)
    }

    @JvmStatic public fun blendFuncSeparate(srcFactor: Int, dstFactor: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) {
        //#if MC >= 1.17
        RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#elseif MC >= 1.14
        //$$ GlStateManager.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#else
        //$$ GlStateManager.tryBlendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha)
        //#endif
    }

    @JvmStatic public fun blendFuncSeparate(srcFactor: SrcFactor, dstFactor: DstFactor, srcFactorAlpha: SrcFactor, dstFactorAlpha: DstFactor) {
        blendFuncSeparate(srcFactor.value, dstFactor.value, srcFactorAlpha.value, dstFactorAlpha.value)
    }

    @JvmStatic public fun defaultBlendFunc() {
        //#if MC >= 1.17
        RenderSystem.defaultBlendFunc()
        //#else
        //$$ blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO)
        //#endif
    }

    @JvmStatic public fun blendEquation(equation: Int) {
        //#if MC >= 1.17
        RenderSystem.blendEquation(equation)
        //#else
        //$$ GL14.glBlendEquation(equation)
        //#endif
    }

    @JvmStatic public fun enableDepth() {
        //#if MC >= 1.17
        RenderSystem.enableDepthTest()
        //#elseif MC >= 1.14
        //$$ GlStateManager.enableDepthTest()
        //#else
        //$$ GlStateManager.enableDepth()
        //#endif
    }

    @JvmStatic public fun disableDepth() {
        //#if MC >= 1.17
        RenderSystem.disableDepthTest()
        //#elseif MC >= 1.14
        //$$ GlStateManager.disableDepthTest()
        //#else
        //$$ GlStateManager.disableDepth()
        //#endif
    }

    @JvmStatic public fun toggleDepth(enable: Boolean) {
        if (enable) enableDepth() else disableDepth()
    }

    @JvmStatic public fun depthFunc(func: Int) {
        //#if MC >= 1.17
        RenderSystem.depthFunc(func)
        //#else
        //$$ GlStateManager.depthFunc(func)
        //#endif
    }

    @JvmStatic public fun depthFunc(state: DepthState) {
        depthFunc(state.value)
    }

    @JvmStatic public fun depthMask(flag: Boolean) {
        //#if MC >= 1.17
        RenderSystem.depthMask(flag)
        //#else
        //$$ GlStateManager.depthMask(flag)
        //#endif
    }

    @JvmStatic public fun enableLighting() {
        //#if MC < 1.17
        //$$ GlStateManager.enableLighting()
        //#endif
    }

    @JvmStatic public fun disableLighting() {
        //#if MC < 1.17
        //$$ GlStateManager.disableLighting()
        //#endif
    }

    @JvmStatic public fun toggleLighting(enable: Boolean) {
        //#if MC < 1.17
        //$$ if (enable) enableLighting() else disableLighting()
        //#endif
    }

    public enum class GlError(
        public val value: Int
    ) {
        NO_ERROR(GL11.GL_NO_ERROR),
        INVALID_ENUM(GL11.GL_INVALID_ENUM),
        INVALID_VALUE(GL11.GL_INVALID_VALUE),
        INVALID_OPERATION(GL11.GL_INVALID_OPERATION),
        STACK_OVERFLOW(GL11.GL_STACK_OVERFLOW),
        STACK_UNDERFLOW(GL11.GL_STACK_UNDERFLOW),
        OUT_OF_MEMORY(GL11.GL_OUT_OF_MEMORY),
        INVALID_FRAMEBUFFER_OPERATION(GL30.GL_INVALID_FRAMEBUFFER_OPERATION)
    }

    public enum class SrcFactor(
        public val value: Int
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

    public enum class DstFactor(
        public val value: Int
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

    public enum class DepthState(
        public val value: Int
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

    public enum class ClearMask(
        public val value: Int
    ) {
        COLOR(GL11.GL_COLOR_BUFFER_BIT),
        DEPTH(GL11.GL_DEPTH_BUFFER_BIT),
        STENCIL(GL11.GL_STENCIL_BUFFER_BIT)
    }
}
