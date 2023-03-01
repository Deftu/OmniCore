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
