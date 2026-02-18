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
                texture = OmniGpuTexture.framebuffer(handle, label),
                baseMipLevel = 0,
                mipLevels = 1
            )
        }

        public fun framebuffer(texture: OmniGpuTexture): OmniGpuTextureView {
            return OmniGpuTextureView(
                texture = texture,
                baseMipLevel = 0,
                mipLevels = 1
            )
        }
    }
}
