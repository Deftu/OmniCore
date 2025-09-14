package dev.deftu.omnicore.api.client.render.shader.attributes

public sealed interface AttributeDefinition {
    public val name: String
    public val semantic: AttributeSemantic
    public val kind: AttributeKind
    public val location: Int?
    public val divisor: Int

    public fun declaration(version: Int): String {
        val attr = if (version >= 130) "in" else "attribute"
        val type = when (kind) {
            AttributeKind.Float1 -> "float"
            AttributeKind.Vec2f -> "vec2"
            AttributeKind.Vec3f -> "vec3"
            AttributeKind.Vec4f -> "vec4"
        }

        val loc = if (version >= 130 && location != null) {
            "layout(location = $location) "
        } else {
            ""
        }

        return "$loc$attr $type $name;"
    }

    public data class Float1(
        override val name: String,
        override val semantic: AttributeSemantic,
        override val location: Int? = null,
        override val divisor: Int = 0
    ) : AttributeDefinition {
        override val kind: AttributeKind = AttributeKind.Float1
    }

    public data class Vec2f(
        override val name: String,
        override val semantic: AttributeSemantic,
        override val location: Int? = null,
        override val divisor: Int = 0
    ) : AttributeDefinition {
        override val kind: AttributeKind = AttributeKind.Vec2f
    }

    public data class Vec3f(
        override val name: String,
        override val semantic: AttributeSemantic,
        override val location: Int? = null,
        override val divisor: Int = 0
    ) : AttributeDefinition {
        override val kind: AttributeKind = AttributeKind.Vec3f
    }

    public data class Vec4f(
        override val name: String,
        override val semantic: AttributeSemantic,
        override val location: Int? = null,
        override val divisor: Int = 0
    ) : AttributeDefinition {
        override val kind: AttributeKind = AttributeKind.Vec4f
    }
}
