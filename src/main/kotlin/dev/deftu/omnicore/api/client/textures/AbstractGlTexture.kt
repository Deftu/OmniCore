package dev.deftu.omnicore.api.client.textures

import dev.deftu.omnicore.api.client.framebuffer.FramebufferTarget
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferHelper
import dev.deftu.omnicore.api.client.render.ClearMask
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.state.OmniColorMask
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.omnicore.internal.client.render.GlInternals
import dev.deftu.omnicore.internal.client.textures.TextureInternals
import net.minecraft.util.Identifier
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer
import java.util.UUID
import kotlin.math.max

public abstract class AbstractGlTexture(override val format: OmniTextureFormat) : OmniTextureHandle {
    private companion object {
        private val internalDrawFramebuffer by lazy(FramebufferInternals::create)
        private val internalReadFramebuffer by lazy(FramebufferInternals::create)

        private var internalFloatBuffer: FloatBuffer = BufferUtils.createFloatBuffer(4)

        private fun ensureFloatBufferCapacity(required: Int) {
            if (internalFloatBuffer.capacity() < required) {
                val newCap = nextPow2(max(4, required))
                internalFloatBuffer = BufferUtils.createFloatBuffer(newCap)
            }

            internalFloatBuffer.clear()
        }

        private fun nextPow2(x: Int): Int {
            var v = x - 1
            v = v or (v ushr 1)
            v = v or (v ushr 2)
            v = v or (v ushr 4)
            v = v or (v ushr 8)
            v = v or (v ushr 16)
            return v + 1
        }

        private fun <T> flipRowsTopToBottom(src: Array<T>, width: Int, height: Int): Array<T> {
            val out = java.lang.reflect.Array.newInstance(src.javaClass.componentType, src.size) as Array<T>
            var dst = 0
            for (row in 0 until height) {
                val srcRowStart = (height - 1 - row) * width
                System.arraycopy(src, srcRowStart, out, dst, width)
                dst += width
            }

            return out
        }

    }

    override val location: Identifier = identifierOrThrow("omnicore", "texture_${UUID.randomUUID()}")

    override fun bind(unit: OmniTextureUnit): () -> Unit {
        val prevUnit = TextureInternals.activeUnit
        if (prevUnit != unit) {
            TextureInternals.activeUnit = unit
        }

        TextureInternals.active = id

        return {
            if (TextureInternals.activeUnit != unit) {
                TextureInternals.activeUnit = unit
            }

            TextureInternals.unbind()

            if (TextureInternals.activeUnit != prevUnit) {
                TextureInternals.activeUnit = prevUnit
            }
        }
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        FramebufferHelper.attachColor(internalDrawFramebuffer, id, FramebufferTarget.WRITE)
        OmniColorMask(
            red = true,
            green = true,
            blue = true,
            alpha = true
        ).submit(false)
        FramebufferHelper.with(FramebufferTarget.WRITE, internalDrawFramebuffer) {
            GlInternals.clearColor(red, green, blue, alpha)
            GlInternals.clear(ClearMask.COLOR)
        }
    }

