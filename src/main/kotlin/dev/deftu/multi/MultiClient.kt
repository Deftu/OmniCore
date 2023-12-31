package dev.deftu.multi

//#if MC >= 1.16
import net.minecraft.client.util.GlfwUtil
//#endif

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

public object MultiClient {
    @JvmStatic
    public val isRunningOnMac: Boolean
        get() = MinecraftClient.IS_SYSTEM_MAC
    @JvmStatic
    public val isRunningOnMainThread: Boolean
        //#if MC >= 1.15
        get() = getInstance().isOnThread
        //#else
        //$$ get() = getInstance().isCallingFromMinecraftThread()
        //#endif

    @JvmStatic
    public fun getInstance(): MinecraftClient = MinecraftClient.getInstance()
    @JvmStatic
    public fun getWorld(): ClientWorld? = getInstance().world
    @JvmStatic
    public fun getServer(): IntegratedServer? = getInstance().server
    @JvmStatic
    public fun getPlayer(): ClientPlayerEntity? = getInstance().player
    @JvmStatic
    public fun getHud(): InGameHud = getInstance().inGameHud
    @JvmStatic
    public fun getChat(): ChatHud =
        //#if MC > 1.15
        getHud().chatHud
        //#else
        //$$ getHud().chatGUI
        //#endif
    @JvmStatic
    public fun getCurrentServerInfo(): ServerInfo? = getInstance().currentServerEntry
    @JvmStatic
    public fun getNetworkHandler(): ClientPlayNetworkHandler? = getInstance().networkHandler
    @JvmStatic
    public fun getSoundManager(): SoundManager = getInstance().soundManager
    @JvmStatic
    public fun getFontRenderer(): TextRenderer = getInstance().textRenderer
    @JvmStatic
    public fun getOptions(): GameOptions = getInstance().options
    @JvmStatic
    public fun getCurrentScreen(): Screen? = getInstance().currentScreen
    @JvmStatic
    public fun getTextureManager(): MultiTextureManager = MultiTextureManager.INSTANCE

    @JvmStatic public fun execute(runnable: () -> Unit) {
        //#if MC >= 1.15.2
        getInstance().execute(runnable)
        //#else
        //$$ getInstance().addScheduledTask(runnable::invoke)
        //#endif
    }

    @JvmStatic public fun execute(runnable: Runnable): Unit = execute(runnable::run)

    @JvmStatic
    public fun getTime(): Long {
        //#if MC >= 1.14
        return (GlfwUtil.getTime() * 1000).toLong()
        //#else
        //$$ return Minecraft.getSystemTime()
        //#endif
    }

    public object Multiplayer {
        @JvmStatic public fun getServerBrand(): String? =
            //#if MC >= 1.20.2
            getPlayer()?.networkHandler?.brand
            //#else
            //$$ getPlayer()?.serverBrand
            //#endif
        @JvmStatic public fun getCurrentServerAddress(): String? = getCurrentServerInfo()?.address

        @JvmStatic
        public fun isMultiplayerEnabled(): Boolean =
            //#if MC >= 1.19.2
            getInstance().isMultiplayerEnabled
            //#else
            //$$ true // TODO - Find a way to fetch this value in earlier versions
            //#endif

        @JvmStatic
        public fun isMultiplayerBanned(): Boolean =
            //#if MC >= 1.20.2
            getInstance().multiplayerBanDetails != null
            //#elseif MC >= 1.19.2
            //$$ getInstance().isMultiplayerBanned
            //#else
            //$$ false // TODO - Find a way to fetch this value in earlier versions
            //#endif

        @JvmStatic
        public fun isInMultiplayer(): Boolean = getWorld() != null && getServer() != null && isMultiplayerEnabled() && !isMultiplayerBanned() && run {
            if (getInstance().isInSingleplayer) return@run false

            val serverInfo = getInstance().currentServerEntry
            serverInfo?.address != null
        }
    }
}
