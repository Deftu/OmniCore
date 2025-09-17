package dev.deftu.omnicore.api.color

import com.mojang.serialization.Codec
import kotlin.math.roundToInt

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

    public fun mix(first: Int, second: Int, weight: Float): Int {
        val clampedWeight = weight.coerceIn(0f, 1f)
        return pack(
            lerp0(clampedWeight, red(first), red(second)),
            lerp0(clampedWeight, green(first), green(second)),
            lerp0(clampedWeight, blue(first), blue(second)),
            lerp0(clampedWeight, alpha(first), alpha(second))
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
        return (start + (end - start) * clampedProgress).roundToInt()
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
