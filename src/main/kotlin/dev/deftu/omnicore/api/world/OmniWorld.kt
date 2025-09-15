@file:JvmName("OmniWorld")

package dev.deftu.omnicore.api.world

import dev.deftu.omnicore.api.data.pos.OmniBlockPos
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World
import java.util.UUID

//#if MC < 1.16.5
//$$ import com.google.common.base.Predicate
//#endif

public val World.isClientWorld: Boolean
    get() = this.isClient

public val World.spawnPosition: BlockPos
    get() {
        //#if MC >= 1.19.2
        return this.spawnPos
        //#elseif MC >= 1.16.5
        //$$ return when (this) {
        //$$     is net.minecraft.server.world.ServerWorld -> this.spawnPos
        //$$     is net.minecraft.client.world.ClientWorld -> this.spawnPos
        //$$     else -> throw IllegalStateException("Unsupported world type: ${this::class.java.name}")
        //$$ }
        //#else
        //$$ return this.spawnPoint
        //#endif
    }

public val World.maxWorldHeight: Int
    get() {
        //#if MC >= 1.16.5
        return this.height
        //#else
        //$$ return this.actualHeight
        //#endif
    }

public val World.currentTime: Long
    get() = this.timeOfDay

public val World.isDayTime: Boolean
    get() = this.isDay

public val World.isNightTime: Boolean
    get() {
        //#if MC >= 1.16.5
        return this.isNight
        //#else
        //$$ return !isDayTime
        //#endif
    }

public val World.isRaining: Boolean
    get() = this.isRaining

public val World.isThundering: Boolean
    get() = this.isThundering

public val World.isClearWeather: Boolean
    get() = !isRaining && !isThundering

public val World.allPlayers: List<PlayerEntity>
    get() {
        //#if MC >= 1.16.5
        return this.players
        //#else
        //$$ return this.playerEntities
        //#endif
    }

public fun World.isBlockLoadedAt(pos: BlockPos): Boolean {
    //#if MC >= 1.16.5
    return isPosLoaded(pos)
    //#else
    //$$ return isBlockLoaded(pos)
    //#endif
}

public fun World.isBlockLoadedAt(x: Int, y: Int, z: Int): Boolean {
    return isBlockLoadedAt(OmniBlockPos(x, y, z).vanilla)
}

public fun World.getBlockTypeAt(pos: BlockPos): Block? {
    return this.getBlockState(pos)?.block
}

public fun World.getBlockTypeAt(x: Int, y: Int, z: Int): Block? {
    return getBlockTypeAt(OmniBlockPos(x, y, z).vanilla)
}

public fun World.getPlayerFromUuid(uuid: UUID): PlayerEntity? {
    //#if MC >= 1.16.5
    return getPlayerByUuid(uuid)
    //#else
    //$$ return getPlayerEntityByUUID(uuid)
    //#endif
}

public fun World.getEntitiesByType(
    type: Class<out Entity>,
    box: Box,
    filter: (Entity) -> Boolean = { true }
): List<Entity> {
    //#if MC >= 1.16.5
    return getEntitiesByClass(type, box, filter)
    //#else
    //$$ return getEntitiesWithinAABB(type, box, Predicate {
    //$$     it?.let(filter) ?: false
    //$$ })
    //#endif
}

public fun World.getClosestPlayerTo(
    x: Double,
    y: Double,
    z: Double,
    maxDistance: Double,
    filter: (Entity) -> Boolean = { true }
): PlayerEntity? {
    //#if MC >= 1.16.5
    return getClosestPlayer(x, y, z, maxDistance, filter)
    //#elseif MC >= 1.12.2
    //$$ return getClosestPlayer(x, y, z, maxDistance, Predicate {
    //$$     it?.let(filter) ?: false
    //$$ })
    //#else
    //$$ if (maxDistance < 0) {
    //$$     return null
    //$$ }
    //$$
    //$$ var closestDistanceTo = -1.0
    //$$ var closestPlayer: PlayerEntity? = null
    //$$ for (player in playerEntities) {
    //$$     if (!filter(player)) {
    //$$         continue
    //$$     }
    //$$
    //$$     val distanceTo = player.squaredDistanceTo(x, y, z)
    //$$     if (distanceTo > maxDistance * maxDistance) {
    //$$         continue
    //$$     }
    //$$
    //$$     if (closestDistanceTo !in 0.0..distanceTo) {
    //$$         closestDistanceTo = distanceTo
    //$$         closestPlayer = player
    //$$     }
    //$$ }
    //$$
    //$$ return closestPlayer
    //#endif
}

public fun World.getClosestPlayerTo(
    pos: BlockPos,
    maxDistance: Double,
    filter: (Entity) -> Boolean = { true }
): PlayerEntity? {
    return getClosestPlayerTo(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), maxDistance, filter)
}

public fun World.getClosestPlayerTo(
    entity: Entity,
    maxDistance: Double,
): PlayerEntity? {
    //#if MC >= 1.16.5
    return getClosestPlayer(entity, maxDistance)
    //#else
    //$$ return getClosestPlayerToEntity(entity, maxDistance)
    //#endif
}
