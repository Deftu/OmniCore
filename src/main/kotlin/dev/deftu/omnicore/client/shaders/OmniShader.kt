package dev.deftu.omnicore.client.shaders

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//$$ import org.lwjgl.opengl.GL11
//$$ import java.nio.ByteBuffer
//#endif

import com.mojang.blaze3d.platform.GlStateManager
import dev.deftu.omnicore.client.render.OmniRenderEnv
import dev.deftu.omnicore.client.render.OmniTessellator
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.GL20
import java.nio.FloatBuffer

public interface OmniShader {
    public companion object {

        @JvmStatic
        public fun fromLegacyShader(
            vert: String,
            frag: String,
            blend: BlendState,
            vertexFormat: OmniTessellator.VertexFormats
        ): OmniShader {
            //#if MC >= 1.17
            return MinecraftShader.fromLegacyShader(vert, frag, blend, vertexFormat)
            //#else
            //$$ return GlShader(vert, frag, blend)
            //#endif
        }

        // Utilities

        @JvmStatic public fun getCurrentProgram(): Int {
            //#if MC >= 1.17
            return GlStateManager._getInteger(GL20.GL_CURRENT_PROGRAM)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getInteger(GL20.GL_CURRENT_PROGRAM)
            //#else
            //$$ return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
            //#endif
        }

        @JvmStatic public fun createProgram(): Int {
            //#if MC >= 1.17
            return GlStateManager.glCreateProgram()
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.createProgram()
            //#else
            //$$ return OpenGlHelper.glCreateProgram()
            //#endif
        }

        @JvmStatic public fun linkProgram(program: Int) {
            //#if MC >= 1.17
            GlStateManager.glLinkProgram(program)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.linkProgram(program)
            //#else
            //$$ OpenGlHelper.glLinkProgram(program)
            //#endif
        }

        @JvmStatic public fun getProgram(program: Int, pname: Int): Int {
            //#if MC >= 1.17
            return GlStateManager.glGetProgrami(program, pname)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getProgram(program, pname)
            //#else
            //$$ return OpenGlHelper.glGetProgrami(program, pname)
            //#endif
        }

        @JvmStatic public fun validateProgram(program: Int) {
            if (OmniRenderEnv.isGl21Available()) {
                GL20.glValidateProgram(program)
            } else ARBShaderObjects.glValidateProgramARB(program)
        }

        @JvmStatic public fun getProgramInfoLog(program: Int, maxLength: Int): String {
            return if (OmniRenderEnv.isGl21Available()) {
                //#if MC >= 1.17
                GlStateManager.glGetProgramInfoLog(program, maxLength)
                //#elseif MC >= 1.15.2
                //$$ GlStateManager.getProgramInfoLog(program, maxLength)
                //#else
                //$$ OpenGlHelper.glGetProgramInfoLog(program, maxLength)
                //#endif
            } else ARBShaderObjects.glGetInfoLogARB(program, maxLength)
        }

        @JvmStatic public fun getProgramValidateStatus(program: Int): Int {
            return if (OmniRenderEnv.isGl21Available()) {
                GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS)
            } else ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB)
        }

        @JvmStatic public fun useProgram(program: Int) {
            //#if MC >= 1.17
            GlStateManager._glUseProgram(program)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.useProgram(program)
            //#else
            //$$ OpenGlHelper.glUseProgram(program)
            //#endif
        }

        @JvmStatic public fun createShader(type: Int): Int {
            //#if MC >= 1.17
            return GlStateManager.glCreateShader(type)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.createShader(type)
            //#else
            //$$ return OpenGlHelper.glCreateShader(type)
            //#endif
        }

        @JvmStatic public fun deleteShader(shader: Int) {
            if (OmniRenderEnv.isGl21Available()) {
                //#if MC >= 1.17
                GlStateManager.glDeleteShader(shader)
                //#elseif MC >= 1.15.2
                //$$ GlStateManager.deleteShader(shader)
                //#else
                //$$ ARBShaderObjects.glDeleteObjectARB(shader)
                //#endif
            } else ARBShaderObjects.glDeleteObjectARB(shader)
        }

        @JvmStatic public fun detachShader(program: Int, shader: Int) {
            if (OmniRenderEnv.isGl21Available()) {
                GL20.glDetachShader(program, shader)
            } else ARBShaderObjects.glDetachObjectARB(program, shader)
        }

        @JvmStatic public fun compileShader(shader: Int) {
            //#if MC >= 1.17
            GlStateManager.glCompileShader(shader)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.compileShader(shader)
            //#else
            //$$ OpenGlHelper.glCompileShader(shader)
            //#endif
        }

        @JvmStatic public fun shaderSource(shader: Int, source: String) {
            //#if MC >= 1.17
            GlStateManager.glShaderSource(shader, listOf(source))
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.shaderSource(shader, source)
            //#else
            //$$ OpenGlHelper.glShaderSource(shader, ByteBuffer.wrap(source.toByteArray()))
            //#endif
        }

        @JvmStatic public fun getShader(shader: Int, pname: Int): Int {
            //#if MC >= 1.17
            return GlStateManager.glGetShaderi(shader, pname)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getShader(shader, pname)
            //#else
            //$$ return OpenGlHelper.glGetShaderi(shader, pname)
            //#endif
        }

        @JvmStatic public fun getShaderInfoLog(shader: Int, maxLength: Int): String {
            //#if MC >= 1.17
            return GlStateManager.glGetShaderInfoLog(shader, maxLength)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getShaderInfoLog(shader, maxLength)
            //#else
            //$$ return OpenGlHelper.glGetShaderInfoLog(shader, maxLength)
            //#endif
        }

        @JvmStatic public fun attachShader(program: Int, shader: Int) {
            //#if MC >= 1.17
            GlStateManager.glAttachShader(program, shader)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.attachShader(program, shader)
            //#else
            //$$ OpenGlHelper.glAttachShader(program, shader)
            //#endif
        }

        @JvmStatic public fun getUniformLocation(program: Int, name: String): Int {
            //#if MC >= 1.17
            return GlStateManager._glGetUniformLocation(program, name)
            //#elseif MC >= 1.15.2
            //$$ return GlStateManager.getUniformLocation(program, name)
            //#else
            //$$ return OpenGlHelper.glGetUniformLocation(program, name)
            //#endif
        }

        @JvmStatic public fun uniform1i(location: Int, value: Int) {
            //#if MC >= 1.17
            GlStateManager._glUniform1i(location, value)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniform1(location, value)
            //#else
            //$$ OpenGlHelper.glUniform1i(location, value)
            //#endif
        }

        @JvmStatic public fun uniform1f(location: Int, value: Float) {
            val buffer = FloatBuffer.wrap(floatArrayOf(value))
            //#if MC >= 1.17
            GlStateManager._glUniform1(location, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniform1(location, buffer)
            //#else
            //$$ OpenGlHelper.glUniform1(location, buffer)
            //#endif
        }

        @JvmStatic public fun uniform2f(location: Int, a: Float, b: Float) {
            val buffer = FloatBuffer.wrap(floatArrayOf(a, b))
            //#if MC >= 1.17
            GlStateManager._glUniform2(location, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniform2(location, buffer)
            //#else
            //$$ OpenGlHelper.glUniform2(location, buffer)
            //#endif
        }

        @JvmStatic public fun uniform3f(location: Int, a: Float, b: Float, c: Float) {
            val buffer = FloatBuffer.wrap(floatArrayOf(a, b, c))
            //#if MC >= 1.17
            GlStateManager._glUniform3(location, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniform3(location, buffer)
            //#else
            //$$ OpenGlHelper.glUniform3(location, buffer)
            //#endif
        }

        @JvmStatic public fun uniform4f(location: Int, a: Float, b: Float, c: Float, d: Float) {
            val buffer = FloatBuffer.wrap(floatArrayOf(a, b, c, d))
            //#if MC >= 1.17
            GlStateManager._glUniform4(location, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniform4(location, buffer)
            //#else
            //$$ OpenGlHelper.glUniform4(location, buffer)
            //#endif
        }

        @JvmStatic public fun uniformMatrix4f(location: Int, transpose: Boolean, matrix: FloatArray) {
            val buffer = FloatBuffer.wrap(matrix)
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix4(location, transpose, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix4(location, transpose, buffer)
            //#else
            //$$ OpenGlHelper.glUniformMatrix4(location, transpose, buffer)
            //#endif
        }

        @JvmStatic public fun uniformMatrix3f(location: Int, transpose: Boolean, matrix: FloatArray) {
            val buffer = FloatBuffer.wrap(matrix)
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix3(location, transpose, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix3(location, transpose, buffer)
            //#else
            //$$ OpenGlHelper.glUniformMatrix3(location, transpose, buffer)
            //#endif
        }

        @JvmStatic public fun uniformMatrix2f(location: Int, transpose: Boolean, matrix: FloatArray) {
            val buffer = FloatBuffer.wrap(matrix)
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix2(location, transpose, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix2(location, transpose, buffer)
            //#else
            //$$ OpenGlHelper.glUniformMatrix2(location, transpose, buffer)
            //#endif
        }

        @JvmStatic public fun uniformMatrix4fv(location: Int, transpose: Boolean, matrix: FloatArray) {
            val buffer = FloatBuffer.wrap(matrix)
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix4(location, transpose, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix4(location, transpose, buffer)
            //#else
            //$$ OpenGlHelper.glUniformMatrix4(location, transpose, buffer)
            //#endif
        }

        @JvmStatic public fun uniformMatrix3fv(location: Int, transpose: Boolean, matrix: FloatArray) {
            val buffer = FloatBuffer.wrap(matrix)
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix3(location, transpose, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix3(location, transpose, buffer)
            //#else
            //$$ OpenGlHelper.glUniformMatrix3(location, transpose, buffer)
            //#endif
        }

        @JvmStatic public fun uniformMatrix2fv(location: Int, transpose: Boolean, matrix: FloatArray) {
            val buffer = FloatBuffer.wrap(matrix)
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix2(location, transpose, buffer)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix2(location, transpose, buffer)
            //#else
            //$$ OpenGlHelper.glUniformMatrix2(location, transpose, buffer)
            //#endif
        }

        @JvmStatic public fun uniformMatrix4fv(location: Int, transpose: Boolean, matrix: FloatBuffer) {
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix4(location, transpose, matrix)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix4(location, transpose, matrix)
            //#else
            //$$ OpenGlHelper.glUniformMatrix4(location, transpose, matrix)
            //#endif
        }

        @JvmStatic public fun uniformMatrix3fv(location: Int, transpose: Boolean, matrix: FloatBuffer) {
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix3(location, transpose, matrix)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix3(location, transpose, matrix)
            //#else
            //$$ OpenGlHelper.glUniformMatrix3(location, transpose, matrix)
            //#endif
        }

        @JvmStatic public fun uniformMatrix2fv(location: Int, transpose: Boolean, matrix: FloatBuffer) {
            //#if MC >= 1.17
            GlStateManager._glUniformMatrix2(location, transpose, matrix)
            //#elseif MC >= 1.15.2
            //$$ GlStateManager.uniformMatrix2(location, transpose, matrix)
            //#else
            //$$ OpenGlHelper.glUniformMatrix2(location, transpose, matrix)
            //#endif
        }
    }

    public val usable: Boolean

    public fun bind()
    public fun unbind()

    public fun getIntUniformOrNull(name: String): IntUniform?
    public fun getVecUniformOrNull(name: String): VecUniform?
    public fun getVec2UniformOrNull(name: String): Vec2Uniform?
    public fun getVec3UniformOrNull(name: String): Vec3Uniform?
    public fun getVec4UniformOrNull(name: String): Vec4Uniform?
    public fun getMatrixUniformOrNull(name: String): MatrixUniform?
    public fun getSamplerUniformOrNull(name: String): SamplerUniform?

    public fun getIntUniform(name: String): IntUniform = getIntUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    public fun getVecUniform(name: String): VecUniform = getVecUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    public fun getVec2Uniform(name: String): Vec2Uniform = getVec2UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    public fun getVec3Uniform(name: String): Vec3Uniform = getVec3UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    public fun getVec4Uniform(name: String): Vec4Uniform = getVec4UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    public fun getMatrixUniform(name: String): MatrixUniform = getMatrixUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
    public fun getSamplerUniform(name: String): SamplerUniform = getSamplerUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")
}
