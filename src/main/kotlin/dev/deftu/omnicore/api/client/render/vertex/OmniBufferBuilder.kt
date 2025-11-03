package dev.deftu.omnicore.api.client.render.vertex

import com.mojang.blaze3d.vertex.BufferBuilder

public interface OmniBufferBuilder : OmniVertexConsumer {
    public val vanilla: BufferBuilder

    public fun buildOrNull(): OmniMeshData?
    public fun buildOrThrow(): OmniMeshData
}
