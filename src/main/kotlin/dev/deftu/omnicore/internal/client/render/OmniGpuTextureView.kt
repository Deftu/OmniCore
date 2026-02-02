package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.opengl.GlTextureView
import com.mojang.blaze3d.textures.FilterMode
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle

public class OmniGpuTextureView(
    public val texture: OmniGpuTexture,
    baseMipLevel: Int,
    mipLevels: Int,
) : GlTextureView(
    texture,
    baseMipLevel,
    mipLevels,
) {
    public companion object {
        public fun framebuffer(
            handle: OmniTextureHandle,
            label: String,
        ): OmniGpuTextureView {
            return OmniGpuTextureView(
                texture = OmniGpuTexture(
                    handle = handle,
                    usage = OmniGpuTexture.COPY_DST or OmniGpuTexture.TEXTURE_BINDING or OmniGpuTexture.RENDER_ATTACHMENT,
                    label = label,
                    depthOrLayers = 1,
                    mipLevels = 1,
                ).apply {
                    //#if MC < 1.21.11
                    setTextureFilter(FilterMode.NEAREST, true)
                    //#endif
                },
                baseMipLevel = 0,
                mipLevels = 1
            )
        }
    }
}
