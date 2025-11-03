package dev.deftu.omnicore.internal.client.render.state

import dev.deftu.omnicore.api.client.render.state.DepthFunction
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11

//#if MC >= 1.21.5
import com.mojang.blaze3d.opengl.GlStateManager
//#elseif MC >= 1.17.1
//$$ import com.mojang.blaze3d.systems.RenderSystem
//#else
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

@ApiStatus.Internal
public object DepthStateInternals {
    @JvmStatic
    public val isEnabled: Boolean
        get() = GL11.glIsEnabled(GL11.GL_DEPTH_TEST)

    @JvmStatic
    public val func: DepthFunction
        get() = DepthFunction.findOrThrow(GL11.glGetInteger(GL11.GL_DEPTH_FUNC))

    @JvmStatic
    public val mask: Boolean
        get() = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK)

    @JvmStatic
    public fun enable() {
        //#if MC >= 1.21.5
        GlStateManager._enableDepthTest()
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.enableDepthTest()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager._enableDepthTest()
        //#else
        //$$ GlStateManager.enableDepth()
        //#endif
    }

    @JvmStatic
    public fun disable() {
        //#if MC >= 1.21.5
        GlStateManager._disableDepthTest()
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.disableDepthTest()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager._disableDepthTest()
        //#else
        //$$ GlStateManager.disableDepth()
        //#endif
    }

    @JvmStatic
    public fun func(func: Int) {
        //#if MC >= 1.21.5
        GlStateManager._depthFunc(func)
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.depthFunc(func)
        //#else
        //$$ GlStateManager._depthFunc(func)
        //#endif
    }

    @JvmStatic
    public fun mask(mask: Boolean) {
        //#if MC >= 1.21.5
        GlStateManager._depthMask(mask)
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.depthMask(mask)
        //#else
        //$$ GlStateManager._depthMask(mask)
        //#endif
    }
}
