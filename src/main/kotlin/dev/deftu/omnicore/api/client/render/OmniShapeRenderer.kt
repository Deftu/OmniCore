package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.data.aabb.OmniAABB
import dev.deftu.omnicore.api.data.shape.OmniVoxelShape
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import dev.deftu.omnicore.api.math.OmniVector3f
import org.jetbrains.annotations.ApiStatus
import kotlin.math.abs

@ApiStatus.Experimental
public object OmniShapeRenderer {
    @JvmStatic
    public fun renderOutline(
        matrices: OmniMatrixStack,
        vertexConsumer: OmniVertexConsumer,
        shape: OmniVoxelShape,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        shape.forEachEdge { box ->
            val normalX = (box.maxX - box.minX).toFloat()
            val normalY = (box.maxY - box.minY).toFloat()
            val normalZ = (box.maxZ - box.minZ).toFloat()

            vertexConsumer
                .vertex(matrices, box.minX + x, box.minY + y, box.minZ + z)
                .color(color)
                .normal(matrices, normalX, normalY, normalZ)
                .next()
            vertexConsumer
                .vertex(matrices, box.maxX + x, box.minY + y, box.minZ + z)
                .color(color)
                .normal(matrices, normalX, normalY, normalZ)
                .next()
        }
    }

    @JvmStatic
    public fun renderOutline(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        shape: OmniVoxelShape,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.LINES, DefaultVertexFormats.POSITION_COLOR_NORMAL)
        renderOutline(matrices, buffer, shape, x, y, z, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }

    @JvmStatic
    public fun renderBox(
        matrices: OmniMatrixStack,
        vertexConsumer: OmniVertexConsumer,
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
    public fun renderBox(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        aabb: OmniAABB,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.LINES, DefaultVertexFormats.POSITION_COLOR_NORMAL)
        renderBox(matrices, buffer, aabb, x, y, z, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }

    @JvmStatic
    public fun renderFilledBox(
        matrices: OmniMatrixStack,
        vertexConsumer: OmniVertexConsumer,
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
    public fun renderFilledBox(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        aabb: OmniAABB,
        x: Double,
        y: Double,
        z: Double,
        color: OmniColor
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.QUADS, DefaultVertexFormats.POSITION_COLOR)
        renderFilledBox(matrices, buffer, aabb, x, y, z, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }

    @JvmStatic
    public fun renderVector(
        matrices: OmniMatrixStack,
        vertexConsumer: OmniVertexConsumer,
        origin: OmniVector3f,
        delta: OmniVec3d,
        color: OmniColor
    ) {
        val ox = origin.x.toDouble()
        val oy = origin.y.toDouble()
        val oz = origin.z.toDouble()
        val dx = delta.x
        val dy = delta.y
        val dz = delta.z

        // Vertex 1: at origin
        vertexConsumer.vertex(matrices, ox, oy, oz)
            .color(color)
            .normal(matrices, dx.toFloat(), dy.toFloat(), dz.toFloat())
            .next()

        // Vertex 2: at origin + delta
        vertexConsumer.vertex(matrices, ox + dx, oy + dy, oz + dz)
            .color(color)
            .normal(matrices, dx.toFloat(), dy.toFloat(), dz.toFloat())
            .next()
    }

    @JvmStatic
    public fun renderVector(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        origin: OmniVector3f,
        delta: OmniVec3d,
        color: OmniColor
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.LINES, DefaultVertexFormats.POSITION_COLOR_NORMAL)
        renderVector(matrices, buffer, origin, delta, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }
}
