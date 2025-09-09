package dev.deftu.omnicore.api.client.render.state

import org.lwjgl.opengl.GL11

//#if MC >= 1.21.5
import com.mojang.blaze3d.platform.SourceFactor
import dev.deftu.omnicore.api.client.utils.GlConstEnum

//#endif

public enum class SrcFactor(
    public val shaderName: String,
    override val const: Int
) : GlConstEnum {
    CONSTANT_ALPHA("", GL11.GL_SRC_ALPHA),
    CONSTANT_COLOR("", GL11.GL_SRC_COLOR),
    DST_ALPHA("dstalpha", GL11.GL_DST_ALPHA),
    DST_COLOR("dstcolor", GL11.GL_DST_COLOR),
    ONE("1", GL11.GL_ONE),
    ONE_MINUS_CONSTANT_ALPHA("1-srcalpha", GL11.GL_ONE_MINUS_SRC_ALPHA),
    ONE_MINUS_CONSTANT_COLOR("", GL11.GL_ONE_MINUS_SRC_COLOR),
    ONE_MINUS_DST_ALPHA("1-dstalpha", GL11.GL_ONE_MINUS_DST_ALPHA),
    ONE_MINUS_DST_COLOR("1-dstcolor", GL11.GL_ONE_MINUS_DST_COLOR),
    ONE_MINUS_SRC_ALPHA("1-srcalpha", GL11.GL_ONE_MINUS_SRC_ALPHA),
    ONE_MINUS_SRC_COLOR("1-srccolor", GL11.GL_ONE_MINUS_SRC_COLOR),
    SRC_ALPHA("srcalpha", GL11.GL_SRC_ALPHA),
    SRC_ALPHA_SATURATE("", GL11.GL_SRC_ALPHA_SATURATE),
    SRC_COLOR("srccolor", GL11.GL_SRC_COLOR),
    ZERO("0", GL11.GL_ZERO);

    //#if MC >= 1.21.5
    public val vanilla: SourceFactor
        get() {
            return when (this) {
                CONSTANT_ALPHA -> SourceFactor.CONSTANT_ALPHA
                CONSTANT_COLOR -> SourceFactor.CONSTANT_COLOR
                DST_ALPHA -> SourceFactor.DST_ALPHA
                DST_COLOR -> SourceFactor.DST_COLOR
                ONE -> SourceFactor.ONE
                ONE_MINUS_CONSTANT_ALPHA -> SourceFactor.ONE_MINUS_CONSTANT_ALPHA
                ONE_MINUS_CONSTANT_COLOR -> SourceFactor.ONE_MINUS_CONSTANT_COLOR
                ONE_MINUS_DST_ALPHA -> SourceFactor.ONE_MINUS_DST_ALPHA
                ONE_MINUS_DST_COLOR -> SourceFactor.ONE_MINUS_DST_COLOR
                ONE_MINUS_SRC_ALPHA -> SourceFactor.ONE_MINUS_SRC_ALPHA
                ONE_MINUS_SRC_COLOR -> SourceFactor.ONE_MINUS_SRC_COLOR
                SRC_ALPHA -> SourceFactor.SRC_ALPHA
                SRC_ALPHA_SATURATE -> SourceFactor.SRC_ALPHA_SATURATE
                SRC_COLOR -> SourceFactor.SRC_COLOR
                ZERO -> SourceFactor.ZERO
            }
        }
    //#endif

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<SrcFactor> = SrcFactor.values().toList()

        @JvmStatic
        public fun findOrNull(const: Int): SrcFactor? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        @JvmStatic
        public fun findOrThrow(const: Int): SrcFactor {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}
