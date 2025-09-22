package dev.deftu.omnicore.api.client.render

//#if MC >= 1.16.5
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
//#else
//$$ import org.lwjgl.opengl.ContextCapabilities
//$$ import org.lwjgl.opengl.GLContext
//#endif

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//#endif

public object GlCapabilities {
    @JvmStatic
    public inline val gl:
            //#if MC >= 1.16.5
            GLCapabilities
            //#else
            //$$ ContextCapabilities
            //#endif
        get() {
            //#if MC >= 1.16.5
            return GL.getCapabilities()
            //#else
            //$$ return GLContext.getCapabilities()
            //#endif
        }

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
        get() = gl.OpenGL30

    @JvmStatic
    public val isGl31Available: Boolean
        get() = gl.OpenGL31

    @JvmStatic
    public val isGl32Available: Boolean
        get() = gl.OpenGL32

    @JvmStatic
    public val isGl33Available: Boolean
        get() = gl.OpenGL33

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
