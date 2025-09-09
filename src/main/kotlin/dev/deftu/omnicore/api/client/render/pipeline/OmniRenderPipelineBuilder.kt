package dev.deftu.omnicore.api.client.render.pipeline

import com.mojang.blaze3d.opengl.GlStateManager
import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.provider.ShaderProvider
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPipelineImpl
import net.minecraft.util.Identifier
import org.lwjgl.opengl.GL11

//#if MC >= 1.21.5
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.platform.LogicOp
import com.mojang.blaze3d.shaders.ShaderType
import net.minecraft.client.gl.GlCommandEncoder
//#endif

public class OmniRenderPipelineBuilder internal constructor(
    private val location: Identifier,
    private val vertexFormat: VertexFormat,
    private val drawMode: DrawMode,
    //#if MC >= 1.21.5
    private val shaderProvider: ShaderProvider,
    //#else
    //$$ private val shaderProvider: ShaderProvider?,
    //#endif
) {
    @JvmField public var depthTest: OmniRenderPipeline.DepthTest = OmniRenderPipeline.DepthTest.DISABLED
    @JvmField public var culling: Boolean = false
    @JvmField public var colorLogic: OmniRenderPipeline.ColorLogic = OmniRenderPipeline.ColorLogic.NONE
    @JvmField public var blendState: OmniBlendState = OmniBlendState.DISABLED
    @JvmField public var colorMask: Pair<Boolean, Boolean> = true to true // rgb and alpha
    @JvmField public var depthMask: Boolean = true
    @JvmField public var polygonOffset: Pair<Float, Float> = 0f to 0f // factor and units
    // TODO: lighting, alpha

    public fun build(): OmniRenderPipeline {
        //#if MC >= 1.21.5
        val shaderSourcesFunction: ((Identifier, ShaderType) -> String?)? = null
        var vanilla = RenderPipeline.builder().apply {
            withLocation(location)
            withVertexFormat(vertexFormat, drawMode.vanilla)

            withDepthTestFunction(when (depthTest) {
                OmniRenderPipeline.DepthTest.DISABLED -> DepthTestFunction.NO_DEPTH_TEST
                OmniRenderPipeline.DepthTest.ALWAYS -> DepthTestFunction.NO_DEPTH_TEST // manually implemented
                OmniRenderPipeline.DepthTest.EQUAL -> DepthTestFunction.EQUAL_DEPTH_TEST
                OmniRenderPipeline.DepthTest.LESS_OR_EQUAL -> DepthTestFunction.LEQUAL_DEPTH_TEST
                OmniRenderPipeline.DepthTest.LESS -> DepthTestFunction.LESS_DEPTH_TEST
                OmniRenderPipeline.DepthTest.GREATER -> DepthTestFunction.GREATER_DEPTH_TEST
            })

            withCull(culling)

            withColorLogic(when (colorLogic) {
                OmniRenderPipeline.ColorLogic.NONE -> LogicOp.NONE
                OmniRenderPipeline.ColorLogic.OR_REVERSE -> LogicOp.OR_REVERSE
            })

            if (blendState.isEnabled) {
                withBlend(BlendFunction(
                    blendState.function.srcColor.vanilla,
                    blendState.function.dstColor.vanilla,
                    blendState.function.srcAlpha.vanilla,
                    blendState.function.dstAlpha.vanilla,
                ))
            } else {
                withoutBlend()
            }

            colorMask.let { (colorMask, alphaMask) ->
                withColorWrite(colorMask, alphaMask)
            }

            withDepthWrite(depthMask)

            polygonOffset.let { (factor, units) ->
                withDepthBias(factor, units)
            }

            when (shaderProvider) {
                is ShaderProvider.Vanilla -> {
                    withVertexShader(shaderProvider.vertexLocation)
                    withFragmentShader(shaderProvider.fragmentLocation)
                    shaderProvider.samplers.forEach(::withSampler)
                    shaderProvider.uniforms.forEach(::withUniform)
                }

                is ShaderProvider.Compatible -> {
                    // TODO
                }
            }
        }.build()

        // Manually handle ALWAYS depth test
        if (depthTest == OmniRenderPipeline.DepthTest.ALWAYS) {
            abstract class WrappingPipeline(inner: RenderPipeline) : RenderPipeline(
                inner.location, inner.vertexShader, inner.fragmentShader,
                inner.shaderDefines, inner.samplers, inner.uniforms,
                inner.blendFunction,
                inner.depthTestFunction,
                inner.polygonMode,
                inner.isCull,
                inner.isWriteColor,
                inner.isWriteAlpha,
                inner.isWriteDepth,
                inner.colorLogic,
                inner.vertexFormat,
                inner.vertexFormatMode,
                inner.depthBiasScaleFactor,
                inner.depthBiasConstant,
                //#if MC >= 1.21.6
                inner.sortKey
                //#endif
            )

            vanilla = object : WrappingPipeline(vanilla) {
                val stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                override fun isCull(): Boolean {
                    if (stackWalker.callerClass == GlCommandEncoder::class.java) {
                        GlStateManager._enableDepthTest()
                        GlStateManager._depthFunc(GL11.GL_ALWAYS)
                    }

                    return super.isCull()
                }
            }
        }

        return OmniRenderPipelineImpl(location, vertexFormat, vanilla, shaderSourcesFunction)
        //#else
        //$$ TODO("Support for MC versions below 1.21.5")
        //#endif
    }

    public fun setDepthTest(mode: OmniRenderPipeline.DepthTest): OmniRenderPipelineBuilder {
        this.depthTest = mode; return this
    }

    public fun setCulling(enabled: Boolean): OmniRenderPipelineBuilder {
        this.culling = enabled; return this
    }

    public fun setColorLogic(mode: OmniRenderPipeline.ColorLogic): OmniRenderPipelineBuilder {
        this.colorLogic = mode; return this
    }

    public fun setBlendState(state: OmniBlendState): OmniRenderPipelineBuilder {
        this.blendState = state; return this
    }

    public fun setColorMask(writeRgb: Boolean, writeAlpha: Boolean): OmniRenderPipelineBuilder {
        this.colorMask = writeRgb to writeAlpha; return this
    }

    public fun setDepthMask(writeDepth: Boolean): OmniRenderPipelineBuilder {
        this.depthMask = writeDepth; return this
    }

    public fun setPolygonOffset(factor: Float, units: Float): OmniRenderPipelineBuilder {
        this.polygonOffset = factor to units; return this
    }
}
