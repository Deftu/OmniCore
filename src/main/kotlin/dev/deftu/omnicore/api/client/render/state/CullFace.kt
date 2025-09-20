package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.api.client.utils.GlConstEnum
import org.lwjgl.opengl.GL11

public enum class CullFace(override val const: Int) : GlConstEnum {
    FRONT(GL11.GL_FRONT),
    BACK(GL11.GL_BACK),
    FRONT_AND_BACK(GL11.GL_FRONT_AND_BACK);

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<CullFace> = CullFace.values().toList()

        @JvmStatic
        public fun findOrNull(const: Int): CullFace? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        @JvmStatic
        public fun findOrThrow(const: Int): CullFace {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}
