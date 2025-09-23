package dev.deftu.omnicore.api.client.render.shader

import dev.deftu.omnicore.api.client.render.shader.attributes.AttributeBuilder
import dev.deftu.omnicore.api.client.render.shader.attributes.AttributeDefinition
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformBuilder
import dev.deftu.omnicore.api.client.render.shader.uniforms.UniformDefinition

public class ShaderSchema private constructor(
    public val attributes: List<AttributeDefinition>,
    public val uniforms: List<UniformDefinition<*>>,
) {
    public companion object {
        public inline fun build(block: Builder.() -> Unit): ShaderSchema {
            return Builder().apply(block).build()
        }
    }

    public class Builder {
        private val attributes = mutableListOf<AttributeDefinition>()
        private val uniforms = mutableListOf<UniformDefinition<*>>()

        public fun attributes(vararg definitions: AttributeDefinition) {
            attributes.addAll(definitions)
        }

        public fun attributes(definitions: Collection<AttributeDefinition>) {
            attributes.addAll(definitions)
        }

        @JvmSynthetic
        public fun attributes(block: AttributeBuilder.() -> Unit) {
            attributes.addAll(AttributeBuilder().apply(block).build())
        }

        public fun uniforms(vararg definitions: UniformDefinition<*>) {
            uniforms.addAll(definitions)
        }

        public fun uniforms(definitions: Collection<UniformDefinition<*>>) {
            uniforms.addAll(definitions)
        }

        @JvmSynthetic
        public fun uniforms(block: UniformBuilder.() -> Unit) {
            uniforms.addAll(UniformBuilder().apply(block).build())
        }

        public fun build(): ShaderSchema {
            return ShaderSchema(attributes.toList(), uniforms.toList())
        }
    }

    public val samplers: List<UniformDefinition.Sampler>
        get() = uniforms.filterIsInstance<UniformDefinition.Sampler>()
}
