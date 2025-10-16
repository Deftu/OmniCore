@file:JvmName("OmniClientPlayer")

package dev.deftu.omnicore.api.client.player

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.player
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.Entity
import java.util.UUID

public val MinecraftClient.uuid: UUID
    get() {
        //#if MC >= 1.20.1
        return session.uuidOrNull ?: error("Session UUID is null")
        //#else
        //$$ return session.profile.id
        //#endif
    }

public val MinecraftClient.username: String
    get() = session.username

public val playerUuid: UUID
    get() = client.uuid

public val playerName: String
    get() = client.username

/**
 * Checks if the entity is the local player by type or by comparing to the local player instance.
 */
public val Entity.isLocalPlayer: Boolean
    get() = this is ClientPlayerEntity || this == player

/**
 * Checks [Entity.isLocalPlayer] or compares the entity's UUID to the local player's UUID.
 * This is its own separate check as some mods or server plugins may create fake players who share the player's UUID.
 * If you permit this, or only allow environments within which this doesn't happen, this is a more solid check, otherwise it can be considered unsafe.
 */
public val Entity.isLocalPlayerLenient: Boolean
    get() = isLocalPlayer || this.uuid == playerUuid
