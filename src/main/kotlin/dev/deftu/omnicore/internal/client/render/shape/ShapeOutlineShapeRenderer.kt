package dev.deftu.omnicore.internal.client.render.shape

import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.data.shape.OmniVoxelShape
import dev.deftu.omnicore.api.math.OmniVector3f
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object ShapeOutlineShapeRenderer {
    @JvmStatic
    public fun render(
        vertexConsumer: OmniVertexConsumer,
        matrices: OmniMatrixStack,
        shape: OmniVoxelShape,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        shape.forEachEdge { box ->
            val startX = box.minX; val startY = box.minY; val startZ = box.minZ
            val endX = box.maxX; val endY = box.maxY; val endZ = box.maxZ

            val normal = OmniVector3f(
                (endX - startX).toFloat(),
                (endY - startY).toFloat(),
                (endZ - startZ).toFloat()
            ).normalized()

            vertexConsumer
                .vertex(matrices, startX + x, startY + y, startZ + z)
                .color(color)
                .normal(matrices, normal)
                .next()
            vertexConsumer
                .vertex(matrices, endX + x, endY + y, endZ + z)
                .color(color)
                .normal(matrices, normal)
                .next()
        }
    }

    @JvmStatic
    public fun render(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        shape: OmniVoxelShape,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.LINES, DefaultVertexFormats.POSITION_COLOR_NORMAL)
        render(buffer, matrices, shape, x, y, z, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }

    @JvmStatic
    public fun render(
        vertexConsumer: OmniVertexConsumer,
        matrices: OmniMatrixStack,
        shape: OmniVoxelShape,
        color: OmniColor
    ) {
        render(vertexConsumer, matrices, shape, 0.0, 0.0, 0.0, color)
    }

    @JvmStatic
    public fun render(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        shape: OmniVoxelShape,
        color: OmniColor
    ) {
        render(pipeline, matrices, shape, 0.0, 0.0, 0.0, color)
    }
}
