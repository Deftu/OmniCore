package dev.deftu.omnicore.client

//#if MC >= 1.16.5
import net.minecraft.client.util.GlfwUtil
//#endif

import dev.deftu.omnicore.client.exceptions.UnavailableClientPlayerException
import dev.deftu.omnicore.client.render.OmniTextureManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.hud.ChatHud
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ServerInfo
import net.minecraft.client.option.GameOptions
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.world.ClientWorld
import net.minecraft.server.integrated.IntegratedServer

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
        get() =
            //#if MC >= 1.15
            getInstance().isOnThread
            //#else
            //$$ getInstance().isCallingFromMinecraftThread()
            //#endif

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
    public fun getWorld(): ClientWorld? = getInstance().world

    /**
     * @return The current **integrated** server instance, null if the player is not in a singleplayer world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getIntegratedServer(): IntegratedServer? = getInstance().server

    /**
     * @return The player instance, null if the player is not in a world.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getPlayer(): ClientPlayerEntity? = getInstance().player

    /**
     * @return True if the player is in a world, false otherwise.
     *
     * @since 0.2.2
     * @see getPlayer
     * @author Deftu
     */
    @JvmStatic
    public fun hasPlayer(): Boolean = getPlayer() != null

    /**
     * @return The player instance, throws a [UnavailableClientPlayerException] if the player is not in a world.
     *
     * @throws UnavailableClientPlayerException If the player is not in a world.
     * @since 0.2.2
     * @see getPlayer
     */
    @JvmStatic
    @Throws(UnavailableClientPlayerException::class)
    public fun requirePlayer(): ClientPlayerEntity = getPlayer() ?: throw UnavailableClientPlayerException()

    /**
     * @return An instance of the [InGameHud], which controls the in-game UI elements, such as the hot bar.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getHud(): InGameHud = getInstance().inGameHud

    /**
     * @return An instance of the [ChatHud], which controls the chat messages.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getChat(): ChatHud =
        //#if MC > 1.15
        getHud().chatHud
        //#else
        //$$ getHud().chatGUI
        //#endif

    /**
     * @return The current server information, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getCurrentServerInfo(): ServerInfo? = getInstance().currentServerEntry

    /**
     * @return The current network handler, null if the player is not in a server.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getNetworkHandler(): ClientPlayNetworkHandler? = getInstance().networkHandler

    /**
     * @return The sound manager instance, which controls the game's sound output.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getSoundManager(): SoundManager = getInstance().soundManager

    /**
     * @return The text renderer instance, which is used to render text on the screen.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    public fun getFontRenderer(): TextRenderer = getInstance().textRenderer

    /**
     * @return OmniCore's wrapper around Minecraft's texture manager, which is used to load, bind, and manage textures.
     *
     * @since 0.1.0
     * @author Deftu
     * @see OmniTextureManager
     */
    @JvmStatic
    public fun getTextureManager(): OmniTextureManager = OmniTextureManager.INSTANCE

    /**
     * Executes a task on Minecraft's main thread.
     *
     * @param runnable The task to execute.
     *
     * @since 0.1.0
     * @author Deftu
     * @see execute
     */
    @JvmStatic public fun execute(runnable: () -> Unit) {
        //#if MC >= 1.15.2
        getInstance().execute(runnable)
        //#else
        //$$ getInstance().addScheduledTask(runnable::invoke)
        //#endif
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
    @JvmStatic public fun execute(runnable: Runnable): Unit = execute(runnable::run)

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
        @JvmStatic public fun getServerBrand(): String? =
            //#if MC >= 1.20.2
            getNetworkHandler()?.brand
            //#else
            //$$ getPlayer()?.serverBrand
            //#endif

        /**
         * @return The server address, null if the player is not in a server.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic public fun getCurrentServerAddress(): String? = getCurrentServerInfo()?.address

        /**
         * @return Whether the player has multiplayer enabled, is allowed to join servers, and is not banned from multiplayer.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public fun isMultiplayerEnabled(): Boolean =
            //#if MC >= 1.19.2
            getInstance().isMultiplayerEnabled
            //#else
            //$$ true // TODO - Find a way to fetch this value in earlier versions
            //#endif

        /**
         * @return Whether the player is banned from multiplayer.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public fun isMultiplayerBanned(): Boolean =
            //#if MC >= 1.20.2
            getInstance().multiplayerBanDetails != null
            //#elseif MC >= 1.19.2
            //$$ getInstance().isMultiplayerBanned
            //#else
            //$$ false // TODO - Find a way to fetch this value in earlier versions
            //#endif

        /**
         * @return Whether the player is in a multiplayer environment.
         *
         * @since 0.1.0
         * @author Deftu
         */
        @JvmStatic
        public fun isInMultiplayer(): Boolean = getWorld() != null && getIntegratedServer() != null && isMultiplayerEnabled() && !isMultiplayerBanned() && run {
            if (getInstance().isInSingleplayer) return@run false

            val serverInfo = getInstance().currentServerEntry
            serverInfo?.address != null
        }

    }
}
