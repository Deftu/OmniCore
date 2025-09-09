@file:JvmName("OmniServer")

package dev.deftu.omnicore.api

import dev.deftu.omnicore.api.client.integratedServer
import dev.deftu.omnicore.api.loader.OmniLoader
import dev.deftu.omnicore.internal.serverInstance
import net.minecraft.server.MinecraftServer

public val server: MinecraftServer?
    get() {
        if (OmniLoader.isPhysicalClient) {
            return integratedServer
        }

        return serverInstance
    }
