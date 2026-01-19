@file:JvmName("OmniWorld")

package dev.deftu.omnicore.api.world

import dev.deftu.omnicore.api.data.pos.OmniBlockPos
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.AABB
import java.util.UUID

//#if MC < 1.16.5
//$$ import com.google.common.base.Predicate
//#endif

public val Level.isOnClient: Boolean
    get() = this.isClientSide

public val Level.spawnPosition: BlockPos
    get() {
        //#if MC >= 1.21.9
        return this.respawnData.pos()
        //#elseif MC >= 1.19.2
        //$$ return this.sharedSpawnPos
        //#elseif MC >= 1.16.5
        //$$ return when (this) {
        //$$     is net.minecraft.server.level.ServerLevel -> this.sharedSpawnPos
        //$$     is net.minecraft.client.multiplayer.ClientLevel -> this.sharedSpawnPos
        //$$     else -> throw IllegalStateException("Unsupported world type: ${this::class.java.name}")
        //$$ }
        //#else
        //$$ return this.spawnPoint
        //#endif
    }

public val Level.maxWorldHeight: Int
    get() {
        //#if MC >= 1.16.5
        return this.height
        //#else
        //$$ return this.actualHeight
        //#endif
    }

public val Level.currentTime: Long
    get() {
        //#if MC >= 26.1
        //$$ return this.overworldClockTime
        //#else
        return this.dayTime
        //#endif
    }

public val Level.isDayTime: Boolean
    get() = this.isBrightOutside

public val Level.isNightTime: Boolean
    get() {
        //#if MC >= 1.16.5
        return this.isDarkOutside
        //#else
        //$$ return !isDayTime
        //#endif
    }

public val Level.isRaining: Boolean
    get() = this.isRaining

public val Level.isThundering: Boolean
    get() = this.isThundering

public val Level.isClearWeather: Boolean
    get() = !isRaining && !isThundering

public val Level.allPlayers: List<Player>
    get() {
        //#if MC >= 1.16.5
        return this.players()
        //#else
        //$$ return this.playerEntities
        //#endif
    }

public fun Level.isBlockLoadedAt(pos: BlockPos): Boolean {
    //#if MC >= 1.16.5
    return isLoaded(pos)
    //#else
    //$$ return isBlockLoaded(pos)
    //#endif
}

public fun Level.isBlockLoadedAt(x: Int, y: Int, z: Int): Boolean {
    return isBlockLoadedAt(OmniBlockPos(x, y, z).vanilla)
}

public fun Level.getBlockTypeAt(pos: BlockPos): Block? {
    return this.getBlockState(pos)?.block
}

public fun Level.getBlockTypeAt(x: Int, y: Int, z: Int): Block? {
    return getBlockTypeAt(OmniBlockPos(x, y, z).vanilla)
}

public fun Level.getPlayerFromUuid(uuid: UUID): Player? {
    //#if MC >= 1.16.5
    return getPlayerByUUID(uuid)
    //#else
    //$$ return getPlayerEntityByUUID(uuid)
    //#endif
}

public fun Level.getEntitiesByType(
    type: Class<out Entity>,
    box: AABB,
    filter: (Entity) -> Boolean = { true }
): List<Entity> {
    //#if MC >= 1.16.5
    return getEntitiesOfClass(type, box, filter)
    //#else
    //$$ return getEntitiesWithinAABB(type, box, Predicate {
    //$$     it?.let(filter) ?: false
    //$$ })
    //#endif
}

public fun Level.getClosestPlayerTo(
    x: Double,
    y: Double,
    z: Double,
    maxDistance: Double,
    filter: (Entity) -> Boolean = { true }
): Player? {
    //#if MC >= 1.16.5
    return getNearestPlayer(x, y, z, maxDistance, filter)
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
    //$$ var closestPlayer: EntityPlayer? = null
    //$$ for (player in playerEntities) {
    //$$     if (!filter(player)) {
    //$$         continue
    //$$     }
    //$$
    //$$     val distanceTo = player.getDistanceSq(x, y, z)
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

public fun Level.getClosestPlayerTo(
    pos: BlockPos,
    maxDistance: Double,
    filter: (Entity) -> Boolean = { true }
): Player? {
    return getClosestPlayerTo(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), maxDistance, filter)
}

public fun Level.getClosestPlayerTo(
    entity: Entity,
    maxDistance: Double,
): Player? {
    //#if MC >= 1.16.5
    return getNearestPlayer(entity, maxDistance)
    //#else
    //$$ return getClosestPlayerToEntity(entity, maxDistance)
    //#endif
}
