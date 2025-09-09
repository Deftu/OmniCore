package dev.deftu.omnicore.api.color

public enum class ColorFormat(
    public val redShift: Int,
    public val greenShift: Int,
    public val blueShift: Int,
    public val alphaShift: Int
) {
    RGBA(24, 16, 8, 0),
    ARGB(16, 8, 0, 24),
    ABGR(0, 8, 16, 24);

    // Precomputed masks for each channel
    private val redMask = 0xFF shl redShift
    private val greenMask = 0xFF shl greenShift
    private val blueMask = 0xFF shl blueShift
    private val alphaMask = 0xFF shl alphaShift

    private val invRedMask = redMask.inv()
    private val invGreenMask = greenMask.inv()
    private val invBlueMask = blueMask.inv()
    private val invAlphaMask = alphaMask.inv()

    public fun red(color: Int): Int {
        return color and redMask ushr redShift
    }

    public fun green(color: Int): Int {
        return color and greenMask ushr greenShift
    }

    public fun blue(color: Int): Int {
        return color and blueMask ushr blueShift
    }

    public fun alpha(color: Int): Int {
        return color and alphaMask ushr alphaShift
    }

    public fun overwriteRed(color: Int, red: Int): Int {
        return (color and invRedMask) or ((red and 0xFF) shl redShift)
    }

    public fun overwriteGreen(color: Int, green: Int): Int {
        return (color and invGreenMask) or ((green and 0xFF) shl greenShift)
    }

    public fun overwriteBlue(color: Int, blue: Int): Int {
        return (color and invBlueMask) or ((blue and 0xFF) shl blueShift)
    }

    public fun overwriteAlpha(color: Int, alpha: Int): Int {
        return (color and invAlphaMask) or ((alpha and 0xFF) shl alphaShift)
    }

    public fun guaranteeAlpha(color: Int): Int {
        val alpha = alpha(color)
        return if (alpha == 0) overwriteAlpha(color, 0xFF) else color
    }

    public fun convertTo(other: ColorFormat, color: Int): Int {
        return other.pack(
            red(color),
            green(color),
            blue(color),
            alpha(color)
        )
    }

    public fun pack(red: Int, green: Int, blue: Int, alpha: Int = 0xFF): Int {
        return (red and 0xFF) shl redShift or
            (green and 0xFF) shl greenShift or
            (blue and 0xFF) shl blueShift or
            (alpha and 0xFF) shl alphaShift
    }

    public fun opaque(red: Int, green: Int, blue: Int): Int {
        return pack(red, green, blue, 0xFF)
    }

    public fun mix(first: Int, second: Int): Int {
        return pack(
            (red(first) + red(second)) / 2,
            (green(first) + green(second)) / 2,
            (blue(first) + blue(second)) / 2,
            (alpha(first) * alpha(second)) / 2
        )
    }

    public fun lerp(progress: Float, start: Int, end: Int): Int {
        return pack(
            lerp0(progress, red(start), red(end)),
            lerp0(progress, green(start), green(end)),
            lerp0(progress, blue(start), blue(end)),
            lerp0(progress, alpha(start), alpha(end))
        )
    }

    private fun lerp0(progress: Float, start: Int, end: Int): Int {
        val clampedProgress = progress.coerceIn(0f, 1f)
        return (start + (end - start) * clampedProgress).toInt()
    }
}
