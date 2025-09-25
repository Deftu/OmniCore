package dev.deftu.omnicore.internal.client.render.shape

import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import dev.deftu.omnicore.api.math.OmniVector3f
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public object VectorShapeRenderer {
    @JvmStatic
    public fun render(
        vertexConsumer: OmniVertexConsumer,
        matrices: OmniMatrixStack,
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
    public fun render(
        pipeline: OmniRenderPipeline,
        matrices: OmniMatrixStack,
        origin: OmniVector3f,
        delta: OmniVec3d,
        color: OmniColor
    ) {
        val buffer = OmniBufferBuilders.create(DrawMode.LINES, DefaultVertexFormats.POSITION_COLOR_NORMAL)
        render(buffer, matrices, origin, delta, color)
        buffer.buildOrNull()?.drawAndClose(pipeline)
    }
}
