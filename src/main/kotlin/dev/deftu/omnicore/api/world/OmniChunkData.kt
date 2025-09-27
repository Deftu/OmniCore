package dev.deftu.omnicore.api.world

import dev.deftu.omnicore.api.data.pos.OmniBlockPos
import dev.deftu.omnicore.api.translationKey
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

//#if MC == 1.16.5
//$$ import net.minecraft.util.math.ChunkSectionPos
//#endif

//#if MC >= 1.16.5 && MC < 1.18.2 && FABRIC
//$$ import net.minecraft.util.registry.BuiltinRegistries
//#endif

//#if MC <= 1.12.2 && FABRIC
//$$ import net.minecraft.world.biome.Biome
//#endif

public data class OmniChunkData(
    public val world: World,
    public val dimension: OmniDimension,

    public val chunkX: Int,
    public val chunkZ: Int,

    public val blockStartX: Int,
    public val blockStartZ: Int,
    public val blockEndX: Int,
    public val blockEndZ: Int,

    private val posInvoker: (x: Int, y: Int, z: Int) -> BlockPos
) {
    public companion object {
        @JvmStatic
        public fun from(
            world: World,
            dimension: OmniDimension,
            chunkPos: ChunkPos
        ): OmniChunkData? {
            val chunkX = chunkPos.x
            val chunkZ = chunkPos.z
            val blockStartX = chunkPos.startX
            val blockStartZ = chunkPos.startZ
            val blockEndX = chunkPos.endX
            val blockEndZ = chunkPos.endZ

            return OmniChunkData(
                world = world,
                dimension = dimension,
                chunkX = chunkX,
                chunkZ = chunkZ,
                blockStartX = blockStartX,
                blockStartZ = blockStartZ,
                blockEndX = blockEndX,
                blockEndZ = blockEndZ,
                posInvoker = { x, y, z ->
                    require(x in 0..15) { "x must be between 0 and 15 (inclusive), got $x" }
                    require(z in 0..15) { "z must be between 0 and 15 (inclusive), got $z" }
                    require(y in 0..world.maxWorldHeight) { "y must be between 0 and ${world.maxWorldHeight} (inclusive), got $y" }

                    //#if MC >= 1.17.1
                    chunkPos.getBlockPos(x, y, z)
                    //#elseif MC >= 1.16.5
                    //$$ val xOff = ChunkSectionPos.getBlockCoord(chunkX) + x
                    //$$ val zOff = ChunkSectionPos.getBlockCoord(chunkZ) + z
                    //$$ BlockPos(xOff, y, zOff)
                    //#else
                    //$$ chunkPos.getBlock(x, y, z)
                    //#endif
                }
            )
        }
    }

    init {
        require(this.blockEndX >= this.blockStartX) { "blockEndX must be greater than or equal to blockStartX" }
        require(this.blockEndZ >= this.blockStartZ) { "blockEndZ must be greater than or equal to blockStartZ" }
    }

    public fun getBlockPosOf(x: Int, y: Int, z: Int): BlockPos {
        require(x in 0..15) { "x must be between 0 and 15 (inclusive), got $x" }
        require(z in 0..15) { "z must be between 0 and 15 (inclusive), got $z" }
        require(y in 0..world.maxWorldHeight) { "y must be between 0 and ${world.maxWorldHeight} (inclusive), got $y" }

        return this.posInvoker(x, y, z)
    }

    public fun containsBlockAt(x: Int, z: Int): Boolean {
        return x in this.blockStartX..this.blockEndX && z in this.blockStartZ..this.blockEndZ
    }

    public fun getBiomeAt(pos: BlockPos): OmniBiomeData? {
        val biome = world.getBiome(pos) ?: return null
        //#if MC >= 1.18.2
        val id = biome.key?.map { it.value }?.get() ?: return null
        val value = biome.value()

        return OmniBiomeData(
            world = this.world,
            dimension = this.dimension,
            identifier = id,
            name = id.translationKey("biome"),
            waterColor = value.waterColor,
            grassColorInvoker = { x, z ->
                value.getGrassColorAt(x, z)
            },
            precipitationInvoker = { x, y, z ->
                //#if MC >= 1.19.4
                val precipitation = value.getPrecipitation(
                    OmniBlockPos(x, y, z).vanilla,
                    //#if MC >= 1.21.2
                    y
                    //#endif
                )
                //#else
                //$$ val precipitation = value.precipitation
                //#endif
                PrecipitationType.from(precipitation)
            }
        )
        //#else
        //#if MC >= 1.12.2
        //#if FABRIC
        //#if MC >= 1.16.5
        //$$ val id = BuiltinRegistries.BIOME?.getId(biome) ?: return null
        //#else
        //$$ val id = Biome.REGISTRY?.getIdentifier(biome) ?: return null
        //#endif
        //#else
        //$$ val id = biome.registryName ?: return null
        //#endif
        //#endif
        //$$
        //$$ return OmniBiomeData(
        //$$     world = this.world,
        //$$     dimension = this.dimension,
        //#if MC >= 1.12.2
        //$$     identifier = id,
        //#else
        //$$     identifier = null,
        //#endif
        //#if MC >= 1.16.5
        //$$     name = id.translationKey("biome"),
        //#else
        //$$     name = biome.biomeName,
        //#endif
        //$$     waterColor = biome.waterColor,
        //$$     grassColorInvoker = { x, z ->
        //#if MC >= 1.16.5
        //$$         biome.getGrassColor(x, z)
        //#else
        //$$         biome.getGrassColorAtPos(OmniBlockPos(x.toInt(), pos.y, z.toInt()).vanilla)
        //#endif
        //$$     },
        //$$     precipitationInvoker = { x, y, z ->
        //#if MC >= 1.16.5
        //$$         val precipitation = biome.precipitation
        //$$         PrecipitationType.from(precipitation)
        //#else
        //$$         PrecipitationType.from(biome.isSnowyBiome, biome.canRain())
        //#endif
        //$$     }
        //$$ )
        //#endif
    }

    public fun getBiomeAt(x: Int, y: Int, z: Int): OmniBiomeData? {
        return getBiomeAt(OmniBlockPos(x, y, z).vanilla)
    }
}
