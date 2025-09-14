package dev.deftu.omnicore.api.client.render.shader.uniforms

public sealed interface UniformDefinition<T> {
    public val name: String
    public val kind: UniformKind
    public val location: Int?
    public val divisor: Int
    public val defaultValue: T?

    public fun declaration(version: Int): String {
        val qualifier = if (version >= 130) "uniform" else "varying"
        val type = when (kind) {
            UniformKind.Float1 -> "float"
            UniformKind.Int1 -> "int"
            UniformKind.Bool1 -> "bool"
            UniformKind.Vec2f -> "vec2"
            UniformKind.Vec3f -> "vec3"
            UniformKind.Vec4f -> "vec4"
            UniformKind.Mat2f -> "mat2"
            UniformKind.Mat3f -> "mat3"
            UniformKind.Mat4f -> "mat4"
            is UniformKind.Sampler -> when ((kind as UniformKind.Sampler).target) {
                SamplerTarget.TEX_1D -> "sampler1D"
                SamplerTarget.TEX_2D -> "sampler2D"
                SamplerTarget.TEX_3D -> "sampler3D"
                SamplerTarget.TEX_CUBE -> "samplerCube"
            }
        }

        val loc = if (version >= 130 && location != null) {
            "layout(location = $location) "
        } else {
            ""
        }

        return "$loc$qualifier $type $name;"
    }

    public data class Float1(
        override val name: String,
        override val defaultValue: Float? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<Float> {
        override val kind: UniformKind = UniformKind.Float1
    }

    public data class Int1(
        override val name: String,
        override val defaultValue: Int? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<Int> {
        override val kind: UniformKind = UniformKind.Int1
    }

    public data class Bool1(
        override val name: String,
        override val defaultValue: Boolean? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<Boolean> {
        override val kind: UniformKind = UniformKind.Bool1
    }

    public data class Vec2f(
        override val name: String,
        override val defaultValue: FloatArray? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<FloatArray> {
        override val kind: UniformKind = UniformKind.Vec2f

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Vec2f

            if (name != other.name) return false
            if (!defaultValue.contentEquals(other.defaultValue)) return false
            if (kind != other.kind) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + (defaultValue?.contentHashCode() ?: 0)
            result = 31 * result + kind.hashCode()
            return result
        }
    }

    public data class Vec3f(
        override val name: String,
        override val defaultValue: FloatArray? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<FloatArray> {
        override val kind: UniformKind = UniformKind.Vec3f

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Vec3f

            if (name != other.name) return false
            if (!defaultValue.contentEquals(other.defaultValue)) return false
            if (kind != other.kind) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + (defaultValue?.contentHashCode() ?: 0)
            result = 31 * result + kind.hashCode()
            return result
        }
    }

    public data class Vec4f(
        override val name: String,
        override val defaultValue: FloatArray? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<FloatArray> {
        override val kind: UniformKind = UniformKind.Vec4f

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Vec4f

            if (name != other.name) return false
            if (!defaultValue.contentEquals(other.defaultValue)) return false
            if (kind != other.kind) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + (defaultValue?.contentHashCode() ?: 0)
            result = 31 * result + kind.hashCode()
            return result
        }
    }

    public data class Mat2f(
        override val name: String,
        override val defaultValue: FloatArray? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<FloatArray> {
        override val kind: UniformKind = UniformKind.Mat2f

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Mat2f

            if (name != other.name) return false
            if (!defaultValue.contentEquals(other.defaultValue)) return false
            if (kind != other.kind) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + (defaultValue?.contentHashCode() ?: 0)
            result = 31 * result + kind.hashCode()
            return result
        }
    }

    public data class Mat3f(
        override val name: String,
        override val defaultValue: FloatArray? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<FloatArray> {
        override val kind: UniformKind = UniformKind.Mat3f

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Mat3f

            if (name != other.name) return false
            if (!defaultValue.contentEquals(other.defaultValue)) return false
            if (kind != other.kind) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + (defaultValue?.contentHashCode() ?: 0)
            result = 31 * result + kind.hashCode()
            return result
        }
    }

    public data class Mat4f(
        override val name: String,
        override val defaultValue: FloatArray? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<FloatArray> {
        override val kind: UniformKind = UniformKind.Mat4f

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Mat4f

            if (name != other.name) return false
            if (!defaultValue.contentEquals(other.defaultValue)) return false
            if (kind != other.kind) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + (defaultValue?.contentHashCode() ?: 0)
            result = 31 * result + kind.hashCode()
            return result
        }
    }

    public data class Sampler(
        override val name: String,
        public val target: SamplerTarget,
        override val defaultValue: Int? = null,
        override val location: Int? = null,
        override val divisor: Int = 0,
    ) : UniformDefinition<Int> {
        override val kind: UniformKind = UniformKind.Sampler(target)
    }
}
