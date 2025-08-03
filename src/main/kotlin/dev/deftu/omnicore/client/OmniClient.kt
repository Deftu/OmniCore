package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.render.OmniTextureManager
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
import net.minecraft.server.integrated.IntegratedServer

//#if MC >= 1.16.5
import net.minecraft.client.util.GlfwUtil
import net.minecraft.client.util.InputUtil
//#else
//$$ import net.minecraft.client.settings.GameSettings
//#endif

public object OmniClient {

    private var cachedLastWorld: OmniWorld? = null

    /**
     * @return True if the current working environment is on macOS, false otherwise.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isRunningOnMac: Boolean
        get() = MinecraftClient.IS_SYSTEM_MAC

    /**
     * @return True if the game is running on the main thread, false otherwise.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val isRunningOnMainThread: Boolean
        get() = getInstance().isOnThread

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
     * @return The sound manager instance, which controls the game's sound output.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public val soundManager: SoundManager?
        get() = getInstance().soundManager

    /**
     * @return The text renderer instance, which is used to render text on the screen.
     *
     * @since 0.1.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
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
    @GameSide(Side.CLIENT)
    public fun getTimeSinceStart(): Long {
        //#if MC >= 1.14
        return (GlfwUtil.getTime() * 1000).toLong()
        //#else
        //$$ return Minecraft.getSystemTime()
        //#endif
    }

    /**
     * @return The formatted string using Minecraft's built-in I18n.
     *
     * @since 0.16.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun translate(key: String, vararg args: Any): String {
        return I18n.translate(key, args)
    }

    /**
     * @return The display name of the key with the given code or scan code.
     *
     * @since 0.39.0
     * @author Deftu
     */
    @JvmStatic
    @JvmOverloads
    @GameSide(Side.CLIENT)
    public fun getKeyDisplayName(code: Int, scanCode: Int = -1): String {
        val name =
            //#if MC >= 1.16.5
            InputUtil.fromKeyCode(code, scanCode).toString()
            //#else
            //$$ GameSettings.getKeyDisplayString(code) ?: return "Unknown"
            //#endif

        return if (name.length == 1) name.first().uppercase() else name
    }

}
