package dev.deftu.omnicore.api.client.framebuffer

import org.lwjgl.opengl.GL30

public enum class FramebufferTarget {
    READ_WRITE,
    READ,
    WRITE;

    public val code: Int
        get() {
            return when (this) {
                READ -> GL30.GL_READ_FRAMEBUFFER
                WRITE -> GL30.GL_DRAW_FRAMEBUFFER
                READ_WRITE -> GL30.GL_FRAMEBUFFER
            }
        }

    public val binding: Int
        get() {
            return when (this) {
                READ -> GL30.GL_READ_FRAMEBUFFER_BINDING
                WRITE -> GL30.GL_DRAW_FRAMEBUFFER_BINDING
                READ_WRITE -> GL30.GL_FRAMEBUFFER_BINDING
            }
        }

    public companion object {
        @JvmStatic
        @Suppress("EnumValuesSoftDeprecate")
        public fun from(code: Int): FramebufferTarget? {
            return values().firstOrNull { it.code == code }
        }
    }
}
