@file:JvmName("OmniClient")

package dev.deftu.omnicore.api.client

import net.minecraft.client.MinecraftClient
import net.minecraft.server.integrated.IntegratedServer

public inline val client: MinecraftClient
    @JvmName("get")
    get() = MinecraftClient.getInstance()

public inline val integratedServer: IntegratedServer?
    get() = client.server

public inline val isIntegratedServerRunning: Boolean
    get() = client.isIntegratedServerRunning
