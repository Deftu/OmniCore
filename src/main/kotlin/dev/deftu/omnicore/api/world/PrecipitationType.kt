package dev.deftu.omnicore.api.world

//#if MC >= 1.16.5
import net.minecraft.world.level.biome.Biome
//#endif

public enum class PrecipitationType {
    NONE,
    RAIN,
    SNOW;

    public companion object {
        //#if MC >= 1.16.5
        @JvmStatic
        public fun from(value: Biome.Precipitation): PrecipitationType {
            return when (value) {
                Biome.Precipitation.NONE -> NONE
                Biome.Precipitation.RAIN -> RAIN
                Biome.Precipitation.SNOW -> SNOW
            }
        }
        //#endif

        @JvmStatic
        public fun from(isSnowy: Boolean, canRain: Boolean): PrecipitationType {
            return when {
                isSnowy -> SNOW
                canRain -> RAIN
                else -> NONE
            }
        }
    }
}
