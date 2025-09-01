package dev.deftu.omnicore.api.client.render.vertex

import dev.deftu.omnicore.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.client.render.pipeline.RenderPassBuilder
import dev.deftu.omnicore.internal.client.render.vertex.VanillaBuiltBuffer
import java.util.function.Consumer

public interface OmniBuiltBuffer : AutoCloseable {
    public val vanilla: VanillaBuiltBuffer

    public fun draw(pipeline: OmniRenderPipeline, builder: Consumer<RenderPassBuilder>)

    public fun draw(pipeline: OmniRenderPipeline, builder: RenderPassBuilder.() -> Unit = {}) {
        draw(pipeline, Consumer {
            builder(it)
        })
    }

    public fun drawAndClose(pipeline: OmniRenderPipeline, builder: Consumer<RenderPassBuilder>) {
        use {
            draw(pipeline, builder)
        }
    }

    public fun drawAndClose(pipeline: OmniRenderPipeline, builder: RenderPassBuilder.() -> Unit = {}) {
        drawAndClose(pipeline, Consumer {
            builder(it)
        })
    }

    public fun markClosed()
}
