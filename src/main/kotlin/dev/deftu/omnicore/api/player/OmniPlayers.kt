@file:JvmName("OmniPlayers")

package dev.deftu.omnicore.api.player

import dev.deftu.omnicore.api.OmniGameMode
import dev.deftu.omnicore.api.data.vec.OmniVec3d
import dev.deftu.omnicore.api.equipment.EquipmentType
import dev.deftu.omnicore.api.world.OmniBiomeData
import dev.deftu.omnicore.api.world.OmniChunkData
import dev.deftu.omnicore.api.world.dimensionType
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d
import java.util.UUID

//#if MC <= 1.16.5
//$$ import dev.deftu.omnicore.api.data.pos.OmniBlockPos
//$$ import dev.deftu.omnicore.api.data.pos.OmniChunkPos
//#endif

//#if MC <= 1.12.2
//$$ import kotlin.math.floor
//#endif

//#if MC >= 1.12.2
import net.minecraft.util.Arm
import net.minecraft.world.GameMode
//#else
//$$ import net.minecraft.world.level.LevelInfo.GameMode
//#endif

@get:JvmName("getName")
public val PlayerEntity.trueName: String
    get() = this.gameProfile.name

@get:JvmName("getUuid")
public val PlayerEntity.trueUuid: UUID
    get() = this.gameProfile.id

public val PlayerEntity.chunkData: OmniChunkData?
    get() {
        return OmniChunkData.from(
            world = world,
            dimension = world.dimensionType,
            //#if MC >= 1.17.1
            chunkPos = chunkPos
            //#else
            //$$ chunkPos = OmniChunkPos(chunkX, chunkZ).vanilla
            //#endif
        )
    }

public val PlayerEntity.biomeData: OmniBiomeData?
    get() {
        return chunkData?.getBiomeAt(
            //#if MC >= 1.16.5
            pos = blockPos
            //#else
            //$$ pos = OmniBlockPos(floor(posX).toInt(), floor(posY).toInt(), floor(posZ).toInt()).vanilla
            //#endif
        )
    }

@get:JvmName("getGameMode")
public val PlayerEntity.currentGameMode: OmniGameMode
    get() {
        val gameMode = when (this) {
            is ServerPlayerEntity -> interactionManager?.gameMode
            is ClientPlayerEntity -> networkHandler?.getPlayerListEntry(trueUuid)?.gameMode
            else -> null
        } ?: return OmniGameMode.UNKNOWN

        @Suppress("REDUNDANT_ELSE_IN_WHEN")
        return when (gameMode) {
            GameMode.ADVENTURE -> OmniGameMode.ADVENTURE
            GameMode.CREATIVE -> OmniGameMode.CREATIVE
            GameMode.SPECTATOR -> OmniGameMode.SPECTATOR
            GameMode.SURVIVAL -> OmniGameMode.SURVIVAL
            else -> OmniGameMode.UNKNOWN
        }
    }

public val PlayerEntity.isAdventureMode: Boolean
    get() = currentGameMode == OmniGameMode.ADVENTURE

public val PlayerEntity.isCreativeMode: Boolean
    get() = currentGameMode == OmniGameMode.CREATIVE

public val PlayerEntity.isSpectatorMode: Boolean
    get() = currentGameMode == OmniGameMode.SPECTATOR

public val PlayerEntity.isSurvivalMode: Boolean
    get() = currentGameMode == OmniGameMode.SURVIVAL

public val PlayerEntity.mainArm: EquipmentType
    get() {
        //#if MC >= 1.12.2
        return when (mainArm) {
            Arm.LEFT -> EquipmentType.OffHand
            Arm.RIGHT -> EquipmentType.MainHand
        }
        //#else
        //$$ return EquipmentType.MainHand // Right
        //#endif
    }

public val PlayerEntity.healthLevel: Float
    get() = health

public val PlayerEntity.hungerLevel: Int
    get() = hungerManager.foodLevel

public val PlayerEntity.saturationLevel: Float
    get() = hungerManager.saturationLevel

public var PlayerEntity.posX: Double
    get() {
        //#if MC >= 1.16.5
        return x
        //#else
        //$$ return posX
        //#endif
    }
    set(value) {
        setPos(value, posY, posZ)
    }

public var PlayerEntity.prevPosX: Double
    get() {
        //#if MC >= 1.16.5
        return lastX
        //#else
        //$$ return prevPosX
        //#endif
    }
    set(value) {
        //#if MC >= 1.16.5
        lastX = value
        //#else
        //$$ prevPosX = value
        //#endif
    }

public var PlayerEntity.posY: Double
    get() {
        //#if MC >= 1.16.5
        return y
        //#else
        //$$ return posY
        //#endif
    }
    set(value) {
        setPos(posX, value, posZ)
    }

public var PlayerEntity.prevPosY: Double
    get() {
        //#if MC >= 1.16.5
        return lastY
        //#else
        //$$ return prevPosY
        //#endif
    }
    set(value) {
        //#if MC >= 1.16.5
        lastY = value
        //#else
        //$$ prevPosY = value
        //#endif
    }

public var PlayerEntity.posZ: Double
    get() {
        //#if MC >= 1.16.5
        return z
        //#else
        //$$ return posZ
        //#endif
    }
    set(value) {
        setPos(posX, posY, value)
    }

public var PlayerEntity.prevPosZ: Double
    get() {
        //#if MC >= 1.16.5
        return lastZ
        //#else
        //$$ return prevPosZ
        //#endif
    }
    set(value) {
        //#if MC >= 1.16.5
        lastZ = value
        //#else
        //$$ prevPosZ = value
        //#endif
    }

public var PlayerEntity.pos: Vec3d
    get() {
        //#if MC >= 1.16.5
        return pos
        //#else
        //#if FABRIC
        //$$ return getPos()
        //#else
        //$$ return positionVector
        //#endif
        //#endif
    }
    set(value) {
        setPos(value.x, value.y, value.z)
    }

public var PlayerEntity.prevPos: Vec3d
    get() = OmniVec3d(prevPosX, prevPosY, prevPosZ).vanilla
    set(value) {
        //#if MC >= 1.16.5
        lastX = value.x
        lastY = value.y
        lastZ = value.z
        //#else
        //$$ prevPosX = value.x
        //$$ prevPosY = value.y
        //$$ prevPosZ = value.z
        //#endif
    }

public var PlayerEntity.rotationYaw: Float
    get() = yaw
    set(value) {
        yaw = value
    }

public var PlayerEntity.prevRotationYaw: Float
    get() = lastYaw
    set(value) {
        lastYaw = value
    }

public var PlayerEntity.rotationPitch: Float
    get() = pitch
    set(value) {
        pitch = value
    }

public var PlayerEntity.prevRotationPitch: Float
    get() = lastPitch
    set(value) {
        lastPitch = value
    }
