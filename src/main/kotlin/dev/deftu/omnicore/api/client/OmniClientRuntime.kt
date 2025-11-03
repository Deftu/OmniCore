package dev.deftu.omnicore.api.client

//#if MC >= 1.16.5
import com.mojang.blaze3d.Blaze3D
import com.mojang.blaze3d.systems.RenderSystem
//#else
//$$ import net.minecraft.client.Minecraft
//#endif

public object OmniClientRuntime {
    @JvmStatic
    public val nowMillis: Long
        get() {
            //#if MC >= 1.16.5
            return (Blaze3D.getTime() * 1000).toLong()
            //#else
            //$$ return Minecraft.getSystemTime()
            //#endif
        }

    @JvmStatic
    public val isOnMainThread: Boolean
        get() = client.isSameThread

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
        if (isOnMainThread) {
            block.run()
            return
        }

        client.execute(block)
    }

    @JvmStatic
    public fun runOnRender(block: Runnable) {
        if (isOnRenderThread) {
            block.run()
            return
        }

        //#if MC >= 1.21.5
        RenderSystem.queueFencedTask(block)
        //#elseif MC >= 1.16.5
        //$$ RenderSystem.recordRenderCall { block.run() }
        //#else
        //$$ runOnMain(block)
        //#endif
    }

    @JvmStatic
    public fun assertMainThread() {
        check(isOnMainThread) { "Not on main thread" }
    }

    @JvmStatic
    public fun assertRenderThread() {
        check(isOnRenderThread) { "Not on render thread" }
    }
}
