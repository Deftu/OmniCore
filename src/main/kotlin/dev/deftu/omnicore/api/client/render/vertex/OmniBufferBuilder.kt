package dev.deftu.omnicore.api.client.render.vertex

import net.minecraft.client.render.BufferBuilder

public interface OmniBufferBuilder : OmniVertexConsumer {
    public val vanilla: BufferBuilder

    public fun buildOrNull(): OmniBuiltBuffer?
    public fun buildOrThrow(): OmniBuiltBuffer
}
