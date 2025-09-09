@file:JvmName("OmniPlayers")

package dev.deftu.omnicore.api.player

import dev.deftu.omnicore.api.OmniGameMode
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.GameMode
import java.util.UUID

@get:JvmName("getName")
public val PlayerEntity.trueName: String
    get() = this.gameProfile.name

@get:JvmName("getUuid")
public val PlayerEntity.trueUuid: UUID
    get() = this.gameProfile.id

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
