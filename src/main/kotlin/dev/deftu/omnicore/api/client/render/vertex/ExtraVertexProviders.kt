@file:JvmName("ExtraVertexProviders")
@file:ApiStatus.Experimental

package dev.deftu.omnicore.api.client.render.vertex

import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.math.OmniVector4f
import org.jetbrains.annotations.ApiStatus
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

@JvmOverloads
public fun OmniVertexConsumer.roundedQuad(
    stack: OmniMatrixStack,
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    color: OmniColor,
    radii: OmniVector4f,
    segmentScale: Double = 0.25
): OmniVertexConsumer {
    val (tl, tr, br, bl) = clampRadii(width, height, radii)
    val innerLeft = x + max(tl, bl)
    val innerRight = x + width - max(tr, br)
    val innerTop = y + max(tl, tr)
    val innerBottom = y + height - max(bl, br)

    // Center quad
    if (innerLeft < innerRight && innerTop < innerBottom) {
        emitTriangleQuad(stack, innerLeft, innerTop, innerRight, innerBottom, color)
    }


    // Edge strips
    if (tr > 0f || tl > 0f) {
        if (x + tl < x + width - tr && y < innerTop) {
            emitTriangleQuad(stack, x + tl, y, x + width - tr, innerTop, color)
        }
    } else {
        if (y < innerTop) {
            emitTriangleQuad(stack, x, y, x + width, innerTop, color)
        }
    }

    if (br > 0f || bl > 0f) {
        if (x + bl < x + width - br && y + height > innerBottom) {
            emitTriangleQuad(stack, x + bl, innerBottom, x + width - br, y + height, color)
        }
    } else {
        if (y + height > innerBottom) {
            emitTriangleQuad(stack, x, innerBottom, x + width, y + height, color)
        }
    }

    if (tl > 0f || bl > 0f) {
        if (y + tl < y + height - bl && x < innerLeft) {
            emitTriangleQuad(stack, x, y + tl, innerLeft, y + height - bl, color)
        }
    } else {
        if (x < innerLeft) {
            emitTriangleQuad(stack, x, y, innerLeft, y + height, color)
        }
    }

    if (tr > 0f || br > 0f) {
        if (y + tr < y + height - br && x + width > innerRight) {
            emitTriangleQuad(stack, innerRight, y + tr, x + width, y + height - br, color)
        }
    } else {
        if (x + width > innerRight) {
            emitTriangleQuad(stack, innerRight, y, x + width, y + height, color)
        }
    }

    // Corner arcs
    if (tl > 0f) {
        val cx = x + tl
        val cy = y + tl
        emitCornerArc(stack, cx, cy, tl, PI, 1.5 * PI, color, quadSegmentsFor(tl, segmentScale))
    }

    if (tr > 0f) {
        val cx = x + width - tr
        val cy = y + tr
        emitCornerArc(stack, cx, cy, tr, 1.5 * PI, 2.0 * PI, color, quadSegmentsFor(tr, segmentScale))
    }

    if (br > 0f) {
        val cx = x + width - br
        val cy = y + height - br
        emitCornerArc(stack, cx, cy, br, 0.0, 0.5 * PI, color, quadSegmentsFor(br, segmentScale))
    }

    if (bl > 0f) {
        val cx = x + bl
        val cy = y + height - bl
        emitCornerArc(stack, cx, cy, bl, 0.5 * PI, PI, color, quadSegmentsFor(bl, segmentScale))
    }

    return this
}

@JvmOverloads
public fun OmniVertexConsumer.roundedQuad(
    stack: OmniMatrixStack,
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    color: OmniColor,
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomRightRadius: Float,
    bottomLeftRadius: Float,
    segmentScale: Double = 0.25
): OmniVertexConsumer {
    return roundedQuad(
        stack,
        x, y,
        width, height,
        color,
        OmniVector4f(
            topLeftRadius,
            topRightRadius,
            bottomRightRadius,
            bottomLeftRadius
        ),
        segmentScale
    )
}

