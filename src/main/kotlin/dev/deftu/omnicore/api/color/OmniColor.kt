@file:JvmName("OmniColor")

package dev.deftu.omnicore.api.color

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deftu.omnicore.api.serialization.OmniCodecOps
import java.awt.Color

public data class OmniColor(
    public val format: ColorFormat,
    public val value: Int,
) {
    public companion object {
        @JvmField
        public val FORMAT_AWARE_CODEC: Codec<OmniColor> = RecordCodecBuilder.create { instance ->
            instance.group(
                ColorFormat.CODEC.fieldOf("format").forGetter(OmniColor::format),
                Codec.INT.fieldOf("value").forGetter(OmniColor::value)
            ).apply(instance, ::OmniColor)
        }

        @JvmField
        public val PACKED_CODEC: Codec<OmniColor> = Codec.INT.xmap(::OmniColor, OmniColor::pack)

        @JvmField
        public val CODEC: Codec<OmniColor> = OmniCodecOps.withAlternative(FORMAT_AWARE_CODEC, PACKED_CODEC)
    }

    @JvmOverloads
    public constructor(red: Int, green: Int, blue: Int, alpha: Int = 255)
            : this(ColorFormat.RGBA, ColorFormat.RGBA.pack(red, green, blue, alpha))

    @JvmOverloads
    public constructor(red: Float, green: Float, blue: Float, alpha: Float = 1.0f) : this(
        ColorFormat.RGBA,
        ColorFormat.RGBA.pack(
            (red * 255.0f).toInt().coerceIn(0, 255),
            (green * 255.0f).toInt().coerceIn(0, 255),
            (blue * 255.0f).toInt().coerceIn(0, 255),
            (alpha * 255.0f).toInt().coerceIn(0, 255)
        )
    )

    public constructor(value: Int) : this(ColorFormat.RGBA, value)

    public constructor(other: Color) : this(other.red, other.green, other.blue, other.alpha)

    public val red: Int get() = format.red(value)
    public val green: Int get() = format.green(value)
    public val blue: Int get() = format.blue(value)
    public val alpha: Int get() = format.alpha(value)

    public val awt: Color
        get() = Color(red, green, blue, alpha)

    public fun pack(target: ColorFormat = this.format): Int {
        return if (target == this.format) {
            value
        } else {
            format.convertTo(target, value)
        }
    }

    public fun convertTo(target: ColorFormat): OmniColor {
        return if (target == this.format) {
            this
        } else {
            OmniColor(target, format.convertTo(target, value))
        }
    }

    public fun asOpaque(): OmniColor {
        return withAlpha(0xFF)
    }

    public fun mix(other: OmniColor): OmniColor {
        val otherValue = other.pack(this.format)
        val newValue = format.mix(value, otherValue)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun mix(other: OmniColor, weight: Float): OmniColor {
        val otherValue = other.pack(this.format)
        val newValue = format.mix(value, otherValue, weight)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun invert(): OmniColor {
        val newValue = format.invert(value)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun grayscale(): OmniColor {
        val newValue = format.grayscale(value)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun lerp(progress: Float, other: OmniColor): OmniColor {
        val otherValue = other.pack(this.format)
        val newValue = format.lerp(progress, value, otherValue)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun deepCopy(): OmniColor {
        return OmniColor(format, value)
    }

    public fun withRed(red: Int): OmniColor {
        val newValue = format.overwriteRed(value, red)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun withGreen(green: Int): OmniColor {
        val newValue = format.overwriteGreen(value, green)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun withBlue(blue: Int): OmniColor {
        val newValue = format.overwriteBlue(value, blue)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    public fun withAlpha(alpha: Int): OmniColor {
        val newValue = format.overwriteAlpha(value, alpha)
        return if (newValue == value) this else OmniColor(format, newValue)
    }

    override fun toString(): String {
        return "OmniColor(format=$format, value=${String.format("#%08X", value)}, r=$red, g=$green, b=$blue, a=$alpha)"
    }
}
