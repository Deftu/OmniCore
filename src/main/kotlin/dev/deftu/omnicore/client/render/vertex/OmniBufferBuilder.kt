package dev.deftu.omnicore.client.render.vertex

import dev.deftu.omnicore.client.render.pipeline.DrawModes
import dev.deftu.omnicore.client.render.pipeline.VertexFormats
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat

public class OmniBufferBuilder(private val value: BufferBuilder) : OmniVertexConsumer, MCVertexConsumer(value) {

    //#if MC >= 1.21.1
    //$$ public fun build(): OmniBuiltBuffer? {
    //$$     return value.build()?.let(::VanillaWrappingBuiltBuffer)
    //$$ }
    //#elseif MC >= 1.19.2
    public fun build(): OmniBuiltBuffer? {
        return value.endNullable()?.let(::VanillaWrappingBuiltBuffer)
    }
    //#else
    //$$ private var vertexCount = 0
    //$$
    //$$ public fun build(): OmniBuiltBuffer? {
    //$$     value.end()
    //$$     return if (vertexCount > 0) {
    //$$         VanillaWrappingBuiltBuffer(value)
    //$$     } else {
    //$$         value.reset()
    //$$         bufferPool.add(value)
    //$$         null
    //$$     }
    //$$ }
    //$$
    //$$ override fun next(): OmniVertexConsumer {
    //$$     vertexCount++
    //$$     return super.next()
    //$$ }
    //#endif

    public companion object {

        //#if MC < 1.19.2
        //$$ private val bufferPool = mutableListOf<BufferBuilder>()
        //#endif

        @JvmStatic
        public fun create(drawMode: DrawModes, format: VertexFormat): OmniBufferBuilder {
            //#if MC >= 1.21.1
            //$$ val vanilla = Tesselator.getInstance().begin(drawMode.vanilla, format)
            //#else
            //#if MC >= 1.19.2
            val vanilla = Tessellator.getInstance().buffer
            //#else
            //$$ val vanilla = bufferPool.removeLastOrNull() ?: BufferBuilder(1024 * 1024)
            //#endif
            vanilla.begin(drawMode.vanilla, format)
            //#endif
            return OmniBufferBuilder(vanilla)
        }

        @JvmStatic
        public fun create(drawMode: DrawModes, format: VertexFormats): OmniBufferBuilder {
            return create(drawMode, format.vanilla)
        }

    }

}
