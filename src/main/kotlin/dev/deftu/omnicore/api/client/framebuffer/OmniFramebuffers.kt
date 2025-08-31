package dev.deftu.omnicore.api.client.framebuffer

import com.mojang.blaze3d.systems.RenderSystem
import dev.deftu.omnicore.api.client.textures.AbstractGlTexture
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import dev.deftu.omnicore.api.client.textures.OmniTextures
import net.minecraft.client.gl.Framebuffer
import net.minecraft.client.gl.GlBackend
import net.minecraft.client.texture.GlTexture

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
        //#endif
        return wrap(
            id = id,
            width = framebuffer.viewportWidth,
            height = framebuffer.viewportHeight,
            colorTexture = OmniTextures.wrap(colorTexture),
            depthStencilTexture = framebuffer.depthAttachment?.let(OmniTextures::wrap)
        )
    }
}
