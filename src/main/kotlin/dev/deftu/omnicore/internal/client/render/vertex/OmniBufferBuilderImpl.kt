package dev.deftu.omnicore.internal.client.render.vertex

import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.api.client.render.vertex.OmniMeshData
import com.mojang.blaze3d.vertex.BufferBuilder

//#if MC < 1.19.2
//$$ import dev.deftu.omnicore.api.client.render.vertex.OmniVertexConsumer
//#endif

public class OmniBufferBuilderImpl(override val vanilla: BufferBuilder) : OmniBufferBuilder, OmniVertexConsumerImpl(vanilla) {
    public companion object {
        //#if MC < 1.19.2
        //$$ internal val bufferPool = mutableListOf<BufferBuilder>()
        //#endif
    }

    //#if MC >= 1.21.1
    override fun buildOrNull(): OmniMeshData? {
        return vanilla.build()?.let(::OmniMeshDataImpl)
    }
    //#elseif MC >= 1.19.2
    //$$ override fun buildOrNull(): OmniMeshData? {
    //$$     return vanilla.endOrDiscardIfEmpty()?.let(::OmniMeshDataImpl)
    //$$ }
    //#else
    //$$ private var vertexCount = 0
    //$$
    //$$ override fun buildOrNull(): OmniMeshData? {
    //$$     vanilla.end()
    //$$     return if (vertexCount > 0) {
    //$$         OmniMeshDataImpl(vanilla)
    //$$     } else {
    //$$         vanilla.discard()
    //$$         bufferPool.add(vanilla)
    //$$         null
    //$$     }
    //$$ }
    //$$
    //$$ override fun next(): OmniVertexConsumer {
    //$$     vertexCount++
    //$$     return super.next()
    //$$ }
    //#endif

    override fun buildOrThrow(): OmniMeshData {
        return buildOrNull() ?: throw IllegalStateException("Tried to build an empty buffer")
    }
}
