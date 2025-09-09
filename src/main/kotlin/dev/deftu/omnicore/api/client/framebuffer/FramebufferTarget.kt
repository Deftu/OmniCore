package dev.deftu.omnicore.api.client.framebuffer

import org.lwjgl.opengl.ARBFramebufferObject
import org.lwjgl.opengl.EXTFramebufferBlit
import org.lwjgl.opengl.EXTFramebufferObject
import org.lwjgl.opengl.GL30

//#if MC >= 1.16.5
import org.lwjgl.opengl.GL
//#else
//$$ import org.lwjgl.opengl.GLContext
//#endif

public enum class FramebufferTarget {
    READ_WRITE,
    READ,
    WRITE;

    public val code: Int
        get() {
            //#if MC >= 1.16.5
            val caps = GL.getCapabilities()
            //#else
            //$$ val caps = GLContext.getCapabilities()
            //#endif
            if (caps.OpenGL30) {
                return when (this) {
                    READ -> GL30.GL_READ_FRAMEBUFFER
                    WRITE -> GL30.GL_DRAW_FRAMEBUFFER
                    READ_WRITE -> GL30.GL_FRAMEBUFFER
                }
            }

            if (caps.GL_ARB_framebuffer_object) {
                return when (this) {
                    READ -> ARBFramebufferObject.GL_READ_FRAMEBUFFER
                    WRITE -> ARBFramebufferObject.GL_DRAW_FRAMEBUFFER
                    READ_WRITE -> ARBFramebufferObject.GL_FRAMEBUFFER
                }
            }

            if (caps.GL_EXT_framebuffer_blit && caps.GL_EXT_framebuffer_object) {
                return when (this) {
                    READ -> EXTFramebufferBlit.GL_READ_FRAMEBUFFER_EXT
                    WRITE -> EXTFramebufferBlit.GL_DRAW_FRAMEBUFFER_EXT
                    READ_WRITE -> EXTFramebufferObject.GL_FRAMEBUFFER_EXT
                }
            }

            throw UnsupportedOperationException("Framebuffers are not supported on this context.")
        }

    public companion object {
        @JvmStatic
        @Suppress("EnumValuesSoftDeprecate")
        public fun from(code: Int): FramebufferTarget? {
            return values().firstOrNull { it.code == code }
        }
    }
}
