package dev.deftu.omnicore.api.client.render.vertex

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.color.ColorFormat
import dev.deftu.omnicore.api.color.OmniColor
import java.awt.Color

public interface OmniVertexConsumer {
    public fun vertex(stack: OmniMatrixStack, x: Double, y: Double, z: Double): OmniVertexConsumer
    public fun color(red: Int, green: Int, blue: Int, alpha: Int): OmniVertexConsumer
    public fun color(red: Float, green: Float, blue: Float, alpha: Float): OmniVertexConsumer

    public fun color(color: OmniColor): OmniVertexConsumer {
        val color = color.convertTo(ColorFormat.ARGB)
        return color(color.red, color.green, color.blue, color.alpha)
    }

    public fun color(color: Color): OmniVertexConsumer {
        return color(color.red, color.green, color.blue, color.alpha)
    }

    public fun color(color: Int): OmniVertexConsumer
    public fun texture(u: Double, v: Double): OmniVertexConsumer
    public fun overlay(u: Int, v: Int): OmniVertexConsumer
    public fun light(u: Int, v: Int): OmniVertexConsumer
    public fun normal(stack: OmniMatrixStack, nx: Float, ny: Float, nz: Float): OmniVertexConsumer
    public fun next(): OmniVertexConsumer

    // Extra utility functions for common shapes

    public fun line(
        stack: OmniMatrixStack,
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        color: OmniColor
    ): OmniVertexConsumer {
        return this
            .vertex(stack, x1, y1, 0.0).color(color).next()
            .vertex(stack, x2, y2, 0.0).color(color).next()
    }

    public fun line(
        stack: OmniMatrixStack,
        x: Double,
        y: Double,
        length: Double,
        isHorizontal: Boolean,
        color: OmniColor
    ): OmniVertexConsumer {
        return if (isHorizontal) {
            line(stack, x, y, x + length, y, color)
        } else {
            line(stack, x, y, x, y + length, color)
        }
    }

    public fun triangle(
        stack: OmniMatrixStack,
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        x3: Double,
        y3: Double,
        color: OmniColor
    ): OmniVertexConsumer {
        return this
            .vertex(stack, x1, y1, 0.0).color(color).next()
            .vertex(stack, x2, y2, 0.0).color(color).next()
            .vertex(stack, x3, y3, 0.0).color(color).next()
    }

    public fun quad(
        stack: OmniMatrixStack,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        color: OmniColor
    ): OmniVertexConsumer {
        return this
            .vertex(stack, x, y, 0.0).color(color).next()
            .vertex(stack, x + width, y, 0.0).color(color).next()
            .vertex(stack, x + width, y + height, 0.0).color(color).next()
            .vertex(stack, x, y + height, 0.0).color(color).next()
    }

    public fun quad(
        stack: OmniMatrixStack,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        color: OmniColor,
        uv: UV
    ): OmniVertexConsumer {
        return this
            .vertex(stack, x, y, 0.0).color(color).texture(uv.u0, uv.v0).next()
            .vertex(stack, x + width, y, 0.0).color(color).texture(uv.u1, uv.v0).next()
            .vertex(stack, x + width, y + height, 0.0).color(color).texture(uv.u1, uv.v1).next()
            .vertex(stack, x, y + height, 0.0).color(color).texture(uv.u0, uv.v1).next()
    }
}
