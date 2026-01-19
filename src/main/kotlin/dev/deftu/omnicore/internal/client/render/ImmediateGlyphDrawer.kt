package dev.deftu.omnicore.internal.client.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.textures.GpuTextureView
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPass
import dev.deftu.omnicore.internal.client.render.vertex.OmniMeshDataImpl
import net.minecraft.client.gui.Font
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.Tesselator
import dev.deftu.omnicore.internal.client.render.pipeline.RenderPassEncoderImpl
import net.minecraft.client.gui.font.TextRenderable
import org.joml.Matrix4f

//#if MC >= 1.21.11
//$$ import com.mojang.blaze3d.systems.RenderSystem
//$$ import com.mojang.blaze3d.textures.FilterMode
//#endif

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
            //#if MC >= 26.1
            //$$ val lightTexture = client.gameRenderer.levelLightmap()
            //#else
            val lightTexture = client.gameRenderer.lightTexture().textureView
            //#endif
            OmniRenderPass().use { renderPass ->
                renderPass.draw(
                    builtBuffer = OmniMeshDataImpl(builtBuffer),
                    pipeline = OmniRenderPipelines.wrap(cachedPipeline!!)
                ) { builder ->
                    (builder as RenderPassEncoderImpl).initialize()

                    //#if MC >= 1.21.11
                    //$$ fun sampler(mode: FilterMode) = RenderSystem.getSamplerCache().getRepeat(mode)
                    //$$ builder.vanilla.bindTexture("Sampler0", cachedTexture, sampler(FilterMode.NEAREST))
                    //$$ builder.vanilla.bindTexture("Sampler2", lightTexture, sampler(FilterMode.LINEAR))
                    //#else
                    builder.vanilla.bindSampler("Sampler0", cachedTexture)
                    builder.vanilla.bindSampler("Sampler2", lightTexture)
                    //#endif
                }
            }
        }
    }

    override fun acceptGlyph(
        //#if MC >= 1.21.11
        //$$ renderable: TextRenderable.Styled
        //#else
        renderable: TextRenderable
        //#endif
    ) {
        if (renderable.textureView() == null) {
            return
        }

        if (renderable.guiPipeline() != renderPipeline || renderable.textureView() != texture) {
            flush()
            renderPipeline = renderable.guiPipeline()
            texture = renderable.textureView()
            buffer = Tesselator.getInstance().begin(renderPipeline!!.vertexFormatMode, renderPipeline!!.vertexFormat)
        }

        buffer?.let { renderable.render(matrix, it, LIGHT, false) }
    }

    override fun acceptEffect(renderable: TextRenderable) {
        buffer?.let { renderable.render(matrix, it, LIGHT, false) }
    }
}