package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.textures.GpuTexture
import com.mojang.blaze3d.textures.GpuTextureView
import com.mojang.blaze3d.textures.TextureFormat

public class TextureAllocation(
    public val width: Int,
    public val height: Int
) : AutoCloseable {
    private val device = RenderSystem.getDevice()

    public val colorTexture: GpuTexture = device.createTexture(
        { "Pre-rendered texture" },
        GpuTexture.USAGE_RENDER_ATTACHMENT or GpuTexture.USAGE_TEXTURE_BINDING or GpuTexture.USAGE_COPY_DST,
        TextureFormat.RGBA8,
        width, height,
        1, 1
    ).apply {
        //#if MC < 1.21.11
        setTextureFilter(FilterMode.NEAREST, false)
        //#endif
    }

    public val colorTextureView: GpuTextureView = device.createTextureView(colorTexture)

    public val depthTexture: GpuTexture = device.createTexture(
        { "Pre-rendered depth texture" },
        GpuTexture.USAGE_RENDER_ATTACHMENT or GpuTexture.USAGE_COPY_DST,
        TextureFormat.DEPTH32,
        width, height,
        1, 1
    )

    public val depthTextureView: GpuTextureView = device.createTextureView(depthTexture)

    override fun close() {
        depthTextureView.close()
        colorTextureView.close()
        depthTexture.close()
        colorTexture.close()
    }
}
