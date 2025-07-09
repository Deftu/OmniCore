package dev.deftu.omnicore.client.render.texture

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.client.render.state.OmniManagedColorMask
import dev.deftu.omnicore.client.render.state.OmniManagedScissorState
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.awt.Color

public abstract class GlTexture(override val format: GpuTexture.TextureFormat) : GpuTexture {

    private companion object {

        private val internalFramebuffer1 by lazy(GL30::glGenFramebuffers)
        private val internalFramebuffer2 by lazy(GL30::glGenFramebuffers)
        private val internalFloatBuffer by lazy { BufferUtils.createFloatBuffer(4) }

    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        val prevFramebuffer = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, internalFramebuffer1)
        GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, id, 0)

        OmniManagedColorMask(red = true, green = true, blue = true, alpha = true).activate()
        GL11.glClearColor(red / 255f, green / 255f, blue / 255f, alpha / 255f)
        clear(GL11.GL_COLOR_BUFFER_BIT)

        GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, prevFramebuffer)
    }

    override fun clearDepth(depth: Float) {
        val prevFramebuffer = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, internalFramebuffer1)
        GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, id, 0)

        GlStateManager._depthMask(true)
        GL11.glClearDepth(depth.toDouble())
        clear(GL11.GL_DEPTH_BUFFER_BIT)

        GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, prevFramebuffer)
    }

    override fun readColor(x: Int, y: Int, width: Int, height: Int): Array<Color> {
        val prevFramebuffer = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, internalFramebuffer1)
        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, id, 0)

        val color = readColorInternal(x, y, width, height)

        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, prevFramebuffer)

        return color
    }

    override fun readColor(x: Int, y: Int): Color {
        val prevFramebuffer = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, internalFramebuffer1)
        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, id, 0)

        val color = readColorInternal(x, y)

        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, prevFramebuffer)

        return color
    }

    override fun readDepth(x: Int, y: Int, width: Int, height: Int): Array<Float> {
        val prevFramebuffer = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, internalFramebuffer1)
        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, id, 0)

        val depth = readDepthInternal(x, y, width, height)

        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, prevFramebuffer)

        return depth
    }

    override fun readDepth(x: Int, y: Int): Float {
        val prevFramebuffer = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, internalFramebuffer1)
        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, id, 0)

        val depth = readDepthInternal(x, y)

        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, prevFramebuffer)

        return depth
    }

    override fun copyFrom(operations: Iterable<GpuTexture.Copy>) {
        val prevScissorState = OmniManagedScissorState.active()
        OmniManagedScissorState.disable()

        val attachment = if (format.isColor) GL30.GL_COLOR_ATTACHMENT0 else GL30.GL_DEPTH_ATTACHMENT
        val bufferBit = if (format.isColor) GL11.GL_COLOR_BUFFER_BIT else GL11.GL_DEPTH_BUFFER_BIT
        val prevDrawFramebuffer = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING)
        val prevReadFramebuffer = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, internalFramebuffer1)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, internalFramebuffer2)
        GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, id, 0)

        for ((src, srcX, srcY, dstX, dstY, width, height) in operations) {
            GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, src.id, 0)
            GL30.glBlitFramebuffer(
                srcX, srcY,
                srcX + width, srcY + height,
                dstX, dstY,
                dstX + width, dstY + height,
                bufferBit, GL11.GL_NEAREST
            )
        }

        GL30.glFramebufferTexture2D(GL30.GL_DRAW_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glFramebufferTexture2D(GL30.GL_READ_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, 0, 0)
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, prevDrawFramebuffer)
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, prevReadFramebuffer)
        prevScissorState.activate()
    }

    private fun clear(bits: Int) {
        val prevScissorState = OmniManagedScissorState.active()
        OmniManagedScissorState.disable()

        GL11.glClear(bits)

        prevScissorState.activate()
    }

    private fun readColorInternal(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ): Array<Color> {
        GL11.glReadPixels(x, y, width, height, GL11.GL_RGBA, GL11.GL_FLOAT, internalFloatBuffer)
        val result = Array(width * height) { Color(0, 0, 0, 0) }
        for (i in 0 until width * height) {
            val r = internalFloatBuffer.get(i * 4)
            val g = internalFloatBuffer.get(i * 4 + 1)
            val b = internalFloatBuffer.get(i * 4 + 2)
            val a = internalFloatBuffer.get(i * 4 + 3)
            result[i] = Color(r, g, b, a)
        }

        return result
    }

    private fun readColorInternal(x: Int, y: Int): Color {
        GL11.glReadPixels(x, y, 1, 1, GL11.GL_RGBA, GL11.GL_FLOAT, internalFloatBuffer)
        val r = internalFloatBuffer.get(0)
        val g = internalFloatBuffer.get(1)
        val b = internalFloatBuffer.get(2)
        val a = internalFloatBuffer.get(3)
        return Color(r, g, b, a)
    }

    private fun readDepthInternal(x: Int, y: Int): Float {
        GL11.glReadPixels(x, y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, internalFloatBuffer)
        return internalFloatBuffer.get(0)
    }

    private fun readDepthInternal(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ): Array<Float> {
        GL11.glReadPixels(x, y, width, height, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, internalFloatBuffer)
        val result = Array(width * height) { 0f }
        for (i in 0 until width * height) {
            result[i] = internalFloatBuffer.get(i)
        }

        return result
    }

}
