package dev.deftu.omnicore.internal.client.render.vertex

import dev.deftu.omnicore.api.client.render.vertex.OmniBuiltBuffer
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPass
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.client.render.pipeline.RenderPassBuilder
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer
import kotlin.use

@ApiStatus.Internal
public class OmniBuiltBufferImpl(override val vanilla: VanillaBuiltBuffer) : OmniBuiltBuffer {
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
        vanilla.close()
        //#elseif MC >= 1.19.2
        //$$ vanilla.release()
        //#else
        //$$ vanilla.reset()
        //$$ OmniBufferBuilder.bufferPool.add(vanilla)
        //#endif

        isClosed = true
    }

    override fun markClosed() {
        isClosed = true
        //#if MC < 1.19.2
        //$$ OmniBufferBuilder.bufferPool.add(vanilla)
        //#endif
    }
}
