@file:JvmName("OmniClient")

package dev.deftu.omnicore.api.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.hud.ChatHud
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ServerInfo
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.resource.ResourceManager
import net.minecraft.server.integrated.IntegratedServer

public inline val client: MinecraftClient
    @JvmName("get")
    get() = MinecraftClient.getInstance()

public inline val player: ClientPlayerEntity?
    get() = client.player

public inline val world: ClientWorld?
    get() = client.world

public inline val integratedServer: IntegratedServer?
    get() = client.server

public inline val isIntegratedServerRunning: Boolean
    get() = client.isIntegratedServerRunning

public inline val camera: Entity?
    get() = client.cameraEntity ?: player

public inline val playerHud: InGameHud?
    get() = client.inGameHud

public inline val chatHud: ChatHud?
    get() = playerHud?.chatHud

public inline val serverInfo: ServerInfo?
    get() = client.currentServerEntry

public inline val resourceManager: ResourceManager
    get() = client.resourceManager
