package dev.deftu.omnicore.internal.client.render.shader

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.render.GlCapabilities
import dev.deftu.omnicore.api.client.render.shader.uniforms.SamplerTarget
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//$$ import java.nio.ByteBuffer
//$$ import java.nio.FloatBuffer
//#endif

@ApiStatus.Internal
public object ShaderInternals {
    // Creation

    @JvmStatic
    public fun program(): Int {
        //#if MC >= 1.16.5
        return GlStateManager.glCreateProgram()
        //#else
        //$$ return OpenGlHelper.glCreateProgram()
        //#endif
    }

    @JvmStatic
    public fun shader(type: Int): Int {
        //#if MC >= 1.16.5
        return GlStateManager.glCreateShader(type)
        //#else
        //$$ return OpenGlHelper.glCreateShader(type)
        //#endif
    }

    @JvmStatic
    public fun vertexShader(): Int {
        return shader(GL20.GL_VERTEX_SHADER)
    }

    @JvmStatic
    public fun fragmentShader(): Int {
        return shader(GL20.GL_FRAGMENT_SHADER)
    }

    // Linking

    @JvmStatic
    public fun compileAndLink(program: Int, vertShader: Int, fragShader: Int, vertSource: String, fragSource: String): Boolean {
        if (!compileShader(vertShader, vertSource)) {
            deleteShader(vertShader)
            return false
        }

        if (!compileShader(fragShader, fragSource)) {
            deleteShader(vertShader)
            deleteShader(fragShader)
            return false
        }

        linkProgram(program)
        attachShader(program, vertShader)
        attachShader(program, fragShader)
        linkProgram(program)

        val linked = programi(program, GL20.GL_LINK_STATUS) == GL11.GL_TRUE
        if (!linked) {
            val log = programInfoLog(program, Short.MAX_VALUE.toInt())
            System.err.println("Failed to link shader program:\n$log")
            deleteShader(vertShader)
            deleteShader(fragShader)
            deleteProgram(program)
            return false
        }

        // Shaders can be deleted after linking
        deleteShader(vertShader)
        deleteShader(fragShader)

        return true
    }

    @JvmStatic
    public fun attachShader(program: Int, shader: Int) {
        //#if MC >= 1.16.5
        GlStateManager.glAttachShader(program, shader)
        //#else
        //$$ OpenGlHelper.glAttachShader(program, shader)
        //#endif
    }

    @JvmStatic
    public fun linkProgram(program: Int) {
        //#if MC >= 1.16.5
        GlStateManager.glLinkProgram(program)
        //#else
        //$$ OpenGlHelper.glLinkProgram(program)
        //#endif
    }

    @JvmStatic
    public fun validateProgram(program: Int) {
        if (GlCapabilities.isGl21Available) {
            GL20.glValidateProgram(program)
        } else {
            ARBShaderObjects.glValidateProgramARB(program)
        }
    }

    @JvmStatic
    public fun deleteProgram(program: Int) {
        //#if MC >= 1.16.5
        GlStateManager.glDeleteProgram(program)
        //#else
        //$$ OpenGlHelper.glDeleteProgram(program)
        //#endif
    }

    @JvmStatic
    public fun useProgram(program: Int) {
        //#if MC >= 1.16.5
        GlStateManager._glUseProgram(program)
        //#else
        //$$ OpenGlHelper.glUseProgram(program)
        //#endif
    }

    @JvmStatic
    public fun currentProgram(): Int {
        return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
    }

    // Info

    @JvmStatic
    public fun programi(program: Int, pname: Int): Int {
        //#if MC >= 1.16.5
        return GlStateManager.glGetProgrami(program, pname)
        //#else
        //$$ return OpenGlHelper.glGetProgrami(program, pname)
        //#endif
    }

    @JvmStatic
    public fun programInfoLog(program: Int, maxLength: Int): String {
        //#if MC >= 1.16.5
        return GlStateManager.glGetProgramInfoLog(program, maxLength)
        //#else
        //$$ return OpenGlHelper.glGetProgramInfoLog(program, maxLength)
        //#endif
    }

    @JvmStatic
    public fun shaderSource(shader: Int, source: String) {
        if (GlCapabilities.isGl21Available) {
            GL20.glShaderSource(shader, source)
        } else {
            ARBShaderObjects.glShaderSourceARB(shader, source)
        }
    }

