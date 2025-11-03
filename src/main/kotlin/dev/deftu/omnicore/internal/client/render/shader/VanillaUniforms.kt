package dev.deftu.omnicore.internal.client.render.shader

//#if MC >= 1.17.1 && MC < 1.21.5
//$$ import dev.deftu.omnicore.api.client.render.shader.uniforms.SamplerTarget
//$$ import com.mojang.blaze3d.shaders.Uniform as VanillaUniform
//$$ import dev.deftu.omnicore.api.client.render.shader.uniforms.Uniform
//$$ import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind
//$$ import net.minecraft.client.renderer.CompiledShaderProgram
//$$
//$$ public class VanillaFloat1Uniform(public val uniform: VanillaUniform): Uniform.Float1Uniform {
//$$     override val kind: UniformKind = UniformKind.Float1
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(value: Float) {
//$$         uniform.set(value)
//$$     }
//$$ }
//$$
//$$ public class VanillaInt1Uniform(public val uniform: VanillaUniform): Uniform.Int1Uniform {
//$$     override val kind: UniformKind = UniformKind.Int1
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(value: Int) {
//$$         uniform.set(value)
//$$     }
//$$ }
//$$
//$$ public class VanillaBool1Uniform(public val uniform: VanillaUniform): Uniform.Bool1Uniform {
//$$     override val kind: UniformKind = UniformKind.Bool1
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(value: Boolean) {
//$$         uniform.set(if (value) 1 else 0)
//$$     }
//$$ }
//$$
//$$ public class VanillaVec2fUniform(public val uniform: VanillaUniform): Uniform.Vec2fUniform {
//$$     override val kind: UniformKind = UniformKind.Vec2f
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(x: Float, y: Float) {
//$$         uniform.set(x, y)
//$$     }
//$$
//$$     override fun set(v: FloatArray) {
//$$         require(v.size == 2) { "Expected array of size 2, got ${v.size}" }
//$$         uniform.set(v[0], v[1])
//$$     }
//$$ }
//$$
//$$ public class VanillaVec3fUniform(public val uniform: VanillaUniform): Uniform.Vec3fUniform {
//$$     override val kind: UniformKind = UniformKind.Vec3f
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(x: Float, y: Float, z: Float) {
//$$         uniform.set(x, y, z)
//$$     }
//$$
//$$     override fun set(v: FloatArray) {
//$$         require(v.size == 3) { "Expected array of size 3, got ${v.size}" }
//$$         uniform.set(v[0], v[1], v[2])
//$$     }
//$$ }
//$$
//$$ public class VanillaVec4fUniform(public val uniform: VanillaUniform): Uniform.Vec4fUniform {
//$$     override val kind: UniformKind = UniformKind.Vec4f
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(x: Float, y: Float, z: Float, w: Float) {
//$$         uniform.set(x, y, z, w)
//$$     }
//$$
//$$     override fun set(v: FloatArray) {
//$$         require(v.size == 4) { "Expected array of size 4, got ${v.size}" }
//$$         uniform.set(v[0], v[1], v[2], v[3])
//$$     }
//$$ }
//$$
//$$ public class VanillaMat2fUniform(public val uniform: VanillaUniform): Uniform.Mat2fUniform {
//$$     override val kind: UniformKind = UniformKind.Mat2f
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(m: FloatArray) {
//$$         require(m.size == 4) { "Expected array of size 4, got ${m.size}" }
//$$         uniform.setMat2x2(m[0], m[1], m[2], m[3])
//$$     }
//$$ }
//$$
//$$ public class VanillaMat3fUniform(public val uniform: VanillaUniform): Uniform.Mat3fUniform {
//$$     override val kind: UniformKind = UniformKind.Mat3f
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(m: FloatArray) {
//$$         require(m.size == 9) { "Expected array of size 9, got ${m.size}" }
//$$         uniform.set(m)
//$$     }
//$$ }
//$$
//$$ public class VanillaMat4fUniform(public val uniform: VanillaUniform): Uniform.Mat4fUniform {
//$$     override val kind: UniformKind = UniformKind.Mat4f
//$$     override val location: Int
//$$         get() = uniform.location
//$$
//$$     override fun set(m: FloatArray) {
//$$         require(m.size == 16) { "Expected array of size 16, got ${m.size}" }
//$$         uniform.set(m)
//$$     }
//$$ }
//$$
//$$ public class VanillaSamplerUniform(public val program: CompiledShaderProgram, public val name: String): Uniform.SamplerUniform {
//$$     override val target: SamplerTarget = SamplerTarget.TEX_2D
//$$     override val kind: UniformKind = UniformKind.Sampler(target)
//$$     override val location: Int = 0
//$$
//$$     override fun setTextureIndex(index: Int) {
//$$         // no-op
//$$     }
//$$
//$$     override fun setTexture(id: Int) {
            //#if MC >= 1.21.2
            //$$ program.bindSampler(name, id)
            //#else
            //$$ program.setSampler(name, id)
            //#endif
//$$     }
//$$ }
//#endif
