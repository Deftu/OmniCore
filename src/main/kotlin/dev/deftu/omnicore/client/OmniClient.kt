package dev.deftu.omnicore.client

import dev.deftu.omnicore.client.render.OmniTextureManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.hud.ChatHud
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.gui.screen.ConnectScreen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ServerInfo
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.world.ClientWorld
import net.minecraft.server.integrated.IntegratedServer

//#if MC >= 1.17.1
import net.minecraft.client.network.ServerAddress
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.util.GlfwUtil
//#endif

public object OmniClient {

    /**
     * @return True if the current working environment is on macOS, false otherwise.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val isRunningOnMac: Boolean
        get() = MinecraftClient.IS_SYSTEM_MAC

    /**
     * @return True if the game is running on the main thread, false otherwise.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val isRunningOnMainThread: Boolean
        get() = getInstance().isOnThread

    /**
     * @return The static instance of Minecraft's main class.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getInstance(): MinecraftClient = MinecraftClient.getInstance()

    /**
     * @return The current world instance, null if the player is not in a world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val world: ClientWorld?
        get() = getInstance().world

    /**
     * @return True if the client is aware of an existing world, false otherwise.
     *
     * @since 0.13.0
     * @see world
     * @author Deftu
     */
    @JvmStatic
    public val hasWorld: Boolean
        get() = world != null

    /**
     * @return The current **integrated** server instance, null if the player is not in a singleplayer world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val integratedServer: IntegratedServer?
        get() = getInstance().server

    /**
     * @return The player instance, null if the player is not in a world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val player: ClientPlayerEntity?
        get() = getInstance().player

    /**
     * @return True if the player is in a world, false otherwise.
     *
     * @since 0.2.2
     * @see player
     * @author Deftu
     */
    @JvmStatic
    public val hasPlayer: Boolean
        get() = player != null

    /**
     * @return An instance of the [InGameHud], which controls the in-game UI elements, such as the hot bar.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val hud: InGameHud
        get() = getInstance().inGameHud

    /**
     * @return An instance of the [ChatHud], which controls the chat messages.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val chat: ChatHud
        get() = hud.chatHud

    /**
     * @return The current server information, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val currentServerInfo: ServerInfo?
        get() = getInstance().currentServerEntry

    /**
     * @return The current network handler, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val networkHandler: ClientPlayNetworkHandler?
        get() = getInstance().networkHandler

    /**
     * @return The sound manager instance, which controls the game's sound output.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val soundManager: SoundManager?
        get() = getInstance().soundManager

    /**
     * @return The text renderer instance, which is used to render text on the screen.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public val fontRenderer: TextRenderer
        get() = getInstance().textRenderer

    /**
     * @return OmniCore's wrapper around Minecraft's texture manager, which is used to load, bind, and manage textures.
     *
     * @since 0.1.0
     * @author Deftu
     * @see OmniTextureManager
     */
    @JvmStatic
    public val textureManager: OmniTextureManager = OmniTextureManager.INSTANCE

    /**
     * Executes a task on Minecraft's main thread.
     *
     * @param runnable The task to execute.
     *
     * @since 0.1.0
     * @author Deftu
     * @see execute
     */
    @JvmStatic
    public fun execute(runnable: () -> Unit) {
        getInstance().execute(runnable)
    }

    /**
     * Executes a task on Minecraft's main thread.
     *
     * @param runnable The task to execute.
     *
     * @since 0.1.0
     * @author Deftu
     * @see execute
     */
    @JvmStatic
    public fun execute(runnable: Runnable) {
        execute(runnable::run)
    }

    /**
     * @return The time since the game started in milliseconds.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getTimeSinceStart(): Long {
        //#if MC >= 1.14
        return (GlfwUtil.getTime() * 1000).toLong()
        //#else
        //$$ return Minecraft.getSystemTime()
        //#endif
    }

    /**
     * A collection of utilities for multiplayer-related tasks.
     */
    public object Multiplayer {

        /**
         * @return The server brand, null if the player is not in a server.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public val serverBrand: String?
            get() {
                //#if MC >= 1.20.4
                //$$ return networkHandler?.serverBrand()
                //#else
                return player?.serverBrand
                //#endif
            }

        /**
         * @return The server address, null if the player is not in a server.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public val currentServerAddress: String?
            get() = currentServerInfo?.address

        /**
         * @return Whether the player is in a LAN server.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        public val isInLan: Boolean
            get() = currentServerInfo?.isLocal ?: false

        /**
         * @return Whether the player has multiplayer enabled, is allowed to join servers, and is not banned from multiplayer.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public val isMultiplayerEnabled: Boolean
            get() {
                //#if MC >= 1.19.2
                return getInstance().isMultiplayerEnabled
                //#else
                //$$ return true // TODO - Find a way to fetch this value in earlier versions
                //#endif
            }

        /**
         * @return Whether the player is banned from multiplayer.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public val isMultiplayerBanned: Boolean
            get() {
                //#if MC >= 1.20.4
                //$$ return getInstance().multiplayerBan() != null
                //#elseif MC >= 1.19.2
                return getInstance().isMultiplayerBanned
                //#else
                //$$ return false // TODO - Find a way to fetch this value in earlier versions
                //#endif
            }

        /**
         * @return Whether the player is in a singleplayer environment.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @JvmStatic
        public val isInSingleplayer: Boolean
            get() = getInstance().isInSingleplayer

        /**
         * @return Whether the player is in a multiplayer environment.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public val isInMultiplayer: Boolean
            get() = world != null && !isInSingleplayer && isMultiplayerEnabled && !isMultiplayerBanned && currentServerInfo != null && currentServerInfo?.address != null

        @JvmStatic
        @JvmOverloads
        public fun connectTo(hostname: String, name: String = hostname) {
            val serverInfo = ServerInfo(
                name,
                hostname,
                //#if MC >= 1.20.4
                //$$ ServerData.Type.OTHER,
                //#else
                false,
                //#endif
            )

            //#if MC >= 1.17.1
            val serverAddress = ServerAddress.parse(hostname)
            ConnectScreen.connect(
                MultiplayerScreen(OmniScreen.currentScreen),
                getInstance(),
                serverAddress,
                serverInfo,
                //#if MC >= 1.20.1
                false,
                //#endif
                //#if MC >= 1.20.6
                //$$ null,
                //#endif
            )
            //#else
            //$$ OmniScreen.currentScreen = ConnectScreen(MultiplayerScreen(OmniScreen.currentScreen), getInstance(), serverInfo)
            //#endif
        }

    }
}
