package dev.deftu.omnicore.internal.client.render.shape

import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.data.aabb.OmniAABB
import org.jetbrains.annotations.ApiStatus
import kotlin.math.abs

@ApiStatus.Internal
public object BoxOutlineShapeRenderer {
    @JvmStatic
    public fun render(
        vertexConsumer: OmniVertexConsumer,
        matrices: OmniMatrixStack,
        aabb: OmniAABB,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        fun normals(
            x1: Double, y1: Double, z1: Double,
            x2: Double, y2: Double, z2: Double
        ): Triple<Float, Float, Float> {
            val dx = x2 - x1; val dy = y2 - y1; val dz = z2 - z1
            // choose dominant axis and sign
            return when {
                abs(dx) >= abs(dy) && abs(dx) >= abs(dz) -> Triple(if (dx >= 0) 1f else -1f, 0f, 0f)
                abs(dy) >= abs(dx) && abs(dy) >= abs(dz) -> Triple(0f, if (dy >= 0) 1f else -1f, 0f)
                else -> Triple(0f, 0f, if (dz >= 0) 1f else -1f)
            }
        }

        val edges = arrayOf(
            // bottom
            doubleArrayOf(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.minY, aabb.minZ),
            doubleArrayOf(aabb.maxX, aabb.minY, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ),
            doubleArrayOf(aabb.maxX, aabb.minY, aabb.maxZ, aabb.minX, aabb.minY, aabb.maxZ),
            doubleArrayOf(aabb.minX, aabb.minY, aabb.maxZ, aabb.minX, aabb.minY, aabb.minZ),
            // top
            doubleArrayOf(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.minZ),
            doubleArrayOf(aabb.maxX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ),
            doubleArrayOf(aabb.maxX, aabb.maxY, aabb.maxZ, aabb.minX, aabb.maxY, aabb.maxZ),
            doubleArrayOf(aabb.minX, aabb.maxY, aabb.maxZ, aabb.minX, aabb.maxY, aabb.minZ),
            // verticals
            doubleArrayOf(aabb.minX, aabb.minY, aabb.minZ, aabb.minX, aabb.maxY, aabb.minZ),
            doubleArrayOf(aabb.maxX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.minZ),
            doubleArrayOf(aabb.maxX, aabb.minY, aabb.maxZ, aabb.maxX, aabb.maxY, aabb.maxZ),
            doubleArrayOf(aabb.minX, aabb.minY, aabb.maxZ, aabb.minX, aabb.maxY, aabb.maxZ),
        )

        for (edge in edges) {
            val x1 = edge[0] + x; val y1 = edge[1] + y; val z1 = edge[2] + z
            val x2 = edge[3] + x; val y2 = edge[4] + y; val z2 = edge[5] + z
            val (nx, ny, nz) = normals(x1, y1, z1, x2, y2, z2)

            vertexConsumer.vertex(matrices, x1, y1, z1).color(color).normal(matrices, nx, ny, nz).next()
            vertexConsumer.vertex(matrices, x2, y2, z2).color(color).normal(matrices, nx, ny, nz).next()
        }
    }

    @JvmStatic
    public fun render(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        aabb: OmniAABB,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.LINES, DefaultVertexFormats.POSITION_COLOR_NORMAL)
        render(buffer, matrices, aabb, x, y, z, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }

    @JvmStatic
    public fun render(
        vertexConsumer: OmniVertexConsumer,
        matrices: OmniMatrixStack,
        aabb: OmniAABB,
        color: OmniColor
    ) {
        render(vertexConsumer, matrices, aabb, 0.0, 0.0, 0.0, color)
    }

    @JvmStatic
    public fun render(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        aabb: OmniAABB,
        color: OmniColor
    ) {
        render(pipeline, matrices, aabb, 0.0, 0.0, 0.0, color)
    }
}
