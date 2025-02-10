package dev.deftu.omnicore.client.shaders

//#if MC >= 1.17 && MC <= 1.21.1
import net.minecraft.client.gl.GlBlendState
//#endif

//#if MC >= 1.15
import org.lwjgl.opengl.GL20
//#endif

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.render.OmniRenderState

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
@GameSide(Side.CLIENT)
public data class BlendState(
    public val equation: BlendEquation,
    public val srcRgb: BlendFactor,
    public val dstRgb: BlendFactor,
    public val srcAlpha: BlendFactor = srcRgb,
    public val dstAlpha: BlendFactor = dstRgb,
    public val enabled: Boolean = true
) {

    public companion object {

        @JvmField
        @GameSide(Side.CLIENT)
        public val DISABLED: BlendState = BlendState(BlendEquation.ADD, BlendFactor.ONE, BlendFactor.ZERO, enabled = false)

        @JvmField
        @GameSide(Side.CLIENT)
        public val NORMAL: BlendState = BlendState(BlendEquation.ADD, BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA)

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun active(): BlendState = BlendState(
            //#if MC >= 1.15
            BlendEquation.fromId(GL11.glGetInteger(GL20.GL_BLEND_EQUATION_RGB)) ?: BlendEquation.ADD,
            //#else
            //$$ BlendEquation.fromId(GL11.glGetInteger(GL14.GL_BLEND_EQUATION)) ?: BlendEquation.ADD,
            //#endif
            BlendFactor.fromId(GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB)) ?: BlendFactor.ONE,
            BlendFactor.fromId(GL11.glGetInteger(GL14.GL_BLEND_DST_RGB)) ?: BlendFactor.ZERO,
            BlendFactor.fromId(GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA)) ?: BlendFactor.ONE,
            BlendFactor.fromId(GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA)) ?: BlendFactor.ZERO,
            GL11.glGetBoolean(GL11.GL_BLEND)
        )

    }

    @GameSide(Side.CLIENT)
    public val separate: Boolean = srcRgb != srcAlpha || dstRgb != dstAlpha

    @GameSide(Side.CLIENT)
    public val separateSrc: Boolean = srcRgb != srcAlpha

    @GameSide(Side.CLIENT)
    public val separateDst: Boolean = dstRgb != dstAlpha

    //#if MC >= 1.17 && MC <= 1.21.1
    @GameSide(Side.CLIENT)
    private inner class VanillaBlendState : GlBlendState {
        constructor() : super()
        constructor(
            srcRgb: Int,
            dstRgb: Int,
            func: Int
        ) : super(srcRgb, dstRgb, func)
        constructor(
            srcRgb: Int,
            dstRgb: Int,
            srcAlpha: Int,
            dstAlpha: Int,
            func: Int
        ) : super(srcRgb, dstRgb, srcAlpha, dstAlpha, func)

        override fun enable() {
            super.enable()
            this@BlendState.apply()
        }
    }

    @GameSide(Side.CLIENT)
    public val vanilla: GlBlendState = if (enabled) {
        if (separate) VanillaBlendState(srcRgb.value, dstRgb.value, srcAlpha.value, dstAlpha.value, equation.value)
        else VanillaBlendState(srcRgb.value, dstRgb.value, equation.value)
    } else {
        VanillaBlendState()
    }

    @GameSide(Side.CLIENT)
    public fun activate(): Unit = vanilla.enable()
    //#else
    //$$ @GameSide(Side.CLIENT)
    //$$ public fun activate(): Unit = apply()
    //#endif

    private fun apply() {
        OmniRenderState.toggleBlend(enabled)
        OmniRenderState.setBlendEquation(equation.value)
        OmniRenderState.setBlendFuncSeparate(srcRgb.value, dstRgb.value, srcAlpha.value, dstAlpha.value)
    }

    @GameSide(Side.CLIENT)
    public enum class BlendEquation(
        internal val mcStr: String,
        internal val value: Int
    ) {
        ADD("add", GL14.GL_FUNC_ADD),
        SUBTRACT("subtract", GL14.GL_FUNC_SUBTRACT),
        REVERSE_SUBTRACT("reverse_subtract", GL14.GL_FUNC_REVERSE_SUBTRACT),
        MIN("min", GL14.GL_MIN),
        MAX("max", GL14.GL_MAX);

        public companion object {

            private val mappedToGlId = values().associateBy { it.value }

            @JvmStatic
            @GameSide(Side.CLIENT)
            public fun fromId(id: Int): BlendEquation? = mappedToGlId[id]

        }
    }

    @GameSide(Side.CLIENT)
    public enum class BlendFactor(
        internal val mcStr: String,
        internal val value: Int
    ) {
        ZERO("0", GL11.GL_ZERO),
        ONE("1", GL11.GL_ONE),
        SRC_COLOR("srccolor", GL11.GL_SRC_COLOR),
        ONE_MINUS_SRC_COLOR("1-srccolor", GL11.GL_ONE_MINUS_SRC_COLOR),
        DST_COLOR("dstcolor", GL11.GL_DST_COLOR),
        ONE_MINUS_DST_COLOR("1-dstcolor", GL11.GL_ONE_MINUS_DST_COLOR),
        SRC_ALPHA("srcalpha", GL11.GL_SRC_ALPHA),
        ONE_MINUS_SRC_ALPHA("1-srcalpha", GL11.GL_ONE_MINUS_SRC_ALPHA),
        DST_ALPHA("dstalpha", GL11.GL_DST_ALPHA),
        ONE_MINUS_DST_ALPHA("1-dstalpha", GL11.GL_ONE_MINUS_DST_ALPHA);

        public companion object {

            private val mappedToGlId = values().associateBy { it.value }

            @JvmStatic
            @GameSide(Side.CLIENT)
            public fun fromId(id: Int): BlendFactor? = mappedToGlId[id]

        }
    }
}
