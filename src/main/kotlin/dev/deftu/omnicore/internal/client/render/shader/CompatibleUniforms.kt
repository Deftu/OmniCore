package dev.deftu.omnicore.internal.client.render.shader

import dev.deftu.omnicore.api.client.render.shader.uniforms.SamplerTarget
import dev.deftu.omnicore.api.client.render.shader.uniforms.Uniform
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind

//#if MC < 1.16.5
//$$ import java.nio.ByteBuffer
//$$ import java.nio.ByteOrder
//$$ import java.nio.FloatBuffer
//$$
//$$ private fun createSizedBuffer(array: FloatArray): FloatBuffer {
//$$     val buffer = ByteBuffer.allocateDirect(array.size * 4)
//$$         .order(ByteOrder.nativeOrder())
//$$         .asFloatBuffer()
//$$     buffer.put(array)
//$$     buffer.rewind()
//$$     return buffer
//$$ }
//#endif

public class CompatibleFloat1Uniform(override val location: Int) : Uniform.Float1Uniform {
    override val kind: UniformKind = UniformKind.Float1

    override fun set(value: Float) {
        ShaderInternals.uniform1f(location, value)
    }
}

public class CompatibleInt1Uniform(override val location: Int) : Uniform.Int1Uniform {
    override val kind: UniformKind = UniformKind.Int1

    override fun set(value: Int) {
        ShaderInternals.uniform1i(location, value)
    }
}

public class CompatibleBool1Uniform(override val location: Int) : Uniform.Bool1Uniform {
    override val kind: UniformKind = UniformKind.Bool1

    override fun set(value: Boolean) {
        ShaderInternals.uniform1i(location, if (value) 1 else 0)
    }
}

public class CompatibleVec2fUniform(override val location: Int) : Uniform.Vec2fUniform {
    override val kind: UniformKind = UniformKind.Vec2f

    override fun set(x: Float, y: Float) {
        ShaderInternals.uniform2f(location, x, y)
    }

    override fun set(v: FloatArray) {
        require(v.size == 2) { "Expected array of size 2, got ${v.size}" }
        ShaderInternals.uniform2f(location, v[0], v[1])
    }
}

public class CompatibleVec3fUniform(override val location: Int) : Uniform.Vec3fUniform {
    override val kind: UniformKind = UniformKind.Vec3f

    override fun set(x: Float, y: Float, z: Float) {
        ShaderInternals.uniform3f(location, x, y, z)
    }

    override fun set(v: FloatArray) {
        require(v.size == 3) { "Expected array of size 3, got ${v.size}" }
        ShaderInternals.uniform3f(location, v[0], v[1], v[2])
    }
}

public class CompatibleVec4fUniform(override val location: Int) : Uniform.Vec4fUniform {
    override val kind: UniformKind = UniformKind.Vec4f

    override fun set(x: Float, y: Float, z: Float, w: Float) {
        ShaderInternals.uniform4f(location, x, y, z, w)
    }

    override fun set(v: FloatArray) {
        require(v.size == 4) { "Expected array of size 4, got ${v.size}" }
        ShaderInternals.uniform4f(location, v[0], v[1], v[2], v[3])
    }
}

public class CompatibleMat2fUniform(override val location: Int) : Uniform.Mat2fUniform {
    override val kind: UniformKind = UniformKind.Mat2f

    override fun set(m: FloatArray) {
        require(m.size == 4) { "Expected array of size 4, got ${m.size}" }
        //#if MC < 1.16.5
        //$$ val m = createSizedBuffer(m)
        //#endif
        ShaderInternals.uniformMatrix2(location, false, m)
    }
}

public class CompatibleMat3fUniform(override val location: Int) : Uniform.Mat3fUniform {
    override val kind: UniformKind = UniformKind.Mat3f

    override fun set(m: FloatArray) {
        require(m.size == 9) { "Expected array of size 9, got ${m.size}" }
        //#if MC < 1.16.5
        //$$ val m = createSizedBuffer(m)
        //#endif
        ShaderInternals.uniformMatrix3(location, false, m)
    }
}

public class CompatibleMat4fUniform(override val location: Int) : Uniform.Mat4fUniform {
    override val kind: UniformKind = UniformKind.Mat4f

    override fun set(m: FloatArray) {
        require(m.size == 16) { "Expected array of size 16, got ${m.size}" }
        //#if MC < 1.16.5
        //$$ val m = createSizedBuffer(m)
        //#endif
        ShaderInternals.uniformMatrix4(location, false, m)
    }
}

public class CompatibleSamplerUniform(
    private val shader: CompatibleShader,
    override val location: Int,
    override val target: SamplerTarget
) : Uniform.SamplerUniform {
    internal var index: Int = -1
        private set

    internal var texture: Int = -1
        private set

    override val kind: UniformKind = UniformKind.Sampler(target)

    override fun setTextureIndex(index: Int) {
        this.index = index
        ShaderInternals.uniform1i(location, index)
    }

    override fun setTexture(id: Int) {
        require(index >= 0) { "Texture index not set. Call setTextureIndex first." }
        this.texture = id
        if (shader.isBound) {
            shader.bindSampler(index, id)
        }
    }
}
