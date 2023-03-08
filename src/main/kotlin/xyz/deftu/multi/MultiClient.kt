package xyz.deftu.multi

//#if MC>=11400
import net.minecraft.client.util.GlfwUtil
//#endif

import net.minecraft.client.MinecraftClient

object MultiClient {
    @JvmStatic
    val isRunningOnMac: Boolean
        get() = MinecraftClient.IS_SYSTEM_MAC
    @JvmStatic
    val isRunningOnMainThread: Boolean
        //#if MC>=11500
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
    fun getChat() = getHud().chatHud
    @JvmStatic
    fun getNetworkHandler() = getInstance().networkHandler
    @JvmStatic
    fun getSoundManager() = getInstance().soundManager
    @JvmStatic
    fun getFontRenderer() = getInstance().textRenderer
    @JvmStatic
    fun getOptions() = getInstance().options
    @JvmStatic
    fun getTextureManager() = MultiTextureManager.INSTANCE

    @JvmStatic fun execute(runnable: () -> Unit) {
        //#if MC>=11502
        getInstance().execute(runnable)
        //#else
        //$$ getInstance().addScheduledTask(runnable::invoke)
        //#endif
    }

    @JvmStatic fun execute(runnable: Runnable) = execute(runnable::run)

    @JvmStatic
    fun getTime(): Long {
        //#if MC>=11400
        return (GlfwUtil.getTime() * 1000).toLong()
        //#else
        //#if MC>=11400
        //$$ return MinecraftClient.getSystemTime()
        //#else
        //$$ return Minecraft.getSystemTime()
        //#endif
        //#endif
    }
}
