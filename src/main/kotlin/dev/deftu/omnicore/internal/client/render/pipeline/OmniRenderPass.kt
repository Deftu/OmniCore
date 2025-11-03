package dev.deftu.omnicore.internal.client.render.pipeline

import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.RenderPassEncoder
import dev.deftu.omnicore.api.client.render.vertex.OmniMeshData
import java.util.function.Consumer

//#if MC < 1.21.5
//$$ import dev.deftu.omnicore.api.client.render.state.OmniRenderStates
//#endif

internal class OmniRenderPass : AutoCloseable {
    //#if MC < 1.21.5
    //$$ internal val prevRenderState = OmniRenderStates.current
    //$$ internal val activeRenderState = prevRenderState.copy()
    //$$ internal var activePipeline: OmniRenderPipeline? = null
    //#endif

    fun draw(builtBuffer: OmniMeshData, pipeline: OmniRenderPipeline, action: Consumer<RenderPassEncoder>) {
        val encoder = RenderPassEncoderImpl(
            //#if MC < 1.21.5
            //$$ renderPass = this,
            //#endif
            renderPipeline = pipeline as OmniRenderPipelineImpl,
            builtBuffer = builtBuffer
        )

        action.accept(encoder)
        encoder.submit()
    }

    override fun close() {
        //#if MC < 1.21.5
        //$$ prevRenderState.applyTo(activeRenderState)
        //#endif
    }
}
