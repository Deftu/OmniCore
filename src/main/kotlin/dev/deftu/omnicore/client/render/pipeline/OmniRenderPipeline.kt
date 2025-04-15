package dev.deftu.omnicore.client.render.pipeline

import net.minecraft.client.render.VertexFormat
import net.minecraft.util.Identifier

//#if MC >= 1.21.5
//$$ import com.mojang.blaze3d.pipeline.RenderPipeline
//$$ import com.mojang.blaze3d.shaders.ShaderType
//$$ import com.mojang.blaze3d.shaders.UniformType
//$$ import com.mojang.blaze3d.systems.RenderPass
//$$ import com.mojang.blaze3d.vertex.MeshData
//$$ import com.mojang.blaze3d.vertex.VertexFormatElement
//#else
import dev.deftu.omnicore.client.render.state.OmniManagedRenderState
import dev.deftu.omnicore.client.render.OmniTextureManager
import dev.deftu.omnicore.client.render.vertex.OmniBuiltBuffer
import net.minecraft.client.render.BufferRenderer
//#endif

//#if MC >= 1.21.2 && MC < 1.21.5
//$$ import dev.deftu.omnicore.client.OmniClient
//$$ import net.minecraft.client.gl.ShaderProgramKey
//#endif

//#if MC >= 1.17.1 && MC < 1.21.5
import net.minecraft.client.gl.ShaderProgram
import java.util.function.Supplier
//#endif

//#if MC >= 1.16.5
import com.mojang.blaze3d.systems.RenderSystem
//#endif

