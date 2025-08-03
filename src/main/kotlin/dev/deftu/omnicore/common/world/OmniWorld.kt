package dev.deftu.omnicore.common.world

import dev.deftu.omnicore.common.OmniBlockPos
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

public open class OmniWorld(public val vanilla: World) {

    /** Whether this world instance exists on the logical client-side. False if this is the physical server. */
    public open val isClient: Boolean
        get() = vanilla.isClient

    public open val dimension: OmniDimension
        get() = OmniDimension.from(vanilla)

    public open val spawnPosition: BlockPos
        get() {
            //#if MC >= 1.19.2
            return vanilla.spawnPos
            //#elseif MC >= 1.16.5
            //$$ return when (vanilla) {
            //$$     is net.minecraft.server.world.ServerWorld -> vanilla.spawnPos
            //$$     is net.minecraft.client.world.ClientWorld -> vanilla.spawnPos
            //$$     else -> throw IllegalStateException("Unsupported world type: ${vanilla::class.java.name}")
            //$$ }
            //#else
            //$$ return vanilla.spawnPoint
            //#endif
        }

    public open val worldTime: Long
        get() = vanilla.timeOfDay

    public open val isDay: Boolean
        get() = vanilla.isDay

    public open val isNight: Boolean
        get() {
            //#if MC >= 1.16.5
            return vanilla.isNight
            //#else
            //$$ return !isDay
            //#endif
        }

    public open val isRaining: Boolean
        get() = vanilla.isRaining

    public open val isThundering: Boolean
        get() = vanilla.isThundering

    public open val isClearWeather: Boolean
        get() = !isRaining && !isThundering

    /** Returns the chunk at the given **chunk coordinates** (__not__ block coords!) */
    public open fun getChunk(chunkX: Int, chunkZ: Int): OmniChunkData? {
        val chunk = vanilla.getChunk(chunkX, chunkZ) ?: return null
        val chunkPos = chunk.pos
        return OmniChunkData.from(this.vanilla, this.dimension, chunkPos)
    }

    /** Returns the chunk at the given **block coordinates** (__not__ chunk coords!) */
    public open fun getChunkAt(pos: BlockPos): OmniChunkData? {
        val chunk = vanilla.getChunk(pos) ?: return null
        val chunkPos = chunk.pos
        return OmniChunkData.from(this.vanilla, this.dimension, chunkPos)
    }

    /** Returns the chunk at the given **block coordinates** (__not__ chunk coords!) */
    public open fun getChunkAt(x: Int, y: Int, z: Int): OmniChunkData? {
        return getChunkAt(OmniBlockPos.from(x, y, z))
    }

    public open fun getBiomeAt(pos: BlockPos): OmniBiomeData? {
        return getChunkAt(pos)?.getBiomeAt(pos)
    }

    public open fun getBiomeAt(x: Int, y: Int, z: Int): OmniBiomeData? {
        return getBiomeAt(OmniBlockPos.from(x, y, z))
    }

    public open fun isBlockLoaded(pos: BlockPos): Boolean {
        //#if MC >= 1.16.5
        return vanilla.isPosLoaded(pos)
        //#else
        //$$ return vanilla.isBlockLoaded(pos)
        //#endif
    }

    public open fun isBlockLoaded(x: Int, y: Int, z: Int): Boolean {
        return isBlockLoaded(OmniBlockPos.from(x, y, z))
    }

}
