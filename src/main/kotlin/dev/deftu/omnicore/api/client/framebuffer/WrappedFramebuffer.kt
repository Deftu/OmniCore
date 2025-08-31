package dev.deftu.omnicore.api.client.framebuffer

import dev.deftu.omnicore.api.client.textures.AbstractGlTexture
import dev.deftu.omnicore.api.client.render.ClearMask
import dev.deftu.omnicore.internal.client.render.GlInternals

public class WrappedFramebuffer(
    override val id: Int,
    override val width: Int,
    override val height: Int,
    override val colorTexture: AbstractGlTexture,
    /**
     * The depth(-stencil?) texture. This may be null if the framebuffer does not have a depth texture.
     * As it is handled by the user, it's not guaranteed to have a stencil attachment.
     */
    public val depthStencilTexture: AbstractGlTexture?,
) : OmniFramebuffer {
    override fun resize(width: Int, height: Int) {
        throw UnsupportedOperationException("Resizing a wrapped framebuffer is not supported.")
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        this.using {
            GlInternals.clearColor(red, green, blue, alpha)
            GlInternals.clear(ClearMask.COLOR)
        }
    }

    override fun close() {
        throw UnsupportedOperationException("Closing a wrapped framebuffer is not supported.")
    }
}
