package dev.deftu.omnicore.api.client.render.pipeline

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.provider.ShaderProvider
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPipelineImpl
import net.minecraft.util.Identifier
import org.lwjgl.opengl.GL11
import java.util.function.Consumer

//#if MC >= 1.21.5
import com.mojang.blaze3d.opengl.GlStateManager
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.platform.LogicOp
import com.mojang.blaze3d.shaders.ShaderType
import dev.deftu.omnicore.api.ID
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformDefinition
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind
import dev.deftu.omnicore.api.identifierOrThrow
import net.minecraft.client.gl.GlCommandEncoder
import org.apache.commons.codec.digest.DigestUtils
//#else
//$$ import dev.deftu.omnicore.api.client.render.state.CullFace
//$$ import dev.deftu.omnicore.api.client.render.state.DepthFunction
//$$ import dev.deftu.omnicore.api.client.render.state.OmniColorMask
//$$ import dev.deftu.omnicore.api.client.render.state.OmniCullState
//$$ import dev.deftu.omnicore.api.client.render.state.OmniDepthState
//$$ import dev.deftu.omnicore.api.client.render.state.OmniPolygonOffset
//$$ import dev.deftu.omnicore.api.client.render.state.OmniRenderState
//$$ import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyLightingState
//$$ import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyRenderState
//$$ import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyRenderStates
//$$ import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyShadeModelState
//$$ import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyTextureState
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
    @JvmField public var depthTest: OmniRenderPipeline.DepthTest = OmniRenderPipeline.DepthTest.LESS_OR_EQUAL
    @JvmField public var culling: Boolean = false
    @JvmField public var colorLogic: OmniRenderPipeline.ColorLogic = OmniRenderPipeline.ColorLogic.NONE
    @JvmField public var blendState: OmniBlendState = OmniBlendState.DISABLED
    @JvmField public var colorMask: Pair<Boolean, Boolean> = true to true // rgb and alpha
    @JvmField public var depthMask: Boolean = true
    @JvmField public var polygonOffset: Pair<Float, Float> = 0f to 0f // factor and units

    // legacy
    @JvmField public var legacyEffects: LegacyEffects = LegacyEffects()

    public fun build(): OmniRenderPipeline {
        //#if MC >= 1.21.5
        var shaderSourcesFunction: ((Identifier, ShaderType) -> String?)? = null
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
                    val vertexLocation = identifierOrThrow(ID, "shader/generated/${DigestUtils.sha1Hex(shaderProvider.vertexSource)}")
                    val fragmentLocation = identifierOrThrow(ID, "shader/generated/${DigestUtils.sha1Hex(shaderProvider.fragmentSource)}")

                    shaderSourcesFunction = { id, _ ->
                        when (id) {
                            vertexLocation -> shaderProvider.vertexSource
                            fragmentLocation -> shaderProvider.fragmentSource
                            else -> null
                        }
                    }

                    withVertexShader(vertexLocation)
                    withFragmentShader(fragmentLocation)
                    shaderProvider.schema.samplers.map(UniformDefinition.Sampler::name).forEach(::withSampler)
                    for (uniform in shaderProvider.schema.uniforms) {
                        val name = uniform.name
                        val kind = uniform.kind
                        if (kind is UniformKind.Sampler) {
                            continue
                        }

                        withUniform(name, kind.vanilla)
                    }
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

        return OmniRenderPipelineImpl(location, drawMode, vertexFormat, vanilla, shaderProvider, shaderSourcesFunction)
        return OmniRenderPipelineImpl(
            builderSnapshotSnippet = OmniRenderPipeline.Snippet(
                location = null,
                vertexFormat = null,
                drawMode = null,
                depthTest = depthTest,
                culling = culling,
                colorLogic = colorLogic,
                blendState = blendState,
                colorMask = colorMask,
                depthMask = depthMask,
                polygonOffset = polygonOffset,
                legacyEffects = legacyEffects,
            ),
            location = location,
            drawMode = drawMode,
            vertexFormat = vertexFormat,
            vanilla = vanilla,
            shaderProvider = shaderProvider,
            shaderSourcesFunction = shaderSourcesFunction
        )
        //#else
        //$$ return OmniRenderPipelineImpl(
        //$$     builderSnapshotSnippet = OmniRenderPipeline.Snippet(
        //$$         location = null,
        //$$         vertexFormat = null,
        //$$         drawMode = null,
        //$$         depthTest = depthTest,
        //$$         culling = culling,
        //$$         colorLogic = colorLogic,
        //$$         blendState = blendState,
        //$$         colorMask = colorMask,
        //$$         depthMask = depthMask,
        //$$         polygonOffset = polygonOffset,
        //$$         legacyEffects = legacyEffects,
        //$$     ),
        //$$     location = location,
        //$$     drawMode = drawMode,
        //$$     vertexFormat = vertexFormat,
        //$$     shaderProvider = shaderProvider,
        //$$     requestedRenderState = OmniRenderState(
        //$$         blendState = blendState,
        //$$         depthState = OmniDepthState(
        //$$             isEnabled = depthTest != OmniRenderPipeline.DepthTest.DISABLED,
        //$$             function = when (depthTest) {
        //$$                 OmniRenderPipeline.DepthTest.DISABLED -> DepthFunction.LESS
        //$$                 OmniRenderPipeline.DepthTest.ALWAYS -> DepthFunction.ALWAYS
        //$$                 OmniRenderPipeline.DepthTest.EQUAL -> DepthFunction.EQUAL
        //$$                 OmniRenderPipeline.DepthTest.LESS_OR_EQUAL -> DepthFunction.LESS_OR_EQUAL
        //$$                 OmniRenderPipeline.DepthTest.LESS -> DepthFunction.LESS
        //$$                 OmniRenderPipeline.DepthTest.GREATER -> DepthFunction.GREATER
        //$$             },
        //$$             mask = depthMask
        //$$         ),
        //$$         cullState = OmniCullState(
        //$$             isEnabled = culling,
        //$$             mode = CullFace.BACK
        //$$         ),
        //$$         colorMaskState = OmniColorMask(
        //$$             red = colorMask.first,
        //$$             green = colorMask.first,
        //$$             blue = colorMask.first,
        //$$             alpha = colorMask.second
        //$$         ),
        //$$         polygonOffsetState = OmniPolygonOffset(
        //$$             isEnabled = polygonOffset.first != 0f || polygonOffset.second != 0f,
        //$$             factor = polygonOffset.first,
        //$$             units = polygonOffset.second
        //$$         ),
        //$$         legacyState = OmniLegacyRenderState(
        //$$             alphaState = legacyEffects.alpha,
        //$$             lightingState = OmniLegacyLightingState(legacyEffects.lighting),
        //$$             lineStippleState = OmniLegacyRenderStates.lineStipple.withState(legacyEffects.lineStipple),
        //$$             shadeModelState = OmniLegacyShadeModelState(legacyEffects.shadeModel),
        //$$             textureStates = legacyEffects.textureStates.map { (unit, state) ->
        //$$                 OmniLegacyTextureState(unit, state)
        //$$             }
        //$$         ),
        //$$     ),
        //$$ )
        //#endif
    }

    public fun applySnippet(vararg snippets: OmniRenderPipeline.Snippet): OmniRenderPipelineBuilder {
        for (snippet in snippets) {
            snippet.depthTest?.let { depthTest = it }
            snippet.culling?.let { culling = it }
            snippet.colorLogic?.let { colorLogic = it }
            snippet.blendState?.let { blendState = it }
            snippet.colorMask?.let { colorMask = it }
            snippet.depthMask?.let { depthMask = it }
            snippet.polygonOffset?.let { polygonOffset = it }
            snippet.legacyEffects?.let { legacyEffects = it }
        }

        return this
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

    public fun setLegacyEffects(effects: LegacyEffects): OmniRenderPipelineBuilder {
        this.legacyEffects = effects; return this
    }

    public fun configureLegacyEffects(configure: Consumer<LegacyEffects.Builder>): OmniRenderPipelineBuilder {
        this.legacyEffects = LegacyEffects.Builder(this.legacyEffects).apply(configure::accept).build()
        return this
    }

    public fun configureLegacyEffects(configure: LegacyEffects.Builder.() -> Unit): OmniRenderPipelineBuilder {
        this.legacyEffects = LegacyEffects.Builder(this.legacyEffects).apply(configure).build()
        return this
    }
}
