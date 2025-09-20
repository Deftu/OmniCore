package dev.deftu.omnicore.api.client.render.shader.attributes

public sealed interface AttributeKind {
    public data object Float1 : AttributeKind
    public data object Vec2f : AttributeKind
    public data object Vec3f : AttributeKind
    public data object Vec4f : AttributeKind
}
