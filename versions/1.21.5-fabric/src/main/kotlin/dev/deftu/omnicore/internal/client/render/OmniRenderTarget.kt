package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.pipeline.RenderTarget
import dev.deftu.omnicore.api.client.framebuffer.ManagedFramebuffer
import dev.deftu.omnicore.api.client.framebuffer.OmniFramebuffer
import dev.deftu.omnicore.api.client.framebuffer.WrappedFramebuffer

public class OmniRenderTarget(framebuffer: OmniFramebuffer) : RenderTarget(framebuffer.vanillaColorTexture.label, true) {
    init {
        this.colorTexture = framebuffer.vanillaColorTexture
        this.depthTexture = when (framebuffer) {
            is ManagedFramebuffer -> framebuffer.vanillaDepthStencilTexture
            is WrappedFramebuffer -> framebuffer.vanillaDepthStencilTexture
            else -> null
        }
    }
}
