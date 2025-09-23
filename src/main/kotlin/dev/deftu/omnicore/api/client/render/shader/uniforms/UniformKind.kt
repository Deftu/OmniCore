package dev.deftu.omnicore.api.client.render.shader.uniforms

import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.21.5
import net.minecraft.client.gl.UniformType
//#endif

@ApiStatus.Experimental
public sealed interface UniformKind {
    public val shaderName: String
        get() = when (this) {
            Float1 -> "float"
            Int1 -> "int"
            Bool1 -> "bool"

            Vec2f -> "vec2"
            Vec3f -> "vec3"
            Vec4f -> "vec4"

            Mat2f -> "mat2"
            Mat3f -> "mat3"
            Mat4f -> "mat4"

            is Sampler -> target.shaderName
        }

    //#if MC >= 1.21.5
    public val vanilla: UniformType
        get() {
            //#if MC >= 1.21.6
            return UniformType.UNIFORM_BUFFER
            //#else
            //$$ return when (this) {
            //$$     Int1 -> UniformType.INT
            //$$     Float1 -> UniformType.FLOAT
            //$$     Bool1 -> UniformType.INT
            //$$
            //$$     Vec2f -> UniformType.VEC2
            //$$     Vec3f -> UniformType.VEC3
            //$$     Vec4f -> UniformType.VEC4
            //$$
            //$$     Mat2f -> throw IllegalStateException("Mat2f is not supported in 1.21.5+")
            //$$     Mat3f -> throw IllegalStateException("Mat3f is not supported in 1.21.5+")
            //$$     Mat4f -> UniformType.MATRIX4X4
            //$$
            //$$     else -> throw IllegalStateException("Can't convert this kind of uniform to vanilla type: $this")
            //$$ }
            //#endif
        }
    //#endif

    public val default: IntArray
        get() = when (this) {
            Float1 -> intArrayOf(0)
            Int1 -> intArrayOf(0)
            Bool1 -> intArrayOf(0)

            Vec2f -> intArrayOf(0, 0)
            Vec3f -> intArrayOf(0, 0, 0)
            Vec4f -> intArrayOf(0, 0, 0, 0)

            Mat2f -> intArrayOf(1, 0, 0, 1)
            Mat3f -> intArrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1)
            Mat4f -> intArrayOf(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)

            is Sampler -> intArrayOf(0)
        }

    public val isSampler: Boolean
        get() = this is Sampler

    public val isNonSampler: Boolean
        get() = !isSampler

    public data object Float1 : UniformKind
    public data object Int1 : UniformKind
    public data object Bool1 : UniformKind

    public data object Vec2f : UniformKind
    public data object Vec3f : UniformKind
    public data object Vec4f : UniformKind

    public data object Mat2f : UniformKind
    public data object Mat3f : UniformKind
    public data object Mat4f : UniformKind

    public data class Sampler(public val target: SamplerTarget) : UniformKind
}
