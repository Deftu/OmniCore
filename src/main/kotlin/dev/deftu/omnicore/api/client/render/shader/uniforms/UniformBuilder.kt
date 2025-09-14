package dev.deftu.omnicore.api.client.render.shader.uniforms

import kotlin.collections.plusAssign

public class UniformBuilder {
    public companion object {
        public inline fun build(block: UniformBuilder.() -> Unit): List<UniformDefinition<*>> {
            return UniformBuilder().apply(block).build()
        }
    }

    private val uniforms = mutableListOf<UniformDefinition<*>>()

    public fun uniform(definition: UniformDefinition<*>) {
        uniforms += definition
    }

    @JvmOverloads
    public fun float1(
        name: String,
        defaultValue: Float? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Float1(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun int1(
        name: String,
        defaultValue: Int? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Int1(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun bool1(
        name: String,
        defaultValue: Boolean? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Bool1(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun vec2f(
        name: String,
        defaultValue: FloatArray? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Vec2f(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun vec3f(
        name: String,
        defaultValue: FloatArray? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Vec3f(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun vec4f(
        name: String,
        defaultValue: FloatArray? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Vec4f(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun mat2f(
        name: String,
        defaultValue: FloatArray? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Mat2f(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun mat3f(
        name: String,
        defaultValue: FloatArray? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Mat3f(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun mat4f(
        name: String,
        defaultValue: FloatArray? = null,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Mat4f(name, defaultValue, location, divisor)
    }

    @JvmOverloads
    public fun sampler(
        name: String,
        target: SamplerTarget,
        location: Int? = null,
        divisor: Int = 0,
    ) {
        uniforms += UniformDefinition.Sampler(name, target, 0, location, divisor)
    }

    public fun build(): List<UniformDefinition<*>> {
        return uniforms.toList()
    }
}

