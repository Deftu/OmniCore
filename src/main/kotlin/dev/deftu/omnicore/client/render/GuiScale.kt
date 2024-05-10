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

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun fromInt(value: Int): GuiScale =
            values()[value]

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getCurrentScale(): GuiScale {
            val guiScale =
                //#if MC >= 1.19.2
                OmniClient.getInstance().options.guiScale.value
                //#else
                //$$ OmniClient.getInstance().options.guiScale
                //#endif

            return fromInt(guiScale)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun setScale(value: Int) {
            //#if MC >= 1.19.2
            OmniClient.getInstance().options.guiScale.value = value
            //#else
            //$$ OmniClient.getInstance().options.guiScale = value
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun setScale(scale: GuiScale): Unit =
            setScale(scale.ordinal)

    }
}
