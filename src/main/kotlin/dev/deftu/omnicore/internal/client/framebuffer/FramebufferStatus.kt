package dev.deftu.omnicore.internal.client.framebuffer

import org.lwjgl.opengl.ARBFramebufferObject
import org.lwjgl.opengl.EXTFramebufferObject
import org.lwjgl.opengl.GL30

//#if MC >= 1.16.5
import org.lwjgl.opengl.GL
//#else
//$$ import org.lwjgl.opengl.GLContext
//#endif

public enum class FramebufferStatus {
    COMPLETE,
    INCOMPLETE_ATTACHMENT,
    INCOMPLETE_MISSING_ATTACHMENT,
    INCOMPLETE_DRAW_BUFFER,
    INCOMPLETE_READ_BUFFER,
    UNSUPPORTED,
    INCOMPLETE_MULTISAMPLE,
    UNDEFINED,

    UNKNOWN;

    public val code: Int
        get() {
            //#if MC >= 1.16.5
            val caps = GL.getCapabilities()
            //#else
            //$$ val caps = GLContext.getCapabilities()
            //#endif
            if (caps.OpenGL30) return when (this) {
                COMPLETE -> GL30.GL_FRAMEBUFFER_COMPLETE
                INCOMPLETE_ATTACHMENT -> GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT
                INCOMPLETE_MISSING_ATTACHMENT -> GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT
                INCOMPLETE_DRAW_BUFFER -> GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER
                INCOMPLETE_READ_BUFFER -> GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER
                UNSUPPORTED -> GL30.GL_FRAMEBUFFER_UNSUPPORTED
                INCOMPLETE_MULTISAMPLE -> GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE
                UNDEFINED -> GL30.GL_FRAMEBUFFER_UNDEFINED
                UNKNOWN -> -1
            }

            if (caps.GL_ARB_framebuffer_object) return when (this) {
                COMPLETE -> ARBFramebufferObject.GL_FRAMEBUFFER_COMPLETE
                INCOMPLETE_ATTACHMENT -> ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT
                INCOMPLETE_MISSING_ATTACHMENT -> ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT
                INCOMPLETE_DRAW_BUFFER -> ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER
                INCOMPLETE_READ_BUFFER -> ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER
                UNSUPPORTED -> ARBFramebufferObject.GL_FRAMEBUFFER_UNSUPPORTED
                INCOMPLETE_MULTISAMPLE -> ARBFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE
                UNDEFINED -> 0
                UNKNOWN -> -1
            }

            if (caps.GL_EXT_framebuffer_object) return when (this) {
                COMPLETE -> EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT
                INCOMPLETE_ATTACHMENT -> EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT
                INCOMPLETE_MISSING_ATTACHMENT -> EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT
                INCOMPLETE_DRAW_BUFFER -> EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT
                INCOMPLETE_READ_BUFFER -> EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT
                UNSUPPORTED -> EXTFramebufferObject.GL_FRAMEBUFFER_UNSUPPORTED_EXT
                else -> -1
            }

            return -1
        }

    public companion object {
        @JvmStatic
        public fun from(code: Int): FramebufferStatus {
            return entries.firstOrNull { it.code == code } ?: UNKNOWN
        }
    }
}
