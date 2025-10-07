package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.textures.GpuTextureView
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPass
import dev.deftu.omnicore.internal.client.render.vertex.OmniBuiltBufferImpl
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import dev.deftu.omnicore.internal.client.render.pipeline.RenderPassEncoderImpl
import net.minecraft.client.font.TextDrawable
import org.joml.Matrix4f

internal class ImmediateGlyphDrawer(private val matrix: Matrix4f) : TextRenderer.GlyphDrawer {
    private companion object {
        const val LIGHT = 0x00F0_00F0
    }

    private var renderPipeline: RenderPipeline? = null
    private var texture: GpuTextureView? = null
    private var buffer: BufferBuilder? = null

    fun flush() {
        if (buffer == null) {
            return
        }

        val cachedPipeline = renderPipeline
        val cachedTexture = texture
        val cachedBuffer = buffer
        renderPipeline = null
        texture = null
        buffer = null

        cachedBuffer!!.endNullable().use { builtBuffer ->
            builtBuffer ?: return
            val lightTexture = client.gameRenderer.lightmapTextureManager.glTextureView
            OmniRenderPass().use { renderPass ->
                renderPass.draw(
                    builtBuffer = OmniBuiltBufferImpl(builtBuffer),
                    pipeline = OmniRenderPipelines.wrap(cachedPipeline!!)
                ) { builder ->
                    (builder as RenderPassEncoderImpl).initialize()
                    builder.vanilla.bindSampler("Sampler0", cachedTexture)
                    builder.vanilla.bindSampler("Sampler2", lightTexture)
                }
            }
        }
    }

    override fun drawGlyph(renderable: TextDrawable) {
        if (renderable.textureView() == null) {
            return
        }

        if (renderable.pipeline != renderPipeline || renderable.textureView() != texture) {
            flush()
            renderPipeline = renderable.pipeline
            texture = renderable.textureView()
            buffer = Tessellator.getInstance().begin(renderPipeline!!.vertexFormatMode, renderPipeline!!.vertexFormat)
        }

        if (buffer != null) {
            renderable.render(matrix, buffer, LIGHT, false)
        }
    }

    override fun drawRectangle(renderable: TextDrawable) {
        if (buffer != null) {
            renderable.render(matrix, buffer, LIGHT, false)
        }
    }
}