package dev.deftu.omnicore.common

public object OmniColor {

    public object Rgba {

        @JvmStatic
        public fun asOpaque(color: Int): Int {
            return color or 0xFF000000.toInt()
        }

        @JvmStatic
        public fun withRed(color: Int, red: Int): Int {
            return red shl 24 or (color and 0x00FFFFFF)
        }

        @JvmStatic
        public fun withGreen(color: Int, green: Int): Int {
            return green shl 16 or (color and 0xFF00FFFF.toInt())
        }

        @JvmStatic
        public fun withBlue(color: Int, blue: Int): Int {
            return blue shl 8 or (color and 0xFFFF00FF.toInt())
        }

        @JvmStatic
        public fun withAlpha(color: Int, alpha: Int): Int {
            return alpha or (color and 0xFFFFFF00.toInt())
        }

        @JvmStatic
        public fun getRed(color: Int): Int {
            return color ushr 24
        }

        @JvmStatic
        public fun getGreen(color: Int): Int {
            return color shr 16 and 0xFF
        }

        @JvmStatic
        public fun getBlue(color: Int): Int {
            return color shr 8 and 0xFF
        }

        @JvmStatic
        public fun getAlpha(color: Int): Int {
            return color and 0xFF
        }

        @JvmStatic
        public fun mix(first: Int, second: Int): Int {
            return getRgba(
                (getRed(first) + getRed(second)) / 2,
                (getGreen(first) + getGreen(second)) / 2,
                (getBlue(first) + getBlue(second)) / 2,
                (getAlpha(first) + getAlpha(second)) / 2
            )
        }

        @JvmStatic
        public fun lerp(progress: Float, start: Int, end: Int): Int {
            val red = OmniColor.lerp(progress, getRed(start), getRed(end))
            val green = OmniColor.lerp(progress, getGreen(start), getGreen(end))
            val blue = OmniColor.lerp(progress, getBlue(start), getBlue(end))
            val alpha = OmniColor.lerp(progress, getAlpha(start), getAlpha(end))
            return getRgba(red, green, blue, alpha)
        }

        @JvmStatic
        public fun getRgba(red: Int, green: Int, blue: Int, alpha: Int): Int {
            return red shl 24 or (green shl 16) or (blue shl 8) or alpha
        }

        @JvmStatic
        public fun getRgb(red: Int, green: Int, blue: Int): Int {
            return (red shl 16) or (green shl 8) or blue
        }

    }

    public object Argb {

        @JvmStatic
        public fun asOpaque(color: Int): Int {
            return color or 0xFF000000.toInt()
        }

        @JvmStatic
        public fun withRed(color: Int, red: Int): Int {
            return red shl 16 or (color and 0xFF00FFFF.toInt())
        }

        @JvmStatic
        public fun withGreen(color: Int, green: Int): Int {
            return green shl 8 or (color and 0xFFFF00FF.toInt())
        }

        @JvmStatic
        public fun withBlue(color: Int, blue: Int): Int {
            return blue or (color and 0xFFFFFF00.toInt())
        }

        @JvmStatic
        public fun withAlpha(color: Int, alpha: Int): Int {
            return alpha shl 24 or (color and 0xFFFFFF)
        }

        @JvmStatic
        public fun getRed(color: Int): Int {
            return color shr 16 and 0xFF
        }

        @JvmStatic
        public fun getGreen(color: Int): Int {
            return color shr 8 and 0xFF
        }

        @JvmStatic
        public fun getBlue(color: Int): Int {
            return color and 0xFF
        }

        @JvmStatic
        public fun getAlpha(color: Int): Int {
            return color ushr 24
        }

        @JvmStatic
        public fun mix(first: Int, second: Int): Int {
            return getArgb(
                (getAlpha(first) * getAlpha(second)) / 2,
                (getRed(first) + getRed(second)) / 2,
                (getGreen(first) + getGreen(second)) / 2,
                (getBlue(first) + getBlue(second)) / 2
            )
        }

        @JvmStatic
        public fun lerp(progress: Float, start: Int, end: Int): Int {
            val alpha = OmniColor.lerp(progress, getAlpha(start), getAlpha(end))
            val red = OmniColor.lerp(progress, getRed(start), getRed(end))
            val green = OmniColor.lerp(progress, getGreen(start), getGreen(end))
            val blue = OmniColor.lerp(progress, getBlue(start), getBlue(end))
            return getArgb(alpha, red, green, blue)
        }

        @JvmStatic
        public fun getArgb(alpha: Int, red: Int, green: Int, blue: Int): Int {
            return alpha shl 24 or (red shl 16) or (green shl 8) or blue
        }

        @JvmStatic
        public fun getRgb(red: Int, green: Int, blue: Int): Int {
            return (red shl 16) or (green shl 8) or blue
        }

    }

    public object Abgr {

        @JvmStatic
        public fun asOpaque(color: Int): Int {
            return color or 0xFF000000.toInt()
        }

        @JvmStatic
        public fun withRed(color: Int, red: Int): Int {
            return red or (color and 0xFF00FFFF.toInt())
        }

        @JvmStatic
        public fun withGreen(color: Int, green: Int): Int {
            return green shl 8 or (color and 0xFFFF00FF.toInt())
        }

        @JvmStatic
        public fun withBlue(color: Int, blue: Int): Int {
            return blue shl 16 or (color and 0xFFFFFF00.toInt())
        }

        @JvmStatic
        public fun withAlpha(color: Int, alpha: Int): Int {
            return alpha shl 24 or (color and 0xFFFFFF)
        }

        @JvmStatic
        public fun getRed(color: Int): Int {
            return color and 0xFF
        }

        @JvmStatic
        public fun getGreen(color: Int): Int {
            return color shr 8 and 0xFF
        }

        @JvmStatic
        public fun getBlue(color: Int): Int {
            return color shr 16 and 0xFF
        }

        @JvmStatic
        public fun getAlpha(color: Int): Int {
            return color ushr 24
        }

        @JvmStatic
        public fun mix(first: Int, second: Int): Int {
            return getAbgr(
                (getAlpha(first) * getAlpha(second)) / 2,
                (getBlue(first) + getBlue(second)) / 2,
                (getGreen(first) + getGreen(second)) / 2,
                (getRed(first) + getRed(second)) / 2
            )
        }

        @JvmStatic
        public fun lerp(progress: Float, start: Int, end: Int): Int {
            val alpha = OmniColor.lerp(progress, getAlpha(start), getAlpha(end))
            val blue = OmniColor.lerp(progress, getBlue(start), getBlue(end))
            val green = OmniColor.lerp(progress, getGreen(start), getGreen(end))
            val red = OmniColor.lerp(progress, getRed(start), getRed(end))
            return getAbgr(alpha, blue, green, red)
        }

        @JvmStatic
        public fun getAbgr(alpha: Int, blue: Int, green: Int, red: Int): Int {
            return alpha shl 24 or (blue shl 16) or (green shl 8) or red
        }

        @JvmStatic
        public fun getBgr(blue: Int, green: Int, red: Int): Int {
            return (blue shl 16) or (green shl 8) or red
        }

    }

    private fun lerp(progress: Float, start: Int, end: Int): Int {
        return (start + (end - start) * progress).toInt()
    }

}
