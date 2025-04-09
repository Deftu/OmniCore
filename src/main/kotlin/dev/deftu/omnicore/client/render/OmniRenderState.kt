package dev.deftu.omnicore.client.render

import com.mojang.blaze3d.platform.GlStateManager
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.util.Identifier
import org.lwjgl.opengl.GL11
import java.util.function.Supplier

//#if MC >= 1.17.1
import net.minecraft.client.gl.ShaderProgram
//#endif

//#if MC >= 1.16.5
import com.mojang.blaze3d.systems.RenderSystem
import dev.deftu.omnicore.annotations.VersionedAbove
import org.jetbrains.annotations.ApiStatus

//#endif

//#if MC <= 1.16.5
//$$ import dev.deftu.omnicore.client.OmniClient
//$$ import org.lwjgl.opengl.GL13
//#endif

//#if MC <= 1.16.5 || MC >= 1.21.5
//$$ import org.lwjgl.opengl.GL14
//#endif

@GameSide(Side.CLIENT)
public object OmniRenderState {

    public val isCullingEnabled: Boolean
        get() = GL11.glIsEnabled(GL11.GL_CULL_FACE)

    public val isBlendEnabled: Boolean
        get() = GL11.glIsEnabled(GL11.GL_BLEND)

    public val isDepthEnabled: Boolean
        get() = GL11.glIsEnabled(GL11.GL_DEPTH_TEST)

    @Suppress("EnumValuesSoftDeprecate")
    public val depthState: DepthState
        get() = DepthState.values().first { it.value == GL11.glGetInteger(GL11.GL_DEPTH_FUNC) }

    //#if MC >= 1.17.1
    @JvmStatic
    @ApiStatus.Internal
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.17.1")
    public fun setShader(supplier: Supplier<ShaderProgram?>) {
        //#if MC < 1.21.5
        //#if MC >= 1.21.2
        //$$ @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        //$$ RenderSystem.setShader(supplier.get())
        //#else
        RenderSystem.setShader(supplier)
        //#endif
        //#endif
    }

