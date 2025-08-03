package dev.deftu.omnicore.common.world

import dev.deftu.omnicore.common.OmniBlockPos
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World
import java.util.UUID

//#if MC <= 1.12.2
//$$ import com.google.common.base.Predicate
//#endif

public class OmniWorld(public val vanilla: World) {

    public companion object {

        public val spectatorFilter: (Entity) -> Boolean by lazy {
            { entity: Entity ->
                //#if MC >= 1.16.5
                !entity.isSpectator
                //#else
                //$$ entity is EntityPlayer && entity.isSpectator
                //#endif
            }
        }

    }

    /** Whether this world instance exists on the logical client-side. False if this is the physical server. */
    public val isClient: Boolean
        get() = vanilla.isClient

    public val dimension: OmniDimension
        get() = OmniDimension.from(vanilla)

    public val spawnPosition: BlockPos
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

    public val worldTime: Long
        get() = vanilla.timeOfDay

    public val isDay: Boolean
        get() = vanilla.isDay

    public val isNight: Boolean
        get() {
            //#if MC >= 1.16.5
            return vanilla.isNight
            //#else
            //$$ return !isDay
            //#endif
        }

    public val isRaining: Boolean
        get() = vanilla.isRaining

    public val isThundering: Boolean
        get() = vanilla.isThundering

    public val isClearWeather: Boolean
        get() = !isRaining && !isThundering

    public val players: List<PlayerEntity>
        get() {
            //#if MC >= 1.16.5
            return vanilla.players
            //#else
            //$$ return vanilla.playerEntities
            //#endif
        }

    /** Returns the chunk at the given **chunk coordinates** (__not__ block coords!) */
    public fun getChunk(chunkX: Int, chunkZ: Int): OmniChunkData? {
        val chunk = vanilla.getChunk(chunkX, chunkZ) ?: return null
        val chunkPos = chunk.pos
        return OmniChunkData.from(this.vanilla, this.dimension, chunkPos)
    }

    /** Returns the chunk at the given **block coordinates** (__not__ chunk coords!) */
    public fun getChunkAt(pos: BlockPos): OmniChunkData? {
        val chunk = vanilla.getChunk(pos) ?: return null
        val chunkPos = chunk.pos
        return OmniChunkData.from(this.vanilla, this.dimension, chunkPos)
    }

    /** Returns the chunk at the given **block coordinates** (__not__ chunk coords!) */
    public fun getChunkAt(x: Int, y: Int, z: Int): OmniChunkData? {
        return getChunkAt(OmniBlockPos.from(x, y, z))
    }

    public fun getBiomeAt(pos: BlockPos): OmniBiomeData? {
        return getChunkAt(pos)?.getBiomeAt(pos)
    }

    public fun getBiomeAt(x: Int, y: Int, z: Int): OmniBiomeData? {
        return getBiomeAt(OmniBlockPos.from(x, y, z))
    }

    public fun isBlockLoaded(pos: BlockPos): Boolean {
        //#if MC >= 1.16.5
        return vanilla.isPosLoaded(pos)
        //#else
        //$$ return vanilla.isBlockLoaded(pos)
        //#endif
    }

    public fun isBlockLoaded(x: Int, y: Int, z: Int): Boolean {
        return isBlockLoaded(OmniBlockPos.from(x, y, z))
    }
    
    public fun getPlayerByUuid(uuid: UUID): PlayerEntity? {
        //#if MC >= 1.16.5
        return vanilla.getPlayerByUuid(uuid)
        //#else
        //$$ return vanilla.getPlayerEntityByUUID(uuid)
        //#endif
    }

    public fun getEntitiesByType(
        type: Class<out Entity>,
        box: Box,
        filter: (Entity) -> Boolean = { true }
    ): List<Entity> {
        //#if MC >= 1.16.5
        return vanilla.getEntitiesByClass(type, box, filter)
        //#else
        //$$ return vanilla.getEntitiesWithinAABB(type, box, Predicate {
        //$$     it?.let(filter) ?: false
        //$$ })
        //#endif
    }

    public fun getClosestPlayerTo(
        x: Double,
        y: Double,
        z: Double,
        maxDistance: Double,
        filter: (Entity) -> Boolean = { true }
    ): PlayerEntity? {
        //#if MC >= 1.16.5
        return vanilla.getClosestPlayer(x, y, z, maxDistance, filter)
        //#elseif MC >= 1.12.2
        //$$ return vanilla.getClosestPlayer(x, y, z, maxDistance, Predicate {
        //$$     it?.let(filter) ?: false
        //$$ })
        //#else
        //$$ if (maxDistance < 0) {
        //$$     return null
        //$$ }
        //$$
        //$$ var closestDistanceTo = -1.0
        //$$ var closestPlayer: PlayerEntity? = null
        //$$ for (player in players) {
        //$$     if (!filter(player)) {
        //$$         continue
        //$$     }
        //$$
        //$$     val distanceTo = player.squaredDistanceTo(x, y, z)
        //$$     if (distanceTo > maxDistance * maxDistance) {
        //$$         continue
        //$$     }
        //$$
        //$$     if (closestDistanceTo < 0 || distanceTo < closestDistanceTo) {
        //$$         closestDistanceTo = distanceTo
        //$$         closestPlayer = player
        //$$     }
        //$$ }
        //$$
        //$$ return closestPlayer
        //#endif
    }

    public fun getClosestPlayerTo(
        pos: BlockPos,
        maxDistance: Double,
        filter: (Entity) -> Boolean = { true }
    ): PlayerEntity? {
        return getClosestPlayerTo(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), maxDistance, filter)
    }

    public fun getClosestPlayerTo(
        entity: Entity,
        maxDistance: Double,
    ): PlayerEntity? {
        //#if MC >= 1.16.5
        return vanilla.getClosestPlayer(entity, maxDistance)
        //#else
        //$$ return vanilla.getClosestPlayerToEntity(entity, maxDistance)
        //#endif
    }

}
