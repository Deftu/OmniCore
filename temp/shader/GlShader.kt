// For versions 1.16.5 and below
package dev.deftu.multi.shader

//#if MC < 1.17
//$$
//#if MC <= 1.12.2
//$$ import java.nio.ByteBuffer
//$$ import java.nio.ByteOrder
//#endif
//$$ import org.lwjgl.opengl.ARBShaderObjects
//$$ import org.lwjgl.opengl.GL11
//$$ import org.lwjgl.opengl.GL13
//$$ import org.lwjgl.opengl.GL20
//$$ import dev.deftu.multi.MultiRenderEnvironment
//$$ import dev.deftu.multi.MultiTextureManager
//$$
//$$ /**
//$$  * Adapted from EssentialGG UniversalCraft under LGPL-3.0
//$$  * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
//$$  */
//$$ class GlShader(
//$$     val name: String,
//$$     val vert: String,
//$$     val frag: String,
//$$     val blend: BlendState
//$$ ) : MultiShader {
//$$     private var program = MultiShader.createProgram()
//$$     private var vertShader = MultiShader.createShader(GL20.GL_VERTEX_SHADER)
//$$     private var fragShader = MultiShader.createShader(GL20.GL_FRAGMENT_SHADER)
//$$     private var samplers = mutableMapOf<String, DirectSamplerUniform>()
//$$
//$$     override var usable = false
//$$     var bound = false
//$$         private set
//$$     private var prevActiveTexture = 0
//$$     private var prevTextureBindings = mutableMapOf<Int, Int>()
//$$     private var prevBlend: BlendState? = null
//$$
//$$     init {
//$$         createShader()
//$$     }
//$$
//$$     override fun bind() {
//$$         prevActiveTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)
//$$         for (sampler in samplers.values) doBindTexture(sampler.textureUnit, sampler.textureId)
//$$         prevBlend = BlendState.active()
//$$         blend.activate()
//$$         MultiShader.useProgram(program)
//$$         bound = true
//$$     }
//$$
//$$     internal fun doBindTexture(textureUnit: Int, textureId: Int) {
//$$         MultiTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
//$$         prevTextureBindings.computeIfAbsent(textureUnit) { GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) }
//$$         MultiTextureManager.bindTexture(textureId)
//$$     }
//$$
//$$     override fun unbind() {
//$$         for ((textureUnit, textureId) in prevTextureBindings) {
//$$             MultiTextureManager.setActiveTexture(GL13.GL_TEXTURE0 + textureUnit)
//$$             MultiTextureManager.bindTexture(textureId)
//$$         }
//$$         prevTextureBindings.clear()
//$$         MultiTextureManager.setActiveTexture(prevActiveTexture)
//$$         prevBlend?.activate()
//$$         MultiShader.useProgram(0)
//$$         bound = false
//$$     }
//$$
//$$     internal inline fun withProgramBound(block: () -> Unit) {
//$$         if (bound) {
//$$             block()
//$$         } else {
//$$             val previousProgram = MultiShader.getCurrentProgram()
//$$             try {
//$$                 MultiShader.useProgram(program)
//$$                 block()
//$$             } finally {
//$$                 MultiShader.useProgram(previousProgram)
//$$             }
//$$         }
//$$     }
//$$
//$$     private fun getUniformLocation(name: String): Int? {
//$$         val location = if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glGetUniformLocation(program, name)
//$$         } else {
//$$             ARBShaderObjects.glGetUniformLocationARB(program, name)
//$$         }
//$$
//$$         return if (location == -1) null else location
//$$     }
//$$
//$$     override fun getIntUniformOrNull(name: String): IntUniform? = getUniformLocation(name)?.let(::DirectIntUniform)
//$$     override fun getVecUniformOrNull(name: String): VecUniform? = getUniformLocation(name)?.let(::DirectVecUniform)
//$$     override fun getVec2UniformOrNull(name: String): Vec2Uniform? = getUniformLocation(name)?.let(::DirectVec2Uniform)
//$$     override fun getVec3UniformOrNull(name: String): Vec3Uniform? = getUniformLocation(name)?.let(::DirectVec3Uniform)
//$$     override fun getVec4UniformOrNull(name: String): Vec4Uniform? = getUniformLocation(name)?.let(::DirectVec4Uniform)
//$$     override fun getMatrixUniformOrNull(name: String): MatrixUniform? = getUniformLocation(name)?.let(::DirectMatrixUniform)
//$$     override fun getSamplerUniformOrNull(name: String): SamplerUniform? {
//$$         samplers[name]?.let { return it }
//$$         val location = getUniformLocation(name) ?: return null
//$$         val uniform = DirectSamplerUniform(location, samplers.size, this)
//$$         samplers[name] = uniform
//$$         return uniform
//$$     }
//$$
//$$     private fun createShader() {
//$$         for ((shader, source) in listOf(vertShader to vert, fragShader to frag)) {
//$$             if (MultiRenderEnvironment.isGL21Available()) GL20.glShaderSource(shader, source) else ARBShaderObjects.glShaderSourceARB(shader, source)
//$$             MultiShader.compileShader(shader)
//$$
//$$             if (MultiShader.getShader(shader, GL20.GL_COMPILE_STATUS) != 1) {
//$$                 val log = MultiShader.getShaderInfoLog(shader, 32768)
//$$                 println("Shader compilation failed ($name): $log")
//$$                 return
//$$             }
//$$
//$$             MultiShader.attachShader(program, shader)
//$$         }
//$$
//$$         MultiShader.linkProgram(program)
//$$
//$$         if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glDetachShader(program, vertShader)
//$$             GL20.glDetachShader(program, fragShader)
//$$             GL20.glDeleteShader(vertShader)
//$$             GL20.glDeleteShader(fragShader)
//$$         } else {
//$$             ARBShaderObjects.glDetachObjectARB(program, vertShader)
//$$             ARBShaderObjects.glDetachObjectARB(program, fragShader)
//$$             ARBShaderObjects.glDeleteObjectARB(vertShader)
//$$             ARBShaderObjects.glDeleteObjectARB(fragShader)
//$$         }
//$$
//$$         if (MultiShader.getProgram(program, GL20.GL_LINK_STATUS) != 1) {
//$$             val log = MultiShader.getProgramInfoLog(program, 32768)
//$$             println("Shader linking failed: $log")
//$$             return
//$$         }
//$$
//$$         if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glValidateProgram(program)
//$$             if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) != 1) {
//$$                 val log = GL20.glGetProgramInfoLog(program, 32768)
//$$                 println("Shader validation failed: $log")
//$$                 return
//$$             }
//$$         } else {
//$$             ARBShaderObjects.glValidateProgramARB(program)
//$$             if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) != 1) {
//$$                 val log = ARBShaderObjects.glGetInfoLogARB(program, 32768)
//$$                 println("Shader validation failed: $log")
//$$                 return
//$$             }
//$$         }
//$$
//$$         usable = true
//$$     }
//$$ }
//$$
//$$ internal abstract class DirectShaderUniform(override val location: Int) : ShaderUniform
//$$
//$$ internal class DirectIntUniform(
//$$     location: Int
//$$ ) : DirectShaderUniform(location), IntUniform {
//$$     override fun setValue(value: Int) {
//$$         if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glUniform1i(location, value)
//$$         } else {
//$$             ARBShaderObjects.glUniform1iARB(location, value)
//$$         }
//$$     }
//$$ }
//$$
//$$ internal class DirectVecUniform(
//$$     location: Int
//$$ ) : DirectShaderUniform(location), VecUniform {
//$$     override fun setValue(a: Float) {
//$$         if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glUniform1f(location, a)
//$$         } else {
//$$             ARBShaderObjects.glUniform1fARB(location, a)
//$$         }
//$$     }
//$$ }
//$$
//$$ internal class DirectVec2Uniform(
//$$     location: Int
//$$ ) : DirectShaderUniform(location), Vec2Uniform {
//$$     override fun setValue(a: Float, b: Float) {
//$$         if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glUniform2f(location, a, b)
//$$         } else {
//$$             ARBShaderObjects.glUniform2fARB(location, a, b)
//$$         }
//$$     }
//$$ }
//$$
//$$ internal class DirectVec3Uniform(
//$$     location: Int
//$$ ) : DirectShaderUniform(location), Vec3Uniform {
//$$     override fun setValue(a: Float, b: Float, c: Float) {
//$$         if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glUniform3f(location, a, b, c)
//$$         } else {
//$$             ARBShaderObjects.glUniform3fARB(location, a, b, c)
//$$         }
//$$     }
//$$ }
//$$
//$$ internal class DirectVec4Uniform(
//$$     location: Int
//$$ ) : DirectShaderUniform(location), Vec4Uniform {
//$$     override fun setValue(a: Float, b: Float, c: Float, d: Float) {
//$$         if (MultiRenderEnvironment.isGL21Available()) {
//$$             GL20.glUniform4f(location, a, b, c, d)
//$$         } else {
//$$             ARBShaderObjects.glUniform4fARB(location, a, b, c, d)
//$$         }
//$$     }
//$$ }
//$$
//$$ internal class DirectMatrixUniform(
//$$     location: Int
//$$ ) : DirectShaderUniform(location), MatrixUniform {
//$$     override fun setValue(matrix: FloatArray) {
        //#if MC<11400
        //$$ val buffer = ByteBuffer.allocateDirect(matrix.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        //$$ buffer.put(matrix)
        //$$ buffer.rewind()
        //#endif
