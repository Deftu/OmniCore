package dev.deftu.omnicore.client.render.pipeline

import dev.deftu.omnicore.client.render.state.OmniManagedRenderState
import dev.deftu.omnicore.client.render.vertex.OmniBuiltBuffer
import java.util.function.Consumer

internal class OmniRenderPass : AutoCloseable {

    //#if MC < 1.21.5
    internal val prevRenderState = OmniManagedRenderState.active()
    internal val activeRenderState = prevRenderState
    internal var activePipeline: OmniRenderPipeline? = null
    //#endif

    fun draw(builtBuffer: OmniBuiltBuffer, pipeline: OmniRenderPipeline, builder: Consumer<RenderPassBuilder>) {
        val builderInstance = RenderPassBuilder(this, pipeline, builtBuffer)
        builder.accept(builderInstance)
        builderInstance.activate()
    }

    override fun close() {
        //#if MC < 1.21.5
        prevRenderState.apply(activeRenderState)
        //#endif
    }

}
