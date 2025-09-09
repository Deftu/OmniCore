package dev.deftu.omnicore.api.client.render.state

import dev.deftu.omnicore.api.client.utils.GlConstEnum
import org.lwjgl.opengl.GL11

//#if MC >= 1.21.5
import com.mojang.blaze3d.platform.DestFactor
//#endif

public enum class DstFactor(
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
    public val vanilla: DestFactor
        get() {
            return when (this) {
                CONSTANT_ALPHA -> DestFactor.CONSTANT_ALPHA
                CONSTANT_COLOR -> DestFactor.CONSTANT_COLOR
                DST_ALPHA -> DestFactor.DST_ALPHA
                DST_COLOR -> DestFactor.DST_COLOR
                ONE -> DestFactor.ONE
                ONE_MINUS_CONSTANT_ALPHA -> DestFactor.ONE_MINUS_CONSTANT_ALPHA
                ONE_MINUS_CONSTANT_COLOR -> DestFactor.ONE_MINUS_CONSTANT_COLOR
                ONE_MINUS_DST_ALPHA -> DestFactor.ONE_MINUS_DST_ALPHA
                ONE_MINUS_DST_COLOR -> DestFactor.ONE_MINUS_DST_COLOR
                ONE_MINUS_SRC_ALPHA -> DestFactor.ONE_MINUS_SRC_ALPHA
                ONE_MINUS_SRC_COLOR -> DestFactor.ONE_MINUS_SRC_COLOR
                SRC_ALPHA -> DestFactor.SRC_ALPHA
                SRC_COLOR -> DestFactor.SRC_COLOR
                ZERO -> DestFactor.ZERO
                else -> throw IllegalStateException("Unknown DstFactor: $this")
            }
        }
    //#endif

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<DstFactor> = DstFactor.values().toList()

        @JvmStatic
        public fun findOrNull(const: Int): DstFactor? {
            return GlConstEnum.findOrNull(ALL, const)
        }

        @JvmStatic
        public fun findOrThrow(const: Int): DstFactor {
            return GlConstEnum.findOrThrow(ALL, const)
        }
    }
}