    @JvmStatic
    public fun compileShader0(shader: Int) {
        //#if MC >= 1.16.5
        GlStateManager.glCompileShader(shader)
        //#else
        //$$ OpenGlHelper.glCompileShader(shader)
        //#endif
    }

    @JvmStatic
    public fun compileShader(shader: Int, source: String): Boolean {
        shaderSource(shader, source)
        compileShader0(shader)

        val compiled = shaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE
        if (!compiled) {
            val log = shaderInfoLog(shader, Short.MAX_VALUE.toInt())
            System.err.println("Failed to compile shader:\n$log")
            return false
        }

        return true
    }

    @JvmStatic
    public fun deleteShader(shader: Int) {
        if (GlCapabilities.isGl21Available) {
            //#if MC >= 1.16.5
            GlStateManager.glDeleteShader(shader)
            //#else
            //$$ GL20.glDeleteShader(shader)
            //#endif
        } else {
            ARBShaderObjects.glDeleteObjectARB(shader)
        }
    }

    @JvmStatic
    public fun shaderi(shader: Int, pname: Int): Int {
        //#if MC >= 1.16.5
        return GlStateManager.glGetShaderi(shader, pname)
        //#else
        //$$ return OpenGlHelper.glGetShaderi(shader, pname)
        //#endif
    }

    @JvmStatic
    public fun shaderInfoLog(shader: Int, maxLength: Int): String {
        //#if MC >= 1.16.5
        return GlStateManager.glGetShaderInfoLog(shader, maxLength)
        //#else
        //$$ return OpenGlHelper.glGetShaderInfoLog(shader, maxLength)
        //#endif
    }

    // Uniform reflection

    @JvmStatic
    public fun activeUniforms(program: Int): Int {
        return programi(program, GL20.GL_ACTIVE_UNIFORMS)
    }

    @JvmStatic
    public fun uniformName(program: Int, index: Int): String? {
        val count = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS)
        if (index !in 0..<count) {
            return null
        }

