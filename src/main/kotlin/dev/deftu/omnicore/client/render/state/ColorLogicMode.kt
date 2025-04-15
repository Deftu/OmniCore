package dev.deftu.omnicore.client.render.state

import org.lwjgl.opengl.GL11

public enum class ColorLogicMode(
    public val value: Int
) {

    NONE(GL11.GL_NONE),
    CLEAR(GL11.GL_CLEAR),
    SET(GL11.GL_SET),
    COPY(GL11.GL_COPY),
    COPY_INVERTED(GL11.GL_COPY_INVERTED),
    NO_OP(GL11.GL_NOOP),
    INVERT(GL11.GL_INVERT),
    AND(GL11.GL_AND),
    NAND(GL11.GL_NAND),
    OR(GL11.GL_OR),
    NOR(GL11.GL_NOR),
    XOR(GL11.GL_XOR),
    EQUIV(GL11.GL_EQUIV),
    AND_REVERSE(GL11.GL_AND_REVERSE),
    AND_INVERTED(GL11.GL_AND_INVERTED),
    OR_REVERSE(GL11.GL_OR_REVERSE),
    OR_INVERTED(GL11.GL_OR_INVERTED);

    public companion object {

        @JvmStatic
        public fun active(): ColorLogicMode {
            return from(GL11.glGetInteger(GL11.GL_LOGIC_OP))
        }

        @JvmStatic
        @Suppress("EnumValuesSoftDeprecate")
        public fun from(value: Int): ColorLogicMode {
            return values().firstOrNull { it.value == value } ?: throw IllegalArgumentException("Invalid ColorLogicMode value: $value")
        }

    }

}
