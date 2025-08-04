package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.OmniClient

/**
 * Represents the GUI scales Minecraft provides, which force render operations to be scaled by a certain factor.
 *
 * @since 0.2.1
 * @author Deftu
 */
@GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
    public fun apply() {
        rawCurrentScale = ordinal
    }

    public companion object {

        @JvmField
        @GameSide(Side.BOTH)
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<GuiScale> = GuiScale.values().toList()

        /**
         * The raw integer value of the current GUI scale as provided by the Minecraft client.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public var rawCurrentScale: Int
            get() {
                val guiScale =
                    //#if MC >= 1.19.2
                    OmniClient.getInstance().options.guiScale.value
                    //#else
                    //$$ OmniClient.getInstance().options.guiScale
                    //#endif

                return guiScale
            }
            set(value) {
                //#if MC >= 1.19.2
                OmniClient.getInstance().options.guiScale.value = value
                //#else
                //$$ OmniClient.getInstance().options.guiScale = value
                //#endif

                //#if MC >= 1.15.2 && MC < 1.20.5
                //$$ val client = OmniClient.getInstance()
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
        @GameSide(Side.CLIENT)
        public var currentScale: GuiScale
            get() = fromInt(rawCurrentScale)
            set(value) { rawCurrentScale = value.ordinal }

        /**
         * Gets the GUI scale from an integer value.
         *
         * @since 0.2.1
         * @param value The integer value to get the GUI scale from.
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun fromInt(value: Int): GuiScale {
            return ALL[value]
        }

    }
}
