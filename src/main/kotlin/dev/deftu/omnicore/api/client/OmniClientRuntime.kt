package dev.deftu.omnicore.api.client

//#if MC >= 1.16.5
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.util.GlfwUtil
//#else
//$$ import net.minecraft.client.Minecraft
//#endif

public object OmniClientRuntime {
    @JvmStatic
    public val nowMillis: Long
        get() {
            //#if MC >= 1.16.5
            return (GlfwUtil.getTime() * 1000).toLong()
            //#else
            //$$ return Minecraft.getSystemTime()
            //#endif
        }

    @JvmStatic
    public val isOnMainThread: Boolean
        get() = client.isOnThread

    @JvmStatic
    public val isOnRenderThread: Boolean
        get() {
            //#if MC >= 1.16.5
            return RenderSystem.isOnRenderThread()
            //#else
            //$$ return isOnMainThread
            //#endif
        }

    @JvmStatic
    public fun runOnMain(block: Runnable) {
        client.execute(block)
    }

    @JvmStatic
    public fun runOnRender(block: Runnable) {
        //#if MC >= 1.16.5
        RenderSystem.queueFencedTask(block)
        //#else
        //$$ runOnMain(block)
        //#endif
    }
}
