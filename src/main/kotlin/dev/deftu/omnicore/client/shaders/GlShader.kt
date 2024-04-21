package dev.deftu.omnicore.shaders

import dev.deftu.omnicore.MultiTextureManager
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20

internal class GlShader(
    val vert: String,
    val frag: String,
    val blend: BlendState
) : MultiShader {
    private var program = MultiShader.createProgram()
    private var vertShader = MultiShader.createShader(GL20.GL_VERTEX_SHADER)
    private var fragShader = MultiShader.createShader(GL20.GL_FRAGMENT_SHADER)
    private var samplers = mutableMapOf<String, DirectSamplerUniform>()

    override val usable: Boolean = false
    var bound = false
        private set

    private var prevActiveTexture = GL11.GL_NONE
    private var prevTextureBindings = mutableMapOf<Int, Int>()
    private var prevBlendState: BlendState? = null

    init {
        setupShader()
    }

    override fun bind() {
        prevActiveTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)
        for (sampler in samplers.values) bindTexture(sampler.textureUnit, sampler.texture)
        prevBlendState = BlendState.active()
        blend.activate()
        MultiShader.useProgram(program)
        bound = true
    }

    override fun unbind() {
        for ((textureUnit, texture) in prevTextureBindings) {
            MultiTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
            MultiTextureManager.bindTexture(texture)
        }

        prevTextureBindings.clear()
        MultiTextureManager.setActiveTexture(prevActiveTexture)
        prevBlendState?.activate()
        MultiShader.useProgram(GL11.GL_NONE)
        bound = false
    }

     override fun getIntUniformOrNull(name: String): IntUniform? = getUniformLocation(name)?.let(::DirectUniform)
     override fun getVecUniformOrNull(name: String): VecUniform? = getUniformLocation(name)?.let(::DirectUniform)
     override fun getVec2UniformOrNull(name: String): Vec2Uniform? = getUniformLocation(name)?.let(::DirectUniform)
     override fun getVec3UniformOrNull(name: String): Vec3Uniform? = getUniformLocation(name)?.let(::DirectUniform)
     override fun getVec4UniformOrNull(name: String): Vec4Uniform? = getUniformLocation(name)?.let(::DirectUniform)
     override fun getMatrixUniformOrNull(name: String): MatrixUniform? = getUniformLocation(name)?.let(::DirectUniform)
     override fun getSamplerUniformOrNull(name: String): SamplerUniform? {
         samplers[name]?.let { return it }
         val location = getUniformLocation(name) ?: return null
         val uniform = DirectSamplerUniform(location, samplers.size, this)
         samplers[name] = uniform
         return uniform
     }

    private fun getUniformLocation(name: String): Int? {
        val location = MultiShader.getUniformLocation(program, name)
        return if (location == -1) null else location
    }

    internal inline fun withProgram(block: () -> Unit) {
        if (!bound) {
            val previousProgram = MultiShader.getCurrentProgram()
            try {
                MultiShader.useProgram(program)
                block()
            } finally {
                MultiShader.useProgram(previousProgram)
            }
        } else block()
    }

    internal fun bindTexture(textureUnit: Int, texture: Int) {
        MultiTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
        prevTextureBindings.computeIfAbsent(textureUnit) { GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) }
        MultiTextureManager.bindTexture(texture)
    }

    private fun setupShader() {
        for ((shader, source) in listOf(
            vertShader to vert,
            fragShader to frag
        )) {
            MultiShader.shaderSource(shader, source)
            MultiShader.compileShader(shader)
            if (MultiShader.getShader(shader, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
                val log = MultiShader.getShaderInfoLog(shader, Short.MAX_VALUE.toInt())
                throw IllegalStateException("Shader failed to compile: $log")
            }

            MultiShader.attachShader(program, shader)
        }

        MultiShader.linkProgram(program)

        if (MultiShader.getProgram(program, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
            val log = MultiShader.getProgramInfoLog(program, Short.MAX_VALUE.toInt())
            throw IllegalStateException("Shader failed to link: $log")
        }

        MultiShader.validateProgram(program)
        if (MultiShader.getProgramValidateStatus(program) != GL11.GL_TRUE) {
            val log = MultiShader.getProgramInfoLog(program, Short.MAX_VALUE.toInt())
            throw IllegalStateException("Shader failed to validate: $log")
        }

        MultiShader.detachShader(program, vertShader)
        MultiShader.detachShader(program, fragShader)
        MultiShader.deleteShader(vertShader)
        MultiShader.deleteShader(fragShader)
    }
}

internal class DirectUniform(
    override val location: Int
) : ShaderUniform,
    IntUniform,
    VecUniform,
    Vec2Uniform,
    Vec3Uniform,
    Vec4Uniform,
    MatrixUniform {

    override fun setValue(value: Int) {
        MultiShader.uniform1i(location, value)
    }

    override fun setValue(a: Float) {
        MultiShader.uniform1f(location, a)
    }

    override fun setValue(a: Float, b: Float) {
        MultiShader.uniform2f(location, a, b)
    }

    override fun setValue(a: Float, b: Float, c: Float) {
        MultiShader.uniform3f(location, a, b, c)
    }

    override fun setValue(a: Float, b: Float, c: Float, d: Float) {
        MultiShader.uniform4f(location, a, b, c, d)
    }

    override fun setValue(matrix: FloatArray) {
        val size = matrix.size
        //#if MC <= 1.12.2
        //$$ val matrix = ByteBuffer.allocateDirect(matrix.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        //$$ matrix.put(matrix)
        //$$ matrix.rewind()
        //#endif

        when (size) {
            4 -> MultiShader.uniformMatrix2fv(location, false, matrix)
            9 -> MultiShader.uniformMatrix3fv(location, false, matrix)
            16 -> MultiShader.uniformMatrix4fv(location, false, matrix)
            else -> throw IllegalArgumentException("Invalid matrix size: $size")
        }
    }
}

internal class DirectSamplerUniform(
    override val location: Int,
    val textureUnit: Int,
    private val shader: GlShader
) : SamplerUniform {
    var texture: Int = 0

    init {
        shader.withProgram {
            DirectUniform(location).setValue(textureUnit)
        }
    }

    override fun setValue(value: Int) {
        texture = value
        if (shader.bound) {
            shader.bindTexture(textureUnit, texture)
        }
    }
}