@JvmOverloads
public fun OmniVertexConsumer.roundedQuad(
    stack: OmniMatrixStack,
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    color: OmniColor,
    radius: Float,
    segmentScale: Double = 0.25
): OmniVertexConsumer {
    return roundedQuad(
        stack,
        x, y,
        width, height,
        color,
        OmniVector4f(radius, radius, radius, radius),
        segmentScale
    )
}

@JvmOverloads
public fun OmniVertexConsumer.circle(
    stack: OmniMatrixStack,
    cx: Double,
    cy: Double,
    radius: Float,
    color: OmniColor,
    segmentScale: Double = 0.8
): OmniVertexConsumer {
    val segments = circleSegmentsFor(radius, segmentScale)

    var prevX = cx + radius
    var prevY = cy
    val step = (2.0 * PI) / segments.toDouble()
    for (i in 1..segments) {
        val angle = step * i
        val x = cx + radius * cos(angle)
        val y = cy + radius * sin(angle)

        this
            .vertex(stack, cx, cy, 0.0).color(color).next()
            .vertex(stack, prevX, prevY, 0.0).color(color).next()
            .vertex(stack, x, y, 0.0).color(color).next()
        prevX = x
        prevY = y
    }

    return this
}

private fun OmniVertexConsumer.emitTriangleQuad(
    stack: OmniMatrixStack,
    x1: Double, y1: Double,
    x2: Double, y2: Double,
    color: OmniColor
) {
    // Triangle 1
    this
        .vertex(stack, x1, y1, 0.0).color(color).next()
        .vertex(stack, x2, y1, 0.0).color(color).next()
        .vertex(stack, x1, y2, 0.0).color(color).next()

    // Triangle 2
    this
        .vertex(stack, x2, y1, 0.0).color(color).next()
        .vertex(stack, x2, y2, 0.0).color(color).next()
        .vertex(stack, x1, y2, 0.0).color(color).next()
}

private fun OmniVertexConsumer.emitCornerArc(
    stack: OmniMatrixStack,
    cx: Double,
    cy: Double,
    radius: Float,
    startAngle: Double,
    endAngle: Double,
    color: OmniColor,
    segments: Int
) {
    var prevX = cx + radius * cos(startAngle)
    var prevY = cy + radius * sin(startAngle)
    val step = (endAngle - startAngle) / segments.toDouble()
    for (i in 1..segments) {
        val angle = startAngle + step * i
        val x = cx + radius * cos(angle)
        val y = cy + radius * sin(angle)

        this
            .vertex(stack, cx, cy, 0.0).color(color).next()
            .vertex(stack, prevX, prevY, 0.0).color(color).next()
            .vertex(stack, x, y, 0.0).color(color).next()

        prevX = x
        prevY = y
    }
}

private fun clampRadii(
    width: Double,
    height: Double,
    radii: OmniVector4f
): OmniVector4f {
    val max = min(width, height).toFloat() / 2f
    val tl = radii.x.coerceIn(0f, max)
    val tr = radii.y.coerceIn(0f, max)
    val br = radii.z.coerceIn(0f, max)
    val bl = radii.w.coerceIn(0f, max)

    fun scale(a: Float, b: Float, limit: Float): Pair<Float, Float> {
        val s = a + b
        if (s <= limit || s <= 0f) {
            return a to b
        }

        val k = limit / s
        return a * k to b * k
    }

    val (wtl, wtr) = scale(tl, tr, width.toFloat())
    val (wbr, wbl) = scale(br, bl, width.toFloat())
    val (htl, hbl) = scale(wtl, wbl, height.toFloat())
    val (htr, hbr) = scale(wtr, wbr, height.toFloat())
    return OmniVector4f(htl, htr, hbr, hbl)
}

private fun quadSegmentsFor(radius: Float, scale: Double): Int {
    val s = ceil(radius * scale).toInt()
    if (s < 1) {
        return 1
    }

    return s
}

private fun circleSegmentsFor(radius: Float, scale: Double): Int {
    val s = ceil(radius * scale).toInt()
    return s.coerceIn(8, 720) // Prevents outrageously high segment counts
}
