package xyz.deftu.multi

//#if MC>=11400
import net.minecraft.client.util.GlfwUtil
//#endif

typealias MinecraftClient = net.minecraft.client.MinecraftClient

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
    fun getNetworkHandler() = getInstance().networkHandler
    @JvmStatic
    fun getSoundManager() = getInstance().soundManager
    @JvmStatic
    fun getFontRenderer() = getInstance().textRenderer
    @JvmStatic
    fun getOptions() = getInstance().options

    @JvmStatic
    fun getTime(): Long {
        //#if MC>=11400
        return (GlfwUtil.getTime() * 1000).toLong()
        //#else
        //$$ return MinecraftClient.getSystemTime()
        //#endif
    }
}
