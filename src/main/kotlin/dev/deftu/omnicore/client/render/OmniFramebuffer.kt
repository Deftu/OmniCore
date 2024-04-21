@file:Suppress("MemberVisibilityCanBePrivate")

package dev.deftu.omnicore.client.render

//#if MC >= 1.16
import dev.deftu.omnicore.client.*
import net.minecraft.client.texture.NativeImage
//#else
//$$ import org.lwjgl.BufferUtils
//$$ import java.awt.image.BufferedImage
//$$ import javax.imageio.ImageIO
//#endif

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL30
import java.awt.Color
import java.io.File
import java.nio.ByteBuffer
import kotlin.math.max

public class OmniFramebuffer {
    public companion object {
        private var maxSupportedTextureSize = -1

        @JvmStatic
        public fun genFramebuffer(): Int =
            GL30.glGenFramebuffers()

        @JvmStatic
        public fun bindFramebuffer(fbo: Int) {
            GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo)
        }

        @JvmStatic
        public fun bindDrawFramebuffer(fbo: Int) {
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo)
        }

        @JvmStatic
        public fun bindReadFramebuffer(fbo: Int) {
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo)
        }

        @JvmStatic
        public fun deleteFramebuffer(fbo: Int) {
            GL30.glDeleteFramebuffers(fbo)
        }

        private fun getMaxSupportedTextureSize(): Int {
            if (maxSupportedTextureSize == -1) {
                val glValue = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE)
                var value = max(Short.MAX_VALUE.toInt(), glValue)
                while (value >= 1024) {
                    GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, value, value, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
                    val status = GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH)
                    if (status == 0) {
                        value = value shr 1
                        continue
                    }

                    maxSupportedTextureSize = value
                    return value
                }

                maxSupportedTextureSize = max(glValue, 1024)
            }

            return maxSupportedTextureSize
        }
    }

    private var width: Int = 0
    private var height: Int = 0

    private val mcFbo: Int
        get() =
            //#if MC >= 1.16.5
            OmniClient.getInstance().framebuffer.fbo
            //#else
            //$$ MultiClient.getInstance().framebuffer.framebufferObject
            //#endif

    private var fbo = -1
    private var colorAttachment = -1
    private var depthAttachment = -1

    private var clearColor = floatArrayOf(0f, 0f, 0f, 0f)

    public fun bind(modifyViewport: Boolean) {
        bindFramebuffer(fbo)
        if (modifyViewport) {
            OmniRenderState.setViewport(0, 0, width, height)
        }
    }

    public fun unbind() {
        bindFramebuffer(mcFbo)
    }

    public fun bindTexture() {
        OmniTextureManager.bindTexture(colorAttachment)
    }

    public fun unbindTexture() {
        OmniTextureManager.removeTexture()
    }

    public fun resize(
        width: Int,
        height: Int
    ) {
        if (
            this.width == width &&
            this.height == height
        ) return
        delete()
        initialize(width, height)
        unbind()
    }

    public fun delete() {
        unbindTexture()
        unbind()

        if (depthAttachment != -1) {
            OmniTextureManager.deleteTexture(depthAttachment)
            depthAttachment = -1
        }

        if (colorAttachment != -1) {
            OmniTextureManager.deleteTexture(colorAttachment)
            colorAttachment = -1
        }

        if (fbo != -1) {
            unbindTexture()
            deleteFramebuffer(fbo)
            fbo = -1
        }
    }

    public fun draw(stack: OmniMatrixStack) {
        stack.push()

        OmniRenderState.setTexture(0, colorAttachment)
        OmniRenderState.setColorMask(red = true, green = true, blue = true, alpha = false)
        OmniRenderState.disableDepth()
        OmniRenderState.setDepthMask(false)
        OmniRenderState.setViewport(0, 0, width, height)
        val scaleFactor = OmniResolution.scaleFactor
        val xScale = width / scaleFactor / width.toDouble()
        val yScale = height / scaleFactor / height.toDouble()
        stack.scale(xScale, yScale, 0.0)
        OmniRenderState.enableBlend()

        val tessellator = OmniTessellator.getFromBuffer()
        tessellator.beginWithDefaultShader(
            OmniTessellator.DrawModes.QUADS,
            OmniTessellator.VertexFormats.POSITION_TEXTURE_COLOR
        )
        tessellator.vertex(stack, 0f, 0f, 0f)
            .texture(0f, 1f)
            .color(Color.WHITE)
            .next()
        tessellator.vertex(stack, 0f, height.toFloat(), 0f)
            .texture(0f, 0f)
            .color(Color.WHITE)
            .next()
        tessellator.vertex(stack, width.toFloat(), height.toFloat(), 0f)
            .texture(1f, 0f)
            .color(Color.WHITE)
            .next()
        tessellator.vertex(stack, width.toFloat(), 0f, 0f)
            .texture(1f, 1f)
            .color(Color.WHITE)
            .next()

        tessellator.draw()

        unbindTexture()
        OmniRenderState.setDepthMask(true)
        OmniRenderState.setColorMask(red = true, green = true, blue = true, alpha = true)
        OmniRenderState.setViewport(0, 0, width, height)
        stack.pop()
    }

    public fun clear() {
        bind(true)
        OmniRenderState.setClearColor(clearColor[0], clearColor[1], clearColor[2], clearColor[3])
        OmniRenderState.clear(OmniRenderState.ClearMask.COLOR, OmniRenderState.ClearMask.DEPTH)
        unbind()
    }

    public fun setClearColor(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        val r = red.coerceIn(0f, 1f)
        val g = green.coerceIn(0f, 1f)
        val b = blue.coerceIn(0f, 1f)
        val a = alpha.coerceIn(0f, 1f)
        clearColor = floatArrayOf(r, g, b, a)
    }

    public fun setClearColor(
        color: Color
    ) {
        setClearColor(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)
    }

    /**
     * Copies the contents of the other framebuffer's texture to this framebuffer's texture.
     */
    // IMPLEMENTING NOW - @Deprecated("Use copyFromRender(stack, other) instead. This function is currently unimplemented.", level = DeprecationLevel.ERROR)
    public fun copyFrom(
        other: OmniFramebuffer
    ) {
        // Clear the current framebuffer.
        bind(true)
        setClearColor(other.clearColor[0], other.clearColor[1], other.clearColor[2], other.clearColor[3])
        clear()
        unbind()

        // Set the draw and read framebuffers to this framebuffer and the other framebuffer, respectively.
        bindDrawFramebuffer(fbo)
        bindReadFramebuffer(other.fbo)

        // Blit the other framebuffer's texture to this framebuffer's texture.
        GL30.glBlitFramebuffer(
            0, 0, other.width, other.height,
            0, 0, width, height,
            GL11.GL_COLOR_BUFFER_BIT,
            GL11.GL_NEAREST
        )

        // Unbind the framebuffers.
        unbind()
    }

    @JvmOverloads
    public fun copyFromRender(
        stack: OmniMatrixStack = OmniMatrixStack(),
        other: OmniFramebuffer
    ) {
        bind(true)
        other.draw(stack)
        unbind()
    }

    public fun writeToFile(file: File) {
        //#if MC >= 1.16
        val image = NativeImage(width, height, false)
        bindTexture()
        image.loadFromTextureImage(0, true)
        image.mirrorVertically()
        image.writeTo(file)
        unbindTexture()
        //#else
        //$$ val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        //$$ val buffer = BufferUtils.createByteBuffer(width * height * 4)
        //$$ bindTexture()
        //$$ GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)
        //$$ for (y in 0..<height) {
        //$$     for (x in 0..<width) {
        //$$         val i = (x + y * width) * 4
        //$$         val r = buffer.get(i).toInt() and 0xFF
        //$$         val g = buffer.get(i + 1).toInt() and 0xFF
        //$$         val b = buffer.get(i + 2).toInt() and 0xFF
        //$$         val a = buffer.get(i + 3).toInt() and 0xFF
        //$$         val argb = (a shl 24) or (r shl 16) or (g shl 8) or b
        //$$         image.setRGB(x, height - y - 1, argb)
        //$$     }
        //$$ }
        //$$
        //$$ unbindTexture()
        //$$ ImageIO.write(image, "png", file)
        //#endif
    }

    private fun initialize(
        width: Int,
        height: Int
    ) {
        this.width = width
        this.height = height

        this.colorAttachment = OmniTextureManager.generateTexture()
        this.depthAttachment = OmniTextureManager.generateTexture()
        val size = findSize(width, height)

        this.fbo = genFramebuffer()

        bind(false)
        createColorAttachment()
        createDepthAttachment()
        unbindTexture()
        this.width = size.width
        this.height = size.height
        checkStatus()
        clear()
        unbind()
    }

    private fun findSize(
        width: Int,
        height: Int
    ): Size {
        var attachment: Attachment
        for (size in Size.find(width, height)) {
            attachment = Attachment.NONE

            if (trySetupColor(size)) {
                attachment = attachment.with(Attachment.COLOR)
            }

            if (trySetupDepth(size)) {
                attachment = attachment.with(Attachment.DEPTH)
            }

            if (attachment != Attachment.COLOR_DEPTH) continue
            return size
        }

        error("Failed to find suitable size for framebuffer")
    }

    private fun trySetupColor(size: Size): Boolean {
        if (colorAttachment == -1) {
            colorAttachment = OmniTextureManager.generateTexture()
        }

        OmniRenderEnv.getError()
        OmniTextureManager.bindTexture(colorAttachment)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, size.width, size.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
        return OmniRenderEnv.getError() == OmniRenderEnv.GlError.NO_ERROR
    }

    private fun trySetupDepth(size: Size): Boolean {
        if (depthAttachment == -1) {
            depthAttachment = OmniTextureManager.generateTexture()
        }

        OmniRenderEnv.getError()
        OmniTextureManager.bindTexture(depthAttachment)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, size.width, size.height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
        return OmniRenderEnv.getError() == OmniRenderEnv.GlError.NO_ERROR
    }

    private fun createColorAttachment() {
        OmniTextureManager.bindTexture(colorAttachment)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorAttachment, 0)
    }

    private fun createDepthAttachment() {
        OmniTextureManager.bindTexture(depthAttachment)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthAttachment, 0)
    }

    private fun checkStatus() {
        val status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER)
        if (status != GL30.GL_FRAMEBUFFER_COMPLETE) {
            val message = when (status) {
                GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT -> "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT -> "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER -> "GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER -> "GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER"
                GL30.GL_FRAMEBUFFER_UNSUPPORTED -> "GL_FRAMEBUFFER_UNSUPPORTED"
                GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE -> "GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE"
                else -> "Unknown error"
            }

            error("Framebuffer is not complete: $message (${status.toString(16)})")
        }
    }

    private class Size(
        val width: Int,
        val height: Int
    ) {
        companion object {
            internal val DEFAULT = Size(854, 480)

            @JvmStatic
            internal fun find(
                width: Int,
                height: Int
            ): List<Size> {
                val maxSupportedTextureSize = getMaxSupportedTextureSize()
                if (
                    width <= 0 ||
                    width > maxSupportedTextureSize ||
                    height <= 0 ||
                    height > maxSupportedTextureSize
                ) return listOf(DEFAULT)

                return listOf(Size(width, height), DEFAULT)
            }
        }
    }

    private enum class Attachment {
        NONE,
        COLOR,
        DEPTH,
        COLOR_DEPTH;

        @Suppress("EnumValuesSoftDeprecate")
        fun with(
            other: Attachment
        ): Attachment {
            return values()[ordinal or other.ordinal]
        }
    }
}
