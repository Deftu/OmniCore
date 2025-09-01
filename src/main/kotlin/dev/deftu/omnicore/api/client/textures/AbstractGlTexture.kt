package dev.deftu.omnicore.api.client.textures

import dev.deftu.omnicore.api.client.framebuffer.FramebufferTarget
import dev.deftu.omnicore.client.render.state.OmniManagedColorMask
import dev.deftu.omnicore.client.render.state.OmniManagedScissorState
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferHelper
import dev.deftu.omnicore.api.client.render.ClearMask
import dev.deftu.omnicore.internal.client.render.GlInternals
import dev.deftu.omnicore.internal.client.textures.TextureInternals
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.awt.Color

public abstract class AbstractGlTexture(override val format: OmniTextureFormat) : OmniTextureHandle {
    private companion object {
        private val internalDrawFramebuffer by lazy(FramebufferInternals::create)
        private val internalReadFramebuffer by lazy(FramebufferInternals::create)
        private val internalFloatBuffer by lazy { BufferUtils.createFloatBuffer(4) }

        private fun ensureFloatBufferCapacity(capacity: Int) {
            if (internalFloatBuffer.capacity() < capacity) {
                internalFloatBuffer.clear()
                internalFloatBuffer.put(FloatArray(capacity))
                internalFloatBuffer.flip()
            }
        }
    }

    override fun bind(unit: Int) {
        val prevUnit = TextureInternals.activeUnit
        val prevTexture = TextureInternals.active
        if (prevUnit != unit) {
            TextureInternals.activeUnit = unit
        }

        TextureInternals.active = id

        if (prevUnit != unit) {
            TextureInternals.activeUnit = prevUnit
        }
    }

    override fun unbind(unit: Int) {
        val prevUnit = TextureInternals.activeUnit
        val prevTexture = TextureInternals.active
        if (prevUnit != unit) {
            TextureInternals.activeUnit = unit
        }

        TextureInternals.active = 0

        if (prevUnit != unit) {
            TextureInternals.activeUnit = prevUnit
        }
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        FramebufferHelper.attachColor(internalDrawFramebuffer, id, FramebufferTarget.WRITE)
        OmniManagedScissorState.without {
            OmniManagedColorMask(
                red = true,
                green = true,
                blue = true,
                alpha = true
            ).activate()
            FramebufferHelper.with(FramebufferTarget.WRITE, internalDrawFramebuffer) {
                GlInternals.clearColor(red, green, blue, alpha)
                GlInternals.clear(ClearMask.COLOR)
            }
        }
    }

    override fun clearDepth(depth: Float) {
        FramebufferHelper.attachDepth(internalDrawFramebuffer, id, target = FramebufferTarget.WRITE)
        OmniManagedScissorState.without {
            val prevDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK)
            FramebufferHelper.with(FramebufferTarget.WRITE, internalDrawFramebuffer) {
                GlInternals.depthMask(true)
                GlInternals.clearDepth(depth.toDouble())
                GlInternals.clear(ClearMask.DEPTH)
                GlInternals.depthMask(prevDepthMask)
            }
        }
    }

    override fun readColor(x: Int, y: Int, width: Int, height: Int): Array<Color> {
        require(format.isColor) { "readColor on non-color texture ($format)" }

        FramebufferHelper.attachColor(internalReadFramebuffer, id, FramebufferTarget.READ)
        ensureFloatBufferCapacity(width * height * 4)
        FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
            GL30.glReadPixels(
                x, y, width, height,
                GL11.GL_RGBA, GL11.GL_FLOAT, internalFloatBuffer
            )
        }

        val output = Array(width * height) { Color(0, 0, 0, 0) }
        for (i in 0 until width * height) {
            val pos = i * 4
            val r = internalFloatBuffer.get(pos)
            val g = internalFloatBuffer.get(pos + 1)
            val b = internalFloatBuffer.get(pos + 2)
            val a = internalFloatBuffer.get(pos + 3)
            output[i] = Color(r, g, b, a)
        }

        internalFloatBuffer.clear()
        return output
    }

    override fun readColor(x: Int, y: Int): Color {
        require(format.isColor) { "readColor on non-color texture ($format)" }

        FramebufferHelper.attachColor(internalReadFramebuffer, id, FramebufferTarget.READ)
        ensureFloatBufferCapacity(4)
        FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
            GL30.glReadPixels(
                x, y, 1, 1,
                GL11.GL_RGBA, GL11.GL_FLOAT, internalFloatBuffer
            )
        }

        val r = internalFloatBuffer.get(0)
        val g = internalFloatBuffer.get(1)
        val b = internalFloatBuffer.get(2)
        val a = internalFloatBuffer.get(3)

        internalFloatBuffer.clear()
        return Color(r, g, b, a)
    }

    override fun readDepth(x: Int, y: Int, width: Int, height: Int): Array<Float> {
        FramebufferHelper.attachDepth(internalReadFramebuffer, id, target = FramebufferTarget.READ)
        ensureFloatBufferCapacity(width * height)
        FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
            GL30.glReadPixels(
                x, y, width, height,
                GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, internalFloatBuffer
            )
        }

        val output = Array(width * height) { 0f }
        for (i in 0 until width * height) {
            output[i] = internalFloatBuffer.get(i)
        }

        internalFloatBuffer.clear()
        return output
    }

    override fun readDepth(x: Int, y: Int): Float {
        FramebufferHelper.attachDepth(internalReadFramebuffer, id, target = FramebufferTarget.READ)
        ensureFloatBufferCapacity(1)
        FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
            GL30.glReadPixels(
                x, y, 1, 1,
                GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, internalFloatBuffer
            )
        }

        val depth = internalFloatBuffer.get(0)
        internalFloatBuffer.clear()
        return depth
    }

    override fun copyFrom(operations: Iterable<OmniTextureHandle.CopyOp>) {
        val attachment = if (format.isColor) GL30.GL_COLOR_ATTACHMENT0 else GL30.GL_DEPTH_ATTACHMENT
        val bufferBit = if (format.isColor) GL11.GL_COLOR_BUFFER_BIT else GL11.GL_DEPTH_BUFFER_BIT

        FramebufferHelper.with(FramebufferTarget.WRITE, internalDrawFramebuffer) {
            FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
                OmniManagedScissorState.without {
                    FramebufferHelper.attach(FramebufferTarget.WRITE, attachment, id)
                    for ((src, srcX, srcY, dstX, dstY, width, height) in operations) {
                        FramebufferHelper.attach(FramebufferTarget.READ, attachment, src.id)
                        GL30.glBlitFramebuffer(
                            srcX, srcY,
                            srcX + width, srcY + height,
                            dstX, dstY,
                            dstX + width, dstY + height,
                            bufferBit, GL11.GL_NEAREST
                        )
                    }

                    FramebufferHelper.detach(FramebufferTarget.WRITE, attachment)
                    FramebufferHelper.detach(FramebufferTarget.READ, attachment)
                }
            }
        }
    }
}
