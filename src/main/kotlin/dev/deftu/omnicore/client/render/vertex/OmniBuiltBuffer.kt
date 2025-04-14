package dev.deftu.omnicore.client.render.vertex

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPass
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.client.render.pipeline.RenderPassBuilder
import java.util.function.Consumer

public interface OmniBuiltBuffer : AutoCloseable {

    public val vanilla: VanillaBuiltBuffer

    public fun draw(pipeline: OmniRenderPipeline, builder: Consumer<RenderPassBuilder>)

    public fun drawWithCleanup(pipeline: OmniRenderPipeline, builder: Consumer<RenderPassBuilder>) {
        use {
            draw(pipeline, builder)
        }
    }

    public fun draw(pipeline: OmniRenderPipeline, builder: RenderPassBuilder.() -> Unit = {}) {
        draw(pipeline, Consumer {
            builder(it)
        })
    }

    public fun drawWithCleanup(pipeline: OmniRenderPipeline, builder: RenderPassBuilder.() -> Unit = {}) {
        drawWithCleanup(pipeline, Consumer {
            builder(it)
        })
    }

    public fun forceClose()

    public companion object {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun wrapping(vanilla: VanillaBuiltBuffer): OmniBuiltBuffer {
            return VanillaWrappingBuiltBuffer(vanilla)
        }

    }

}

internal class VanillaWrappingBuiltBuffer(override val vanilla: VanillaBuiltBuffer) : OmniBuiltBuffer {

    private var isClosed = false

    override fun draw(pipeline: OmniRenderPipeline, builder: Consumer<RenderPassBuilder>) {
        OmniRenderPass().use { renderPass ->
            renderPass.draw(this, pipeline, builder)
        }
    }

    override fun close() {
        if (isClosed) {
            return
        }

        //#if MC >= 1.21.1
        //$$ vanilla.close()
        //#elseif MC >= 1.19.2
        vanilla.release()
        //#else
        //$$ vanilla.reset()
        //#endif

        isClosed = true
    }

    override fun forceClose() {
        isClosed = true
    }

}
