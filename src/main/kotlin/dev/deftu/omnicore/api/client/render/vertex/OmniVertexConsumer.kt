package dev.deftu.omnicore.api.client.render.vertex

import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.math.OmniVector3f
import java.awt.Color

public interface OmniVertexConsumer {
    public fun vertex(pose: OmniPoseStack, x: Double, y: Double, z: Double): OmniVertexConsumer
    public fun color(red: Int, green: Int, blue: Int, alpha: Int): OmniVertexConsumer

    public fun color(red: Float, green: Float, blue: Float, alpha: Float): OmniVertexConsumer {
        return color(
            (red * 255).toInt().coerceIn(0, 255),
            (green * 255).toInt().coerceIn(0, 255),
            (blue * 255).toInt().coerceIn(0, 255),
            (alpha * 255).toInt().coerceIn(0, 255)
        )
    }

    public fun color(color: OmniColor): OmniVertexConsumer {
        return color(color.red, color.green, color.blue, color.alpha)
    }

    public fun color(color: Color): OmniVertexConsumer {
        return color(color.red, color.green, color.blue, color.alpha)
    }

    public fun color(color: Int): OmniVertexConsumer {
        return color(OmniColor(color))
    }

    public fun texture(u: Double, v: Double): OmniVertexConsumer
    public fun overlay(u: Int, v: Int): OmniVertexConsumer
    public fun light(u: Int, v: Int): OmniVertexConsumer
    public fun normal(pose: OmniPoseStack, nx: Float, ny: Float, nz: Float): OmniVertexConsumer

    public fun normal(pose: OmniPoseStack, normal: OmniVector3f): OmniVertexConsumer {
        return normal(pose, normal.x, normal.y, normal.z)
    }

    public fun next(): OmniVertexConsumer

    // Extra utility functions for common shapes

    public fun line(
        pose: OmniPoseStack,
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        color: OmniColor
    ): OmniVertexConsumer {
        return this
            .vertex(pose, x1, y1, 0.0).color(color).next()
            .vertex(pose, x2, y2, 0.0).color(color).next()
    }

    public fun line(
        pose: OmniPoseStack,
        x: Double,
        y: Double,
        length: Double,
        isHorizontal: Boolean,
        color: OmniColor
    ): OmniVertexConsumer {
        return if (isHorizontal) {
            line(pose, x, y, x + length, y, color)
        } else {
            line(pose, x, y, x, y + length, color)
        }
    }

    public fun triangle(
        pose: OmniPoseStack,
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        x3: Double,
        y3: Double,
        color: OmniColor
    ): OmniVertexConsumer {
        return this
            .vertex(pose, x1, y1, 0.0).color(color).next()
            .vertex(pose, x2, y2, 0.0).color(color).next()
            .vertex(pose, x3, y3, 0.0).color(color).next()
    }

    public fun quad(
        pose: OmniPoseStack,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        color: OmniColor
    ): OmniVertexConsumer {
        return this
            .vertex(pose, x, y, 0.0).color(color).next()
            .vertex(pose, x + width, y, 0.0).color(color).next()
            .vertex(pose, x + width, y + height, 0.0).color(color).next()
            .vertex(pose, x, y + height, 0.0).color(color).next()
    }

    public fun quad(
        pose: OmniPoseStack,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        color: OmniColor,
        uv: UV
    ): OmniVertexConsumer {
        return this
            .vertex(pose, x, y, 0.0).color(color).texture(uv.u0, uv.v0).next()
            .vertex(pose, x + width, y, 0.0).color(color).texture(uv.u1, uv.v0).next()
            .vertex(pose, x + width, y + height, 0.0).color(color).texture(uv.u1, uv.v1).next()
            .vertex(pose, x, y + height, 0.0).color(color).texture(uv.u0, uv.v1).next()
    }
}
