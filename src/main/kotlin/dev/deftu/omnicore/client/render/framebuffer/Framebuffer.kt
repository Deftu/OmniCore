package dev.deftu.omnicore.client.render.framebuffer

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.render.OmniImage
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.omnicore.client.render.OmniResolution
import dev.deftu.omnicore.client.render.OmniTextureManager
import dev.deftu.omnicore.client.render.pipeline.DrawModes
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.client.render.pipeline.VertexFormats
import dev.deftu.omnicore.client.render.state.OmniManagedBlendState
import dev.deftu.omnicore.client.render.state.OmniManagedScissorState
import dev.deftu.omnicore.client.render.texture.GlTexture
import dev.deftu.omnicore.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.common.OmniIdentifier
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import java.io.File

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

//#if MC < 1.21.5
import com.mojang.blaze3d.platform.GlStateManager
//#endif

public interface Framebuffer : AutoCloseable {

    public companion object {

        private val PIPELINE by lazy {
            OmniRenderPipeline.builderWithDefaultShader(
                identifier = OmniIdentifier.create("omnicore", "framebuffer"),
                vertexFormat = VertexFormats.POSITION_TEXTURE_COLOR,
                mode = DrawModes.QUADS,
            ).apply {
                blendState = OmniManagedBlendState.ALPHA
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
        public fun apiClear(mask: Int) {
            //#if MC >= 1.21.5
            //$$ GL11.glClear(mask)
            //#else
            GlStateManager._clear(
                mask,
                //#if MC >= 1.16.5 && MC < 1.21.2
                false,
                //#endif
            )
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun apiClearColor(red: Float, green: Float, blue: Float, alpha: Float) {
            //#if MC >= 1.21.5
            //$$ GL11.glClearColor(red, green, blue, alpha)
            //#else
            GlStateManager._clearColor(red, green, blue, alpha)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun apiClearDepth(depth: Double) {
            //#if MC >= 1.21.5
            //$$ GL11.glClearDepth(depth)
            //#else
            GlStateManager._clearDepth(depth)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun apiClearStencil(stencil: Int) {
            //#if MC >= 1.21.5 || MC <= 1.12.2
            //$$ GL11.glClearStencil(stencil)
            //#else
            GlStateManager._clearStencil(stencil)
            //#endif
        }

    }

    public val id: Int

    public val width: Int

    public val height: Int

    public val colorTexture: GlTexture

    // We don't include the depth texture here because there's no guarantee that it exists, or it could be a stencil texture alongside being a depth texture

    public fun resize(width: Int, height: Int)

    public fun clearColor(red: Float, green: Float, blue: Float, alpha: Float)

    // Same as stated above about the depth texture, but for clearing depth

    /**
     * Binds this framebuffer for rendering and returns a callable to unbind it.
     */
    public fun bind(): () -> Unit {
        return bindFramebuffer(id)
    }

    public fun <T> using(target: Int, block: () -> T): T {
        if (this.id == -1) {
            this.resize(this.width, this.height)
        }

        return withFramebuffer(target, this.id, block)
    }

    public fun <T> using(block: () -> T): T {
        return using(GL30.GL_FRAMEBUFFER, block)
    }

    public fun <T> usingToRender(block: (matrixStack: OmniMatrixStack, width: Int, height: Int) -> T): T {
        return this.using {
            // Prepare our framebuffer by ensuring that scissor state is disabled
            val prevScissorState = OmniManagedScissorState.active()
            OmniManagedScissorState.disable()

            // Define our GL viewport as only being within our framebuffer's bounds
            GL11.glViewport(0, 0, width, height)

            // Undo Minecraft's scaling factor, and the distortion caused by using its projection matrix in a differently sized viewport
            val stack = OmniMatrixStack()
            val scale = 1.0 / OmniResolution.scaleFactor
            stack.scale(scale * OmniResolution.viewportWidth / width, scale * OmniResolution.viewportHeight / height, 1.0)

            // Perform the consumer's operation
            val result = block(stack, width, height)

            // Restore all GL states and reset the viewport
            GL11.glViewport(0, 0, OmniResolution.viewportWidth, OmniResolution.viewportHeight)
            prevScissorState.activate()

            // Return the result
            result
        }
    }

    public fun drawColorTexture(matrixStack: OmniMatrixStack, x: Float, y: Float, width: Float, height: Float, color: Int) {
        matrixStack.push()
        matrixStack.scale(1f, 1f, 50f)

        val buffer = OmniBufferBuilder.create(DrawModes.QUADS, VertexFormats.POSITION_TEXTURE_COLOR)
        buffer
            .vertex(matrixStack, x.toDouble(), (y + height).toDouble(), 0.0)
            .texture(0.0, 0.0)
            .color(color)
            .next()
        buffer
            .vertex(matrixStack, (x + width).toDouble(), (y + height).toDouble(), 0.0)
            .texture(1.0, 0.0)
            .color(color)
            .next()
        buffer
            .vertex(matrixStack, (x + width).toDouble(), y.toDouble(), 0.0)
            .texture(1.0, 1.0)
            .color(color)
            .next()
        buffer
            .vertex(matrixStack, x.toDouble(), y.toDouble(), 0.0)
            .texture(0.0, 1.0)
            .color(color)
            .next()
        buffer.build()?.drawWithCleanup(PIPELINE) {
            texture(0, colorTexture.id)
        }

        matrixStack.pop()
    }

    public fun writeToFile(file: File) {
        OmniTextureManager.bindTexture(colorTexture.id)
        OmniImage(this.width, this.height).use { image ->
            image.loadFromBoundTexture()
            image.flipY()
            image.saveTo(file)
        }

        OmniTextureManager.bindTexture(0)
    }

}
