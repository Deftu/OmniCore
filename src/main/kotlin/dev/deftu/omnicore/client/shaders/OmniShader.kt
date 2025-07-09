package dev.deftu.omnicore.client.shaders

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import dev.deftu.omnicore.client.render.state.OmniManagedBlendState
import dev.deftu.omnicore.client.render.OmniRenderEnv
import com.mojang.blaze3d.vertex.VertexFormat
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.OpenGlHelper
//$$ import java.nio.ByteBuffer
//$$ import java.nio.FloatBuffer
//#endif

@GameSide(Side.CLIENT)
public interface OmniShader {
    public companion object {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun fromLegacyShader(
            vert: String,
            frag: String,
            blend: OmniManagedBlendState,
            vertexFormat: VertexFormat?
        ): OmniShader {
            //#if MC >= 1.17.1 && MC < 1.21.5
            //$$ return MinecraftShader.fromLegacyShader(vert, frag, blend, vertexFormat)
            //#else
            return GlShader(vert, frag, blend)
            //#endif
        }

        // Utilities

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getCurrentProgram(): Int {
            return GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun createProgram(): Int {
            //#if MC >= 1.16.5
            return GlStateManager.glCreateProgram()
            //#else
            //$$ return OpenGlHelper.glCreateProgram()
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun linkProgram(program: Int) {
            //#if MC >= 1.16.5
            GlStateManager.glLinkProgram(program)
            //#else
            //$$ OpenGlHelper.glLinkProgram(program)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getProgram(program: Int, pname: Int): Int {
            //#if MC >= 1.16.5
            return GlStateManager.glGetProgrami(program, pname)
            //#else
            //$$ return OpenGlHelper.glGetProgrami(program, pname)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun validateProgram(program: Int) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glValidateProgram(program)
            } else {
                ARBShaderObjects.glValidateProgramARB(program)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getProgramInfoLog(program: Int, maxLength: Int): String {
            return if (OmniRenderEnv.isGl21Available) {
                //#if MC >= 1.16.5
                GlStateManager.glGetProgramInfoLog(program, maxLength)
                //#else
                //$$ OpenGlHelper.glGetProgramInfoLog(program, maxLength)
                //#endif
            } else {
                ARBShaderObjects.glGetInfoLogARB(program, maxLength)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getProgramValidateStatus(program: Int): Int {
            return if (OmniRenderEnv.isGl21Available) {
                GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS)
            } else {
                ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun useProgram(program: Int) {
            //#if MC >= 1.16.5
            GlStateManager._glUseProgram(program)
            //#else
            //$$ OpenGlHelper.glUseProgram(program)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun createShader(type: Int): Int {
            //#if MC >= 1.16.5
            return GlStateManager.glCreateShader(type)
            //#else
            //$$ return OpenGlHelper.glCreateShader(type)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun deleteShader(shader: Int) {
            if (OmniRenderEnv.isGl21Available) {
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
        @GameSide(Side.CLIENT)
        public fun detachShader(program: Int, shader: Int) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glDetachShader(program, shader)
            } else {
                ARBShaderObjects.glDetachObjectARB(program, shader)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun compileShader(shader: Int) {
            //#if MC >= 1.16.5
            GlStateManager.glCompileShader(shader)
            //#else
            //$$ OpenGlHelper.glCompileShader(shader)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun shaderSource(shader: Int, source: String) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glShaderSource(shader, source)
            } else {
                ARBShaderObjects.glShaderSourceARB(shader, source)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getShader(shader: Int, pname: Int): Int {
            //#if MC >= 1.16.5
            return GlStateManager.glGetShaderi(shader, pname)
            //#else
            //$$ return OpenGlHelper.glGetShaderi(shader, pname)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getShaderInfoLog(shader: Int, maxLength: Int): String {
            //#if MC >= 1.16.5
            return GlStateManager.glGetShaderInfoLog(shader, maxLength)
            //#else
            //$$ return OpenGlHelper.glGetShaderInfoLog(shader, maxLength)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun attachShader(program: Int, shader: Int) {
            //#if MC >= 1.16.5
            GlStateManager.glAttachShader(program, shader)
            //#else
            //$$ OpenGlHelper.glAttachShader(program, shader)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun getUniformLocation(program: Int, name: String): Int {
            return if (OmniRenderEnv.isGl21Available) {
                GL20.glGetUniformLocation(program, name)
            } else {
                ARBShaderObjects.glGetUniformLocationARB(program, name)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun uniform1i(location: Int, value: Int) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniform1i(location, value)
            } else {
                ARBShaderObjects.glUniform1iARB(location, value)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun uniform1f(location: Int, value: Float) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniform1f(location, value)
            } else {
                ARBShaderObjects.glUniform1fARB(location, value)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun uniform2f(location: Int, a: Float, b: Float) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniform2f(location, a, b)
            } else {
                ARBShaderObjects.glUniform2fARB(location, a, b)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun uniform3f(location: Int, a: Float, b: Float, c: Float) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniform3f(location, a, b, c)
            } else {
                ARBShaderObjects.glUniform3fARB(location, a, b, c)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun uniform4f(location: Int, a: Float, b: Float, c: Float, d: Float) {
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniform4f(location, a, b, c, d)
            } else {
                ARBShaderObjects.glUniform4fARB(location, a, b, c, d)
            }
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
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
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniformMatrix2fv(location, transpose, matrix)
            } else {
                ARBShaderObjects.glUniformMatrix2fvARB(location, transpose, matrix)
            }
            //#else
            //$$ if (OmniRenderEnv.isGl21Available) {
            //$$     GL20.glUniformMatrix2(location, transpose, matrix)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix2ARB(location, transpose, matrix)
            //$$ }
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
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
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniformMatrix3fv(location, transpose, matrix)
            } else {
                ARBShaderObjects.glUniformMatrix3fvARB(location, transpose, matrix)
            }
            //#else
            //$$ if (OmniRenderEnv.isGl21Available) {
            //$$     GL20.glUniformMatrix3(location, transpose, matrix)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix3ARB(location, transpose, matrix)
            //$$ }
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
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
            if (OmniRenderEnv.isGl21Available) {
                GL20.glUniformMatrix4fv(location, transpose, matrix)
            } else {
                ARBShaderObjects.glUniformMatrix4fvARB(location, transpose, matrix)
            }
            //#else
            //$$ if (OmniRenderEnv.isGl21Available) {
            //$$     GL20.glUniformMatrix4(location, transpose, matrix)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix4ARB(location, transpose, matrix)
            //$$ }
            //#endif
        }

    }

    @GameSide(Side.CLIENT)
    public val usable: Boolean

    @GameSide(Side.CLIENT)
    public fun bind()

    @GameSide(Side.CLIENT)
    public fun unbind()

    @GameSide(Side.CLIENT)
    public fun getIntUniformOrNull(name: String): IntUniform?

    @GameSide(Side.CLIENT)
    public fun getVecUniformOrNull(name: String): VecUniform?

    @GameSide(Side.CLIENT)
    public fun getVec2UniformOrNull(name: String): Vec2Uniform?

    @GameSide(Side.CLIENT)
    public fun getVec3UniformOrNull(name: String): Vec3Uniform?

    @GameSide(Side.CLIENT)
    public fun getVec4UniformOrNull(name: String): Vec4Uniform?

    @GameSide(Side.CLIENT)
    public fun getMatrixUniformOrNull(name: String): MatrixUniform?

    @GameSide(Side.CLIENT)
    public fun getSamplerUniformOrNull(name: String): SamplerUniform?

    @GameSide(Side.CLIENT)
    public fun getIntUniform(name: String): IntUniform = getIntUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")

    @GameSide(Side.CLIENT)
    public fun getVecUniform(name: String): VecUniform = getVecUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")

    @GameSide(Side.CLIENT)
    public fun getVec2Uniform(name: String): Vec2Uniform = getVec2UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")

    @GameSide(Side.CLIENT)
    public fun getVec3Uniform(name: String): Vec3Uniform = getVec3UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")

    @GameSide(Side.CLIENT)
    public fun getVec4Uniform(name: String): Vec4Uniform = getVec4UniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")

    @GameSide(Side.CLIENT)
    public fun getMatrixUniform(name: String): MatrixUniform = getMatrixUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")

    @GameSide(Side.CLIENT)
    public fun getSamplerUniform(name: String): SamplerUniform = getSamplerUniformOrNull(name) ?: throw NoSuchElementException("Uniform $name not found")

}
