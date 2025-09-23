package dev.deftu.omnicore.api.client.textures

import dev.deftu.omnicore.api.client.utils.GlConstEnum
import org.lwjgl.opengl.GL11

public enum class TextureFilter(override val const: Int) : GlConstEnum {
    NEAREST(GL11.GL_NEAREST),
    LINEAR(GL11.GL_LINEAR),
    NEAREST_MIPMAP_NEAREST(GL11.GL_NEAREST_MIPMAP_NEAREST),
    LINEAR_MIPMAP_NEAREST(GL11.GL_LINEAR_MIPMAP_NEAREST),
    NEAREST_MIPMAP_LINEAR(GL11.GL_NEAREST_MIPMAP_LINEAR),
    LINEAR_MIPMAP_LINEAR(GL11.GL_LINEAR_MIPMAP_LINEAR);

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<TextureFilter> = values().toList()

        public fun findOrNull(const: Int): TextureFilter? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        public fun findOrThrow(const: Int): TextureFilter {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}
