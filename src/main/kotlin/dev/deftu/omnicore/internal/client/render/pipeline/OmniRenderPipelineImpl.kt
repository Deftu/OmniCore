package dev.deftu.omnicore.internal.client.render.pipeline

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.shaders.ShaderType
import com.mojang.blaze3d.systems.RenderPass
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelineBuilder
import dev.deftu.omnicore.api.client.render.provider.ShaderProvider
import net.minecraft.client.render.BuiltBuffer
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class OmniRenderPipelineImpl(
    override val location: Identifier,
    override val drawMode: DrawMode,
    override val vertexFormat: VertexFormat,
    override val vanilla: RenderPipeline,
    private val shaderProvider: ShaderProvider?,
    private val shaderSourcesFunction: ((Identifier, ShaderType) -> String?)?,
) : OmniRenderPipeline {
    internal fun draw(renderPass: RenderPass, builtBuffer: BuiltBuffer) {
        if (shaderSourcesFunction != null) {
            RenderSystem.getDevice()
                .precompilePipeline(vanilla, shaderSourcesFunction)
        }

        renderPass.setPipeline(vanilla)
        //#if MC >= 1.21.6
        renderPass.drawIndexed(0, 0, builtBuffer.drawParameters.indexCount, 1)
        //#else
        //$$ renderPass.drawIndexed(0, builtBuffer.drawParameters.indexCount)
        //#endif
    }

    override fun bind() {
        // no-op in 1.21.6+
    }

    override fun unbind() {
        // no-op in 1.21.6+
    }

    override fun newBuilder(shaderProvider: ShaderProvider?): OmniRenderPipelineBuilder {
        if (shaderProvider == null) {
            return newBuilder()
        }

        return OmniRenderPipelineBuilder(location, vertexFormat, drawMode, shaderProvider)
    }

    override fun newBuilder(): OmniRenderPipelineBuilder {
        checkNotNull(shaderProvider) { "This pipeline was not created with a ShaderProvider, so it cannot be rebuilt." }
        return OmniRenderPipelineBuilder(location, vertexFormat, drawMode, shaderProvider)
    }
}
