package dev.deftu.omnicore.internal.client.render.state

import org.jetbrains.annotations.ApiStatus

//#if MC <= 1.16.5
//$$ import com.mojang.blaze3d.platform.GlStateManager
//$$ import org.lwjgl.opengl.GL11
//#endif

@ApiStatus.Internal
public object AlphaStateInternals {
    @JvmStatic
    public val isEnabled: Boolean
        get() {
            //#if MC <= 1.16.5
            //$$ return GL11.glIsEnabled(GL11.GL_ALPHA_TEST)
            //#else
            return false
            //#endif
        }

    @JvmStatic
    public val func: Int
        get() {
            //#if MC <= 1.16.5
            //$$ return GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC)
            //#else
            return 0
            //#endif
        }

    @JvmStatic
    public val ref: Float
        get() {
            //#if MC <= 1.16.5
            //$$ return GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF)
            //#else
            return 0f
            //#endif
        }

    @JvmStatic
    public fun enable() {
        //#if MC <= 1.16.5
        //$$ GlStateManager.enableAlphaTest()
        //#endif
    }

    @JvmStatic
    public fun disable() {
        //#if MC <= 1.16.5
        //$$ GlStateManager.disableAlphaTest()
        //#endif
    }

    @JvmStatic
    public fun configure(func: Int, ref: Float) {
        //#if MC <= 1.16.5
        //$$ GlStateManager.alphaFunc(func, ref)
        //#endif
    }
}
