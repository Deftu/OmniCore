package dev.deftu.omnicore.api.client.render.shader

import dev.deftu.omnicore.api.client.render.shader.uniforms.SamplerTarget
import dev.deftu.omnicore.api.client.render.shader.uniforms.Uniform
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformKind

public interface OmniShader {
    public val isUsable: Boolean

    public fun bind()
    public fun unbind()

    public fun <T> OmniShader.withBound(block: () -> T): T {
        bind()
        try {
            return block()
        } finally {
            unbind()
        }
    }

    public fun uniformOrNull(name: String, kind: UniformKind): Uniform?

    public fun uniformOrThrow(name: String, kind: UniformKind): Uniform

    public fun int1(name: String): Uniform.Int1Uniform =
        requireUniform(name, UniformKind.Int1)

    public fun float1(name: String): Uniform.Float1Uniform =
        requireUniform(name, UniformKind.Float1)

    public fun bool1(name: String): Uniform.Bool1Uniform =
        requireUniform(name, UniformKind.Bool1)

    public fun vec2f(name: String): Uniform.Vec2fUniform =
        requireUniform(name, UniformKind.Vec2f)

    public fun vec3f(name: String): Uniform.Vec3fUniform =
        requireUniform(name, UniformKind.Vec3f)

    public fun vec4f(name: String): Uniform.Vec4fUniform =
        requireUniform(name, UniformKind.Vec4f)

    public fun mat2f(name: String): Uniform.Mat2fUniform =
        requireUniform(name, UniformKind.Mat2f)

    public fun mat3f(name: String): Uniform.Mat3fUniform =
        requireUniform(name, UniformKind.Mat3f)

    public fun mat4f(name: String): Uniform.Mat4fUniform =
        requireUniform(name, UniformKind.Mat4f)

    public fun sampler(name: String, target: SamplerTarget): Uniform.SamplerUniform {
        val uniform = requireUniform<Uniform.SamplerUniform>(name, UniformKind.Sampler(target))
        val kind = uniform.kind
        if (kind !is UniformKind.Sampler || kind.target != target) {
            error("Uniform '$name' kind mismatch: expected Sampler($target) but found ${kind}.")
        }

        return uniform
    }
}

private inline fun <reified T : Uniform> OmniShader.requireUniform(
    name: String,
    kind: UniformKind
): T {
    val uniform = uniformOrNull(name, kind)
        ?: error("Uniform '$name' not found (expected kind=$kind).")
    if (uniform !is T) {
        error("Uniform '$name' type mismatch: expected ${T::class.simpleName} (kind=$kind) but was ${uniform::class.simpleName} (kind=${uniform.kind}).")
    }

    return uniform
}
