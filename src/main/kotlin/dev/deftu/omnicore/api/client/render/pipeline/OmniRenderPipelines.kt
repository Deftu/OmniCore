package dev.deftu.omnicore.api.client.render.pipeline

import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.provider.ShaderProvider
import dev.deftu.omnicore.api.client.render.shader.ShaderSchema
import dev.deftu.omnicore.internal.internalLocationOf
import dev.deftu.omnicore.internal.client.render.pipeline.OmniRenderPipelineImpl
import net.minecraft.resources.ResourceLocation

//#if MC >= 1.21.5
import com.mojang.blaze3d.pipeline.RenderPipeline
import dev.deftu.omnicore.api.client.render.DrawModes
import com.mojang.blaze3d.shaders.UniformType
//#else
//#if MC >= 1.21.2
//$$ import dev.deftu.omnicore.api.client.client
//$$ import net.minecraft.client.renderer.ShaderProgram
//#endif
//$$
//#if MC >= 1.17.1
//$$ import net.minecraft.client.renderer.CompiledShaderProgram
//$$ import java.util.function.Supplier
//#endif
//#endif

//#if MC >= 1.17.1
import dev.deftu.omnicore.internal.client.render.DefaultShaders
//#endif

public object OmniRenderPipelines {
    @JvmField
    public val POSITION: OmniRenderPipeline = builderWithDefaultShader(
        location = internalLocationOf("pipeline/position"),
        snippets = arrayOf(OmniRenderPipelineSnippets.POSITION)
    ).build()

    @JvmField
    public val POSITION_COLOR: OmniRenderPipeline = builderWithDefaultShader(
        location = internalLocationOf("pipeline/position_color"),
        snippets = arrayOf(OmniRenderPipelineSnippets.POSITION_COLOR)
    ).build()

    @JvmField
    public val POSITION_COLOR_TRIANGLES: OmniRenderPipeline = builderWithDefaultShader(
        location = internalLocationOf("pipeline/position_color_triangles"),
        snippets = arrayOf(OmniRenderPipelineSnippets.POSITION_COLOR_TRIANGLES)
    ).build()

    @JvmField
    public val TEXTURED: OmniRenderPipeline = builderWithDefaultShader(
        location = internalLocationOf("pipeline/textured"),
        snippets = arrayOf(OmniRenderPipelineSnippets.POSITION_TEXTURE_COLOR)
    ).build()

    @JvmField
    public val LINES: OmniRenderPipeline = builderWithDefaultShader(
        location = internalLocationOf("pipeline/lines"),
        snippets = arrayOf(OmniRenderPipelineSnippets.LINES)
    ).build()

    //#if MC >= 1.21.5
    @JvmStatic
    public fun wrap(vanilla: RenderPipeline): OmniRenderPipeline {
        return OmniRenderPipelineImpl(
            builderSnapshotSnippet = null,
            location = vanilla.location,
            drawMode = DrawModes.from(vanilla.vertexFormatMode),
            vertexFormat = vanilla.vertexFormat,
            vanilla = vanilla,
            shaderProvider = deriveShaderProvider(vanilla),
            shaderSourcesFunction = null
        )
    }

    @JvmStatic
    public fun builderFrom(vanilla: RenderPipeline): OmniRenderPipelineBuilder {
        return builder(
            location = vanilla.location,
            vertexFormat = vanilla.vertexFormat,
            drawMode = DrawModes.from(vanilla.vertexFormatMode),
            shaderProvider = deriveShaderProvider(vanilla)
        )
    }

    private fun deriveShaderProvider(vanilla: RenderPipeline): ShaderProvider {
        return ShaderProvider.Vanilla(
            vertexLocation = vanilla.vertexShader,
            fragmentLocation = vanilla.fragmentShader,
            samplers = vanilla.samplers,
            uniforms = vanilla.uniforms.associate { description ->
                description.name to description.type
            }
        )
    }

