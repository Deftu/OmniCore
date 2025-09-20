package dev.deftu.omnicore.internal.client.render.shader

//#if MC >= 1.17.1 && MC < 1.21.5
//$$ import com.mojang.blaze3d.systems.RenderSystem
//$$ import dev.deftu.omnicore.api.client.render.shader.OmniShader
//$$ import dev.deftu.omnicore.api.client.render.shader.uniforms.Uniform
//$$ import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind
//$$ import dev.deftu.omnicore.api.client.render.state.OmniBlendState
//$$ import net.minecraft.client.renderer.CompiledShaderProgram
//$$ import org.jetbrains.annotations.ApiStatus
//$$
//$$ @ApiStatus.Internal
//$$ public class VanillaShader(
//$$     public val program: CompiledShaderProgram,
//$$     public val blendState: OmniBlendState,
//$$ ) : OmniShader {
//$$     override val isUsable: Boolean = true
//$$
//$$     override fun bind() {
            //#if MC >= 1.21.2
            //$$ RenderSystem.setShader(program)
            //#else
            //$$ RenderSystem.setShader { program }
            //#endif
//$$         blendState.submit()
//$$     }
//$$
//$$     override fun unbind() {
            //#if MC >= 1.21.2
            //$$ RenderSystem.clearShader()
            //#else
            //$$ RenderSystem.setShader { null }
            //#endif
//$$     }
//$$
//$$     override fun uniformOrNull(
//$$         name: String,
//$$         kind: UniformKind
//$$     ): Uniform? {
//$$         val vanilla = program.getUniform(name) ?: return null
//$$         val result = when (kind) {
//$$             UniformKind.Float1 -> VanillaFloat1Uniform(vanilla)
//$$             UniformKind.Int1 -> VanillaInt1Uniform(vanilla)
//$$             UniformKind.Bool1 -> VanillaBool1Uniform(vanilla)
//$$
//$$             UniformKind.Vec2f -> VanillaVec2fUniform(vanilla)
//$$             UniformKind.Vec3f -> VanillaVec3fUniform(vanilla)
//$$             UniformKind.Vec4f -> VanillaVec4fUniform(vanilla)
//$$
//$$             UniformKind.Mat2f -> VanillaMat2fUniform(vanilla)
//$$             UniformKind.Mat3f -> VanillaMat3fUniform(vanilla)
//$$             UniformKind.Mat4f -> VanillaMat4fUniform(vanilla)
//$$
//$$             is UniformKind.Sampler -> VanillaSamplerUniform(program, name)
//$$         }
//$$
//$$         if (result.kind != kind) {
//$$             return null
//$$         }
//$$
//$$         return result
//$$     }
//$$
//$$     override fun uniformOrThrow(
//$$         name: String,
//$$         kind: UniformKind
//$$     ): Uniform {
//$$         return uniformOrNull(name, kind)
//$$             ?: error("Uniform '$name' not found, or kind mismatch (expected $kind).")
//$$     }
//$$ }
//#endif
