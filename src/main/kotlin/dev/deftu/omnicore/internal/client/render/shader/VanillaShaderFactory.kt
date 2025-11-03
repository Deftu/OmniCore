package dev.deftu.omnicore.internal.client.render.shader

//#if MC >= 1.17.1 && MC < 1.21.5
//$$ import com.google.gson.JsonArray
//$$ import com.google.gson.JsonObject
//$$ import com.mojang.blaze3d.vertex.VertexFormat
//$$ import com.mojang.blaze3d.vertex.VertexFormatElement
//$$ import dev.deftu.omnicore.api.client.render.shader.OmniShader
//$$ import dev.deftu.omnicore.api.client.render.shader.ShaderSchema
//$$ import dev.deftu.omnicore.api.client.render.shader.attributes.AttributeDefinition
//$$ import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformDefinition
//$$ import dev.deftu.omnicore.api.client.render.state.OmniBlendState
//$$ import net.minecraft.client.renderer.CompiledShaderProgram
//$$ import net.minecraft.resources.ResourceLocation
//$$ import org.apache.commons.codec.digest.DigestUtils
//$$ import org.jetbrains.annotations.ApiStatus
//$$
//#if MC >= 1.21.2
//$$ import dev.deftu.omnicore.api.locationOrThrow
//$$ import com.mojang.blaze3d.shaders.CompiledShader
//#endif
//$$
//#if MC < 1.21.1
//$$ import com.mojang.blaze3d.vertex.DefaultVertexFormat
//$$ import com.google.common.collect.ImmutableMap
//#endif
//$$
//#if MC >= 1.19.2
//$$ import net.minecraft.server.packs.resources.Resource
//$$ import java.util.Optional
//#else
//$$ import net.minecraft.server.packs.resources.SimpleResource
//#endif
//$$
//$$ @ApiStatus.Internal
//$$ public object VanillaShaderFactory {
//$$     @JvmStatic
//$$     public fun create(
//$$         vertSource: String,
//$$         fragSource: String,
//$$         vertexFormat: VertexFormat?,
//$$         schema: ShaderSchema,
//$$         blendState: OmniBlendState,
//$$     ): OmniShader {
//$$         val preprocessor = ShaderSourcePreprocessor(vertexFormat, 150)
//$$         val processedVert = preprocessor.processVertexShader(vertSource)
//$$         val processedFrag = preprocessor.processFragmentShader(fragSource)
//$$         val vertName = DigestUtils.sha1Hex(processedVert)
//$$         val fragName = DigestUtils.sha1Hex(processedFrag)
//$$
//$$         val programJson = JsonObject().apply {
//$$             add("blend", JsonObject().apply {
//$$                 addProperty("func", blendState.equation.equationName)
//$$                 addProperty("srcrgb", blendState.function.srcColor.shaderName)
//$$                 addProperty("dstrgb", blendState.function.dstColor.shaderName)
//$$
//$$                 if (blendState.function.isSeparateSrc) {
//$$                     addProperty("srcalpha", blendState.function.srcAlpha.shaderName)
//$$                 }
//$$
//$$                 if (blendState.function.isSeparateDst) {
//$$                     addProperty("dstalpha", blendState.function.dstAlpha.shaderName)
//$$                 }
//$$             })
//$$
//$$             addProperty("vertex", vertName.lowercase())
//$$             addProperty("fragment", fragName.lowercase())
//$$
//$$             add("attributes", JsonArray().apply {
//$$                 schema.attributes.map(AttributeDefinition::name).forEach(::add)
//$$             })
//$$
//$$             add("samplers", JsonArray().apply {
//$$                 schema.uniforms.filterIsInstance<UniformDefinition.Sampler>().map { sampler ->
//$$                     JsonObject().apply {
//$$                         addProperty("name", sampler.name)
//$$                     }
//$$                 }.forEach(::add)
//$$             })
//$$
//$$             add("uniforms", JsonArray().apply {
//$$                 schema.uniforms.filter { it !is UniformDefinition.Sampler }.map { uniform ->
//$$                     JsonObject().apply {
//$$                         addProperty("name", uniform.name)
//$$                         addProperty("type", uniform.kind.shaderName)
//$$                         addProperty("count", uniform.kind.default.size)
//$$                         add("values", JsonArray().apply {
//$$                             uniform.kind.default.forEach(::add)
//$$                         })
//$$                     }
//$$                 }.forEach(::add)
//$$             })
//$$         }
//$$
            //#if MC < 1.21.2
            //$$ val factory = { location: ResourceLocation ->
            //$$     val content = when (location.path.substringAfter(".")) {
            //$$         "json" -> programJson.toString()
            //$$         "vsh" -> processedVert
            //$$         "fsh" -> processedFrag
            //$$         else -> throw IllegalArgumentException("Unknown shader file type: ${location.path.substringAfter(".")}")
            //$$     }
            //$$
                //#if MC >= 1.19.3
                //$$ Optional.of(Resource(GeneratedResourcePack, content::byteInputStream))
                //#elseif MC >= 1.19
                //$$ Optional.of(Resource("__generated__", content::byteInputStream))
                //#else
                //$$ SimpleResource("__generated__", location, content.byteInputStream(), null)
                //#endif
            //$$ }
            //#endif
//$$
//$$         val vertexFormat = if (vertexFormat != null) {
//$$             buildVertexFormat(schema.attributes.withIndex().associate { (index, attribute) ->
//$$                 attribute.name to vertexFormat.elements[index]
//$$             })
//$$         } else {
//$$             buildVertexFormat(schema.attributes.map(AttributeDefinition::name).associateWith {
                    //#if MC >= 1.21.1
                    //$$ VertexFormatElement.POSITION
                    //#else
                    //$$ DefaultVertexFormat.ELEMENT_POSITION
                    //#endif
//$$             })
//$$         }
//$$
            //#if MC >= 1.21.2
            //$$ val vertLocation = locationOrThrow(vertName)
            //$$ val fragLocation = locationOrThrow(fragName)
            //$$ val vertShader = CompiledShader.compile(vertLocation, CompiledShader.Type.VERTEX, processedVert)
            //$$ val fragShader = CompiledShader.compile(fragLocation, CompiledShader.Type.FRAGMENT, processedFrag)
            //$$ return VanillaShader(CompiledShaderProgram.link(vertShader, fragShader, vertexFormat), blendState)
            //#else
            //$$ val shaderName = DigestUtils.sha1Hex(programJson.toString()).lowercase()
            //$$ return VanillaShader(ShaderInstance(factory, shaderName, vertexFormat), blendState)
            //#endif
//$$     }
//$$
//$$     private fun buildVertexFormat(elements: Map<String, VertexFormatElement>): VertexFormat {
            //#if MC >= 1.21.1
            //$$ val builder = VertexFormat.builder()
            //$$ for ((name, element) in elements) {
            //$$     builder.add(name, element)
            //$$ }
            //$$
            //$$ return builder.build()
            //#else
            //$$ return VertexFormat(ImmutableMap.copyOf(elements))
            //#endif
//$$     }
//$$ }
//#endif
