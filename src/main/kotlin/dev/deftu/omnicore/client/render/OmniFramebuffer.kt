@file:Suppress("MemberVisibilityCanBePrivate", "DEPRECATION")

package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Incubating
import dev.deftu.omnicore.client.render.pipeline.DrawModes
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.client.render.pipeline.VertexFormats
import dev.deftu.omnicore.client.render.state.*
import dev.deftu.omnicore.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.common.OmniIdentifier
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL30
import java.awt.Color
import java.io.File
import java.nio.ByteBuffer

//#if MC >= 1.16.5
//#if MC <= 1.16.5
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

//#else
//$$ import net.minecraft.client.renderer.OpenGlHelper
//$$ import org.lwjgl.BufferUtils
//$$ import java.awt.image.BufferedImage
//$$ import javax.imageio.ImageIO
//#endif

@Incubating
@GameSide(Side.CLIENT)
public class OmniFramebuffer {

    public companion object {

        private val PIPELINE by lazy {
            OmniRenderPipeline.builderWithDefaultShader(
                identifier = OmniIdentifier.create("omnicore", "omniframebuffer"),
                vertexFormat = VertexFormats.POSITION_TEXTURE_COLOR,
                mode = DrawModes.QUADS,
            ).apply {
                blendState = OmniManagedBlendState.asEnabled(BlendEquation.active(), BlendFunction.LIGHTMAP)
                depthState = OmniManagedDepthState.DISABLED
            }.build()
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun <T> withFramebuffer(target: Int, fbo: Int, block: () -> T): T {
            val unbind = bindFramebuffer(target, fbo)
            try {
                return block()
            } finally {
                unbind()
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun <T> withFramebuffer(fbo: Int, block: () -> T): T {
            return withFramebuffer(GL30.GL_FRAMEBUFFER, fbo, block)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun genFramebuffer(): Int {
            //#if MC >= 1.17.1
            return GL30.glGenFramebuffers()
            //#elseif MC >= 1.16.5
            //$$ return GlStateManager.genFramebuffers()
            //#else
            //$$ return OpenGlHelper.glGenFramebuffers()
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun bindFramebuffer(target: Int, fbo: Int): () -> Unit {
            val prevReadFramebuffer = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING)
            val prevDrawFramebuffer = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING)

            //#if MC >= 1.17.1
            GL30.glBindFramebuffer(target, fbo)
            //#elseif MC >= 1.16.5
            //$$ GlStateManager.bindFramebuffer(target, fbo)
            //#else
            //$$ OpenGlHelper.glBindFramebuffer(target, fbo)
            //#endif

            return {
                bindReadFramebuffer(prevReadFramebuffer)
                bindDrawFramebuffer(prevDrawFramebuffer)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun bindFramebuffer(fbo: Int): () -> Unit {
            return bindFramebuffer(GL30.GL_FRAMEBUFFER, fbo)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun bindReadFramebuffer(fbo: Int): () -> Unit {
            return bindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun bindDrawFramebuffer(fbo: Int): () -> Unit {
            return bindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun deleteFramebuffer(fbo: Int) {
            //#if MC >= 1.17.1
            GL30.glDeleteFramebuffers(fbo)
            //#elseif MC >= 1.16.5
            //$$ GlStateManager.deleteFramebuffers(fbo)
            //#else
            //$$ OpenGlHelper.glDeleteFramebuffers(fbo)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun createFrom(other: OmniFramebuffer): OmniFramebuffer {
            val result = OmniFramebuffer(other.width, other.height)
            result.copyFrom(other)
            return result
        }

    }

    public var width: Int = 0
        private set
    public var height: Int = 0
        private set

    private var fbo = -1
    private var colorAttachment = -1
    private var depthStencilAttachment = -1

    public var clearColor: Color = Color(0, 0, 0, 0)
    public var clearDepth: Double = 1.0
    public var clearStencil: Int = 0

    public constructor() : this(OmniResolution.viewportWidth, OmniResolution.viewportHeight)

    public constructor(
        width: Int,
        height: Int
    ) {
        resize(width, height)
    }

    public fun initialize(
        width: Int,
        height: Int
    ) {
        this.width = width
        this.height = height

        // Set up color attachment
        this.colorAttachment = OmniTextureManager.generateTexture()
        OmniTextureManager.configureTexture(this.colorAttachment) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, null as ByteBuffer?)
        }

        OmniRenderEnv.error.ifPresent { error("Failed to set up color attachment: $it") }

        // Set up depth attachment
        this.depthStencilAttachment = OmniTextureManager.generateTexture()
        OmniTextureManager.configureTexture(this.depthStencilAttachment) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL11.GL_NONE)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_DEPTH24_STENCIL8, width, height, 0, GL30.GL_DEPTH_STENCIL, GL30.GL_UNSIGNED_INT_24_8, null as ByteBuffer?)
        }

        OmniRenderEnv.error.ifPresent { error("Failed to set up depth attachment: $it") }

        // Set up the framebuffer
        this.fbo = genFramebuffer()
        withFramebuffer(fbo) {
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorAttachment, 0)
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL11.GL_TEXTURE_2D, depthStencilAttachment, 0)
        }

        checkStatus()
        OmniRenderEnv.error.ifPresent { error("Failed to set up framebuffer: $it") }
    }

    @GameSide(Side.CLIENT)
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
    }

