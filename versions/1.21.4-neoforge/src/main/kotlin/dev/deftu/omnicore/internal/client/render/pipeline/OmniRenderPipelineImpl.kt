package dev.deftu.omnicore.internal.client.render.pipeline

import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelineBuilder
import dev.deftu.omnicore.api.client.render.provider.ShaderProvider
import dev.deftu.omnicore.api.client.render.shader.uniforms.SamplerTarget
import dev.deftu.omnicore.api.client.render.state.OmniRenderState
import dev.deftu.omnicore.api.client.render.vertex.OmniBuiltBuffer
import dev.deftu.omnicore.internal.client.textures.TextureInternals
import org.jetbrains.annotations.ApiStatus
import net.minecraft.resources.ResourceLocation

//#if MC >= 1.17.1
import com.mojang.blaze3d.systems.RenderSystem
//#endif

@ApiStatus.Internal
public class OmniRenderPipelineImpl(
    override val location: ResourceLocation,
    override val drawMode: DrawMode,
    override val vertexFormat: VertexFormat,
    private val shaderProvider: ShaderProvider?,
    internal val requestedRenderState: OmniRenderState
) : OmniRenderPipeline {
    internal fun draw(builtBuffer: OmniBuiltBuffer) {
        val vanillaBuiltBuffer = builtBuffer.vanilla
        //#if MC >= 1.19.2
        BufferUploader.drawWithShader(vanillaBuiltBuffer)
        //#elseif MC >= 1.18.2
        //$$ BufferRenderer.draw(vanillaBuiltBuffer)
        //#elseif MC >= 1.16.5
        //$$ BufferUploader.end(vanillaBuiltBuffer)
        //#else
        //$$ WorldVertexBufferUploader().draw(vanillaBuiltBuffer)
        //#endif

        //#if MC >= 1.19.2 && MC < 1.21.1
        //$$ builtBuffer.markClosed()
        //#endif
    }

    override fun bind() {
        shaderProvider?.bind(requestedRenderState.blendState)
    }

    override fun unbind() {
        shaderProvider?.unbind()
    }

    override fun newBuilder(): OmniRenderPipelineBuilder {
        checkNotNull(shaderProvider) { "This pipeline was not created with a ShaderProvider, so it cannot be rebuilt." }
        return OmniRenderPipelineBuilder(location, vertexFormat, drawMode, shaderProvider)
    }

    public fun texture(name: String, id: Int) {
        when (shaderProvider) {
            //#if MC >= 1.17.1
            is ShaderProvider.Vanilla -> {
                val index = name.removePrefix("Sampler").toIntOrNull() ?: return
                RenderSystem.setShaderTexture(index, id)
            }
            //#endif

            is ShaderProvider.Compatible -> shaderProvider.shader.sampler(name, SamplerTarget.TEX_2D).setTexture(id)
            else -> throw IllegalStateException("ShaderProvider is null")
        }
    }

    public fun texture(unit: OmniTextureUnit, id: Int) {
        when (shaderProvider) {
            //#if MC >= 1.17.1
            is ShaderProvider.Vanilla -> RenderSystem.setShaderTexture(unit.id, id)
            //#endif

            is ShaderProvider.Compatible -> shaderProvider.shader.sampler("Sampler${unit.id}", SamplerTarget.TEX_2D).setTexture(id)
            else -> TextureInternals.bindOnUnit(unit, id)
        }
    }

    public fun uniform(name: String, vararg values: Float) {
        when (shaderProvider) {
            //#if MC >= 1.17.1
            is ShaderProvider.Vanilla -> shaderProvider.supplier.get().getUniform(name)?.set(values)
            //#endif
            is ShaderProvider.Compatible -> {
                when (values.size) {
                    1 -> shaderProvider.shader.float1(name).set(values[0])
                    2 -> shaderProvider.shader.vec2f(name).set(values[0], values[1])
                    3 -> shaderProvider.shader.vec3f(name).set(values[0], values[1], values[2])
                    4 -> shaderProvider.shader.vec4f(name).set(values[0], values[1], values[2], values[3])
                    9, 16 -> {
                        val matrix = FloatArray(values.size)
                        for (i in values.indices) {
                            matrix[i] = values[i]
                        }

                        when (values.size) {
                            9 -> shaderProvider.shader.mat3f(name).set(matrix)
                            16 -> shaderProvider.shader.mat4f(name).set(matrix)
                        }
                    }
                    else -> throw IllegalArgumentException("Unsupported number of float values: ${values.size}")
                }
            }

            else -> throw IllegalStateException("ShaderProvider is null")
        }
    }

    public fun uniform(name: String, vararg values: Int) {
        when (shaderProvider) {
            //#if MC >= 1.17.1
            is ShaderProvider.Vanilla -> shaderProvider.supplier.get().getUniform(name)?.set(values[0])
            //#endif
            is ShaderProvider.Compatible -> {
                when (values.size) {
                    1 -> shaderProvider.shader.int1(name).set(values[0])
                    else -> throw IllegalArgumentException("Unsupported number of int values: ${values.size}")
                }
            }

            else -> throw IllegalStateException("ShaderProvider is null")
        }
    }
}
