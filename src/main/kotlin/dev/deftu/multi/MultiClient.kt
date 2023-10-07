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
        get() = dev.deftu.multi.MultiClient.getInstance().isOnThread
        //#else
        //$$ get() = getInstance().isCallingFromMinecraftThread()
        //#endif

    @JvmStatic
    fun getInstance() = MinecraftClient.getInstance()
    @JvmStatic
    fun getWorld() = dev.deftu.multi.MultiClient.getInstance().world
    @JvmStatic
    fun getServer() = dev.deftu.multi.MultiClient.getInstance().server
    @JvmStatic
    fun getPlayer() = dev.deftu.multi.MultiClient.getInstance().player
    @JvmStatic
    fun getHud() = dev.deftu.multi.MultiClient.getInstance().inGameHud
    @JvmStatic
    fun getChat() =
        //#if MC > 1.15
        dev.deftu.multi.MultiClient.getHud().chatHud
        //#else
        //$$ getHud().chatGUI
        //#endif
    @JvmStatic
    fun getCurrentServerInfo() = dev.deftu.multi.MultiClient.getInstance().currentServerEntry
    @JvmStatic
    fun getNetworkHandler() = dev.deftu.multi.MultiClient.getInstance().networkHandler
    @JvmStatic
    fun getSoundManager() = dev.deftu.multi.MultiClient.getInstance().soundManager
    @JvmStatic
    fun getFontRenderer() = dev.deftu.multi.MultiClient.getInstance().textRenderer
    @JvmStatic
    fun getOptions() = dev.deftu.multi.MultiClient.getInstance().options
    @JvmStatic
    fun getCurrentScreen() = dev.deftu.multi.MultiClient.getInstance().currentScreen
    @JvmStatic
    fun getTextureManager() = dev.deftu.multi.MultiTextureManager.Companion.INSTANCE

    @JvmStatic fun execute(runnable: () -> Unit) {
        //#if MC >= 1.15.2
        dev.deftu.multi.MultiClient.getInstance().execute(runnable)
        //#else
        //$$ getInstance().addScheduledTask(runnable::invoke)
        //#endif
    }

    @JvmStatic fun execute(runnable: Runnable) = dev.deftu.multi.MultiClient.execute(runnable::run)

    @JvmStatic
    fun getTime(): Long {
        //#if MC >= 1.14
        return (GlfwUtil.getTime() * 1000).toLong()
        //#else
        //$$ return Minecraft.getSystemTime()
        //#endif
    }

    object Multiplayer {
        @JvmStatic fun getServerBrand() = dev.deftu.multi.MultiClient.getPlayer()?.serverBrand
        @JvmStatic fun getCurrentServerAddress() = dev.deftu.multi.MultiClient.getCurrentServerInfo()?.address

        @JvmStatic
        fun isMultiplayerEnabled() =
            //#if MC >= 1.19.2
            dev.deftu.multi.MultiClient.getInstance().isMultiplayerEnabled
            //#else
            //$$ true // TODO - Find a way to fetch this value in earlier versions
            //#endif

        @JvmStatic
        fun isMultiplayerBanned() =
            //#if MC >= 1.19.2
            dev.deftu.multi.MultiClient.getInstance().isMultiplayerBanned
            //#else
            //$$ false // TODO - Find a way to fetch this value in earlier versions
            //#endif

        @JvmStatic
        fun isInMultiplayer() = dev.deftu.multi.MultiClient.getWorld() != null && dev.deftu.multi.MultiClient.getServer() != null && dev.deftu.multi.MultiClient.Multiplayer.isMultiplayerEnabled() && !dev.deftu.multi.MultiClient.Multiplayer.isMultiplayerBanned() && run {
            if (dev.deftu.multi.MultiClient.getInstance().isInSingleplayer) return@run false

            val serverInfo = dev.deftu.multi.MultiClient.getInstance().currentServerEntry
            serverInfo?.address != null
        }
    }
}
