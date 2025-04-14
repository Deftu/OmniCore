package dev.deftu.omnicore.client.render.state

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14
import org.lwjgl.opengl.GL20

@GameSide(Side.CLIENT)
public enum class BlendEquation(
    internal val equationName: String,
    internal val value: Int
) {
    ADD("add", GL14.GL_FUNC_ADD),
    SUBTRACT("subtract", GL14.GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT("reverse_subtract", GL14.GL_FUNC_REVERSE_SUBTRACT),
    MIN("min", GL14.GL_MIN),
    MAX("max", GL14.GL_MAX);

    public companion object {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun active(): BlendEquation {
            //#if MC >= 1.16.5
            val value = GL11.glGetInteger(GL20.GL_BLEND_EQUATION_RGB)
            //#else
            //$$ val value = GL11.glGetInteger(GL14.GL_BLEND_EQUATION)
            //#endif

            return from(value)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        @Suppress("EnumValuesSoftDeprecate")
        public fun from(value: Int): BlendEquation {
            return values().firstOrNull { it.value == value } ?: throw IllegalArgumentException("Invalid BlendEquation value: $value")
        }

    }
}
