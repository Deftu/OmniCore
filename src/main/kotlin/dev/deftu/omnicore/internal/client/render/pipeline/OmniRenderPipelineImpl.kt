package dev.deftu.omnicore.internal.client.render.pipeline

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.shaders.ShaderType
import com.mojang.blaze3d.systems.RenderPass
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import net.minecraft.client.render.BuiltBuffer
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public class OmniRenderPipelineImpl(
    override val location: Identifier,
    override val vertexFormat: VertexFormat,
    override val vanilla: RenderPipeline,
    private val shaderSourcesFunction: ((Identifier, ShaderType) -> String?)?,
) : OmniRenderPipeline {
    internal fun draw(renderPass: RenderPass, builtBuffer: BuiltBuffer) {
        if (shaderSourcesFunction != null) {
            RenderSystem.getDevice()
                .precompilePipeline(vanilla, shaderSourcesFunction)
        }

        renderPass.setPipeline(vanilla)
        //#if MC >= 1.21.6
        renderPass.drawIndexed(0, 0, builtBuffer.drawParameters.comp_751, 1)
        //#else
        //$$ renderPass.drawIndexed(0, builtBuffer.drawParameters.comp_751)
        //#endif
    }

    override fun bind() {
        // no-op in 1.21.6+
    }

    override fun unbind() {
        // no-op in 1.21.6+
    }
}
