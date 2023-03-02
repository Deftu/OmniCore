//#if MC>=11700
package xyz.deftu.multi.shader

//#if MC>=11903
import xyz.deftu.multi.DummyResourcePack
//#endif

//#if MC>=11900
import net.minecraft.resource.Resource
import java.util.Optional
//#else
//$$ import net.minecraft.resource.ResourceImpl
//#endif

import net.minecraft.client.gl.GlUniform
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import org.apache.commons.codec.digest.DigestUtils
import xyz.deftu.multi.MultiRenderSystem
import com.google.common.collect.ImmutableMap

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
internal class VanillaShader(
    private val vanilla: ShaderProgram,
    private val blend: BlendState
) : MultiShader {
    companion object {
        private val DEBUG: Boolean
            get() = System.getProperty("multicraft.shader.debug")?.toBoolean() ?: false

        fun fromLegacy(
            vert: String,
            frag: String,
            blend: BlendState
        ): VanillaShader {
            val transformer = ShaderTransformer()

            val transformedVert = transformer.transform(vert)
            val transformedFrag = transformer.transform(frag)

            val json = """
                {
                    "blend": {
                        "func": "${blend.equation.mcStr}",
                        "srcrgb": "${blend.srcRgb.mcStr}",
                        "dstrgb": "${blend.dstRgb.mcStr}",
                        "srcalpha": "${blend.srcAlpha.mcStr}",
                        "dstalpha": "${blend.dstAlpha.mcStr}"
                    },
                    "vertex": "${DigestUtils.sha1Hex(transformedVert).lowercase()}",
                    "fragment": "${DigestUtils.sha1Hex(transformedFrag).lowercase()}",
                    "attributes": [ ${transformer.attributes.joinToString { "\"$it\"" }} ],
                    "samplers": [
                        ${transformer.samplers.joinToString(",\n") { "{ \"name\": \"$it\" }" }}
                    ],
                    "uniforms": [
                        ${transformer.uniforms.map { (name, type) -> """
                            { "name": "$name", "type": "${type.typeName}", "count": ${type.default.size}, "values": [ ${type.default.joinToString()} ] }
                        """.trimIndent() }.joinToString(",\n")}
                    ]
                }
            """.trimIndent()

            if (DEBUG) {
                println(transformedVert)
                println(transformedFrag)
                println(json)
            }

            val factory = { id: Identifier ->
                val content = when (id.path.substringAfter(".")) {
                    "json" -> json
                    "vsh" -> transformedVert
                    "fsh" -> transformedFrag
                    else -> throw IllegalArgumentException("Unknown shader resource type: ${id.path}")
                }
                //#if MC>=11903
                Optional.of(Resource(DummyResourcePack, content::byteInputStream))
                //#elseif MC>=11900
                //$$ Optional.of(Resource("__generated__", content::byteInputStream))
                //#else
                //$$ ResourceImpl("__generated__", id, content.byteInputStream(), null)
                //#endif
            }

            val vertexFormat = VertexFormat(ImmutableMap.copyOf(transformer.attributes.associateWith { VertexFormats.POSITION_ELEMENT }))

            val name = DigestUtils.sha1Hex(json).lowercase()
            return VanillaShader(ShaderProgram(factory, name, vertexFormat), blend)
        }
    }

    override val usable = true

    override fun bind() {
        MultiRenderSystem.setShader(::vanilla)
        blend.activate()
    }

    override fun unbind() {
        MultiRenderSystem.removeShader()
    }

    private fun getUniformOrNull(name: String) = vanilla.getUniform(name)?.let(::VanillaShaderUniform)
    override fun getIntUniformOrNull(name: String) = getUniformOrNull(name)
    override fun getVecUniformOrNull(name: String) = getUniformOrNull(name)
    override fun getVec2UniformOrNull(name: String) = getUniformOrNull(name)
    override fun getVec3UniformOrNull(name: String) = getUniformOrNull(name)
    override fun getVec4UniformOrNull(name: String) = getUniformOrNull(name)
    override fun getMatrixUniformOrNull(name: String) = getUniformOrNull(name)
    override fun getSamplerUniformOrNull(name: String) = VanillaSamplerUniform(vanilla, name)
}

internal class VanillaShaderUniform(
    val vanilla: GlUniform
) : ShaderUniform, IntUniform, VecUniform, Vec2Uniform, Vec3Uniform, Vec4Uniform, MatrixUniform, SamplerUniform {
    override val location: Int
        get() = vanilla.location

    override fun setValue(value: Int) = vanilla.set(value)
    override fun setValue(a: Float) = vanilla.set(a)
    override fun setValue(a: Float, b: Float) = vanilla.set(a, b)
    override fun setValue(a: Float, b: Float, c: Float) = vanilla.set(a, b, c)
    override fun setValue(a: Float, b: Float, c: Float, d: Float) = vanilla.setAndFlip(a, b, c, d)
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

/**
 * Taken from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
internal class ShaderTransformer {
    val attributes = mutableSetOf<String>()
    val samplers = mutableSetOf<String>()
    val uniforms = mutableMapOf<String, UniformType>()

    fun transform(originalSource: String): String {
        var source = originalSource

        source = source.replace("gl_ModelViewProjectionMatrix", "gl_ProjectionMatrix * gl_ModelViewMatrix")
        source = source.replace("texture2D", "texture")

        val replacements = mutableMapOf<String, String>()
        val transformed = mutableListOf<String>()
        transformed.add("#version 150")

        val frag = "gl_FragColor" in source
        val vert = !frag

        if (frag) {
            transformed.add("out vec4 uc_FragColor;")
            replacements["gl_FragColor"] = "uc_FragColor"
        }

        if (vert && "gl_FrontColor" in source) {
            transformed.add("out vec4 uc_FrontColor;")
            replacements["gl_FrontColor"] = "uc_FrontColor"
        }
        if (frag && "gl_Color" in source) {
            transformed.add("in vec4 uc_FrontColor;")
            replacements["gl_Color"] = "uc_FrontColor"
        }

        fun replaceAttribute(needle: String, type: String, replacementName: String = "uc_" + needle.substringAfter("_"), replacement: String = replacementName) {
            if (needle in source) {
                replacements[needle] = replacement
                if (replacementName !in attributes) {
                    attributes.add(replacementName)
                    transformed.add("in $type $replacementName;")
                }
            }
        }
        if (vert) {
            replaceAttribute("gl_Vertex", "vec3", replacement = "vec4(uc_Vertex, 1.0)")
            replaceAttribute("gl_Color", "vec4")
            replaceAttribute("gl_MultiTexCoord0.st", "vec2", "uc_UV0")
            replaceAttribute("gl_MultiTexCoord1.st", "vec2", "uc_UV1")
            replaceAttribute("gl_MultiTexCoord2.st", "vec2", "uc_UV2")
        }

        fun replaceUniform(needle: String, type: UniformType, replacementName: String, replacement: String = replacementName) {
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
                line.startsWith("varying ") -> (if (frag) "in " else "out ") + line.substringAfter("varying ")
                line.startsWith("uniform ") -> {
                    val (_, glslType, name) = line.trimEnd(';').split(" ")
                    if (glslType == "sampler2D") {
                        samplers.add(name)
                    } else {
                        uniforms[name] = UniformType.fromGlsl(glslType)
                    }
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
//#endif
