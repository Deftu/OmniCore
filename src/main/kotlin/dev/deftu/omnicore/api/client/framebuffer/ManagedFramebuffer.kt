package dev.deftu.omnicore.api.client.framebuffer

import dev.deftu.omnicore.api.client.textures.AbstractGlTexture
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferHelper
import dev.deftu.omnicore.api.client.render.ClearMask
import dev.deftu.omnicore.api.client.textures.TextureConfiguration
import dev.deftu.omnicore.internal.client.render.GlInternals

//#if MC >= 1.21.6
import com.mojang.blaze3d.textures.GpuTextureView
import dev.deftu.omnicore.internal.client.render.OmniGpuTextureView
//#endif

//#if MC >= 1.21.5
import dev.deftu.omnicore.internal.client.render.OmniGpuTexture
//#endif

public class ManagedFramebuffer @JvmOverloads public constructor(
    width: Int,
    height: Int,
    public val colorFormat: OmniTextureFormat,
    public val depthFormat: OmniTextureFormat,
    public val colorConfiguration: TextureConfiguration = TextureConfiguration.FRAMEBUFFER,
    public val depthConfiguration: TextureConfiguration = TextureConfiguration.FRAMEBUFFER,
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

    //#if MC >= 1.21.6
    override val vanillaColorTextureView: GpuTextureView by lazy {
        OmniGpuTextureView.framebuffer(this.vanillaColorTexture)
    }

    public val vanillaDepthStencilTextureView: GpuTextureView? by lazy {
        this.vanillaDepthStencilTexture?.let { OmniGpuTextureView.framebuffer(it) }
    }
    //#endif

    //#if MC >= 1.21.5
    override val vanillaColorTexture: OmniGpuTexture by lazy {
        OmniGpuTexture.framebuffer(handle = this.colorTexture, label = "Managed Framebuffer Color Texture")
    }

    public val vanillaDepthStencilTexture: OmniGpuTexture? by lazy {
        if (this.depthStencilTexture.format.isStencil) {
            return@lazy null
        }

        OmniGpuTexture.framebuffer(handle = this.depthStencilTexture, label = "Wrapped Framebuffer Depth-Stencil Texture")
    }
    //#endif

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
