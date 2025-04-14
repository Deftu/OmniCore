package dev.deftu.omnicore.client.shaders

import dev.deftu.omnicore.client.render.state.OmniManagedBlendState
import dev.deftu.omnicore.client.render.OmniTextureManager
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20

//#if MC <= 1.12.2
//$$ import java.nio.ByteBuffer
//$$ import java.nio.ByteOrder
//#endif

internal class GlShader(
    val vert: String,
    val frag: String,
    val blend: OmniManagedBlendState
) : OmniShader {
    private var program = OmniShader.createProgram()
    private var vertShader = OmniShader.createShader(GL20.GL_VERTEX_SHADER)
    private var fragShader = OmniShader.createShader(GL20.GL_FRAGMENT_SHADER)
    private var samplers = mutableMapOf<String, DirectSamplerUniform>()

    override var usable: Boolean = false
    var bound = false
        private set

    private var prevActiveTexture = GL11.GL_NONE
    private var prevTextureBindings = mutableMapOf<Int, Int>()
    private var prevBlendState: OmniManagedBlendState? = null

    init {
        setupShader()
    }

    override fun bind() {
        prevActiveTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)
        for (sampler in samplers.values) bindTexture(sampler.textureUnit, sampler.texture)
        prevBlendState = OmniManagedBlendState.active()
        blend.activate()
        OmniShader.useProgram(program)
        bound = true
    }

    override fun unbind() {
        for ((textureUnit, texture) in prevTextureBindings) {
            OmniTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
            OmniTextureManager.bindTexture(texture)
        }

        prevTextureBindings.clear()
        OmniTextureManager.setActiveTexture(prevActiveTexture)
        prevBlendState?.activate()

        OmniShader.useProgram(GL11.GL_NONE)
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
        val location = OmniShader.getUniformLocation(program, name)
        return if (location == -1) null else location
    }

    internal inline fun withProgram(block: () -> Unit) {
        if (!bound) {
            val previousProgram = OmniShader.getCurrentProgram()
            try {
                OmniShader.useProgram(program)
                block()
            } finally {
                OmniShader.useProgram(previousProgram)
            }
        } else block()
    }

    internal fun bindTexture(textureUnit: Int, texture: Int) {
        OmniTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
        prevTextureBindings.computeIfAbsent(textureUnit) { GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) }
        OmniTextureManager.bindTexture(texture)
    }

    private fun setupShader() {
        for ((shader, source) in listOf(
            vertShader to vert,
            fragShader to frag
        )) {
            OmniShader.shaderSource(shader, source)
            OmniShader.compileShader(shader)
            if (OmniShader.getShader(shader, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
                val log = OmniShader.getShaderInfoLog(shader, Short.MAX_VALUE.toInt())
                throw IllegalStateException("Shader failed to compile: $log")
            }

            OmniShader.attachShader(program, shader)
        }

        OmniShader.linkProgram(program)
        OmniShader.detachShader(program, vertShader)
        OmniShader.detachShader(program, fragShader)
        OmniShader.deleteShader(vertShader)
        OmniShader.deleteShader(fragShader)

        if (OmniShader.getProgram(program, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
            val log = OmniShader.getProgramInfoLog(program, Short.MAX_VALUE.toInt())
            throw IllegalStateException("Shader failed to link: $log")
        }

        OmniShader.validateProgram(program)
        if (OmniShader.getProgramValidateStatus(program) != GL11.GL_TRUE) {
            val log = OmniShader.getProgramInfoLog(program, Short.MAX_VALUE.toInt())
            throw IllegalStateException("Shader failed to validate: $log")
        }

        usable = true
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
        OmniShader.uniform1i(location, value)
    }

    override fun setValue(a: Float) {
        OmniShader.uniform1f(location, a)
    }

    override fun setValue(a: Float, b: Float) {
        OmniShader.uniform2f(location, a, b)
    }

    override fun setValue(a: Float, b: Float, c: Float) {
        OmniShader.uniform3f(location, a, b, c)
    }

    override fun setValue(a: Float, b: Float, c: Float, d: Float) {
        OmniShader.uniform4f(location, a, b, c, d)
    }

    override fun setValue(matrix: FloatArray) {
        val size = matrix.size
        //#if MC <= 1.12.2
        //$$ val matrix = ByteBuffer.allocateDirect(matrix.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        //$$ matrix.put(matrix)
        //$$ matrix.rewind()
        //#endif

        when (size) {
            4 -> OmniShader.uniformMatrix2(location, false, matrix)
            9 -> OmniShader.uniformMatrix3(location, false, matrix)
            16 -> OmniShader.uniformMatrix4(location, false, matrix)
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
