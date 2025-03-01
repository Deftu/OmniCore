package dev.deftu.omnicore.client.shaders

import com.google.common.collect.ImmutableMap
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.client.render.OmniTessellator
import dev.deftu.omnicore.client.render.OmniRenderState
import net.minecraft.client.gl.GlUniform
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormatElement
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import kotlin.NoSuchElementException

//#if MC >= 1.21.4
//$$ import dev.deftu.omnicore.common.OmniIdentifier
//$$ import net.minecraft.client.gl.CompiledShader
//#endif

//#if MC >= 1.17
//#if MC >= 1.19
import net.minecraft.resource.Resource
import java.util.Optional
//#else
//$$ import net.minecraft.resource.ResourceImpl
//#endif

internal class MinecraftShader(
    private val shader: ShaderProgram,
    private val blend: BlendState
) : OmniShader {

    companion object {

        private val isDebug: Boolean
            get() = OmniCore.isDebug || System.getProperty("omnicore.shader.debug") == "true"

        private val logger by lazy {
            LoggerFactory.getLogger(MinecraftShader::class.java)
        }

        private val gson by lazy {
            GsonBuilder()
                .setPrettyPrinting()
                .create()
        }

        @JvmStatic
        internal fun fromLegacyShader(
            vert: String,
            frag: String,
            blend: BlendState,
            vertexFormat: OmniTessellator.VertexFormats?
        ): MinecraftShader {
            val transformer = Transformer(vertexFormat)

            val transformedVert = transformer.transform(vert)
            val transformedFrag = transformer.transform(frag)

            val json = JsonObject().apply {
                add("blend", JsonObject().apply {
                    addProperty("func", blend.equation.mcStr)
                    addProperty("srcrgb", blend.srcRgb.mcStr)
                    addProperty("dstrgb", blend.dstRgb.mcStr)
                    if (blend.separateSrc) addProperty("srcalpha", blend.srcAlpha.mcStr)
                    if (blend.separateDst) addProperty("dstalpha", blend.dstAlpha.mcStr)
                })

                addProperty("vertex", DigestUtils.sha1Hex(transformedVert).lowercase())
                addProperty("fragment", DigestUtils.sha1Hex(transformedFrag).lowercase())

                add("attributes", JsonArray().apply {
                    transformer.attributes.forEach(::add)
                })

                add("samplers", JsonArray().apply {
                    transformer.samplers.forEach { sampler ->
                        add(JsonObject().apply {
                            addProperty("name", sampler)
                        })
                    }
                })

                add("uniforms", JsonArray().apply {
                    transformer.uniforms.forEach { (name, type) ->
                        add(JsonObject().apply {
                            addProperty("name", name)
                            addProperty("type", type.glslName)
                            addProperty("count", type.default.size)
                            add("values", JsonArray().apply {
                                type.default.forEach(::add)
                            })
                        })
                    }
                })
            }

            val jsonString = gson.toJson(json)
            if (isDebug) {
                logger.info("Transformed vertex shader:\n$transformedVert")
                logger.info("Transformed fragment shader:\n$transformedFrag")
                logger.info("Generated shader JSON: $jsonString")
            }

            //#if MC <= 1.21.1
            val factory = { id: Identifier ->
                val content = when (id.path.substringAfter(".")) {
                    "json" -> jsonString
                    "vsh" -> transformedVert
                    "fsh" -> transformedFrag
                    else -> throw IllegalArgumentException("Unknown shader file type: ${id.path.substringAfter(".")}")
                }

                //#if MC >= 1.19.3
                Optional.of(Resource(DummyResourcePack, content::byteInputStream))
                //#elseif MC >= 1.19
                //$$ Optional.of(Resource("__generated__", content::byteInputStream))
                //#else
                //$$ ResourceImpl("__generated__", id, content.byteInputStream(), null)
                //#endif
            }
            //#endif

            val vertexFormat = if (vertexFormat != null) {
                buildVertexFormat(transformer.attributes.withIndex().associate { (index, name) -> name to vertexFormat.vanilla.elements[index] })
            } else {
                buildVertexFormat(transformer.attributes.associateWith {
                    //#if MC >= 1.21
                    //$$ VertexFormatElement.POSITION
                    //#else
                    VertexFormats.POSITION_ELEMENT
                    //#endif
                })
            }

            //#if MC >= 1.21.4
            //$$ val vertId = OmniIdentifier.create(DigestUtils.sha1Hex(transformedVert).lowercase())
            //$$ val vertShader = CompiledShader.compile(vertId, CompiledShader.Type.VERTEX, transformedVert)
            //$$ val fragId = OmniIdentifier.create(DigestUtils.sha1Hex(transformedFrag).lowercase())
            //$$ val fragShader = CompiledShader.compile(fragId, CompiledShader.Type.FRAGMENT, transformedFrag)
            //$$ return MinecraftShader(ShaderProgram.create(vertShader, fragShader, vertexFormat), blend)
            //#else
            val shaderName = DigestUtils.sha1Hex(jsonString).lowercase()
            return MinecraftShader(ShaderProgram(factory, shaderName, vertexFormat), blend)
            //#endif
        }

        private fun buildVertexFormat(elements: Map<String, VertexFormatElement>): VertexFormat {
            //#if MC >= 1.21
            //$$ val builder = VertexFormat.builder()
            //$$ elements.forEach { (name, element) -> builder.add(name, element) }
            //$$ return builder.build()
            //#else
            return VertexFormat(ImmutableMap.copyOf(elements))
            //#endif
        }

    }

    override val usable: Boolean = true

    override fun bind() {
        OmniRenderState.setShader(::shader)
        blend.activate()
    }

    override fun unbind() {
        OmniRenderState.removeShader()
    }

    private fun getUniformOrNull(name: String) = shader.getUniform(name)?.let(::VanillaShaderUniform)
    override fun getIntUniformOrNull(name: String) = getUniformOrNull(name) as? IntUniform
    override fun getVecUniformOrNull(name: String) = getUniformOrNull(name) as? VecUniform
    override fun getVec2UniformOrNull(name: String) = getUniformOrNull(name) as? Vec2Uniform
    override fun getVec3UniformOrNull(name: String) = getUniformOrNull(name) as? Vec3Uniform
    override fun getVec4UniformOrNull(name: String) = getUniformOrNull(name) as? Vec4Uniform
    override fun getMatrixUniformOrNull(name: String) = getUniformOrNull(name) as? MatrixUniform
    override fun getSamplerUniformOrNull(name: String) = VanillaSamplerUniform(shader, name)

    internal class Transformer(
        private val vertexFormat: OmniTessellator.VertexFormats?
    ) {
        val attributes = mutableListOf<String>()
        val samplers = mutableSetOf<String>()
        val uniforms = mutableMapOf<String, UniformType>()

        fun transform(value: String): String {
            var source = value

            source = source.replace("gl_ModelViewProjectionMatrix", "gl_ProjectionMatrix * gl_ModelViewMatrix")
            source = source.replace("texture2D", "texture")

            val replacements = mutableMapOf<String, String>()
            val transformed = mutableListOf<String>()
            transformed.add("#version 150")

            val isFrag = "gl_FragColor" in value
            val isVert = !isFrag

            if (isFrag) {
                transformed.add("out vec4 oc_FragColor;")
                replacements["gl_FragColor"] = "oc_FragColor"
            }

            if (
                isVert &&
                "gl_FrontColor" in value
            ) {
                transformed.add("out vec4 oc_FrontColor;")
                replacements["gl_FrontColor"] = "oc_FrontColor"
            }

            if (
                isFrag &&
                "gl_Color" in value
            ) {
                transformed.add("in vec4 oc_FrontColor;")
                replacements["gl_Color"] = "oc_FrontColor"
            }

            if (isVert) {
                fun replaceAttrib(
                    value: MutableList<Pair<String, String>>,
                    needle: String,
                    type: String,
                    replacementName: String = "oc_${needle.substringAfter("_")}",
                    replacement: String = replacementName
                ) {
                    if (needle in source) {
                        replacements[needle] = replacement
                        value.add(replacementName to "in $type $replacementName;")
                    }
                }

                val newAttributes = mutableListOf<Pair<String, String>>()
                replaceAttrib(newAttributes, "gl_Vertex", "vec3", "oc_Position", replacement = "vec4(oc_Position, 1.0)")
                replaceAttrib(newAttributes, "gl_Color", "vec4")
                replaceAttrib(newAttributes, "gl_MultiTexCoord0.st", "vec2", "oc_UV0")
                replaceAttrib(newAttributes, "gl_MultiTexCoord1.st", "vec2", "oc_UV1")
                replaceAttrib(newAttributes, "gl_MultiTexCoord2.st", "vec2", "oc_UV2")

                if (vertexFormat != null) {
                    newAttributes.sortedBy {  (name, type) ->
                        vertexFormat.vanilla.attributeNames.indexOf(name.removePrefix("oc_"))
                    }.forEach { (name, type) ->
                        attributes.add(name)
                        transformed.add(type)
                    }
                } else {
                    newAttributes.forEach { (name, type) ->
                        attributes.add(name)
                        transformed.add(type)
                    }
                }
            }

            fun replaceUniform(
                needle: String,
                type: UniformType,
                replacementName: String,
                replacement: String = replacementName
            ) {
                if (needle in source) {
                    replacements[needle] = replacement
                    if (replacementName !in uniforms) {
                        uniforms[replacementName] = type
                        transformed.add("uniform ${type.glslName} $replacementName;")
                    }
                }
            }

            replaceUniform("gl_ModelViewMatrix", UniformType.Mat4, "ModelViewMat")
            replaceUniform("gl_ProjectionMatrix", UniformType.Mat4, "ProjMat")

            for (line in source.lines()) {
                transformed.add(when {
                    line.startsWith("#version") -> continue
                    line.startsWith("varying ") -> (if (isFrag) "in " else "out ") + line.substringAfter("varying ")
                    line.startsWith("uniform ") -> {
                        val (_, typeName, name) = line.trimEnd(';').split(" ")
                        if (typeName == "sampler2D") {
                            samplers.add(name)
                        } else uniforms[name] = UniformType.fromGlsl(typeName)
                        line
                    }

                    else -> replacements.entries.fold(line) { acc, (needle, replacement) -> acc.replace(needle, replacement) }
                })
            }

            return transformed.joinToString("\n")
        }

    }

    internal enum class UniformType(
        val typeName: String,
        val glslName: String,
        val default: IntArray
    ) {
        Int1("int", "int", intArrayOf(0)),
        Float1("float", "float", intArrayOf(0)),
        Float2("float", "vec2", intArrayOf(0, 0)),
        Float3("float", "vec3", intArrayOf(0, 0, 0)),
        Float4("float", "vec4", intArrayOf(0, 0, 0, 0)),
        Mat2("matrix2x2", "mat2", intArrayOf(1, 0, 0, 1)),
        Mat3("matrix3x3", "mat3", intArrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1)),
        Mat4("matrix4x4", "mat4", intArrayOf(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)),
        ;

        companion object {
            fun fromGlsl(glslName: String): UniformType =
                values().find { it.glslName == glslName } ?: throw NoSuchElementException(glslName)
        }
    }
}

internal class VanillaShaderUniform(
    val vanilla: GlUniform
) : ShaderUniform,
    IntUniform,
    VecUniform,
    Vec2Uniform,
    Vec3Uniform,
    Vec4Uniform,
    MatrixUniform,
    SamplerUniform {

    override val location: Int
        get() = vanilla.location

    override fun setValue(value: Int) = vanilla.set(value)
    override fun setValue(a: Float) = vanilla.set(a)
    override fun setValue(a: Float, b: Float) = vanilla.set(a, b)
    override fun setValue(a: Float, b: Float, c: Float) = vanilla.set(a, b, c)
    override fun setValue(a: Float, b: Float, c: Float, d: Float) = vanilla.set(a, b, c, d)
    override fun setValue(matrix: FloatArray) = vanilla.set(matrix)

}

internal class VanillaSamplerUniform(
    val vanilla: ShaderProgram,
    val name: String
) : SamplerUniform {

    override val location = 0

    override fun setValue(value: Int) {
        vanilla.addSampler(name, value)
    }

}
//#endif
