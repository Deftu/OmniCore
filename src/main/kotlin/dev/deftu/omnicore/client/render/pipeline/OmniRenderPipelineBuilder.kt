package dev.deftu.omnicore.client.render.pipeline

import dev.deftu.omnicore.client.render.state.*
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.util.Identifier

//#if MC >= 1.21.5
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.platform.LogicOp
import com.mojang.blaze3d.shaders.ShaderType
import dev.deftu.omnicore.client.shaders.ShaderProcessor
import dev.deftu.omnicore.common.OmniIdentifier
import org.apache.commons.codec.digest.DigestUtils
//#else
//$$ import com.mojang.blaze3d.vertex.VertexFormatElement
//#endif

public class OmniRenderPipelineBuilder internal constructor(
    private val identifier: Identifier,
    private val vertexFormat: VertexFormat,
    private val mode: DrawModes,
    //#if MC >= 1.21.5
    private val shaderSource: ShaderSource
    //#else
    //$$ private val shaderSource: ShaderSource?
    //#endif
) {

    @JvmField
    public var blendState: OmniManagedBlendState = OmniManagedBlendState.DISABLED

    @JvmField
    public var depthState: OmniManagedDepthState = OmniManagedDepthState.DISABLED

    @JvmField
    public var alphaState: OmniManagedAlphaState = OmniManagedAlphaState.DISABLED

    @JvmField
    public var colorLogic: OmniManagedColorLogic = OmniManagedColorLogic.DISABLED

    @JvmField
    public var colorMask: OmniManagedColorMask = OmniManagedColorMask.DEFAULT

    @JvmField
    public var polygonOffset: OmniManagedPolygonOffset = OmniManagedPolygonOffset.DISABLED

    @JvmField
    public var isCullFace: Boolean = false

    public fun build(): OmniRenderPipeline {
        //#if MC >= 1.21.5
        var shaderSourcesFunction: ((Identifier, ShaderType) -> String?)? = null
        var vanillaPipeline = RenderPipeline.builder().apply {
            withLocation(identifier)
            withVertexFormat(vertexFormat, mode.vanilla)
            withDepthWrite(depthState.isMask)
            withCull(isCullFace)

            val isWritingColor = colorMask.red || colorMask.green || colorMask.blue
            val isWritingAlpha = colorMask.alpha
            withColorWrite(isWritingColor, isWritingAlpha)

            if (blendState.isEnabled) {
                withBlend(blendState.function.vanilla)
            } else {
                withoutBlend()
            }

            if (polygonOffset.isEnabled) {
                withDepthBias(polygonOffset.factor, polygonOffset.units)
            }

            withDepthTestFunction(when (depthState.function) {
                DepthFunction.LESS -> DepthTestFunction.LESS_DEPTH_TEST
                DepthFunction.EQUAL -> DepthTestFunction.EQUAL_DEPTH_TEST
                DepthFunction.LESS_OR_EQUAL -> DepthTestFunction.LEQUAL_DEPTH_TEST
                DepthFunction.GREATER -> DepthTestFunction.GREATER_DEPTH_TEST

                else -> DepthTestFunction.NO_DEPTH_TEST
            })

            withColorLogic(when (colorLogic.mode) {
                ColorLogicMode.OR_REVERSE -> LogicOp.OR_REVERSE

                else -> LogicOp.NONE
            })

            when (shaderSource) {
                is LegacyShaderSource -> {
                    val processor = ShaderProcessor(vertexFormat, 150)
                    val transformedVertexSource = processor.transform(shaderSource.vertexSource)
                    val vertexIdentifier = OmniIdentifier.create("omnicore", "shader/generated/${DigestUtils.sha1Hex(transformedVertexSource)}")
                    val transformedFragmentSource = processor.transform(shaderSource.fragmentSource)
                    val fragmentIdentifier = OmniIdentifier.create("omnicore", "shader/generated/${DigestUtils.sha1Hex(transformedFragmentSource)}")

                    shaderSourcesFunction = { id, type ->
                        when (id) {
                            vertexIdentifier -> transformedVertexSource
                            fragmentIdentifier -> transformedFragmentSource
                            else -> null
                        }
                    }

                    withVertexShader(vertexIdentifier)
                    withFragmentShader(fragmentIdentifier)

                    processor.samplers.forEach(::withSampler)
                    processor.uniforms.forEach { (name, type) -> withUniform(name, type.vanilla) }
                }

                is VanillaShaderSource -> {
                    withVertexShader(shaderSource.vertexIdentifier)
                    withFragmentShader(shaderSource.fragmentIdentifier)
                    shaderSource.samplers.forEach(::withSampler)
                    shaderSource.uniforms.forEach(::withUniform)
                    // No need to set the shader sources function here as we assume the provided identifiers are actual resources which the game can load on its own
                }
            }
        }.build()

        return OmniRenderPipeline(
            identifier = identifier,
            vertexFormat = vertexFormat,
            vanilla = vanillaPipeline,
            shaderSourcesFunction = shaderSourcesFunction
        )
        //#else
        //$$ return OmniRenderPipeline(
        //$$     identifier = identifier,
        //$$     vertexFormat = vertexFormat,
        //$$     shaderSource = shaderSource,
        //$$     activeRenderState = OmniManagedRenderState(
        //$$         blendState = blendState,
        //$$         depthState = depthState,
        //$$         alphaState = alphaState,
        //$$         colorLogic = colorLogic,
        //$$         colorMask = colorMask,
        //$$         polygonOffset = polygonOffset,
        //$$         isCullFace = isCullFace,
        //$$         textureStates = mutableListOf(false, false).apply {
        //$$             for (element in vertexFormat.elements) {
        //$$                 if (element.usage != VertexFormatElement.Usage.UV) {
        //$$                     continue
        //$$                 }
        //$$
        //$$                 while (element.index !in indices) {
        //$$                     add(false)
        //$$                 }
        //$$
        //$$                 this[element.index] = true
        //$$             }
        //$$         }
        //$$     )
        //$$ )
        //#endif
    }

}
