package dev.deftu.omnicore.api.client.framebuffer

import dev.deftu.omnicore.api.client.textures.AbstractGlTexture
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import dev.deftu.omnicore.api.client.textures.OmniTextures
import net.minecraft.client.gl.Framebuffer

//#if MC >= 1.21.5
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gl.GlBackend
import net.minecraft.client.texture.GlTexture
//#endif

public object OmniFramebuffers {
    @JvmStatic
    public fun create(
        width: Int,
        height: Int,
        colorFormat: OmniTextureFormat,
        depthFormat: OmniTextureFormat,
    ): OmniFramebuffer {
        return ManagedFramebuffer(width, height, colorFormat, depthFormat)
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
    public fun wrap(framebuffer: Framebuffer): OmniFramebuffer {
        //#if MC >= 1.21.5
        val backend = (RenderSystem.getDevice() as? GlBackend) ?: throw IllegalStateException("RenderSystem is not using a GL backend")
        val id = (framebuffer.colorAttachment as? GlTexture)?.getOrCreateFramebuffer(backend.bufferManager, framebuffer.depthAttachment)
            ?: throw IllegalStateException("Framebuffer does not have a valid GL texture ID")
        val colorTexture = framebuffer.colorAttachment
            ?: throw IllegalStateException("Framebuffer does not have a color attachment")
        val depthTexture = framebuffer.depthAttachment
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
            width = framebuffer.viewportWidth,
            height = framebuffer.viewportHeight,
            //#if MC >= 1.21.5
            colorTexture = OmniTextures.wrap(colorTexture) as AbstractGlTexture,
            depthStencilTexture = depthTexture?.let(OmniTextures::wrap) as AbstractGlTexture
            //#else
            //$$ colorTexture = OmniTextures.wrap(colorTexture, framebuffer.width, framebuffer.height, OmniTextureFormat.RGBA8),
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
