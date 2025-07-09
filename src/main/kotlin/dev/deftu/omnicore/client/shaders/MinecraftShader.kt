package dev.deftu.omnicore.client.shaders

//#if MC >= 1.17.1 && MC < 1.21.5
//$$ import com.google.common.collect.ImmutableMap
//$$ import com.google.gson.GsonBuilder
//$$ import com.google.gson.JsonArray
//$$ import com.google.gson.JsonObject
//$$ import dev.deftu.omnicore.OmniCore
//$$ import dev.deftu.omnicore.client.render.state.OmniManagedBlendState
//$$ import dev.deftu.omnicore.client.render.OmniRenderState
//$$ import com.mojang.blaze3d.shaders.Uniform
//$$ import net.minecraft.client.renderer.CompiledShaderProgram
//$$ import com.mojang.blaze3d.vertex.VertexFormat
//$$ import com.mojang.blaze3d.vertex.VertexFormatElement
//$$ import com.mojang.blaze3d.vertex.DefaultVertexFormat
//$$ import net.minecraft.resources.ResourceLocation
//$$ import org.apache.commons.codec.digest.DigestUtils
//$$ import org.slf4j.LoggerFactory
//$$ import kotlin.NoSuchElementException
//$$
//#if MC >= 1.21.2
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//$$ import com.mojang.blaze3d.shaders.CompiledShader
//#endif
//$$
//#if MC >= 1.19
//$$ import net.minecraft.server.packs.resources.Resource
//$$ import java.util.Optional
//#else
//$$ import net.minecraft.resource.ResourceImpl
//#endif
//$$
//$$ internal class MinecraftShader(
//$$     internal val shader: CompiledShaderProgram,
//$$     private val blend: OmniManagedBlendState
//$$ ) : OmniShader {
//$$
//$$     companion object {
//$$
//$$         private val isDebug: Boolean
//$$             get() = OmniCore.isDebug || System.getProperty("omnicore.shader.debug") == "true"
//$$
//$$         private val logger by lazy {
//$$             LoggerFactory.getLogger(MinecraftShader::class.java)
//$$         }
//$$
//$$         private val gson by lazy {
//$$             GsonBuilder()
//$$                 .setPrettyPrinting()
//$$                 .create()
//$$         }
//$$
//$$         @JvmStatic
//$$         internal fun fromLegacyShader(
//$$             vert: String,
//$$             frag: String,
//$$             blend: OmniManagedBlendState,
//$$             vertexFormat: VertexFormat?
//$$         ): MinecraftShader {
//$$             val transformer = ShaderProcessor(vertexFormat, 150)
//$$
//$$             val transformedVert = transformer.transform(vert)
//$$             val transformedFrag = transformer.transform(frag)
//$$
//$$             val json = JsonObject().apply {
//$$                 add("blend", JsonObject().apply {
//$$                     addProperty("func", blend.equation.equationName)
//$$                     addProperty("srcrgb", blend.function.srcColor.shaderName)
//$$                     addProperty("dstrgb", blend.function.dstColor.shaderName)
//$$
//$$                     if (blend.function.isSeparateSrc) {
//$$                         addProperty("srcalpha", blend.function.srcAlpha.shaderName)
//$$                     }
//$$
//$$                     if (blend.function.isSeparateDst) {
//$$                         addProperty("dstalpha", blend.function.dstAlpha.shaderName)
//$$                     }
//$$                 })
//$$
//$$                 addProperty("vertex", DigestUtils.sha1Hex(transformedVert).lowercase())
//$$                 addProperty("fragment", DigestUtils.sha1Hex(transformedFrag).lowercase())
//$$
//$$                 add("attributes", JsonArray().apply {
//$$                     transformer.attributes.forEach(::add)
//$$                 })
//$$
//$$                 add("samplers", JsonArray().apply {
//$$                     transformer.samplers.forEach { sampler ->
//$$                         add(JsonObject().apply {
//$$                             addProperty("name", sampler)
//$$                         })
//$$                     }
//$$                 })
//$$
//$$                 add("uniforms", JsonArray().apply {
//$$                     transformer.uniforms.forEach { (name, type) ->
//$$                         add(JsonObject().apply {
//$$                             addProperty("name", name)
//$$                             addProperty("type", type.glslName)
//$$                             addProperty("count", type.default.size)
//$$                             add("values", JsonArray().apply {
//$$                                 type.default.forEach(::add)
//$$                             })
//$$                         })
//$$                     }
//$$                 })
//$$             }
//$$
//$$             val jsonString = gson.toJson(json)
//$$             if (isDebug) {
//$$                 logger.info("Transformed vertex shader:\n$transformedVert")
//$$                 logger.info("Transformed fragment shader:\n$transformedFrag")
//$$                 logger.info("Generated shader JSON: $jsonString")
//$$             }
//$$
            //#if MC <= 1.21.1
            //$$ val factory = { id: Identifier ->
            //$$     val content = when (id.path.substringAfter(".")) {
            //$$         "json" -> jsonString
            //$$         "vsh" -> transformedVert
            //$$         "fsh" -> transformedFrag
            //$$         else -> throw IllegalArgumentException("Unknown shader file type: ${id.path.substringAfter(".")}")
            //$$     }
            //$$
                //#if MC >= 1.19.3
                //$$ Optional.of(Resource(DummyResourcePack, content::byteInputStream))
                //#elseif MC >= 1.19
                //$$ Optional.of(Resource("__generated__", content::byteInputStream))
                //#else
                //$$ ResourceImpl("__generated__", id, content.byteInputStream(), null)
                //#endif
            //$$ }
            //#endif
