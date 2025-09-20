package dev.deftu.omnicore.api.client.render.shader.attributes

public class AttributeBuilder {
    public companion object {
        public inline fun build(block: AttributeBuilder.() -> Unit): List<AttributeDefinition> {
            return AttributeBuilder().apply(block).build()
        }
    }

    private val attributes = mutableListOf<AttributeDefinition>()

    public fun attribute(definition: AttributeDefinition) {
        attributes += definition
    }

    @JvmOverloads
    public fun position(name: String = "oc_Position") {
        attributes += AttributeDefinition.Vec3f(name, AttributeSemantic.Position, null, 0)
    }

    @JvmOverloads
    public fun normal(name: String = "oc_Normal") {
        attributes += AttributeDefinition.Vec3f(name, AttributeSemantic.Normal, null, 0)
    }

    @JvmOverloads
    public fun color(name: String = "oc_Color") {
        attributes += AttributeDefinition.Vec4f(name, AttributeSemantic.Color, null, 0)
    }

    @JvmOverloads
    public fun uv0(name: String = "oc_UV0") {
        attributes += AttributeDefinition.Vec2f(name, AttributeSemantic.UV(0), null, 0)
    }

    @JvmOverloads
    public fun uv1(name: String = "oc_UV1") {
        attributes += AttributeDefinition.Vec2f(name, AttributeSemantic.UV(1), null, 0)
    }

    public fun build(): List<AttributeDefinition> {
        return attributes.toList()
    }
}

