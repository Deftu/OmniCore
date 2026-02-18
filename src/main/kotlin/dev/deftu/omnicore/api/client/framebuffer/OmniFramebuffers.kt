package dev.deftu.omnicore.api.client.framebuffer

import com.mojang.blaze3d.pipeline.RenderTarget
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.textures.AbstractGlTexture
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.api.client.textures.TextureConfiguration

//#if MC >= 1.21.5
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.opengl.GlDevice
import com.mojang.blaze3d.opengl.GlTexture
//#endif

public object OmniFramebuffers {
    @JvmStatic
    public val main: OmniFramebuffer
        get() = wrap(client.mainRenderTarget)

    @JvmStatic
    public fun create(
        width: Int,
        height: Int,
        colorFormat: OmniTextureFormat,
        depthFormat: OmniTextureFormat,
        colorConfiguration: TextureConfiguration = TextureConfiguration.FRAMEBUFFER,
        depthConfiguration: TextureConfiguration = TextureConfiguration.FRAMEBUFFER,
    ): OmniFramebuffer {
        return ManagedFramebuffer(width, height, colorFormat, depthFormat, colorConfiguration, depthConfiguration)
    }

    @JvmStatic
    @JvmOverloads
    public fun wrap(
        id: Int,
        width: Int,
        height: Int,
        colorTexture: AbstractGlTexture,
        depthStencilTexture: AbstractGlTexture? = null,
    ): OmniFramebuffer {
        return WrappedFramebuffer(id, width, height, colorTexture, depthStencilTexture)
    }

    @JvmStatic
    public fun wrap(framebuffer: RenderTarget): OmniFramebuffer {
        //#if MC >= 1.21.5
        val backend = (RenderSystem.getDevice() as? GlDevice) ?: throw IllegalStateException("RenderSystem is not using a GL backend")
        val id = (framebuffer.colorTexture as? GlTexture)?.getFbo(backend.directStateAccess(), framebuffer.depthTexture)
            ?: throw IllegalStateException("Framebuffer does not have a valid GL texture ID")
        val colorTexture = framebuffer.colorTexture
            ?: throw IllegalStateException("Framebuffer does not have a color attachment")
        val depthTexture = framebuffer.depthTexture
        //#elseif MC >= 1.16.5
        //$$ val id = framebuffer.frameBufferId
        //$$ val colorTexture = framebuffer.colorTextureId
        //$$ val depthTexture = framebuffer.depthTextureId.takeIf { framebuffer.useDepth }
        //#else
        //$$ val id = framebuffer.framebufferObject
        //$$ val colorTexture = framebuffer.framebufferTexture
        //$$ val depthTexture = framebuffer.depthBuffer.takeIf { framebuffer.useDepth }
        //#endif
        return wrap(
            id = id,
            //#if MC >= 1.21.9
            width = framebuffer.width,
            height = framebuffer.height,
            //#else
            //$$ width = framebuffer.viewWidth,
            //$$ height = framebuffer.viewHeight,
            //#endif
            //#if MC >= 1.21.5
            colorTexture = OmniTextures.wrap(colorTexture) as AbstractGlTexture,
            depthStencilTexture = depthTexture?.let(OmniTextures::wrap) as AbstractGlTexture
            //#else
            //$$ colorTexture = OmniTextures.wrap(colorTexture, framebuffer.width, framebuffer.height, OmniTextureFormat.RGBA8) as AbstractGlTexture,
            //$$ depthStencilTexture = if (depthTexture != null) {
            //$$     OmniTextures.wrap(
            //$$         depthTexture,
            //$$         framebuffer.width,
            //$$         framebuffer.height,
                        //#if FORGE
                        //$$ if (framebuffer.isStencilEnabled) OmniTextureFormat.DEPTH24_STENCIL8 else OmniTextureFormat.DEPTH32
                        //#else
                        //$$ OmniTextureFormat.DEPTH32
                        //#endif
            //$$    ) as AbstractGlTexture
            //$$ } else {
            //$$     null
            //$$ }
            //#endif
        )
    }
}
