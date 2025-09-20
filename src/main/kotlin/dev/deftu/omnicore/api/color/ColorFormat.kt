package dev.deftu.omnicore.api.color

import com.mojang.serialization.Codec
import dev.deftu.omnicore.api.math.OmniMath

public enum class ColorFormat(
    public val id: String,
    public val redShift: Int,
    public val greenShift: Int,
    public val blueShift: Int,
    public val alphaShift: Int
) {
    RGBA("rgba", 24, 16, 8, 0),
    ARGB("argb", 16, 8, 0, 24),
    ABGR("abgr", 0, 8, 16, 24),
    BGRA("bgra", 8, 16, 24, 0);

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
        return (color and redMask) ushr redShift
    }

    public fun green(color: Int): Int {
        return (color and greenMask) ushr greenShift
    }

    public fun blue(color: Int): Int {
        return (color and blueMask) ushr blueShift
    }

    public fun alpha(color: Int): Int {
        return (color and alphaMask) ushr alphaShift
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
        val r = ((red   and 0xFF) shl redShift)
        val g = ((green and 0xFF) shl greenShift)
        val b = ((blue  and 0xFF) shl blueShift)
        val a = ((alpha and 0xFF) shl alphaShift)
        return r or g or b or a
    }

    public fun opaque(red: Int, green: Int, blue: Int): Int {
        return pack(red, green, blue, 0xFF)
    }

    public fun mix(first: Int, second: Int): Int {
        return pack(
            (red(first) + red(second)) / 2,
            (green(first) + green(second)) / 2,
            (blue(first) + blue(second)) / 2,
            (alpha(first) + alpha(second)) / 2
        )
    }

    public fun mix(first: Int, second: Int, delta: Float): Int {
        val clampedDelta = delta.coerceIn(0f, 1f)
        return pack(
            OmniMath.lerp(red(first), red(second), clampedDelta),
            OmniMath.lerp(green(first), green(second), clampedDelta),
            OmniMath.lerp(blue(first), blue(second), clampedDelta),
            OmniMath.lerp(alpha(first), alpha(second), clampedDelta)
        )
    }

    public fun invert(color: Int): Int {
        return pack(
            0xFF - red(color),
            0xFF - green(color),
            0xFF - blue(color),
            alpha(color) // Preserve alpha
        )
    }

    public fun grayscale(color: Int): Int {
        val r = red(color)
        val g = green(color)
        val b = blue(color)
        val gray = ((r + g + b) / 3).coerceIn(0, 255)
        return pack(gray, gray, gray, alpha(color)) // Preserve alpha
    }

    public fun lerp(start: Int, end: Int, delta: Float): Int {
        return pack(
            OmniMath.lerp(red(start), red(end), delta),
            OmniMath.lerp(green(start), green(end), delta),
            OmniMath.lerp(blue(start), blue(end), delta),
            OmniMath.lerp(alpha(start), alpha(end), delta)
        )
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<ColorFormat> = ColorFormat.values().toList()

        @JvmField
        public val CODEC: Codec<ColorFormat> = Codec.STRING.xmap(
            {
                from(it) ?: throw IllegalArgumentException("Unknown color format id: $it")
            },
            { it.id }
        )

        @JvmStatic
        public fun from(id: String): ColorFormat? {
            return ALL.firstOrNull { it.id == id }
        }
    }
}
