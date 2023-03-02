package xyz.deftu.multi.shader

interface ShaderUniform {
    val location: Int
}

interface IntUniform : ShaderUniform {
    fun setValue(value: Int)
}

interface VecUniform : ShaderUniform {
    fun setValue(a: Float)
}

interface Vec2Uniform : ShaderUniform {
    fun setValue(a: Float, b: Float)
}

interface Vec3Uniform : ShaderUniform {
    fun setValue(a: Float, b: Float, c: Float)
}

interface Vec4Uniform : ShaderUniform {
    fun setValue(a: Float, b: Float, c: Float, d: Float)
}

interface MatrixUniform : ShaderUniform {
    fun setValue(matrix: FloatArray)
}

interface SamplerUniform : ShaderUniform {
    fun setValue(value: Int)
}
