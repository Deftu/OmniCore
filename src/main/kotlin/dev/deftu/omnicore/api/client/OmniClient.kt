@file:JvmName("OmniClient")

package dev.deftu.omnicore.api.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.components.ChatComponent
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.server.IntegratedServer
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.entity.Entity

//#if MC >= 1.16.5
import com.mojang.blaze3d.platform.Window
//#endif

public inline val client: Minecraft
    @JvmName("get")
    get() = Minecraft.getInstance()

public inline val textureManager: TextureManager
    get() {
        //#if MC >= 1.16.5
        return client.textureManager // field, textureManager
        //#else
        //$$ return client.textureManager // getter, getTextureManager()
        //#endif
    }

public inline val player: LocalPlayer?
    get() = client.player

public inline val world: ClientLevel?
    get() = client.level

public inline val integratedServer: IntegratedServer?
    get() = client.singleplayerServer

public inline val isIntegratedServerRunning: Boolean
    get() = client.hasSingleplayerServer()

public inline val camera: Entity?
    get() = client.cameraEntity ?: player

public inline val playerHud: Gui?
    get() = client.gui

public inline val chatHud: ChatComponent?
    get() = playerHud?.chat

public inline val serverInfo: ServerData?
    get() = client.currentServer

public inline val resourceManager: ResourceManager
    get() = client.resourceManager

//#if MC >= 1.16.5
public inline val window: Window
    get() = client.window

public inline val windowHandle: Long
    get() = window.handle()
//#endif
