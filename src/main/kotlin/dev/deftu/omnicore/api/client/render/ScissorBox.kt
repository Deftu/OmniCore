package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import kotlin.math.floor

public data class ScissorBox(val x: Int, val y: Int, val width: Int, val height: Int) {
    public val left: Int
        get() = x

    public val top: Int
        get() = y

    public val right: Int
        get() = x + width

    public val bottom: Int
        get() = y + height

    public val isEmpty: Boolean
        get() = width <= 0 || height <= 0

    public fun intersection(other: ScissorBox): ScissorBox? {
        val ix = maxOf(this.x, other.x)
        val iy = maxOf(this.y, other.y)
        val ix2 = minOf(this.x + this.width, other.x + other.width)
        val iy2 = minOf(this.y + this.height, other.y + other.height)
        val iw = ix2 - ix
        val ih = iy2 - iy
        return if (iw > 0 && ih > 0) ScissorBox(ix, iy, iw, ih) else null
    }

    public fun transformQuickly(stack: OmniMatrixStack): ScissorBox {
        val first = stack.transformPosition(left.toFloat(), top.toFloat())
        val second = stack.transformPosition(right.toFloat(), bottom.toFloat())

        val nx = floor(first.x).toInt()
        val ny = floor(first.y).toInt()
        val nw = floor(second.x - first.x).toInt()
        val nh = floor(second.y - first.y).toInt()
        return ScissorBox(nx, ny, nw, nh)
    }

    public fun transformEachVertex(stack: OmniMatrixStack): ScissorBox {
        val p1 = stack.transformPosition(left.toFloat(), top.toFloat())
        val p2 = stack.transformPosition(right.toFloat(), top.toFloat())
        val p3 = stack.transformPosition(left.toFloat(), bottom.toFloat())
        val p4 = stack.transformPosition(right.toFloat(), bottom.toFloat())

        val nx = floor(minOf(p1.x, p2.x, p3.x, p4.x)).toInt()
        val ny = floor(minOf(p1.y, p2.y, p3.y, p4.y)).toInt()
        val nx2 = floor(maxOf(p1.x, p2.x, p3.x, p4.x)).toInt()
        val ny2 = floor(maxOf(p1.y, p2.y, p3.y, p4.y)).toInt()
        return ScissorBox(nx, ny, nx2 - nx, ny2 - ny)
    }
}
