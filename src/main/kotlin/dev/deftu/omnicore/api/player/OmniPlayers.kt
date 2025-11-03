@file:JvmName("OmniPlayers")

package dev.deftu.omnicore.api.player

import dev.deftu.omnicore.api.OmniGameMode
import dev.deftu.omnicore.api.equipment.EquipmentType
import dev.deftu.omnicore.api.world.OmniBiomeData
import dev.deftu.omnicore.api.world.OmniChunkData
import dev.deftu.omnicore.api.world.dimensionType
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.server.level.ServerPlayer
import java.util.UUID

//#if MC <= 1.16.5
//$$ import dev.deftu.omnicore.api.data.pos.OmniBlockPos
//$$ import dev.deftu.omnicore.api.data.pos.OmniChunkPos
//#endif

//#if MC <= 1.12.2
//$$ import kotlin.math.floor
//#endif

//#if MC >= 1.12.2
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.level.GameType
//#else
//$$ import net.minecraft.world.WorldSettings.GameType
//#endif

@get:JvmName("getName")
public val Player.trueName: String
    get() = this.gameProfile.name

@get:JvmName("getUuid")
public val Player.trueUuid: UUID
    get() = this.gameProfile.id()

public val Entity.isPlayer: Boolean
    get() = this is Player

public val Player.theirLevel: Level
    get() {
        //#if MC >= 1.18.2
        return level()
        //#else
        //$$ return level
        //#endif
    }

public val Player.chunkData: OmniChunkData?
    get() {
        return OmniChunkData.from(
            world = theirLevel,
            dimension = theirLevel.dimensionType,
            //#if MC >= 1.17.1
            chunkPos = chunkPosition()
            //#else
            //$$ chunkPos = OmniChunkPos(xChunk, zChunk).vanilla
            //#endif
        )
    }

public val Player.biomeData: OmniBiomeData?
    get() {
        return chunkData?.getBiomeAt(
            //#if MC >= 1.16.5
            pos = blockPosition()
            //#else
            //$$ pos = OmniBlockPos(floor(posX).toInt(), floor(posY).toInt(), floor(posZ).toInt()).vanilla
            //#endif
        )
    }

@get:JvmName("getGameMode")
public val Player.currentGameMode: OmniGameMode
    get() {
        val gameMode = when (this) {
            is ServerPlayer -> gameMode?.gameModeForPlayer
            is LocalPlayer -> connection?.getPlayerInfo(trueUuid)?.gameMode
            else -> null
        } ?: return OmniGameMode.UNKNOWN

        @Suppress("REDUNDANT_ELSE_IN_WHEN")
        return when (gameMode) {
            GameType.ADVENTURE -> OmniGameMode.ADVENTURE
            GameType.CREATIVE -> OmniGameMode.CREATIVE
            GameType.SPECTATOR -> OmniGameMode.SPECTATOR
            GameType.SURVIVAL -> OmniGameMode.SURVIVAL
            else -> OmniGameMode.UNKNOWN
        }
    }

public val Player.isAdventureMode: Boolean
    get() = currentGameMode == OmniGameMode.ADVENTURE

public val Player.isCreativeMode: Boolean
    get() = currentGameMode == OmniGameMode.CREATIVE

public val Player.isSpectatorMode: Boolean
    get() = currentGameMode == OmniGameMode.SPECTATOR

public val Player.isSurvivalMode: Boolean
    get() = currentGameMode == OmniGameMode.SURVIVAL

public val Player.mainArm: EquipmentType
    get() {
        //#if MC >= 1.12.2
        return when (mainArm) {
            HumanoidArm.LEFT -> EquipmentType.OffHand
            HumanoidArm.RIGHT -> EquipmentType.MainHand
        }
        //#else
        //$$ return EquipmentType.MainHand // Right
        //#endif
    }

public val Player.healthLevel: Float
    get() = health

public val Player.hungerLevel: Int
    get() = foodData.foodLevel

public val Player.saturationLevel: Float
    get() = foodData.saturationLevel
