package dev.deftu.omnicore.api.client

import com.mojang.blaze3d.systems.RenderSystem

//#if MC >= 1.16.5
import net.minecraft.client.util.GlfwUtil
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
        get() = RenderSystem.isOnRenderThread()

    @JvmStatic
    public fun runOnMain(block: Runnable) {
        client.execute(block)
    }

    @JvmStatic
    public fun runOnRender(block: Runnable) {
        RenderSystem.queueFencedTask(block)
    }
}
