package dev.deftu.omnicore.api.client.textures

import dev.deftu.omnicore.api.client.utils.GlConstEnum
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL14

public enum class TextureWrap(override val const: Int) : GlConstEnum {
    REPEAT(GL11.GL_REPEAT),
    MIRRORED_REPEAT(GL14.GL_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE);

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<TextureWrap> = values().toList()

        public fun findOrNull(const: Int): TextureWrap? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        public fun findOrThrow(const: Int): TextureWrap {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}
