package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.render.stack.OmniPoseStack
import kotlin.math.ceil
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

    public fun transformQuickly(pose: OmniPoseStack): ScissorBox {
        val p1 = pose.transformPosition(left.toFloat(), top.toFloat())
        val p2 = pose.transformPosition(right.toFloat(), bottom.toFloat())

        val nx  = floor(minOf(p1.x, p2.x)).toInt()
        val ny  = floor(minOf(p1.y, p2.y)).toInt()
        val nx2 = ceil (maxOf(p1.x, p2.x)).toInt()
        val ny2 = ceil (maxOf(p1.y, p2.y)).toInt()
        return ScissorBox(nx, ny, nx2 - nx, ny2 - ny)
    }
}
