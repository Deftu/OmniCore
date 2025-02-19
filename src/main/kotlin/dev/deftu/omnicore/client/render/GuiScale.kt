package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.OmniClient

@GameSide(Side.CLIENT)
public enum class GuiScale {
    AUTO,
    SMALL,
    MEDIUM,
    LARGE,
    VERY_LARGE;

    public companion object {

        /**
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
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
                val client = OmniClient.getInstance()
                val window = client.window
                val scaleFactor = window.calculateScaleFactor(value, client.forcesUnicodeFont())
                window.scaleFactor = scaleFactor.toDouble()
                //#endif
            }

        /**
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        public var currentScale: GuiScale
            get() = fromInt(rawCurrentScale)
            set(value) { rawCurrentScale = value.ordinal }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun fromInt(value: Int): GuiScale {
            return values()[value]
        }

    }
}
