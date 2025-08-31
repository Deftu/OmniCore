package dev.deftu.omnicore.common.world

import dev.deftu.omnicore.api.annotations.VersionedAbove
import dev.deftu.omnicore.client.OmniClient
import net.minecraft.util.Identifier
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import java.util.Optional

public data class OmniBiomeData(
    public val world: World,
    public val dimension: OmniDimension,

    /** Only populated in 1.12.2 and above, as biomes have no true "identifiers" in 1.8.9. */
    @VersionedAbove("1.12.2") public val identifier: Optional<Identifier>,
    public val name: String,

    public val waterColor: Int,

    private val grassColorInvoker: (x: Double, z: Double) -> Int,
    private val precipitationInvoker: (x: Int, y: Int, z: Int) -> PrecipitationType
) {

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

        }

    }

    val translatedName: String
        get() = OmniClient.translate(name)

    public fun getGrassColorAt(x: Double, z: Double): Int {
        return this.grassColorInvoker(x, z)
    }

    public fun getPrecipitationAt(x: Int, y: Int, z: Int): PrecipitationType {
        return this.precipitationInvoker(x, y, z)
    }

}
