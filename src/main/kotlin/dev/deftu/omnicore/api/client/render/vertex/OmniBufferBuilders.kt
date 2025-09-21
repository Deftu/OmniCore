package dev.deftu.omnicore.api.client.render.vertex

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.internal.client.render.vertex.OmniBufferBuilderImpl
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator

public object OmniBufferBuilders {
    @JvmStatic
    public fun create(drawMode: DrawMode, format: VertexFormat): OmniBufferBuilder {
        //#if MC >= 1.21.1
        val vanilla = Tessellator.getInstance().begin(drawMode.vanilla, format)
        //#else
        //#if MC >= 1.19.2
        //$$ val vanilla = Tesselator.getInstance().builder
        //#else
        //$$ val vanilla = OmniBufferBuilderImpl.bufferPool.removeLastOrNull() ?: BufferBuilder(1024 * 1024)
        //#endif
        //$$ vanilla.begin(drawMode.vanilla, format)
        //#endif
        return OmniBufferBuilderImpl(vanilla)
    }

    @JvmStatic
    public fun create(drawMode: DrawMode, format: DefaultVertexFormats): OmniBufferBuilder {
        return create(drawMode, format.vanilla)
    }

    @JvmStatic
    public fun create(pipeline: OmniRenderPipeline): OmniBufferBuilder {
        return create(pipeline.drawMode, pipeline.vertexFormat)
    }

    @JvmStatic
    public fun wrap(vanilla: BufferBuilder): OmniBufferBuilder {
        return OmniBufferBuilderImpl(vanilla)
    }
}
