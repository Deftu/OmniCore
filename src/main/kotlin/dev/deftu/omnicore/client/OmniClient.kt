package dev.deftu.omnicore.client

import dev.deftu.omnicore.common.world.OmniWorld
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.hud.ChatHud
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ServerInfo
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.resource.ResourceManager
import net.minecraft.server.integrated.IntegratedServer

//#if MC >= 1.16.5
//#if FABRIC
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resource.ResourceType
//#elseif FORGE && MC >= 1.17.1
//$$ import net.minecraftforge.common.MinecraftForge
//$$ import net.minecraftforge.client.event.RegisterClientReloadListenersEvent
//#elseif NEOFORGE
//$$ import net.neoforged.neoforge.common.NeoForge
//#if MC >= 1.21.4
//$$ import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
//#else
//$$ import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
//#endif
//#endif
import net.minecraft.client.util.GlfwUtil
import net.minecraft.client.util.InputUtil
//#else
//#if FABRIC
//$$ import net.legacyfabric.fabric.api.resource.ResourceManagerHelper
//#endif
//$$
//$$ import net.minecraft.client.settings.GameSettings
//#endif

//#if FORGE && MC <= 1.16.5
//$$ import net.minecraft.server.packs.resources.ReloadableResourceManager
//#endif

public object OmniClient {

    private var cachedLastWorld: OmniWorld? = null

    /**
     * @return The static instance of Minecraft's main class.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getInstance(): MinecraftClient {
        return MinecraftClient.getInstance()
    }

    /**
     * @return The current world instance, null if the player is not in a world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use currentWorld instead.", ReplaceWith("currentWorld"))
    public val world: ClientWorld?
        get() = getInstance().world

    /**
     * @since 0.40.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val currentWorld: OmniWorld?
        get() {
            @Suppress("DEPRECATION")
            val world = world ?: return null
            if (cachedLastWorld?.vanilla === world) {
                return cachedLastWorld
            }

            cachedLastWorld = OmniWorld(world)
            return cachedLastWorld
        }

    /**
     * @return True if the client is aware of an existing world, false otherwise.
     *
     * @since 0.13.0
     * @see currentWorld
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val hasWorld: Boolean
        @JvmName("hasWorld")
        get() = currentWorld != null

    /**
     * @return The current **integrated** server instance, null if the player is not in a singleplayer world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val integratedServer: IntegratedServer?
        get() = getInstance().server

    /**
     * @return The player instance, null if the player is not in a world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use OmniClientPlayer.getInstance() instead.", ReplaceWith("OmniClientPlayer.getInstance()"))
    public val player: ClientPlayerEntity?
        get() = OmniClientPlayer.getInstance()

    /**
     * @return True if the player is in a world, false otherwise.
     *
     * @since 0.2.2
     * @see player
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use OmniClientPlayer.hasPlayer instead.", ReplaceWith("OmniClientPlayer.hasPlayer"))
    public val hasPlayer: Boolean
        get() = OmniClientPlayer.hasPlayer

    /**
     * @since 0.45.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val cameraEntity: Entity?
        get() = getInstance().cameraEntity ?: OmniClientPlayer.getInstance()

    /**
     * @return The player's unformatted username.
     *
     * @since 0.16.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use OmniClientPlayer.name instead.", ReplaceWith("OmniClientPlayer.name"))
    public val playerName: String
        get() = getInstance().session.username

    /**
     * @return An instance of the [InGameHud], which controls the in-game UI elements, such as the hot bar.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val hud: InGameHud
        get() = getInstance().inGameHud

    /**
     * @return An instance of the [ChatHud], which controls the chat messages.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val chat: ChatHud
        get() = hud.chatHud

    /**
     * @return The current server information, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val currentServerInfo: ServerInfo?
        get() = getInstance().currentServerEntry

    /**
     * @return The current network handler, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val networkHandler: ClientPlayNetworkHandler?
        get() = getInstance().networkHandler

    /**
     * @return OmniCore's wrapper around Minecraft's texture manager, which is used to load, bind, and manage textures.
     *
     * @since 0.1.0
     * @author Deftu
     * @see OmniTextureManager
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val textureManager: OmniTextureManager
        get() = OmniTextureManager.INSTANCE

    /**
     * @since 0.45.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val resourceManager: ResourceManager
        get() = getInstance().resourceManager

    @JvmStatic
    @GameSide(Side.CLIENT)
    @Deprecated("Use hasWorld() instead", ReplaceWith("hasWorld()"))
    public fun getHasWorld(): Boolean {
        return hasWorld
    }

}
