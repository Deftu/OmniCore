package dev.deftu.omnicore.client.options

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

@GameSide(Side.CLIENT)
public enum class SmoothLightingMode {

    OFF,
    MINIMUM,
    MAXIMUM;

    public companion object {

        public fun from(vanilla: Int): SmoothLightingMode {
            return when (vanilla) {
                0 -> OFF
                1 -> MINIMUM
                2 -> MAXIMUM
                else -> throw IllegalArgumentException("Unknown smooth lighting mode: $vanilla")
            }
        }

    }

}
