package dev.deftu.omnicore.api.client.render.vertex

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import java.awt.Color

public interface OmniVertexConsumer {
    public fun vertex(stack: OmniMatrixStack, x: Double, y: Double, z: Double): OmniVertexConsumer
    public fun color(red: Int, green: Int, blue: Int, alpha: Int): OmniVertexConsumer
    public fun color(red: Float, green: Float, blue: Float, alpha: Float): OmniVertexConsumer
    public fun color(color: Color): OmniVertexConsumer
    public fun color(color: Int): OmniVertexConsumer
    public fun texture(u: Double, v: Double): OmniVertexConsumer
    public fun overlay(u: Int, v: Int): OmniVertexConsumer
    public fun light(u: Int, v: Int): OmniVertexConsumer
    public fun normal(stack: OmniMatrixStack, nx: Float, ny: Float, nz: Float): OmniVertexConsumer
    public fun next(): OmniVertexConsumer
}
