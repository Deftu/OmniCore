package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.internal.client.render.state.ColorMaskStateInternals

public data class OmniColorMask(
    val red: Boolean,
    val green: Boolean,
    val blue: Boolean,
    val alpha: Boolean,
) : TrackedState<OmniColorMask> {
    override var prevState: OmniColorMask? = null
        private set

    override fun submit(saveLast: Boolean) {
        if (saveLast) {
            prevState = OmniRenderStates.colorMask
        }

        ColorMaskStateInternals.configure(red, green, blue, alpha)
    }

    public fun withRed(red: Boolean): OmniColorMask {
        return if (this.red == red) this else copy(red = red)
    }

    public fun withGreen(green: Boolean): OmniColorMask {
        return if (this.green == green) this else copy(green = green)
    }

    public fun withBlue(blue: Boolean): OmniColorMask {
        return if (this.blue == blue) this else copy(blue = blue)
    }

    public fun withAlpha(alpha: Boolean): OmniColorMask {
        return if (this.alpha == alpha) this else copy(alpha = alpha)
    }

    public companion object {
        @JvmField public val DEFAULT: OmniColorMask = OmniColorMask(red = true, green = true, blue = true, alpha = true)
        @JvmField public val NONE: OmniColorMask = OmniColorMask(red = false, green = false, blue = false, alpha = false)
    }
}