    override fun clearDepth(depth: Float) {
        FramebufferHelper.attachDepth(internalDrawFramebuffer, id, target = FramebufferTarget.WRITE)
        val prevDepthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK)
        FramebufferHelper.with(FramebufferTarget.WRITE, internalDrawFramebuffer) {
            GlInternals.depthMask(true)
            GlInternals.clearDepth(depth.toDouble())
            GlInternals.clear(ClearMask.DEPTH)
            GlInternals.depthMask(prevDepthMask)
        }
    }

    override fun readColor(x: Int, y: Int, width: Int, height: Int): Array<OmniColor> {
        require(format.isColor) { "readColor on non-color texture ($format)" }

        FramebufferHelper.attachColor(internalReadFramebuffer, id, FramebufferTarget.READ)
        ensureFloatBufferCapacity(width * height * 4)
        FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
            GlInternals.withPackState(width, 4, 4) {
                GL11.glReadPixels(
                    x, y, width, height,
                    GL11.GL_RGBA, GL11.GL_FLOAT, internalFloatBuffer
                )
            }
        }

        val output = Array(width * height) { OmniColor(0, 0, 0, 0) }
        for (i in 0 until width * height) {
            val pos = i * 4
            val r = internalFloatBuffer.get(pos)
            val g = internalFloatBuffer.get(pos + 1)
            val b = internalFloatBuffer.get(pos + 2)
            val a = internalFloatBuffer.get(pos + 3)
            output[i] = OmniColor(r, g, b, a)
        }

        internalFloatBuffer.clear()
        return output
    }

    override fun readColor(x: Int, y: Int): OmniColor {
        require(format.isColor) { "readColor on non-color texture ($format)" }

        FramebufferHelper.attachColor(internalReadFramebuffer, id, FramebufferTarget.READ)
        ensureFloatBufferCapacity(4)
        FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
            GlInternals.withPackState(1, 4, 4) {
                GL11.glReadPixels(
                    x, y, 1, 1,
                    GL11.GL_RGBA, GL11.GL_FLOAT, internalFloatBuffer
                )
            }
        }

        val r = internalFloatBuffer.get(0)
        val g = internalFloatBuffer.get(1)
        val b = internalFloatBuffer.get(2)
        val a = internalFloatBuffer.get(3)

        internalFloatBuffer.clear()
        return OmniColor(r, g, b, a)
    }

    override fun writeColor(x: Int, y: Int, width: Int, height: Int, data: Array<OmniColor>) {
        check(format.isColor) { "writeColor on non-color texture ($format)" }
        check(data.size == width * height) { "Data size (${data.size}) does not match width*height (${width * height})" }

        val bytesNeeded = width * height * 4
        ensureFloatBufferCapacity(bytesNeeded)

        var pos = 0
        for (color in data) {
            internalFloatBuffer.put(pos, color.red / 255f)
            internalFloatBuffer.put(pos + 1, color.green / 255f)
            internalFloatBuffer.put(pos + 2, color.blue / 255f)
            internalFloatBuffer.put(pos + 3, color.alpha / 255f)
            pos += 4
        }

        using {
            GlInternals.withUnpackState(width, 4, 4) {
                internalFloatBuffer.position(0)
                internalFloatBuffer.limit(bytesNeeded)
                GL11.glTexSubImage2D(
                    GL11.GL_TEXTURE_2D, 0,
                    x, y, width, height,
                    GL11.GL_RGBA, GL11.GL_FLOAT,
                    internalFloatBuffer
                )
            }
        }

        internalFloatBuffer.clear()
    }

    override fun writeColor(x: Int, y: Int, color: OmniColor) {
        require(format.isColor) { "writeColor on non-color texture ($format)" }

        ensureFloatBufferCapacity(4)
        internalFloatBuffer.put(0, color.red / 255f)
        internalFloatBuffer.put(1, color.green / 255f)
        internalFloatBuffer.put(2, color.blue / 255f)
        internalFloatBuffer.put(3, color.alpha / 255f)

        using {
            GlInternals.withUnpackState(1, 4, 4) {
                internalFloatBuffer.position(0)
                internalFloatBuffer.limit(4)
                GL11.glTexSubImage2D(
                    GL11.GL_TEXTURE_2D, 0,
                    x, y, 1, 1,
                    GL11.GL_RGBA, GL11.GL_FLOAT,
                    internalFloatBuffer
                )
            }
        }

        internalFloatBuffer.clear()
    }

    override fun readDepth(x: Int, y: Int, width: Int, height: Int): Array<Float> {
        FramebufferHelper.attachDepth(internalReadFramebuffer, id, target = FramebufferTarget.READ)
        ensureFloatBufferCapacity(width * height)
        FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
            GlInternals.withPackState(width, 1, 4) {
                GL11.glReadPixels(
                    x, y, width, height,
                    GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, internalFloatBuffer
                )
            }
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
            GlInternals.withPackState(1, 1, 4) {
                GL11.glReadPixels(
                    x, y, 1, 1,
                    GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, internalFloatBuffer
                )
            }
        }

        val depth = internalFloatBuffer.get(0)
        internalFloatBuffer.clear()
        return depth
    }

    override fun writeDepth(x: Int, y: Int, width: Int, height: Int, data: Array<Float>) {
        check(data.size == width * height) { "Data size (${data.size}) does not match width*height (${width * height})" }

        val bytesNeeded = width * height
        ensureFloatBufferCapacity(bytesNeeded)

        for (i in 0 until width * height) {
            internalFloatBuffer.put(i, data[i])
        }

        using {
            GlInternals.withUnpackState(width, 1, 4) {
                internalFloatBuffer.position(0)
                internalFloatBuffer.limit(bytesNeeded)
                GL11.glTexSubImage2D(
                    GL11.GL_TEXTURE_2D, 0,
                    x, y, width, height,
                    GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT,
                    internalFloatBuffer
                )
            }
        }

        internalFloatBuffer.clear()
    }

    override fun writeDepth(x: Int, y: Int, depth: Float) {
        ensureFloatBufferCapacity(1)
        internalFloatBuffer.put(0, depth)
        using {
            GlInternals.withUnpackState(1, 1, 4) {
                internalFloatBuffer.position(0)
                internalFloatBuffer.limit(1)
                GL11.glTexSubImage2D(
                    GL11.GL_TEXTURE_2D, 0,
                    x, y, 1, 1,
                    GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT,
                    internalFloatBuffer
                )
            }
        }

        internalFloatBuffer.clear()
    }

    override fun copyFrom(operations: Iterable<OmniTextureHandle.CopyOp>) {
        val attachment = if (format.isColor) GL30.GL_COLOR_ATTACHMENT0 else GL30.GL_DEPTH_ATTACHMENT
        val bufferBit = if (format.isColor) GL11.GL_COLOR_BUFFER_BIT else GL11.GL_DEPTH_BUFFER_BIT

        FramebufferHelper.with(FramebufferTarget.WRITE, internalDrawFramebuffer) {
            FramebufferHelper.with(FramebufferTarget.READ, internalReadFramebuffer) {
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
