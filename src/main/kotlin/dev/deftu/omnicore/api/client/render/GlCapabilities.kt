package dev.deftu.omnicore.api.client.render

//#if MC >= 1.16.5
import org.lwjgl.opengl.GL
//#else
//$$ import org.lwjgl.opengl.GLContext
//#endif

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

public object GlCapabilities {
    @JvmStatic
    public val isGl21Available: Boolean
        get() {
            //#if MC >= 1.16.5
            return true
            //#else
            //$$ return OpenGlHelper.openGL21
            //#endif
        }

    @JvmStatic
    public val isGl3Available: Boolean
        get() {
            //#if MC >= 1.16.5
            return GL.getCapabilities().OpenGL30
            //#else
            //$$ return GLContext.getCapabilities().OpenGL30
            //#endif
        }

    @JvmStatic
    public val isFramebufferEnabled: Boolean
        get() {
            //#if MC >= 1.16.5
            return true
            //#else
            //$$ return OpenGlHelper.isFramebufferEnabled()
            //#endif
        }

    @JvmStatic
    public val isShaderSupported: Boolean
        get() {
            //#if MC >= 1.16.5
            return true
            //#else
            //$$ return OpenGlHelper.shadersSupported
            //#endif
        }
}