public class OmniRenderPipeline(
    public val identifier: Identifier,
    public val vertexFormat: VertexFormat,
    public val mode: DrawModes,
    //#if MC >= 1.21.5
    //$$ public val vanilla: RenderPipeline,
    //$$ private val shaderSourcesFunction: ((ResourceLocation, ShaderType) -> String?)?
    //#else
    private val shaderSource: ShaderSource?,
    internal val activeRenderState: OmniManagedRenderState,
    //#endif
) {

    //#if MC >= 1.21.5
    //$$ internal fun draw(renderPass: RenderPass, builtBuffer: MeshData) {
    //$$     if (shaderSourcesFunction != null) {
    //$$         RenderSystem.getDevice().precompilePipeline(vanilla, shaderSourcesFunction)
    //$$     }
    //$$
    //$$     renderPass.setPipeline(vanilla)
    //$$     renderPass.drawIndexed(0, builtBuffer.drawState().indexCount)
    //$$ }
    //#else
    internal fun draw(builtBuffer: OmniBuiltBuffer) {
        val vanillaBuiltBuffer = builtBuffer.vanilla
        //#if MC >= 1.19.2
        BufferRenderer.drawWithGlobalProgram(vanillaBuiltBuffer)
        //#elseif MC >= 1.18.2
        //$$ BufferRenderer.draw(vanillaBuiltBuffer)
        //#elseif MC >= 1.16.5
        //$$ BufferUploader.end(vanillaBuiltBuffer)
        //#else
        //$$ WorldVertexBufferUploader().draw(vanillaBuiltBuffer)
        //#endif

        //#if MC >= 1.19.2 && MC < 1.21
        builtBuffer.forceClose()
        //#endif
    }

    public fun bind() {
        shaderSource?.bind(activeRenderState.blendState)
    }

    public fun unbind() {
        shaderSource?.unbind()
    }

    public fun texture(name: String, glId: Int) {
        when (shaderSource) {
            is LegacyShaderSource -> shaderSource.shader.getSamplerUniformOrNull(name)?.setValue(glId)
            //#if MC >= 1.17.1
            is VanillaShaderSource -> {
                val index = name.removePrefix("Sampler").toIntOrNull() ?: return
                RenderSystem.setShaderTexture(index, glId)
                return
            }

            //#endif
            else -> throw IllegalArgumentException("Invalid shader source type")
        }
    }

    public fun texture(index: Int, glId: Int) {
        when (shaderSource) {
            is LegacyShaderSource -> shaderSource.shader.getSamplerUniformOrNull("Sampler$index")?.setValue(glId)
            //#if MC >= 1.17.1
            is VanillaShaderSource -> RenderSystem.setShaderTexture(index, glId)
            //#endif
            else -> OmniTextureManager.bindTexture(index, glId)
        }
    }

    public fun uniform(name: String, vararg values: Float) {
        when (shaderSource) {
            is LegacyShaderSource -> {
                val shader = shaderSource.shader
                when (values.size) {
                    1 -> shader.getVecUniformOrNull(name)?.setValue(values[0])
                    2 -> shader.getVec2UniformOrNull(name)?.setValue(values[0], values[1])
                    3 -> shader.getVec3UniformOrNull(name)?.setValue(values[0], values[1], values[2])
                    4 -> shader.getVec4UniformOrNull(name)?.setValue(values[0], values[1], values[2], values[3])
                    9, 16 -> shader.getMatrixUniformOrNull(name)?.setValue(values)
                    else -> throw UnsupportedOperationException("Provides too many values when defining a uniform: ${values.contentToString()}")
                }
            }

            //#if MC >= 1.17.1
            is VanillaShaderSource -> shaderSource.supplier.get().getUniform(name)?.set(values)
            //#endif

            else -> throw IllegalArgumentException("Invalid shader source type")
        }
    }

    public fun uniform(name: String, vararg values: Int) {
        when (shaderSource) {
            is LegacyShaderSource -> {
                val shader = shaderSource.shader
                when (values.size) {
                    1 -> shader.getIntUniformOrNull(name)?.setValue(values[0])
                    else -> throw UnsupportedOperationException("Provides too many values when defining a uniform: ${values.contentToString()}")
                }
            }

            //#if MC >= 1.17.1
            is VanillaShaderSource -> shaderSource.supplier.get().getUniform(name)?.set(values[0])
            //#endif

            else -> throw IllegalArgumentException("Invalid shader source type")
        }
    }
    //#endif

    public companion object {

        //#if MC >= 1.21.5
        //$$ @JvmStatic
        //$$ public fun builder(
        //$$     identifier: ResourceLocation,
        //$$     vertexFormat: VertexFormat,
        //$$     mode: DrawModes,
        //$$     vertexIdentifier: ResourceLocation,
        //$$     fragmentIdentifier: ResourceLocation,
        //$$     samplers: List<String>,
        //$$     uniforms: Map<String, UniformType>,
        //$$ ): OmniRenderPipelineBuilder {
        //$$     return OmniRenderPipelineBuilder(
        //$$         identifier = identifier,
        //$$         vertexFormat = vertexFormat,
        //$$         mode = mode,
        //$$         shaderSource = VanillaShaderSource(
        //$$             vertexIdentifier = vertexIdentifier,
        //$$             fragmentIdentifier = fragmentIdentifier,
        //$$             samplers = samplers,
        //$$             uniforms = uniforms
        //$$         )
        //$$     )
        //$$ }
        //#else

        //#if MC >= 1.21.2
        //$$ @JvmStatic
        //$$ public fun builder(
        //$$     identifier: Identifier,
        //$$     vertexFormat: VertexFormat,
        //$$     mode: DrawModes,
        //$$     shader: ShaderProgramKey?
        //$$ ): OmniRenderPipelineBuilder {
        //$$     val supplier = Supplier { OmniClient.getInstance().shaderLoader.getProgramToLoad(shader) }
        //$$     return OmniRenderPipelineBuilder(
        //$$         identifier = identifier,
        //$$         vertexFormat = vertexFormat,
        //$$         mode = mode,
        //$$         shaderSource = VanillaShaderSource(supplier)
        //$$     )
        //$$ }
        //#endif

        //#if MC >= 1.17.1
        @JvmStatic
        public fun builder(
            identifier: Identifier,
            vertexFormat: VertexFormat,
            mode: DrawModes,
            shader: Supplier<ShaderProgram>?
        ): OmniRenderPipelineBuilder {
            return OmniRenderPipelineBuilder(
                identifier = identifier,
                vertexFormat = vertexFormat,
                mode = mode,
                shaderSource = shader?.let(::VanillaShaderSource)
            )
        }
        //#endif

        //#endif

        @JvmStatic
        public fun builderWithDefaultShader(
            identifier: Identifier,
            vertexFormat: VertexFormat,
            mode: DrawModes,
        ): OmniRenderPipelineBuilder {
            //#if MC >= 1.17.1
            val shader = DefaultShaders[vertexFormat]
            //#if MC >= 1.21.5
            //$$ shader ?: throw IllegalArgumentException("No default shader found for vertex format: $vertexFormat")
            //$$ val samplers = List(vertexFormat.elements.count { it.usage == VertexFormatElement.Usage.UV }) { i -> "Sampler$i" }
            //$$ val uniforms = mapOf(
            //$$     "ModelViewMat" to UniformType.MATRIX4X4,
            //$$     "ProjMat" to UniformType.MATRIX4X4,
            //$$     "ColorModulator" to UniformType.VEC4,
            //$$ )
            //$$
            //$$ return builder(identifier, vertexFormat, mode, shader, shader, samplers, uniforms)
            //#else
            return builder(identifier, vertexFormat, mode, shader)
            //#endif
            //#else
            //$$ return OmniRenderPipelineBuilder(
            //$$     identifier = identifier,
            //$$     vertexFormat = vertexFormat,
            //$$     mode = mode,
            //$$     shaderSource = null
            //$$ )
            //#endif
        }

        @JvmStatic
        public fun builderWithDefaultShader(
            identifier: Identifier,
            vertexFormat: VertexFormats,
            mode: DrawModes,
        ): OmniRenderPipelineBuilder {
            return builderWithDefaultShader(identifier, vertexFormat.vanilla, mode)
        }

        @JvmStatic
        public fun builderWithLegacyShader(
            identifier: Identifier,
            vertexFormat: VertexFormat,
            mode: DrawModes,
            vertexSource: String,
            fragmentSource: String,
        ): OmniRenderPipelineBuilder {
            return OmniRenderPipelineBuilder(
                identifier = identifier,
                vertexFormat = vertexFormat,
                mode = mode,
                shaderSource = LegacyShaderSource(
                    vertexFormat = vertexFormat,
                    vertexSource = vertexSource,
                    fragmentSource = fragmentSource,
                )
            )
        }

        @JvmStatic
        public fun builderWithLegacyShader(
            identifier: Identifier,
            vertexFormat: VertexFormats,
            mode: DrawModes,
            vertexSource: String,
            fragmentSource: String,
        ): OmniRenderPipelineBuilder {
            return builderWithLegacyShader(identifier, vertexFormat.vanilla, mode, vertexSource, fragmentSource)
        }

    }

}
