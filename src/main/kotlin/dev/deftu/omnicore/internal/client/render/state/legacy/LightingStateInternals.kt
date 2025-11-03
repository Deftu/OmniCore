package dev.deftu.omnicore.internal.client.render.state.legacy

import org.jetbrains.annotations.ApiStatus

//#if MC <= 1.16.5
//$$ import com.mojang.blaze3d.platform.GlStateManager
//$$ import org.lwjgl.opengl.GL11
//#endif

@ApiStatus.Internal
public object LightingStateInternals {
    @JvmStatic
    public val isEnabled: Boolean
        get() {
            //#if MC >= 1.17.1
            return true
            //#else
            //$$ return GL11.glIsEnabled(GL11.GL_LIGHTING)
            //#endif
        }

    @JvmStatic
    public fun enable() {
        //#if MC <= 1.16.5
        //$$ GlStateManager._enableLighting()
        //#endif
    }

    @JvmStatic
    public fun disable() {
        //#if MC <= 1.16.5
        //$$ GlStateManager._disableLighting()
        //#endif
    }
}
