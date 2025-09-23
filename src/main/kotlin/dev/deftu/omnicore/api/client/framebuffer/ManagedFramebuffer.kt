package dev.deftu.omnicore.api.client.framebuffer

import dev.deftu.omnicore.api.client.textures.AbstractGlTexture
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferHelper
import dev.deftu.omnicore.api.client.render.ClearMask
import dev.deftu.omnicore.api.client.textures.TextureConfiguration
import dev.deftu.omnicore.internal.client.render.GlInternals

public class ManagedFramebuffer @JvmOverloads public constructor(
    width: Int,
    height: Int,
    public val colorFormat: OmniTextureFormat,
    public val depthFormat: OmniTextureFormat,
    public val colorConfiguration: TextureConfiguration = TextureConfiguration.DEFAULT,
    public val depthConfiguration: TextureConfiguration = TextureConfiguration.DEFAULT,
) : OmniFramebuffer {
    override var id: Int = -1
        private set

    override var width: Int = width
        private set

    override var height: Int = height
        private set

    override lateinit var colorTexture: AbstractGlTexture
        private set

    public lateinit var depthStencilTexture: AbstractGlTexture
        private set

    init {
        this.initialize()
    }

    public fun clearDepthStencil(depth: Double, stencil: Int) {
        this.using {
            GlInternals.clearDepth(depth)
            GlInternals.clearStencil(stencil)
            GlInternals.clear(ClearMask.DEPTH)
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
            GlInternals.clearColor(red, green, blue, alpha)
            GlInternals.clear(ClearMask.COLOR)
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
            FramebufferInternals.delete(this.id)
            this.id = -1
        }
    }

    private fun initialize() {
        this.id = FramebufferInternals.create()
        this.colorTexture = OmniTextures.create(width, height, colorFormat, colorConfiguration) as AbstractGlTexture
        this.depthStencilTexture = OmniTextures.create(width, height, depthFormat, depthConfiguration) as AbstractGlTexture
        this.using {
            FramebufferHelper.attachColor(this.id, this.colorTexture.id)
            FramebufferInternals.checkStatus()

            FramebufferHelper.attachDepth(this.id, this.depthStencilTexture.id, this.depthFormat)
            FramebufferInternals.checkStatus()
        }
    }
}
