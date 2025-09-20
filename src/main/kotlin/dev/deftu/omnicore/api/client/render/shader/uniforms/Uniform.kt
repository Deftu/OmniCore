package dev.deftu.omnicore.api.client.render.shader.uniforms

import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public sealed interface Uniform {
    public interface Float1Uniform : Uniform {
        public fun set(value: Float)
    }

    public interface Int1Uniform : Uniform {
        public fun set(value: Int)
    }

    public interface Bool1Uniform : Uniform {
        public fun set(value: Boolean)
    }

    public interface Vec2fUniform : Uniform {
        public fun set(x: Float, y: Float)
        public fun set(v: FloatArray)
    }

    public interface Vec3fUniform : Uniform {
        public fun set(x: Float, y: Float, z: Float)
        public fun set(v: FloatArray)
    }

    public interface Vec4fUniform : Uniform {
        public fun set(x: Float, y: Float, z: Float, w: Float)
        public fun set(v: FloatArray)
    }

    public interface Mat2fUniform : Uniform {
        public fun set(m: FloatArray)
    }

    public interface Mat3fUniform : Uniform {
        public fun set(m: FloatArray)
    }

    public interface Mat4fUniform : Uniform {
        public fun set(m: FloatArray)
    }

    public interface SamplerUniform : Uniform {
        public val target: SamplerTarget

        public fun setTextureIndex(index: Int)
        public fun setTexture(id: Int)
    }

    public val location: Int
    public val kind: UniformKind
}
