package dev.deftu.omnicore.client.render.state

import org.lwjgl.opengl.GL11

public enum class DepthFunction(
    public val value: Int
) {

    ALWAYS(GL11.GL_ALWAYS),
    NEVER(GL11.GL_NEVER),
    LESS(GL11.GL_LESS),
    EQUAL(GL11.GL_EQUAL),
    LESS_OR_EQUAL(GL11.GL_LEQUAL),
    GREATER(GL11.GL_GREATER),
    NOT_EQUAL(GL11.GL_NOTEQUAL),
    GREATER_OR_EQUAL(GL11.GL_GEQUAL);

    public companion object {

        @JvmStatic
        public fun active(): DepthFunction {
            return from(GL11.glGetInteger(GL11.GL_DEPTH_FUNC))
        }

        @JvmStatic
        @Suppress("EnumValuesSoftDeprecate")
        public fun from(value: Int): DepthFunction {
            return values().firstOrNull { it.value == value } ?: throw IllegalArgumentException("Invalid DepthFunction value: $value")
        }

    }

}