        val bufSize = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH)
        if (bufSize <= 0) {
            return null
        }

        val length = BufferUtils.createIntBuffer(1)
        val size = BufferUtils.createIntBuffer(1)
        val type = BufferUtils.createIntBuffer(1)
        val name = BufferUtils.createByteBuffer(bufSize)
        GL20.glGetActiveUniform(program, index, length, size, type, name)

        val len = length.get(0)
        if (len <= 0) {
            return null
        }

        val bytes = ByteArray(len)
        name.position(0)
        name.get(bytes, 0, len)
        return String(bytes, Charsets.UTF_8)
    }

    @JvmStatic
    public fun uniformKind(program: Int, index: Int): UniformKind? {
        val count = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS)
        if (index !in 0..<count) {
            return null
        }

        val maxName = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH)
        if (maxName <= 0) {
            return null
        }

        val length = BufferUtils.createIntBuffer(1)
        val size = BufferUtils.createIntBuffer(1)
        val type = BufferUtils.createIntBuffer(1)
        val name = BufferUtils.createByteBuffer(maxName)
        GL20.glGetActiveUniform(program, index, length, size, type, name)

        val type0 = type.get(0)
        return when (type0) {
            GL11.GL_FLOAT -> UniformKind.Float1
            GL11.GL_INT -> UniformKind.Int1
            GL20.GL_BOOL -> UniformKind.Bool1

            GL20.GL_FLOAT_VEC2 -> UniformKind.Vec2f
            GL20.GL_FLOAT_VEC3 -> UniformKind.Vec3f
            GL20.GL_FLOAT_VEC4 -> UniformKind.Vec4f

            GL20.GL_FLOAT_MAT2 -> UniformKind.Mat2f
            GL20.GL_FLOAT_MAT3 -> UniformKind.Mat3f
            GL20.GL_FLOAT_MAT4 -> UniformKind.Mat4f

            GL20.GL_SAMPLER_1D -> UniformKind.Sampler(SamplerTarget.TEX_1D)
            GL20.GL_SAMPLER_2D -> UniformKind.Sampler(SamplerTarget.TEX_2D)
            GL20.GL_SAMPLER_3D -> UniformKind.Sampler(SamplerTarget.TEX_3D)
            GL20.GL_SAMPLER_CUBE -> UniformKind.Sampler(SamplerTarget.TEX_CUBE)

            else -> null
        }
    }

    @JvmStatic
    public fun uniformLocation(program: Int, name: String): Int {
        //#if MC >= 1.16.5
        return GlStateManager._glGetUniformLocation(program, name)
        //#else
        //$$ return OpenGlHelper.glGetUniformLocation(program, name)
        //#endif
    }

    // Uniform writes

    @JvmStatic
    public fun uniform1f(location: Int, v0: Float) {
        if (GlCapabilities.isGl21Available) {
            GL20.glUniform1f(location, v0)
        } else {
            ARBShaderObjects.glUniform1fARB(location, v0)
        }
    }

    @JvmStatic
    public fun uniform1i(location: Int, v0: Int) {
        if (GlCapabilities.isGl21Available) {
            GL20.glUniform1i(location, v0)
        } else {
            ARBShaderObjects.glUniform1iARB(location, v0)
        }
    }

    @JvmStatic
    public fun uniform2f(location: Int, v0: Float, v1: Float) {
        if (GlCapabilities.isGl21Available) {
            GL20.glUniform2f(location, v0, v1)
        } else {
            ARBShaderObjects.glUniform2fARB(location, v0, v1)
        }
    }

    @JvmStatic
    public fun uniform3f(location: Int, v0: Float, v1: Float, v2: Float) {
        if (GlCapabilities.isGl21Available) {
            GL20.glUniform3f(location, v0, v1, v2)
        } else {
            ARBShaderObjects.glUniform3fARB(location, v0, v1, v2)
        }
    }

    @JvmStatic
    public fun uniform4f(location: Int, v0: Float, v1: Float, v2: Float, v3: Float) {
        if (GlCapabilities.isGl21Available) {
            GL20.glUniform4f(location, v0, v1, v2, v3)
        } else {
            ARBShaderObjects.glUniform4fARB(location, v0, v1, v2, v3)
        }
    }

    @JvmStatic
    public fun uniformMatrix2(
        location: Int,
        transpose: Boolean,
        //#if MC >= 1.16.5
        matrix: FloatArray
        //#else
        //$$ matrix: FloatBuffer
        //#endif
    ) {
        //#if MC >= 1.16.5
        if (GlCapabilities.isGl21Available) {
            GL20.glUniformMatrix2fv(location, transpose, matrix)
        } else {
            ARBShaderObjects.glUniformMatrix2fvARB(location, transpose, matrix)
        }
        //#else
        //$$ if (GlCapabilities.isGl21Available) {
        //$$     GL20.glUniformMatrix2(location, transpose, matrix)
        //$$ } else {
        //$$     ARBShaderObjects.glUniformMatrix2ARB(location, transpose, matrix)
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun uniformMatrix3(
        location: Int,
        transpose: Boolean,
        //#if MC >= 1.16.5
        matrix: FloatArray
        //#else
        //$$ matrix: FloatBuffer
        //#endif
    ) {
        //#if MC >= 1.16.5
        if (GlCapabilities.isGl21Available) {
            GL20.glUniformMatrix3fv(location, transpose, matrix)
        } else {
            ARBShaderObjects.glUniformMatrix3fvARB(location, transpose, matrix)
        }
        //#else
        //$$ if (GlCapabilities.isGl21Available) {
        //$$     GL20.glUniformMatrix3(location, transpose, matrix)
        //$$ } else {
        //$$     ARBShaderObjects.glUniformMatrix3ARB(location, transpose, matrix)
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun uniformMatrix4(
        location: Int,
        transpose: Boolean,
        //#if MC >= 1.16.5
        matrix: FloatArray
        //#else
        //$$ matrix: FloatBuffer
        //#endif
    ) {
        //#if MC >= 1.16.5
        if (GlCapabilities.isGl21Available) {
            GL20.glUniformMatrix4fv(location, transpose, matrix)
        } else {
            ARBShaderObjects.glUniformMatrix4fvARB(location, transpose, matrix)
        }
        //#else
        //$$ if (GlCapabilities.isGl21Available) {
        //$$     GL20.glUniformMatrix4(location, transpose, matrix)
        //$$ } else {
        //$$     ARBShaderObjects.glUniformMatrix4ARB(location, transpose, matrix)
        //$$ }
        //#endif
    }
}
