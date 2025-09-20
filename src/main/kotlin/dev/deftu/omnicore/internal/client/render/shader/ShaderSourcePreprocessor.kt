package dev.deftu.omnicore.internal.client.render.shader

import com.mojang.blaze3d.vertex.VertexFormat
import org.jetbrains.annotations.ApiStatus
import kotlin.collections.set

//#if MC < 1.21.6
//$$ import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind
//#endif

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0 - https://github.com/EssentialGG/UniversalCraft/blob/91ec8f826e7f065f8a70460ab00e6f8f30ad9e6a/src/main/kotlin/gg/essential/universal/shader/ShaderTransformer.kt
 * https://github.com/EssentialGG/UniversalCraft/blob/91ec8f826e7f065f8a70460ab00e6f8f30ad9e6a/LICENSE
 */
@ApiStatus.Internal
public class ShaderSourcePreprocessor(
    private val vertexFormat: VertexFormat?,
    private val targetVersion: Int,
) {
    init {
        if (targetVersion !in setOf(110, 130, 150)) {
            throw IllegalArgumentException("Target version $targetVersion is not supported. Supported versions are 110, 130, and 150.")
        }
    }

    public fun processVertexShader(originalSource: String): String {
        return processShader(originalSource, isVertex = true)
    }

    public fun processFragmentShader(originalSource: String): String {
        return processShader(originalSource, isVertex = false)
    }

    public fun processShader(originalSource: String, isVertex: Boolean): String {
        var source = originalSource

        source = source.replace("gl_ModelViewProjectionMatrix", "gl_ProjectionMatrix * gl_ModelViewMatrix")
        if (targetVersion >= 130) {
            source = source.replace("texture2D", "texture")
        }

        val replacements = mutableMapOf<String, String>()
        val transformed = mutableListOf<String>()
        transformed.add("#version $targetVersion")

        val isFrag = "gl_FragColor" in originalSource
        val isVert = !isFrag

        val attributeIn = if (targetVersion >= 130) "in" else "attribute"
        val varyingIn = if (targetVersion >= 130) "in" else "varying"
        val varyingOut = if (targetVersion >= 130) "out" else "varying"

        if (isFrag && targetVersion >= 130) {
            transformed.add("$varyingOut vec4 oc_FragColor;")
            replacements["gl_FragColor"] = "oc_FragColor"
        }

        if (isVert && "gl_FrontColor" in source) {
            transformed.add("$varyingOut vec4 oc_FrontColor;")
            replacements["gl_FrontColor"] = "oc_FrontColor"
        }

        if (isFrag && "gl_Color" in source) {
            transformed.add("$varyingIn vec4 oc_FrontColor;")
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
                    value.add(replacementName to "$attributeIn $type $replacementName;")
                }
            }

            val newAttributes = mutableListOf<Pair<String, String>>()
            replaceAttrib(newAttributes, "gl_Vertex", "vec3", "oc_Position", replacement = "vec4(oc_Position, 1.0)")
            replaceAttrib(newAttributes, "gl_Color", "vec4")
            replaceAttrib(newAttributes, "gl_MultiTexCoord0.st", "vec2", "oc_UV0")
            replaceAttrib(newAttributes, "gl_MultiTexCoord1.st", "vec2", "oc_UV1")
            replaceAttrib(newAttributes, "gl_MultiTexCoord2.st", "vec2", "oc_UV2")

            //#if MC >= 1.17.1
            if (vertexFormat != null) {
                newAttributes.sortedBy { (name, type) ->
                    vertexFormat.elementAttributeNames.indexOf(name.removePrefix("oc_"))
                }.forEach { (name, type) ->
                    transformed.add(type)
                }
            } else {
                newAttributes.forEach { (name, type) ->
                    transformed.add(type)
                }
            }
            //#else
            //$$ newAttributes.forEach { (name, type) ->
            //$$     transformed.add(type)
            //$$ }
            //#endif
        }

        //#if MC >= 1.21.6
        transformed.add("""
            layout(std140) uniform Projection {
                mat4 ProjMat;
            };

            layout(std140) uniform DynamicTransforms {
                mat4 ModelViewMat;
                vec4 ColorModulator;
                vec3 ModelOffset;
                mat4 TextureMat;
                float LineWidth;
            };
        """.trimIndent())
        replacements["gl_ModelViewMatrix"] = "ModelViewMat"
        replacements["gl_ProjectionMatrix"] = "ProjMat"
        //#else
        //$$ fun replaceUniform(
        //$$     needle: String,
        //$$     kind: UniformKind,
        //$$     replacementName: String,
        //$$     replacement: String = replacementName
        //$$ ) {
        //$$     if (needle in source) {
        //$$         replacements[needle] = replacement
        //$$         transformed.add("uniform ${kind.shaderName} $replacementName;")
        //$$     }
        //$$ }
        //$$
        //$$ replaceUniform("gl_ModelViewMatrix", UniformKind.Mat4f, "ModelViewMat")
        //$$ replaceUniform("gl_ProjectionMatrix", UniformKind.Mat4f, "ProjMat")
        //#endif

        for (line in source.lines()) {
            transformed.add(when {
                line.startsWith("#version") -> continue
                line.startsWith("varying ") && targetVersion >= 130 -> (if (isFrag) "in " else "out ") + line.substringAfter("varying ")
                line.startsWith("uniform ") -> {
                    val (_, typeName, name) = line.trimEnd(';').split(" ")
                    if (typeName == "sampler2D") {
                        line
                    } else {
                        //#if MC >= 1.21.6
                        replacements[name] = "oc_$name"
                        "layout(std140) uniform $name { typeName oc_$name; };"
                        //#else
                        //$$ line
                        //#endif
                    }
                }

                else -> replacements.entries.fold(line) { acc, (needle, replacement) -> acc.replace(needle, replacement) }
            })
        }

        return transformed.joinToString("\n")
    }
}
