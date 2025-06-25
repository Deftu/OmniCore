package dev.deftu.omnicore.client.render.framebuffer

import dev.deftu.omnicore.client.render.texture.GlTexture
import org.lwjgl.opengl.GL11

public class WrappedFramebuffer(
    override val id: Int,
    override val width: Int,
    override val height: Int,
    override val colorTexture: GlTexture,
    /**
     * The depth(-stencil?) texture. This may be null if the framebuffer does not have a depth texture.
     * As it is handled by the user, it's not guaranteed to have a stencil attachment.
     */
    public val depthStencilTexture: GlTexture?,
) : Framebuffer {

    override fun resize(width: Int, height: Int) {
        throw UnsupportedOperationException("Resizing a wrapped framebuffer is not supported.")
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        this.using {
            Framebuffer.apiClearColor(red, green, blue, alpha)
            Framebuffer.apiClear(GL11.GL_COLOR_BUFFER_BIT)
        }
    }

    override fun close() {
        throw UnsupportedOperationException("Closing a wrapped framebuffer is not supported.")
    }

}
