package dev.deftu.omnicore.api.world

import dev.deftu.omnicore.api.annotations.VersionedAbove
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

public data class OmniBiomeData(
    public val world: World,
    public val dimension: OmniDimension,
    /** Present on 1.12.2+; null on 1.8.9. */
    @get:VersionedAbove("1.12.2") public val identifier: Identifier?,
    /** Translation key (e.g., "biome.minecraft.plains") or resolved name on very old MC. */
    public val name: String,

    public val waterColor: Int,
    private val grassColorInvoker: (x: Double, z: Double) -> Int,
    private val precipitationInvoker: (x: Int, y: Int, z: Int) -> PrecipitationType
) {
    public fun getGrassColorAt(x: Double, z: Double): Int {
        return this.grassColorInvoker(x, z)
    }

    public fun getGrassColorAt(pos: BlockPos): Int {
        return this.grassColorInvoker(pos.x.toDouble(), pos.z.toDouble())
    }

    public fun getPrecipitationAt(x: Int, y: Int, z: Int): PrecipitationType {
        return this.precipitationInvoker(x, y, z)
    }

    public fun getPrecipitationAt(pos: BlockPos): PrecipitationType {
        return this.precipitationInvoker(pos.x, pos.y, pos.z)
    }
}
