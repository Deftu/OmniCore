package dev.deftu.omnicore.client.render.state

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14

public data class BlendFunction(
    public val srcColor: SrcFactor,
    public val dstColor: DstFactor,
    public val srcAlpha: SrcFactor,
    public val dstAlpha: DstFactor,
) {

    public val isSeparateSrc: Boolean
        get() = srcColor != srcAlpha

    public val isSeparateDst: Boolean
        get() = dstColor != dstAlpha

    public val isSeparate: Boolean
        get() = isSeparateSrc || isSeparateDst

    //#if MC >= 1.21.5
    public val vanilla: com.mojang.blaze3d.pipeline.BlendFunction
        get() = com.mojang.blaze3d.pipeline.BlendFunction(srcColor.vanilla, dstColor.vanilla, srcAlpha.vanilla, dstAlpha.vanilla)
    //#endif

    public companion object {

        @JvmField
        public val DEFAULT: BlendFunction = of(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA)

        @JvmField
        public val DISABLED: BlendFunction = of(SrcFactor.ONE, DstFactor.ZERO)

        @JvmField
        public val ADDITIVE: BlendFunction = of(SrcFactor.SRC_ALPHA, DstFactor.ONE)

        @JvmField
        public val MULTIPLY: BlendFunction = of(SrcFactor.DST_COLOR, DstFactor.ZERO)

        @JvmField
        public val SCREEN: BlendFunction = of(SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_COLOR)

        @JvmField
        public val SOFT_OVERLAY: BlendFunction = of(SrcFactor.SRC_COLOR, DstFactor.ONE_MINUS_SRC_COLOR)

        @JvmField
        public val MOJANG_OVERLAY: BlendFunction = BlendFunction(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ZERO)

        @JvmField
        public val DARKEN: BlendFunction = of(SrcFactor.DST_COLOR, DstFactor.ONE_MINUS_SRC_COLOR)

        @JvmField
        public val LIGHTEN: BlendFunction = of(SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_COLOR)

        @JvmField
        public val LIGHTMAP: BlendFunction = BlendFunction(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA)

        @JvmField
        public val ALPHA: BlendFunction = BlendFunction(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA)

        @JvmField
        public val ALPHA_OVERWRITE: BlendFunction = BlendFunction(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA, SrcFactor.ONE, DstFactor.ZERO)

        @JvmField
        public val PREMULTIPLIED: BlendFunction = of(SrcFactor.ONE, DstFactor.ONE_MINUS_SRC_ALPHA)

        @JvmStatic
        public fun active(): BlendFunction {
            return BlendFunction(
                srcColor = activeSrcColor(),
                dstColor = activeDstColor(),
                srcAlpha = activeSrcAlpha(),
                dstAlpha = activeDstAlpha()
            )
        }

        @JvmStatic
        public fun activeSrcColor(): SrcFactor {
            return SrcFactor.from(GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB))
        }

        @JvmStatic
        public fun activeDstColor(): DstFactor {
            return DstFactor.from(GL11.glGetInteger(GL14.GL_BLEND_DST_RGB))
        }

        @JvmStatic
        public fun activeSrcAlpha(): SrcFactor {
            return SrcFactor.from(GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA))
        }

        @JvmStatic
        public fun activeDstAlpha(): DstFactor {
            return DstFactor.from(GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA))
        }

        @JvmStatic
        public fun of(src: SrcFactor, dst: DstFactor): BlendFunction {
            return BlendFunction(src, dst, src, dst)
        }

    }

}
