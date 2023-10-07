package dev.deftu.multi

//#if MC >= 1.14
import net.minecraft.client.util.GlfwUtil
//#endif

import net.minecraft.client.MinecraftClient

object MultiClient {
    @JvmStatic
    val isRunningOnMac: Boolean
        get() = MinecraftClient.IS_SYSTEM_MAC
    @JvmStatic
    val isRunningOnMainThread: Boolean
        //#if MC >= 1.15
        get() = getInstance().isOnThread
        //#else
        //$$ get() = getInstance().isCallingFromMinecraftThread()
        //#endif

    @JvmStatic
    fun getInstance() = MinecraftClient.getInstance()
    @JvmStatic
    fun getWorld() = getInstance().world
    @JvmStatic
    fun getServer() = getInstance().server
    @JvmStatic
    fun getPlayer() = getInstance().player
    @JvmStatic
    fun getHud() = getInstance().inGameHud
    @JvmStatic
    fun getChat() =
        //#if MC > 1.15
        getHud().chatHud
        //#else
        //$$ getHud().chatGUI
        //#endif
    @JvmStatic
    fun getCurrentServerInfo() = getInstance().currentServerEntry
    @JvmStatic
    fun getNetworkHandler() = getInstance().networkHandler
    @JvmStatic
    fun getSoundManager() = getInstance().soundManager
    @JvmStatic
    fun getFontRenderer() = getInstance().textRenderer
    @JvmStatic
    fun getOptions() = getInstance().options
    @JvmStatic
    fun getCurrentScreen() = getInstance().currentScreen
    @JvmStatic
    fun getTextureManager() = MultiTextureManager.INSTANCE

    @JvmStatic fun execute(runnable: () -> Unit) {
        //#if MC >= 1.15.2
        getInstance().execute(runnable)
        //#else
        //$$ getInstance().addScheduledTask(runnable::invoke)
        //#endif
    }

    @JvmStatic fun execute(runnable: Runnable) = execute(runnable::run)

    @JvmStatic
    fun getTime(): Long {
        //#if MC >= 1.14
        return (GlfwUtil.getTime() * 1000).toLong()
        //#else
        //$$ return Minecraft.getSystemTime()
        //#endif
    }

    object Multiplayer {
        @JvmStatic fun getServerBrand() =
            //#if MC >= 1.20.2
            getPlayer()?.networkHandler?.brand
            //#else
            //$$ getPlayer()?.serverBrand
            //#endif
        @JvmStatic fun getCurrentServerAddress() = getCurrentServerInfo()?.address

        @JvmStatic
        fun isMultiplayerEnabled() =
            //#if MC >= 1.19.2
            getInstance().isMultiplayerEnabled
            //#else
            //$$ true // TODO - Find a way to fetch this value in earlier versions
            //#endif

        @JvmStatic
        fun isMultiplayerBanned() =
            //#if MC >= 1.20.2
            getInstance().multiplayerBanDetails != null
            //#elseif MC >= 1.19.2
            //$$ getInstance().isMultiplayerBanned
            //#else
            //$$ false // TODO - Find a way to fetch this value in earlier versions
            //#endif

        @JvmStatic
        fun isInMultiplayer() = getWorld() != null && getServer() != null && isMultiplayerEnabled() && !isMultiplayerBanned() && run {
            if (getInstance().isInSingleplayer) return@run false

            val serverInfo = getInstance().currentServerEntry
            serverInfo?.address != null
        }
    }
}
