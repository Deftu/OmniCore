@file:JvmName("OmniClientPlayer")

package dev.deftu.omnicore.api.client.player

import net.minecraft.client.MinecraftClient
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
