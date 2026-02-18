package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.opengl.GlTexture
import com.mojang.blaze3d.textures.FilterMode
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle

public class OmniGpuTexture(
    public val handle: OmniTextureHandle,
    usage: Int,
    label: String,
    depthOrLayers: Int,
    mipLevels: Int,
) : GlTexture(
    //#if MC >= 1.21.6
    usage,
    //#endif
    label,
    handle.format.vanilla,
    handle.width,
    handle.height,
    //#if MC >= 1.21.6
    depthOrLayers,
    //#endif
    mipLevels,
    handle.id
) {
    public companion object {
        public const val COPY_DST: Int = 1
        public const val COPY_SRC: Int = 2
        public const val TEXTURE_BINDING: Int = 4
        public const val RENDER_ATTACHMENT: Int = 8
        public const val CUBEMAP_COMPATIBLE: Int = 16

        public fun framebuffer(
            handle: OmniTextureHandle,
            label: String,
        ): OmniGpuTexture {
            return OmniGpuTexture(
                handle = handle,
                usage = COPY_DST or TEXTURE_BINDING or RENDER_ATTACHMENT,
                label = label,
                depthOrLayers = 1,
                mipLevels = 1,
            ).apply {
                //#if MC < 1.21.11
                setTextureFilter(FilterMode.NEAREST, true)
                //#endif
            }
        }
    }

    private var isClosed = false

    override fun close() {
        this.handle.close()
        super.close()
        this.isClosed = true
    }

    override fun isClosed(): Boolean {
        return this.isClosed && super.isClosed()
    }
}
