package xyz.deftu.multi.shader

interface BaseShader {
    companion object {
        @JvmStatic fun fromLegacyShader(
            vert: String,
            frag: String,
            blend: BlendState
        ): BaseShader {
            //#if MC>=11700
            return VanillaShader.fromLegacy(vert, frag, blend)
            //#else
            //$$ return GlShader(vert, frag, blend)
            //#endif
        }
    }

    val usable: Boolean

    fun bind()
    fun unbind()

    fun getIntUniformOrNull(name: String): IntUniform?
    fun getVecUniformOrNull(name: String): VecUniform?
    fun getVec2UniformOrNull(name: String): Vec2Uniform?
    fun getVec3UniformOrNull(name: String): Vec3Uniform?
    fun getVec4UniformOrNull(name: String): Vec4Uniform?
    fun getMatrixUniformOrNull(name: String): MatrixUniform?
    fun getSamplerUniformOrNull(name: String): SamplerUniform?

    fun getIntUniform(name: String) = getIntUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    fun getVecUniform(name: String) = getVecUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    fun getVec2Uniform(name: String) = getVec2UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    fun getVec3Uniform(name: String) = getVec3UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    fun getVec4Uniform(name: String) = getVec4UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    fun getMatrixUniform(name: String) = getMatrixUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    fun getSamplerUniform(name: String) = getSamplerUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
}
