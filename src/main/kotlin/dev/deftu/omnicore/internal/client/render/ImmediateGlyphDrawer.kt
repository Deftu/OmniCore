package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.textures.GpuTextureView
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPass
import dev.deftu.omnicore.internal.client.render.pipeline.RenderPassEncoderImpl
import dev.deftu.omnicore.internal.client.render.vertex.OmniBuiltBufferImpl
import net.minecraft.client.font.BakedGlyph
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
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

    override fun drawGlyph(drawnGlyph: BakedGlyph.DrawnGlyph) {
        val baked = drawnGlyph.comp_3316
        if (baked.texture == null) {
            return
        }

        if (baked.pipeline != renderPipeline || baked.texture != texture) {
            flush()
            renderPipeline = baked.pipeline
            texture = baked.texture
            buffer = Tessellator.getInstance().begin(renderPipeline!!.vertexFormatMode, renderPipeline!!.vertexFormat)
        }

        baked.draw(drawnGlyph, matrix, buffer, LIGHT, false)
    }

    override fun drawRectangle(bakedGlyph: BakedGlyph, rectangle: BakedGlyph.Rectangle) {
        if (bakedGlyph.texture == null) {
            return
        }

        bakedGlyph.drawRectangle(rectangle, matrix, buffer, LIGHT, false)
    }
}