    @GameSide(Side.CLIENT)
    public fun delete() {
        if (depthStencilAttachment != -1) {
            OmniTextureManager.deleteTexture(depthStencilAttachment)
            depthStencilAttachment = -1

            OmniRenderEnv.error.ifPresent { error("Failed to delete depth attachment: $it") }
        }

        if (colorAttachment != -1) {
            OmniTextureManager.deleteTexture(colorAttachment)
            colorAttachment = -1

            OmniRenderEnv.error.ifPresent { error("Failed to delete color attachment: $it") }
        }

        if (fbo != -1) {
            deleteFramebuffer(fbo)
            fbo = -1

            OmniRenderEnv.error.ifPresent { error("Failed to delete framebuffer: $it") }
        }
    }

    @JvmOverloads
    @GameSide(Side.CLIENT)
    public fun <T> withBound(
        target: Int = GL30.GL_FRAMEBUFFER,
        block: () -> T
    ): T {
        return withFramebuffer(target, fbo, block)
    }

    @GameSide(Side.CLIENT)
    public fun draw(
        stack: OmniMatrixStack,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color
    ) {
        stack.push()
        stack.scale(1f, 1f, 50f)

        val buffer = OmniBufferBuilder.create(DrawModes.QUADS, VertexFormats.POSITION_TEXTURE_COLOR)
        buffer
            .vertex(stack, x.toDouble(), (y + height).toDouble(), 0.0)
            .texture(0.0, 0.0)
            .color(color)
            .next()
        buffer
            .vertex(stack, (x + width).toDouble(), (y + height).toDouble(), 0.0)
            .texture(1.0, 0.0)
            .color(color)
            .next()
        buffer
            .vertex(stack, (x + width).toDouble(), y.toDouble(), 0.0)
            .texture(1.0, 1.0)
            .color(color)
            .next()
        buffer
            .vertex(stack, x.toDouble(), y.toDouble(), 0.0)
            .texture(0.0, 1.0)
            .color(color)
            .next()
        buffer.build()?.drawWithCleanup(PIPELINE) {
            texture(0, colorAttachment)
        }

        stack.pop()
    }

    @GameSide(Side.CLIENT)
    public fun clear() {
        withBound {
            OmniRenderState.setClearColor(clearColor.red / 255f, clearColor.green / 255f, clearColor.blue / 255f, clearColor.alpha / 255f)
            OmniRenderState.setClearDepth(clearDepth)
            OmniRenderState.setClearStencil(clearStencil)
            OmniRenderState.clear(OmniRenderState.ClearMask.COLOR, OmniRenderState.ClearMask.DEPTH, OmniRenderState.ClearMask.STENCIL)

            OmniRenderEnv.error.ifPresent { error("Failed to clear framebuffer: $it") }
        }
    }

    @GameSide(Side.CLIENT)
    public fun clearColor() {
        withBound {
            OmniRenderState.setClearColor(clearColor.red / 255f, clearColor.green / 255f, clearColor.blue / 255f, clearColor.alpha / 255f)
            OmniRenderState.clear(OmniRenderState.ClearMask.COLOR)

            OmniRenderEnv.error.ifPresent { error("Failed to clear color buffer: $it") }
        }
    }

    @GameSide(Side.CLIENT)
    public fun clearDepth() {
        withBound {
            OmniRenderState.setClearDepth(clearDepth)
            OmniRenderState.clear(OmniRenderState.ClearMask.DEPTH)

            OmniRenderEnv.error.ifPresent { error("Failed to clear depth buffer: $it") }
        }
    }

    @GameSide(Side.CLIENT)
    public fun clearStencil() {
        withBound {
            OmniRenderState.setClearStencil(clearStencil)
            OmniRenderState.clear(OmniRenderState.ClearMask.STENCIL)

            OmniRenderEnv.error.ifPresent { error("Failed to clear stencil buffer: $it") }
        }
    }

    @GameSide(Side.CLIENT)
    public fun copyFrom(width: Int, height: Int, otherFbo: Int) {
        if (
            this.width != width ||
            this.height != height
        ) {
            resize(width, height)
        }

        withFramebuffer(GL30.GL_READ_FRAMEBUFFER, otherFbo) {
            withBound(GL30.GL_DRAW_FRAMEBUFFER) {
                GL30.glBlitFramebuffer(
                    0, 0, width, height,
                    0, 0, this.width, this.height,
                    GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT,
                    GL11.GL_NEAREST
                )

                OmniRenderEnv.error.ifPresent { error("Failed to copy (blit) framebuffer: $it") }
            }
        }
    }

    @GameSide(Side.CLIENT)
    public fun copyFrom(other: OmniFramebuffer) {
        if (
            width != other.width ||
            height != other.height
        ) {
            resize(other.width, other.height)
        }

        other.withBound(GL30.GL_READ_FRAMEBUFFER) {
            withBound(GL30.GL_DRAW_FRAMEBUFFER) {
                GL30.glBlitFramebuffer(
                    0, 0, other.width, other.height,
                    0, 0, width, height,
                    GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT,
                    GL11.GL_NEAREST
                )

                OmniRenderEnv.error.ifPresent { error("Failed to copy (blit) framebuffer: $it") }
            }
        }
    }

    @GameSide(Side.CLIENT)
    public fun writeToFile(file: File) {
        OmniTextureManager.bindTexture(colorAttachment)

        OmniImage(width, height).use { image ->
            image.loadFromBoundTexture()
            image.flipY()
            image.saveTo(file)
        }

        OmniTextureManager.bindTexture(0)
    }

    private fun checkStatus() {
        withBound {
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
    }

}
