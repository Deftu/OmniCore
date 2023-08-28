package xyz.deftu.multi.shader

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper;
//#endif

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL20

interface MultiShader {
    companion object {
        @JvmStatic fun fromLegacyShader(
            name: String,
            vert: String,
            frag: String,
            blend: BlendState
        ): MultiShader {
            //#if MC >= 1.17
            return VanillaShader.fromLegacy(name, vert, frag, blend)
            //#else
            //$$ return GlShader(name, vert, frag, blend)
            //#endif
        }

        @JvmStatic fun readFromResource(
            name: String,
            vert: String,
            frag: String,
            blend: BlendState
        ) = fromLegacyShader(name, readShader(vert, "vsh"), readShader(frag, "fsh"), blend)

        private fun readShader(
            name: String,
            extension: String
        ) = MultiShader::class.java.getResource("/shaders/$name.$extension")?.readText() ?: throw IllegalArgumentException("Shader $name not found")

        // Utilities

        @JvmStatic fun getCurrentProgram(): Int {
            //#if MC >= 1.17
            return GlStateManager._getInteger(GL20.GL_CURRENT_PROGRAM)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getInteger(GL20.GL_CURRENT_PROGRAM)
            //#else
            //$$ return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
            //#endif
        }

        @JvmStatic fun createProgram(): Int {
            //#if MC >= 1.17
            return GlStateManager.glCreateProgram()
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.createProgram()
            //#else
            //$$ return OpenGlHelper.glCreateProgram()
            //#endif
        }

        @JvmStatic fun linkProgram(program: Int) {
            //#if MC >= 1.17
            GlStateManager.glLinkProgram(program)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.linkProgram(program)
            //#else
            //$$ OpenGlHelper.glLinkProgram(program)
            //#endif
        }

        @JvmStatic fun getProgram(program: Int, pname: Int): Int {
            //#if MC >= 1.17
            return GlStateManager.glGetProgrami(program, pname)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getProgram(program, pname)
            //#else
            //$$ return OpenGlHelper.glGetProgrami(program, pname)
            //#endif
        }

        @JvmStatic fun getProgramInfoLog(program: Int, maxLength: Int): String {
            //#if MC >= 1.17
            return GlStateManager.glGetProgramInfoLog(program, maxLength)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getProgramInfoLog(program, maxLength)
            //#else
            //$$ return OpenGlHelper.glGetProgramInfoLog(program, maxLength)
            //#endif
        }

        @JvmStatic fun useProgram(program: Int) {
            //#if MC >= 1.17
            GlStateManager._glUseProgram(program)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.useProgram(program)
            //#else
            //$$ OpenGlHelper.glUseProgram(program)
            //#endif
        }

        @JvmStatic fun createShader(type: Int): Int {
            //#if MC >= 1.17
            return GlStateManager.glCreateShader(type)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.createShader(type)
            //#else
            //$$ return OpenGlHelper.glCreateShader(type)
            //#endif
        }

        @JvmStatic fun compileShader(shader: Int) {
            //#if MC >= 1.17
            GlStateManager.glCompileShader(shader)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.compileShader(shader)
            //#else
            //$$ OpenGlHelper.glCompileShader(shader)
            //#endif
        }

        @JvmStatic fun getShader(shader: Int, pname: Int): Int {
            //#if MC >= 1.17
            return GlStateManager.glGetShaderi(shader, pname)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getShader(shader, pname)
            //#else
            //$$ return OpenGlHelper.glGetShaderi(shader, pname)
            //#endif
        }

        @JvmStatic fun getShaderInfoLog(shader: Int, maxLength: Int): String {
            //#if MC >= 1.17
            return GlStateManager.glGetShaderInfoLog(shader, maxLength)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getShaderInfoLog(shader, maxLength)
            //#else
            //$$ return OpenGlHelper.glGetShaderInfoLog(shader, maxLength)
            //#endif
        }

        @JvmStatic fun attachShader(program: Int, shader: Int) {
            //#if MC >= 1.17
            GlStateManager.glAttachShader(program, shader)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.attachShader(program, shader)
            //#else
            //$$ OpenGlHelper.glAttachShader(program, shader)
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
