package dev.deftu.multi

//#if MC >= 1.17.1
import com.mojang.blaze3d.systems.RenderSystem
//#endif

import com.mojang.blaze3d.platform.TextureUtil
import net.minecraft.client.texture.NativeImage
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL30
import java.awt.Color
import java.io.File
import java.nio.ByteBuffer
import kotlin.math.max

public class MultiFramebuffer {
    private companion object {
        private var maxSupportedTextureSize = -1

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
            MultiClient.getInstance().framebuffer.fbo
            //#else
            //$$ MultiClient.getInstance().framebuffer.framebufferObject
            //#endif

    private var fbo = -1
    private var colorAttachment = -1
    private var depthAttachment = -1

    public fun bind(modifyViewport: Boolean) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo)
        if (modifyViewport) {
            GL11.glViewport(0, 0, width, height)
        }
    }

    public fun unbind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, mcFbo)
    }

    public fun bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorAttachment)
    }

    public fun unbindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, GL11.GL_NONE)
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
            TextureUtil.releaseTextureId(depthAttachment)
            depthAttachment = -1
        }

        if (colorAttachment != -1) {
            TextureUtil.releaseTextureId(colorAttachment)
            colorAttachment = -1
        }

        if (fbo != -1) {
            unbindTexture()
            GL30.glDeleteFramebuffers(fbo)
            fbo = -1
        }
    }

    public fun draw(stack: MultiMatrixStack) {
        unbind()

        stack.push()
        MultiGlStateManager.colorMask(true, true, true, false)
        MultiGlStateManager.disableDepth()
        MultiGlStateManager.depthMask(false)
        MultiGlStateManager.viewport(0, 0, width, height)
        val scaleFactor = MultiResolution.scaleFactor
        val xScale = width / scaleFactor / width.toDouble()
        val yScale = height / scaleFactor / height.toDouble()
        stack.scale(xScale, yScale, 0.0)
        MultiGlStateManager.enableBlend()
        //#if MC >= 1.17.1
        RenderSystem.setShaderTexture(0, colorAttachment)
        //#endif
        bindTexture()

        val tessellator = MultiTessellator.getFromBuffer()
        tessellator.beginWithDefaultShader(MultiTessellator.DrawModes.QUADS, MultiTessellator.VertexFormats.POSITION_TEXTURE_COLOR)
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
        MultiGlStateManager.depthMask(true)
        MultiGlStateManager.colorMask(true, true, true, true)
        MultiGlStateManager.viewport(0, 0, width, height)
        stack.pop()
    }

    public fun clear() {
        bind(true)
        GL11.glClearColor(1f, 1f, 1f, 0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        unbind()
    }

    public fun writeToFile(file: File) {
        val image = NativeImage(width, height, false)
        bindTexture()
        image.loadFromTextureImage(0, true)
        image.mirrorVertically()
        image.writeTo(file)
        unbindTexture()
    }

    private fun initialize(
        width: Int,
        height: Int
    ) {
        this.width = width
        this.height = height

        this.colorAttachment = TextureUtil.generateTextureId()
        this.depthAttachment = TextureUtil.generateTextureId()
        val size = findSize(width, height)

        this.fbo = GL30.glGenFramebuffers()

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
            colorAttachment = TextureUtil.generateTextureId()
        }

        GL11.glGetError()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorAttachment)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, size.width, size.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
        return GL11.glGetError() == GL11.GL_NO_ERROR
    }

    private fun trySetupDepth(size: Size): Boolean {
        if (depthAttachment == -1) {
            depthAttachment = TextureUtil.generateTextureId()
        }

        GL11.glGetError()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthAttachment)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, size.width, size.height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
        return GL11.glGetError() == GL11.GL_NO_ERROR
    }

    private fun createColorAttachment() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorAttachment)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorAttachment, 0)
    }

    private fun createDepthAttachment() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthAttachment)
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
        } else println("Framebuffer is complete")
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
