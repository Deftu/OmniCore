package dev.deftu.omnicore.api.client.render.errors

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

public enum class GlError(public val const: Int) {
    NO_ERROR(GL11.GL_NO_ERROR),
    INVALID_ENUM(GL11.GL_INVALID_ENUM),
    INVALID_VALUE(GL11.GL_INVALID_VALUE),
    INVALID_OPERATION(GL11.GL_INVALID_OPERATION),
    STACK_OVERFLOW(GL11.GL_STACK_OVERFLOW),
    STACK_UNDERFLOW(GL11.GL_STACK_UNDERFLOW),
    OUT_OF_MEMORY(GL11.GL_OUT_OF_MEMORY),
    INVALID_FRAMEBUFFER_OPERATION(GL30.GL_INVALID_FRAMEBUFFER_OPERATION);

    public val isPresent: Boolean
        get() = this != NO_ERROR

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<GlError> = values().toList()

        @JvmStatic
        @get:JvmName("current")
        public val current: GlError
            get() {
                val errorCode = GL11.glGetError()
                return ALL.firstOrNull { it.const == errorCode } ?: NO_ERROR
            }

        @JvmStatic
        public fun drain(): GlError {
            var last: GlError
            do {
                last = current
            } while (last.isPresent)
            return last
        }

        @JvmStatic
        public fun from(const: Int): GlError {
            return ALL[const]
        }
    }
}
