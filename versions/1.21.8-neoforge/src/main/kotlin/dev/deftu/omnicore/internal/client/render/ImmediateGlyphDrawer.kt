package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.textures.GpuTextureView
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPass
import dev.deftu.omnicore.internal.client.render.pipeline.RenderPassEncoderImpl
import net.minecraft.client.gui.font.glyphs.BakedGlyph
import net.minecraft.client.gui.Font
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.Tesselator
import dev.deftu.omnicore.internal.client.render.vertex.OmniMeshDataImpl
import org.joml.Matrix4f

internal class ImmediateGlyphDrawer(private val matrix: Matrix4f) : Font.GlyphVisitor {
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

        cachedBuffer!!.build().use { builtBuffer ->
            builtBuffer ?: return
            val lightTexture = client.gameRenderer.lightTexture().textureView
            OmniRenderPass().use { renderPass ->
                renderPass.draw(
                    builtBuffer = OmniMeshDataImpl(builtBuffer),
                    pipeline = OmniRenderPipelines.wrap(cachedPipeline!!)
                ) { builder ->
                    (builder as RenderPassEncoderImpl).initialize()
                    builder.vanilla.bindSampler("Sampler0", cachedTexture)
                    builder.vanilla.bindSampler("Sampler2", lightTexture)
                }
            }
        }
    }

    override fun acceptGlyph(drawnGlyph: BakedGlyph.GlyphInstance) {
        val baked = drawnGlyph.glyph
        if (baked.textureView() == null) {
            return
        }

        if (baked.guiPipeline() != renderPipeline || baked.textureView() != texture) {
            flush()
            renderPipeline = baked.guiPipeline()
            texture = baked.textureView()
            buffer = Tesselator.getInstance().begin(renderPipeline!!.vertexFormatMode, renderPipeline!!.vertexFormat)
        }

        baked.renderChar(drawnGlyph, matrix, buffer, LIGHT, false)
    }

    override fun acceptEffect(bakedGlyph: BakedGlyph, rectangle: BakedGlyph.Effect) {
        if (bakedGlyph.textureView() == null) {
            return
        }

        bakedGlyph.renderEffect(rectangle, matrix, buffer, LIGHT, false)
    }
}