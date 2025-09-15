package dev.deftu.omnicore.internal.client.render.vertex

import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.api.client.render.vertex.OmniBuiltBuffer
import net.minecraft.client.render.BufferBuilder

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
    override fun buildOrNull(): OmniBuiltBuffer? {
        return vanilla.endNullable()?.let(::OmniBuiltBufferImpl)
    }
    //#elseif MC >= 1.19.2
    //$$ override fun buildOrNull(): OmniBuiltBuffer? {
    //$$     return vanilla.endOrDiscardIfEmpty()?.let(::OmniBuiltBufferImpl)
    //$$ }
    //#else
    //$$ private var vertexCount = 0
    //$$
    //$$ override fun buildOrNull(): OmniBuiltBuffer? {
    //$$     vanilla.end()
    //$$     return if (vertexCount > 0) {
    //$$         OmniBuiltBufferImpl(vanilla)
    //$$     } else {
    //$$         vanilla.reset()
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

    override fun buildOrThrow(): OmniBuiltBuffer {
        return buildOrNull() ?: throw IllegalStateException("Tried to build an empty buffer")
    }
}
