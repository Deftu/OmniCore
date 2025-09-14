package dev.deftu.omnicore.api.client.render.vertex

public interface OmniBufferBuilder : OmniVertexConsumer {
    public fun buildOrNull(): OmniBuiltBuffer?
    public fun buildOrThrow(): OmniBuiltBuffer
}