//$$         when (matrix.size) {
            //#if MC >= 1.14
            //$$ 4 -> if (MultiRenderEnvironment.isGL21Available()) {
            //$$     GL20.glUniformMatrix2fv(location, false, matrix)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix2fvARB(location, false, matrix)
            //$$ }
            //$$ 9 -> if (MultiRenderEnvironment.isGL21Available()) {
            //$$     GL20.glUniformMatrix3fv(location, false, matrix)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix3fvARB(location, false, matrix)
            //$$ }
            //$$ 16 -> if (MultiRenderEnvironment.isGL21Available()) {
            //$$     GL20.glUniformMatrix4fv(location, false, matrix)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix4fvARB(location, false, matrix)
            //$$ }
            //#else
            //$$ 4 -> if (MultiRenderEnvironment.isGL21Available()) {
            //$$     GL20.glUniformMatrix2(location, false, buffer)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix2ARB(location, false, buffer)
            //$$ }
            //$$ 9 -> if (MultiRenderEnvironment.isGL21Available()) {
            //$$     GL20.glUniformMatrix3(location, false, buffer)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix3ARB(location, false, buffer)
            //$$ }
            //$$ 16 -> if (MultiRenderEnvironment.isGL21Available()) {
            //$$     GL20.glUniformMatrix4(location, false, buffer)
            //$$ } else {
            //$$     ARBShaderObjects.glUniformMatrix4ARB(location, false, buffer)
            //$$ }
            //#endif
//$$             else -> throw IllegalArgumentException("Invalid matrix size: ${matrix.size}")
//$$         }
//$$     }
//$$ }
//$$
//$$ internal class DirectSamplerUniform(
//$$     location: Int,
//$$     val textureUnit: Int,
//$$     private val shader: GlShader,
//$$ ) : DirectShaderUniform(location), SamplerUniform {
//$$     var textureId: Int = 0
//$$
//$$     init {
//$$         shader.withProgramBound {
//$$             DirectIntUniform(location).setValue(textureUnit)
//$$         }
//$$     }
//$$
//$$     override fun setValue(textureId: Int) {
//$$         this.textureId = textureId
//$$
//$$         if (shader.bound) {
//$$             shader.doBindTexture(textureUnit, textureId)
//$$         }
//$$     }
//$$ }
//#endif