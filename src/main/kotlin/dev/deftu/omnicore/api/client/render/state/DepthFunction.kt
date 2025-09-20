package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.api.client.utils.GlConstEnum
import org.lwjgl.opengl.GL11

public enum class DepthFunction(override val const: Int) : GlConstEnum {
    ALWAYS(GL11.GL_ALWAYS),
    NEVER(GL11.GL_NEVER),
    LESS(GL11.GL_LESS),
    EQUAL(GL11.GL_EQUAL),
    LESS_OR_EQUAL(GL11.GL_LEQUAL),
    GREATER(GL11.GL_GREATER),
    NOT_EQUAL(GL11.GL_NOTEQUAL),
    GREATER_OR_EQUAL(GL11.GL_GEQUAL);

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<DepthFunction> = DepthFunction.values().toList()

        @JvmStatic
        public fun findOrNull(const: Int): DepthFunction? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        @JvmStatic
        public fun findOrThrow(const: Int): DepthFunction {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}
