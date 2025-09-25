package dev.deftu.omnicore.api.client.render.vertex

import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.RenderPassEncoder
import dev.deftu.omnicore.internal.client.render.vertex.VanillaBuiltBuffer
import java.util.function.Consumer

public interface OmniBuiltBuffer : AutoCloseable {
    public val vanilla: VanillaBuiltBuffer

    public fun draw(pipeline: OmniRenderPipeline, builder: Consumer<RenderPassEncoder>)

    public fun draw(pipeline: OmniRenderPipeline, builder: RenderPassEncoder.() -> Unit) {
        draw(pipeline, Consumer {
            builder(it)
        })
    }

    public fun draw(pipeline: OmniRenderPipeline) {
        draw(pipeline, Consumer { })
    }

    public fun drawAndClose(pipeline: OmniRenderPipeline, builder: Consumer<RenderPassEncoder>) {
        use {
            draw(pipeline, builder)
        }
    }

    public fun drawAndClose(pipeline: OmniRenderPipeline, builder: RenderPassEncoder.() -> Unit) {
        drawAndClose(pipeline, Consumer {
            builder(it)
        })
    }

    public fun drawAndClose(pipeline: OmniRenderPipeline) {
        drawAndClose(pipeline, Consumer { })
    }

    public fun markClosed()
}