//$$
//$$             val vertexFormat = if (vertexFormat != null) {
//$$                 buildVertexFormat(transformer.attributes.withIndex().associate { (index, name) -> name to vertexFormat.elements[index] })
//$$             } else {
//$$                 buildVertexFormat(transformer.attributes.associateWith {
                    //#if MC >= 1.21
                    //$$ VertexFormatElement.POSITION
                    //#else
                    //$$ DefaultVertexFormat.ELEMENT_POSITION
                    //#endif
//$$                 })
//$$             }
//$$
            //#if MC >= 1.21.2
            //$$ val vertId = OmniIdentifier.create(DigestUtils.sha1Hex(transformedVert).lowercase())
            //$$ val vertShader = CompiledShader.compile(vertId, CompiledShader.Type.VERTEX, transformedVert)
            //$$ val fragId = OmniIdentifier.create(DigestUtils.sha1Hex(transformedFrag).lowercase())
            //$$ val fragShader = CompiledShader.compile(fragId, CompiledShader.Type.FRAGMENT, transformedFrag)
            //$$ return MinecraftShader(CompiledShaderProgram.link(vertShader, fragShader, vertexFormat), blend)
            //#else
            //$$ val shaderName = DigestUtils.sha1Hex(jsonString).lowercase()
            //$$ return MinecraftShader(ShaderProgram(factory, shaderName, vertexFormat), blend)
            //#endif
//$$         }
//$$
//$$         private fun buildVertexFormat(elements: Map<String, VertexFormatElement>): VertexFormat {
            //#if MC >= 1.21
            //$$ val builder = VertexFormat.builder()
            //$$ elements.forEach { (name, element) -> builder.add(name, element) }
            //$$ return builder.build()
            //#else
            //$$ return VertexFormat(ImmutableMap.copyOf(elements))
            //#endif
//$$         }
//$$
//$$     }
//$$
//$$     override val usable: Boolean = true
//$$
//$$     override fun bind() {
//$$         OmniRenderState.setShader(::shader)
//$$         blend.activate()
//$$     }
//$$
//$$     override fun unbind() {
//$$         OmniRenderState.removeShader()
//$$     }
//$$
//$$     private fun getUniformOrNull(name: String) = shader.getUniform(name)?.let(::VanillaShaderUniform)
//$$     override fun getIntUniformOrNull(name: String) = getUniformOrNull(name) as? IntUniform
//$$     override fun getVecUniformOrNull(name: String) = getUniformOrNull(name) as? VecUniform
//$$     override fun getVec2UniformOrNull(name: String) = getUniformOrNull(name) as? Vec2Uniform
//$$     override fun getVec3UniformOrNull(name: String) = getUniformOrNull(name) as? Vec3Uniform
//$$     override fun getVec4UniformOrNull(name: String) = getUniformOrNull(name) as? Vec4Uniform
//$$     override fun getMatrixUniformOrNull(name: String) = getUniformOrNull(name) as? MatrixUniform
//$$     override fun getSamplerUniformOrNull(name: String) = VanillaSamplerUniform(shader, name)
//$$ }
//$$
//$$ internal class VanillaShaderUniform(
//$$     val vanilla: Uniform
//$$ ) : ShaderUniform,
//$$     IntUniform,
//$$     VecUniform,
//$$     Vec2Uniform,
//$$     Vec3Uniform,
//$$     Vec4Uniform,
//$$     MatrixUniform,
//$$     SamplerUniform {
//$$
//$$     override val location: Int
//$$         get() = vanilla.location
//$$
//$$     override fun setValue(value: Int) = vanilla.set(value)
//$$     override fun setValue(a: Float) = vanilla.set(a)
//$$     override fun setValue(a: Float, b: Float) = vanilla.set(a, b)
//$$     override fun setValue(a: Float, b: Float, c: Float) = vanilla.set(a, b, c)
//$$     override fun setValue(a: Float, b: Float, c: Float, d: Float) = vanilla.setMat2x2(a, b, c, d)
//$$     override fun setValue(matrix: FloatArray) = vanilla.set(matrix)
//$$
//$$ }
//$$
//$$ internal class VanillaSamplerUniform(
//$$     val vanilla: CompiledShaderProgram,
//$$     val name: String
//$$ ) : SamplerUniform {
//$$
//$$     override val location = 0
//$$
//$$     override fun setValue(value: Int) {
//$$         vanilla.bindSampler(name, value)
//$$     }
//$$
//$$ }
//#endif
