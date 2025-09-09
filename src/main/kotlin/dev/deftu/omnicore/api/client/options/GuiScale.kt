package dev.deftu.omnicore.api.client.options

//#if MC >= 1.16.5 && MC < 1.20.5
//$$ import dev.deftu.omnicore.api.client.client
//#endif

/**
 * Represents the GUI scales Minecraft provides, which force render operations to be scaled by a certain factor.
 *
 * @since 0.2.1
 * @author Deftu
 */
public enum class GuiScale {
    AUTO,
    SMALL,
    MEDIUM,
    LARGE,
    VERY_LARGE;

    /**
     * Sets the current GUI scale to this.
     *
     * @since 0.19.0
     * @author Deftu
     */
    public fun apply() {
        rawCurrentScale = ordinal
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<GuiScale> = values().toList()

        /**
         * The raw integer value of the current GUI scale as provided by the Minecraft client.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        public var rawCurrentScale: Int
            get() {
                val guiScale =
                    //#if MC >= 1.19.2
                    options.guiScale.value
                    //#else
                    //$$ options.guiScale
                    //#endif

                return guiScale
            }
            set(value) {
                //#if MC >= 1.19.2
                options.guiScale.value = value
                //#else
                //$$ options.guiScale = value
                //#endif

                //#if MC >= 1.16.5 && MC < 1.20.5
                //$$ val window = client.window
                //$$ val scaleFactor = window.calculateScaleFactor(value, client.forcesUnicodeFont())
                //$$ window.scaleFactor = scaleFactor.toDouble()
                //#endif
            }

        /**
         * The current GUI scale.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        public var currentScale: GuiScale
            get() = from(rawCurrentScale)
            set(value) { rawCurrentScale = value.ordinal }

        /**
         * Gets the GUI scale from an integer value.
         *
         * @since 0.2.1
         * @param value The integer value to get the GUI scale from.
         */
        @JvmStatic
        public fun from(value: Int): GuiScale {
            return ALL[value]
        }
    }
}