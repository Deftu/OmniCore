package dev.deftu.omnicore.client.render.framebuffer

import dev.deftu.omnicore.client.render.texture.GlTexture
import dev.deftu.omnicore.client.render.texture.GpuTexture
import dev.deftu.omnicore.client.render.texture.ManagedTexture
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

public class ManagedFramebuffer(
    width: Int,
    height: Int,
    private val colorFormat: GpuTexture.TextureFormat,
    private val depthFormat: GpuTexture.TextureFormat,
) : Framebuffer {

    override var id: Int = -1
        private set

    override var width: Int = width
        private set

    override var height: Int = height
        private set

    override lateinit var colorTexture: GlTexture
        private set

    public lateinit var depthStencilTexture: GlTexture
        private set

    init {
        this.initialize()
    }

    public fun clearDepthStencil(depth: Double, stencil: Int) {
        this.using {
            Framebuffer.apiClearDepth(depth)
            Framebuffer.apiClearStencil(stencil)
            Framebuffer.apiClear(GL11.GL_DEPTH_BUFFER_BIT)
        }
    }

    override fun resize(width: Int, height: Int) {
        if (
            this.width == width &&
            this.height == height
        ) {
            return
        }

        this.width = width
        this.height = height
        this.close()
        this.initialize()
    }

    override fun clearColor(red: Float, green: Float, blue: Float, alpha: Float) {
        this.using {
            Framebuffer.apiClearColor(red, green, blue, alpha)
            Framebuffer.apiClear(GL11.GL_COLOR_BUFFER_BIT)
        }
    }

    override fun close() {
        if (this::depthStencilTexture.isInitialized) {
            this.depthStencilTexture.close()
        }

        if (this::colorTexture.isInitialized) {
            this.colorTexture.close()
        }

        if (this.id != -1) {
            Framebuffer.deleteFramebuffer(this.id)
            this.id = -1
        }
    }

    private fun initialize() {
        this.id = Framebuffer.genFramebuffer()
        this.colorTexture = ManagedTexture(width, height, colorFormat)
        this.depthStencilTexture = ManagedTexture(width, height, depthFormat)
        this.using {
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, this.colorTexture.id, 0)
            this.checkStatus()

            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, when (this.depthFormat) {
                GpuTexture.TextureFormat.RGBA8 -> throw UnsupportedOperationException("RGBA8 is not a valid depth format")
                GpuTexture.TextureFormat.DEPTH24_STENCIL8 -> GL30.GL_DEPTH_STENCIL_ATTACHMENT
                GpuTexture.TextureFormat.DEPTH32 -> GL30.GL_DEPTH_ATTACHMENT
            }, GL11.GL_TEXTURE_2D, this.depthStencilTexture.id, 0)
            this.checkStatus()
        }
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

}
