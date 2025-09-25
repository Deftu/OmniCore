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

@ApiStatus.Internal
public object FilledBoxShapeRenderer {
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
        fun emitFace(
            xA: Double, yA: Double, zA: Double,
            xB: Double, yB: Double, zB: Double,
            xC: Double, yC: Double, zC: Double,
            xD: Double, yD: Double, zD: Double,
        ) {
            vertexConsumer
                .vertex(matrices, xA, yA, zA)
                .color(color)
                .next()
            vertexConsumer
                .vertex(matrices, xB, yB, zB)
                .color(color)
                .next()
            vertexConsumer
                .vertex(matrices, xC, yC, zC)
                .color(color)
                .next()
            vertexConsumer
                .vertex(matrices, xD, yD, zD)
                .color(color)
                .next()
        }

        val x0 = (x + minOf(aabb.minX, aabb.maxX))
        val x1 = (x + maxOf(aabb.minX, aabb.maxX))
        val y0 = (y + minOf(aabb.minY, aabb.maxY))
        val y1 = (y + maxOf(aabb.minY, aabb.maxY))
        val z0 = (z + minOf(aabb.minZ, aabb.maxZ))
        val z1 = (z + maxOf(aabb.minZ, aabb.maxZ))

        // -X (west): normal (-1,0,0)
        emitFace(
            x0, y0, z1,
            x0, y1, z1,
            x0, y1, z0,
            x0, y0, z0,
        )

        // +X (east): normal (+1,0,0)
        emitFace(
            x1, y0, z0,
            x1, y1, z0,
            x1, y1, z1,
            x1, y0, z1,
        )

        // -Y (down): normal (0,-1,0)
        emitFace(
            x0, y0, z0,
            x0, y0, z1,
            x1, y0, z1,
            x1, y0, z0,
        )

        // +Y (up): normal (0,+1,0)
        emitFace(
            x0, y1, z1,
            x0, y1, z0,
            x1, y1, z0,
            x1, y1, z1,
        )

        // -Z (north): normal (0,0,-1)
        emitFace(
            x0, y0, z0,
            x0, y1, z0,
            x1, y1, z0,
            x1, y0, z0,
        )

        // +Z (south): normal (0,0,+1)
        emitFace(
            x1, y0, z1,
            x1, y1, z1,
            x0, y1, z1,
            x0, y0, z1,
        )
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
        val buffer = OmniBufferBuilders.create(DrawMode.QUADS, DefaultVertexFormats.POSITION_COLOR)
        render(buffer, matrices, aabb, x, y, z, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
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

    @JvmStatic
    public fun render(
        vertexConsumer: OmniVertexConsumer,
        matrices: OmniMatrixStack,
        aabb: OmniAABB,
        color: OmniColor
    ) {
        render(vertexConsumer, matrices, aabb, 0.0, 0.0, 0.0, color)
    }
}
