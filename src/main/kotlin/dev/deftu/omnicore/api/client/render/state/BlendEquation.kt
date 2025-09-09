package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.api.client.utils.GlConstEnum
import org.lwjgl.opengl.GL14

public enum class BlendEquation(
    public val equationName: String,
    override val const: Int
) : GlConstEnum {
    ADD("add", GL14.GL_FUNC_ADD),
    SUBTRACT("subtract", GL14.GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT("reverse_subtract", GL14.GL_FUNC_REVERSE_SUBTRACT),
    MIN("min", GL14.GL_MIN),
    MAX("max", GL14.GL_MAX);

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<BlendEquation> = BlendEquation.values().toList()

        @JvmStatic
        public fun findOrNull(const: Int): BlendEquation? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        @JvmStatic
        public fun findOrThrow(const: Int): BlendEquation {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}
