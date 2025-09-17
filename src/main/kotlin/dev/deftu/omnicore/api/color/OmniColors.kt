package dev.deftu.omnicore.api.color

import kotlin.math.abs

public object OmniColors {
    @JvmField public val TRANSPARENT: OmniColor = OmniColor(0, 0, 0, 0)
    @JvmField public val WHITE: OmniColor = OmniColor(255, 255, 255)
    @JvmField public val LIGHT_GRAY: OmniColor = OmniColor(192, 192, 192)
    @JvmField public val GRAY: OmniColor = OmniColor(128, 128, 128)
    @JvmField public val DARK_GRAY: OmniColor = OmniColor(64, 64, 64)
    @JvmField public val BLACK: OmniColor = OmniColor(0, 0, 0)
    @JvmField public val RED: OmniColor = OmniColor(255, 0, 0)
    @JvmField public val PINK: OmniColor = OmniColor(255, 175, 175)
    @JvmField public val ORANGE: OmniColor = OmniColor(255, 200, 0)
    @JvmField public val YELLOW: OmniColor = OmniColor(255, 255, 0)
    @JvmField public val GREEN: OmniColor = OmniColor(0, 255, 0)
    @JvmField public val LIME: OmniColor = OmniColor(175, 255, 75)
    @JvmField public val MAGENTA: OmniColor = OmniColor(255, 0, 255)
    @JvmField public val CYAN : OmniColor = OmniColor(0, 255, 255)
    @JvmField public val BLUE: OmniColor = OmniColor(0, 0, 255)

    @JvmStatic
    public fun hsv(hue: Float, saturation: Float, value: Float): OmniColor {
        val h = (hue % 1.0f + 1.0f) % 1.0f * 6.0f
        val c = value * saturation
        val x = c * (1 - abs(h % 2 - 1))
        val m = value - c

        val (r1, g1, b1) = when (h.toInt()) {
            0 -> Triple(c, x, 0f)
            1 -> Triple(x, c, 0f)
            2 -> Triple(0f, c, x)
            3 -> Triple(0f, x, c)
            4 -> Triple(x, 0f, c)
            5 -> Triple(c, 0f, x)
            else -> Triple(0f, 0f, 0f) // Should never happen
        }

        val r = ((r1 + m) * 255).toInt().coerceIn(0, 255)
        val g = ((g1 + m) * 255).toInt().coerceIn(0, 255)
        val b = ((b1 + m) * 255).toInt().coerceIn(0, 255)

        return OmniColor(r, g, b)
    }
}
