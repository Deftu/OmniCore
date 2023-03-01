package xyz.deftu.multi

//#if MC>=11500
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC>=11500
import com.mojang.blaze3d.platform.GlStateManager
//#else
//$$ import net.minecraft.client.renderer.GlStateManager
//#endif

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13

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
        //#if MC>=11700
        RenderSystem.enableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
    }

    @JvmStatic fun disableTexture2D() {
        //#if MC>=11700
        RenderSystem.disableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
    }

    @JvmStatic fun enableBasicTexture2D() {
        //#if MC>=11700
        GlStateManager._enableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
    }

    @JvmStatic fun disableBasicTexture2D() {
        //#if MC>=11700
        GlStateManager._disableTexture()
        //#elseif MC>=11400
        //$$ GlStateManager.disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
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

    @JvmStatic fun blendFunc(srcFactor: Int, dstFactor: Int) {
        //#if MC>=11700
        RenderSystem.blendFunc(srcFactor, dstFactor)
        //#else
        //$$ GlStateManager.blendFunc(srcFactor, dstFactor)
        //#endif
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

    @JvmStatic fun defaultBlendFunc() {
        //#if MC>=11700
        RenderSystem.defaultBlendFunc()
        //#else
        //$$ blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO)
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

    @JvmStatic fun depthFunc(func: Int) {
        //#if MC>=11700
        RenderSystem.depthFunc(func)
        //#else
        //$$ GlStateManager.depthFunc(func)
        //#endif
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

    @JvmStatic fun getActiveTexture() =
        GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)

    @JvmStatic fun setActiveTexture(id: Int) {
        //#if MC>=11700
        GlStateManager._activeTexture(id)
        //#elseif MC>=11400
        //$$ GlStateManager.activeTexture(id)
        //#else
        //$$ GlStateManager.setActiveTexture(id)
        //#endif
    }

    @JvmStatic fun bindTexture(id: Int) {
        //#if MC>=11700
        GlStateManager._bindTexture(id)
        //#else
        //$$ GlStateManager.bindTexture(id)
        //#endif
    }

    @JvmStatic fun deleteTexture(id: Int) {
        //#if MC>=11700
        GlStateManager._deleteTexture(id)
        //#else
        //$$ GlStateManager.deleteTexture(id)
        //#endif
    }

    @JvmStatic fun configureTexture(id: Int, block: Runnable) {
        val prevActiveTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
        bindTexture(id)
        block.run()
        bindTexture(prevActiveTexture)
    }

    @JvmStatic fun configureTextureUnit(index: Int, block: Runnable) {
        val prevActiveTexture = getActiveTexture()
        setActiveTexture(GL13.GL_TEXTURE0 + index)
        block.run()
        setActiveTexture(prevActiveTexture)
    }
}
