package dev.deftu.omnicore.client.shaders

public interface ShaderUniform {
    public val location: Int
}

public interface IntUniform : ShaderUniform {
    public fun setValue(value: Int)
}

public interface VecUniform : ShaderUniform {
    public fun setValue(a: Float)
}

public interface Vec2Uniform : ShaderUniform {
    public fun setValue(a: Float, b: Float)
}

public interface Vec3Uniform : ShaderUniform {
    public fun setValue(a: Float, b: Float, c: Float)
}

public interface Vec4Uniform : ShaderUniform {
    public fun setValue(a: Float, b: Float, c: Float, d: Float)
}

public interface MatrixUniform : ShaderUniform {
    public fun setValue(matrix: FloatArray)
}

public interface SamplerUniform : ShaderUniform {
    public fun setValue(value: Int)
}