    @JvmStatic
    public fun builder(
        location: ResourceLocation,
        vertexFormat: VertexFormat,
        drawMode: DrawMode,
        vertexLocation: ResourceLocation,
        fragmentLocation: ResourceLocation,
        samplers: List<String>,
        uniforms: Map<String, UniformType>,
    ): OmniRenderPipelineBuilder {
        return OmniRenderPipelineBuilder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            shaderProvider = ShaderProvider.Vanilla(
                vertexLocation = vertexLocation,
                fragmentLocation = fragmentLocation,
                samplers = samplers,
                uniforms = uniforms
            )
        )
    }

    @JvmStatic
    public fun builder(
        location: ResourceLocation,
        vertexLocation: ResourceLocation,
        fragmentLocation: ResourceLocation,
        samplers: List<String>,
        uniforms: Map<String, UniformType>,
        vararg snippets: OmniRenderPipeline.Snippet,
    ): OmniRenderPipelineBuilder {
        val (vertexFormat, drawMode) = extractSnippet(snippet(*snippets))
        return builder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            vertexLocation = vertexLocation,
            fragmentLocation = fragmentLocation,
            samplers = samplers,
            uniforms = uniforms
        ).applySnippet(*snippets)
    }

    @JvmStatic
    public fun builder(
        vertexLocation: ResourceLocation,
        fragmentLocation: ResourceLocation,
        samplers: List<String>,
        uniforms: Map<String, UniformType>,
        vararg snippets: OmniRenderPipeline.Snippet,
    ): OmniRenderPipelineBuilder {
        val (location, vertexFormat, drawMode) = extractAllSnippet(snippet(*snippets))
        return builder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            vertexLocation = vertexLocation,
            fragmentLocation = fragmentLocation,
            samplers = samplers,
            uniforms = uniforms
        ).applySnippet(*snippets)
    }
    //#else
    //#if MC >= 1.21.2
    //$$ @JvmStatic
    //$$ public fun builder(
    //$$     location: ResourceLocation,
    //$$     vertexFormat: VertexFormat,
    //$$     drawMode: DrawMode,
    //$$     program: ShaderProgram?,
    //$$ ): OmniRenderPipelineBuilder {
    //$$     val supplier = Supplier {
    //$$         client.shaderManager.getProgramForLoading(program)
    //$$     }
    //$$
    //$$     return OmniRenderPipelineBuilder(
    //$$         location = location,
    //$$         vertexFormat = vertexFormat,
    //$$         drawMode = drawMode,
    //$$         shaderProvider = ShaderProvider.Vanilla(supplier)
    //$$     )
    //$$ }
    //#endif
    //$$
    //#if MC >= 1.17.1
    //$$ @JvmStatic
    //$$ public fun builder(
    //$$     location: ResourceLocation,
    //$$     vertexFormat: VertexFormat,
    //$$     drawMode: DrawMode,
    //$$     supplier: Supplier<CompiledShaderProgram>?,
    //$$ ): OmniRenderPipelineBuilder {
    //$$     return OmniRenderPipelineBuilder(
    //$$         location = location,
    //$$         vertexFormat = vertexFormat,
    //$$         drawMode = drawMode,
    //$$         shaderProvider = supplier?.let { ShaderProvider.Vanilla(it) }
    //$$     )
    //$$ }
    //#endif
    //#endif

    @JvmStatic
    public fun builder(
        location: ResourceLocation,
        vertexFormat: VertexFormat,
        drawMode: DrawMode,
        //#if MC >= 1.21.5
        shaderProvider: ShaderProvider,
        //#else
        //$$ shaderProvider: ShaderProvider?,
        //#endif
    ): OmniRenderPipelineBuilder {
        return OmniRenderPipelineBuilder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            shaderProvider = shaderProvider
        )
    }

    @JvmStatic
    public fun builder(
        location: ResourceLocation,
        //#if MC >= 1.21.5
        shaderProvider: ShaderProvider,
        //#else
        //$$ shaderProvider: ShaderProvider?,
        //#endif
        vararg snippets: OmniRenderPipeline.Snippet,
    ): OmniRenderPipelineBuilder {
        val (vertexFormat, drawMode) = extractSnippet(snippet(*snippets))
        return builder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            shaderProvider = shaderProvider
        ).applySnippet(*snippets)
    }

    @JvmStatic
    public fun builder(
        //#if MC >= 1.21.5
        shaderProvider: ShaderProvider,
        //#else
        //$$ shaderProvider: ShaderProvider?,
        //#endif
        vararg snippets: OmniRenderPipeline.Snippet,
    ): OmniRenderPipelineBuilder {
        val (location, vertexFormat, drawMode) = extractAllSnippet(snippet(*snippets))
        return builder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            shaderProvider = shaderProvider
        ).applySnippet(*snippets)
    }

    @JvmStatic
    public fun builderWithDefaultShader(
        location: ResourceLocation,
        vertexFormat: VertexFormat,
        drawMode: DrawMode,
    ): OmniRenderPipelineBuilder {
        //#if MC >= 1.17.1
        val defaultShader = DefaultShaders[vertexFormat]
        //#if MC >= 1.21.5
            ?: throw IllegalArgumentException("No default shader for vertex format $vertexFormat")
        //#endif

        //#if MC >= 1.21.5
        val samplers = List(vertexFormat.elements.map(VertexFormatElement::usage).count(VertexFormatElement.Usage.UV::equals)) { "Sampler$it" }
        val uniforms = mapOf(
            //#if MC >= 1.21.6
            "DynamicTransforms" to UniformType.UNIFORM_BUFFER,
            //#else
            //$$ "ModelViewMat" to UniformType.MATRIX4X4,
            //$$ "ProjMat" to UniformType.MATRIX4X4,
            //$$ "ColorModulator" to UniformType.VEC4,
            //#endif
        )
        //#endif

        return builder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            //#if MC >= 1.21.5
            defaultShader, defaultShader,
            samplers,
            uniforms
            //#else
            //$$ defaultShader
            //#endif
        )
        //#else
        //$$ return OmniRenderPipelineBuilder(
        //$$     location = location,
        //$$     vertexFormat = vertexFormat,
        //$$     drawMode = drawMode,
        //$$     shaderProvider = null
        //$$ )
        //#endif
    }

    @JvmStatic
    public fun builderWithDefaultShader(
        location: ResourceLocation,
        vertexFormat: DefaultVertexFormats,
        drawMode: DrawMode,
    ): OmniRenderPipelineBuilder {
        return builderWithDefaultShader(
            location = location,
            vertexFormat = vertexFormat.vanilla,
            drawMode = drawMode
        )
    }

    @JvmStatic
    public fun builderWithDefaultShader(location: ResourceLocation, vararg snippets: OmniRenderPipeline.Snippet): OmniRenderPipelineBuilder {
        val (vertexFormat, drawMode) = extractSnippet(snippet(*snippets))
        return builderWithDefaultShader(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode
        ).applySnippet(*snippets)
    }

    @JvmStatic
    public fun builderWithDefaultShader(vararg snippets: OmniRenderPipeline.Snippet): OmniRenderPipelineBuilder {
        val (location, vertexFormat, drawMode) = extractAllSnippet(snippet(*snippets))
        return builderWithDefaultShader(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode
        ).applySnippet(*snippets)
    }

    @JvmStatic
    public fun builderWithCompatibleShader(
        location: ResourceLocation,
        vertexFormat: VertexFormat,
        drawMode: DrawMode,
        vertexSource: String,
        fragmentSource: String,
        schema: ShaderSchema,
    ): OmniRenderPipelineBuilder {
        return OmniRenderPipelineBuilder(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            ShaderProvider.Compatible(
                vertexFormat = vertexFormat,
                vertexSource = vertexSource,
                fragmentSource = fragmentSource,
                schema = schema
            )
        )
    }

    @JvmStatic
    public fun builderWithCompatibleShader(
        location: ResourceLocation,
        vertexFormat: DefaultVertexFormats,
        drawMode: DrawMode,
        vertexSource: String,
        fragmentSource: String,
        schema: ShaderSchema,
    ): OmniRenderPipelineBuilder {
        return builderWithCompatibleShader(
            location = location,
            vertexFormat = vertexFormat.vanilla,
            drawMode = drawMode,
            vertexSource = vertexSource,
            fragmentSource = fragmentSource,
            schema = schema
        )
    }

    @JvmStatic
    public fun builderWithCompatibleShader(
        location: ResourceLocation,
        vertexSource: String,
        fragmentSource: String,
        schema: ShaderSchema,
        vararg snippets: OmniRenderPipeline.Snippet,
    ): OmniRenderPipelineBuilder {
        val (vertexFormat, drawMode) = extractSnippet(snippet(*snippets))
        return builderWithCompatibleShader(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            vertexSource = vertexSource,
            fragmentSource = fragmentSource,
            schema = schema
        ).applySnippet(*snippets)
    }

    @JvmStatic
    public fun builderWithCompatibleShader(
        vertexSource: String,
        fragmentSource: String,
        schema: ShaderSchema,
        vararg snippets: OmniRenderPipeline.Snippet,
    ): OmniRenderPipelineBuilder {
        val (location, vertexFormat, drawMode) = extractAllSnippet(snippet(*snippets))
        return builderWithCompatibleShader(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            vertexSource = vertexSource,
            fragmentSource = fragmentSource,
            schema = schema
        ).applySnippet(*snippets)
    }

    private fun snippet(vararg snippets: OmniRenderPipeline.Snippet): OmniRenderPipeline.Snippet {
        val builder = OmniRenderPipelineSnippetBuilder()
        for (snippet in snippets) {
            builder.apply(snippet)
        }

        return builder.build()
    }

    private fun extractSnippet(snippet: OmniRenderPipeline.Snippet): Pair<VertexFormat, DrawMode> {
        val vertexFormat = snippet.vertexFormat
            ?: throw IllegalArgumentException("Snippet must have a vertex format")
        val drawMode = snippet.drawMode
            ?: throw IllegalArgumentException("Snippet must have a draw mode")
        return Pair(vertexFormat, drawMode)
    }

    private fun extractAllSnippet(snippet: OmniRenderPipeline.Snippet): Triple<ResourceLocation, VertexFormat, DrawMode> {
        val location = snippet.location
            ?: throw IllegalArgumentException("Snippet must have a location")
        val (vertexFormat, drawMode) = extractSnippet(snippet)
        return Triple(location, vertexFormat, drawMode)
    }
}
