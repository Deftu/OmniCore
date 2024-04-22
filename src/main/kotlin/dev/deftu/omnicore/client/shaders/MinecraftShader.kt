package dev.deftu.omnicore.client.shaders

//#if MC >= 1.17
//#if MC >= 1.19.3
import dev.deftu.omnicore.client.DummyResourcePack
//#endif

//#if MC >= 1.19
import net.minecraft.resource.Resource
import java.util.Optional
//#else
//$$ import net.minecraft.resource.ResourceImpl
//#endif

import com.google.common.collect.ImmutableMap
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import dev.deftu.omnicore.client.render.OmniTessellator
import dev.deftu.omnicore.client.render.OmniRenderState
import net.minecraft.client.gl.GlUniform
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import org.apache.commons.codec.digest.DigestUtils
import kotlin.NoSuchElementException

internal class MinecraftShader(
    private val shader: ShaderProgram,
    private val blend: BlendState
) : OmniShader {
    companion object {
        private val gson by lazy {
            GsonBuilder()
                .setPrettyPrinting()
                .create()
        }

        @JvmStatic
        fun fromLegacyShader(
            vert: String,
            frag: String,
            blend: BlendState,
            vertexFormat: OmniTessellator.VertexFormats
        ): MinecraftShader {
            val transformer = Transformer(vertexFormat)

            val vert = transformer.transform(vert)
            val frag = transformer.transform(frag)

            val json = JsonObject().apply {
                addProperty("vertex", DigestUtils.sha1Hex(vert).lowercase())
                addProperty("fragment", DigestUtils.sha1Hex(frag).lowercase())

                add("blend", JsonObject().apply {
                    addProperty("func", blend.equation.mcStr)
                    addProperty("srcrgb", blend.srcRgb.mcStr)
                    addProperty("dstrgb", blend.dstRgb.mcStr)
                    if (blend.separateSrc) addProperty("srcalpha", blend.srcAlpha.mcStr)
                    if (blend.separateDst) addProperty("dstalpha", blend.dstAlpha.mcStr)
                })

                add("attributes", JsonArray().apply {
                    transformer.attributes.forEach(::add)
                })

                add("samplers", JsonArray().apply {
                    transformer.samplers.forEach(::add)
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
            val factory = { id: Identifier ->
                val content = when (id.path.substringAfter(".")) {
                    "json" -> jsonString
                    "vsh" -> vert
                    "fsh" -> frag
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

            val vertexFormat = VertexFormat(ImmutableMap.copyOf(transformer.attributes.associateWith {
                VertexFormats.POSITION_ELEMENT
            }))

            val shaderName = DigestUtils.sha1Hex(jsonString).lowercase()
            return MinecraftShader(ShaderProgram(factory, shaderName, vertexFormat), blend)
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
        val uniforms = mutableMapOf<String, UniformType>()
        val samplers = mutableSetOf<String>()
        val attributes = mutableListOf<String>()

        fun transform(value: String): String {
            var value = value

            value = value.replace("texture2D", "texture")
            value = value.replace("gl_ModelViewProjectionMatrix", "gl_ProjectionMatrix * gl_ModelViewMatrix")

            val replacements = mutableMapOf<String, String>()
            val transformed = mutableListOf<String>()
            transformed.add("#version 150")

            val isFrag = "gl_FragColor" in value
            val isVert = !isFrag

            if (isFrag) {
                transformed.add("out vec4 m_FragColor;")
                replacements["gl_FragColor"] = "m_FragColor"
            }

            if (
                isVert &&
                "gl_FrontColor" in value
            ) {
                transformed.add("out vec4 m_FrontColor;")
                replacements["gl_FrontColor"] = "m_FrontColor"
            }

            if (
                isFrag &&
                "gl_Color" in value
            ) {
                transformed.add("in vec4 m_Color;")
                replacements["gl_Color"] = "m_Color"
            }

            if (isVert) {
                val newAttributes = mutableListOf<Pair<String, String>>()
                replaceAttribute(value, replacements, newAttributes, "gl_Color", "vec4")
                replaceAttribute(value, replacements, newAttributes, "gl_MultiTexCoord0.st", "vec2", "m_UV0")
                replaceAttribute(value, replacements, newAttributes, "gl_MultiTexCoord1.st", "vec2", "m_UV1")
                replaceAttribute(value, replacements, newAttributes, "gl_MultiTexCoord2.st", "vec2", "m_UV2")
                replaceAttribute(value, replacements, newAttributes, "gl_Vertex", "vec3", "m_Pos", replacement = "vec4(m_Pos, 1.0)")

                if (vertexFormat != null) {
                    newAttributes.sortedBy {  (name, type) ->
                        vertexFormat.vanilla.attributeNames.indexOf(name.removePrefix("m_"))
                    }.forEach {(name, type) ->
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

            replaceUniform(value, replacements, transformed, "gl_ModelViewMatrix", UniformType.Mat4, "ModelViewMat")
            replaceUniform(value, replacements, transformed, "gl_ProjectionMatrix", UniformType.Mat4, "ProjMat")

            for (line in value.lines()) {
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

        fun replaceAttribute(
            value: String,
            replacements: MutableMap<String, String>,

            newValue: MutableList<Pair<String, String>>,
            needle: String,
            type: String,
            replacementName: String = "m_${needle.substringAfter("_")}",
            replacement: String = replacementName
        ) {
            if (needle in value) {
                replacements[needle] = replacement
                newValue.add(replacementName to "in $type $replacementName;")
            }
        }

        fun replaceUniform(
            value: String,
            replacements: MutableMap<String, String>,
            transformed: MutableList<String>,

            needle: String,
            type: UniformType,
            replacementName: String = "m_${needle.substringAfter("_")}",
            replacement: String = replacementName
        ) {
            if (needle in value) {
                replacements[needle] = replacement
                if (replacementName !in uniforms) {
                    uniforms[replacementName] = type
                    transformed.add("uniform ${type.typeName} $replacementName;")
                }
            }
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