    @JvmStatic
    @ApiStatus.Internal
    @GameSide(Side.CLIENT)
    @VersionedAbove("1.17.1")
    public fun removeShader() {
        setShader { null }
    }
    //#endif

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setTexture(index: Int, texture: Int) {
        //#if MC < 1.21.5
        //#if MC >= 1.17.1
        RenderSystem.setShaderTexture(index, texture)
        //#else
        //$$ OmniTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + index)
        //$$ OmniTextureManager.bindTexture(texture)
        //#endif
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setTexture(index: Int, texture: Identifier) {
        //#if MC < 1.21.5
        //#if MC >= 1.17.1
        RenderSystem.setShaderTexture(index, texture)
        //#else
        //$$ OmniTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + index)
        //$$ OmniClient.textureManager.bindTexture(texture)
        //#endif
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setColor4f(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        //#if MC >= 1.17.1
        RenderSystem.setShaderColor(red, green, blue, alpha)
        //#elseif MC >= 1.16.5
        //$$ RenderSystem.color4f(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.color(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setColor3f(
        red: Float,
        green: Float,
        blue: Float
    ) {
        //#if MC >= 1.17.1
        setColor4f(red, green, blue, 1f)
        //#else
        //#if MC >= 1.16.5
        //$$ RenderSystem.color3f(red, green, blue)
        //#else
        //$$ GlStateManager.color(red, green, blue)
        //#endif
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setColorMask(
        red: Boolean,
        green: Boolean,
        blue: Boolean,
        alpha: Boolean
    ) {
        //#if MC >= 1.21.5
        //$$ GlStateManager._colorMask(red, green, blue, alpha)
        //#elseif MC >= 1.16.5
        RenderSystem.colorMask(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.colorMask(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setClearColor(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        //#if MC >= 1.21.5
        //$$ GL11.glClearColor(red, green, blue, alpha)
        //#elseif MC >= 1.17.1
        RenderSystem.clearColor(red, green, blue, alpha)
        //#else
        //$$ GlStateManager.clearColor(red, green, blue, alpha)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setClearDepth(depth: Double) {
        //#if MC >= 1.21.5
        //$$ GL11.glClearDepth(depth)
        //#else
        GlStateManager._clearDepth(depth)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setClearStencil(stencil: Int) {
        GL11.glClearStencil(stencil)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun clear(mask: Int) {
        //#if MC >= 1.21.5
        //$$ GlStateManager._clear(mask)
        //#elseif MC >= 1.21.2
        //$$ RenderSystem.clear(mask)
        //#elseif MC >= 1.17.1
        RenderSystem.clear(mask, false)
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.clear(mask, false)
        //#else
        //$$ GlStateManager.clear(mask)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun clear(vararg mask: ClearMask) {
        clear(mask.fold(0) { acc, clearMask ->
            acc or clearMask.value
        })
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setViewport(
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) {
        //#if MC >= 1.17.1
        GlStateManager._viewport(x, y, width, height)
        //#else
        //$$ GlStateManager.viewport(x, y, width, height)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun enableTexture2D() {
        //#if MC >= 1.19.4
        // no-op
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.enableTexture()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun disableTexture2D() {
        //#if MC >= 1.19.4
        // no-op
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.disableTexture()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toggleTexture2D(enable: Boolean) {
        if (enable) {
            enableTexture2D()
        } else {
            disableTexture2D()
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun enableCull() {
        //#if MC >= 1.21.5
        //$$ GlStateManager._enableCull()
        //#elseif MC >= 1.17.1
        RenderSystem.enableCull()
        //#else
        //$$ GlStateManager.enableCull()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun disableCull() {
        //#if MC >= 1.21.5
        //$$ GlStateManager._disableCull()
        //#elseif MC >= 1.17.1
        RenderSystem.disableCull()
        //#else
        //$$ GlStateManager.disableCull()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toggleCull(enable: Boolean) {
        if (enable) {
            enableCull()
        } else disableCull()
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun enableAlpha() {
        //#if MC < 1.17.1
        //#if MC >= 1.16.5
        //$$ RenderSystem.enableAlphaTest();
        //#else
        //$$ GlStateManager.enableAlpha()
        //#endif
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun disableAlpha() {
        //#if MC < 1.17
        //#if MC>=1.16.5
        //$$ RenderSystem.disableAlphaTest();
        //#else
        //$$ GlStateManager.disableAlpha()
        //#endif
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toggleAlpha(enable: Boolean) {
        if (enable) {
            enableAlpha()
        } else {
            disableAlpha()
        }
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
    public fun toggleBlend(enable: Boolean) {
        if (enable) {
            enableBlend()
        } else {
            disableBlend()
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setBlendFunc(srcFactor: Int, dstFactor: Int) {
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
    public fun setBlendFunc(srcFactor: SrcFactor, dstFactorAlpha: DstFactor) {
        setBlendFunc(srcFactor.value, dstFactorAlpha.value)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setBlendFuncSeparate(srcFactor: Int, dstFactor: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) {
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
    public fun setBlendFuncSeparate(srcFactor: SrcFactor, dstFactor: DstFactor, srcFactorAlpha: SrcFactor, dstFactorAlpha: DstFactor) {
        setBlendFuncSeparate(srcFactor.value, dstFactor.value, srcFactorAlpha.value, dstFactorAlpha.value)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setDefaultBlendFunc() {
        setBlendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setBlendEquation(equation: Int) {
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
    public fun enableDepth() {
        //#if MC >= 1.21.5
        //$$ GlStateManager._enableDepthTest()
        //#elseif MC >= 1.17.1
        RenderSystem.enableDepthTest()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.enableDepthTest()
        //#else
        //$$ GlStateManager.enableDepth()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun disableDepth() {
        //#if MC >= 1.21.5
        //$$ GlStateManager._disableDepthTest()
        //#elseif MC >= 1.17.1
        RenderSystem.disableDepthTest()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager.disableDepthTest()
        //#else
        //$$ GlStateManager.disableDepth()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toggleDepth(enable: Boolean) {
        if (enable) {
            enableDepth()
        } else {
            disableDepth()
        }
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setDepthFunc(func: Int) {
        //#if MC >= 1.21.5
        //$$ GlStateManager._depthFunc(func)
        //#elseif MC >= 1.17.1
        RenderSystem.depthFunc(func)
        //#else
        //$$ GlStateManager.depthFunc(func)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setDepthFunc(state: DepthState) {
        setDepthFunc(state.value)
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun setDepthMask(flag: Boolean) {
        //#if MC >= 1.21.5
        //$$ GlStateManager._depthMask(flag)
        //#elseif MC >= 1.17.1
        RenderSystem.depthMask(flag)
        //#else
        //$$ GlStateManager.depthMask(flag)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun enableLighting() {
        //#if MC <= 1.16.5
        //$$ GlStateManager.enableLighting()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun disableLighting() {
        //#if MC <= 1.16.5
        //$$ GlStateManager.disableLighting()
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun toggleLighting(@Suppress("UNUSED_PARAMETER") enable: Boolean) {
        //#if MC <= 1.16.5
        //$$ if (enable) enableLighting() else disableLighting()
        //#endif
    }

    @GameSide(Side.CLIENT)
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

    @GameSide(Side.CLIENT)
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

    @GameSide(Side.CLIENT)
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

    @GameSide(Side.CLIENT)
    public enum class ClearMask(
        public val value: Int
    ) {
        COLOR(GL11.GL_COLOR_BUFFER_BIT),
        DEPTH(GL11.GL_DEPTH_BUFFER_BIT),
        STENCIL(GL11.GL_STENCIL_BUFFER_BIT)
    }

}
