package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.data.shape.OmniVoxelShape
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public object OmniShapeRenderer {
    @JvmStatic
    public fun renderOutline(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        shape: OmniVoxelShape,
        x: Double,
        y: Double,
        z: Double,
        color: Int
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.LINES, DefaultVertexFormats.POSITION_COLOR_NORMAL)
        renderOutline(matrices, buffer, shape, x, y, z, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }

    @JvmStatic
    public fun renderOutline(
        matrices: OmniMatrixStack,
        bufferBuilder: OmniBufferBuilder,
        shape: OmniVoxelShape,
        x: Double,
        y: Double,
        z: Double,
        color: Int
    ) {
        shape.forEachEdge { box ->
            val normalX = (box.maxX - box.minX).toFloat()
            val normalY = (box.maxY - box.minY).toFloat()
            val normalZ = (box.maxZ - box.minZ).toFloat()

            bufferBuilder
                .vertex(matrices, box.minX + x, box.minY + y, box.minZ + z)
                .color(color)
                .normal(matrices, normalX, normalY, normalZ)
                .next()
            bufferBuilder
                .vertex(matrices, box.maxX + x, box.minY + y, box.minZ + z)
                .color(color)
                .normal(matrices, normalX, normalY, normalZ)
                .next()
        }
    }
}
