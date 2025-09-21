package dev.deftu.omnicore.api.client.render.pipeline

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import net.minecraft.util.Identifier

//#if MC >= 1.21.5
import com.mojang.blaze3d.pipeline.RenderPipeline
//#endif

public interface OmniRenderPipeline {
    public data class Snippet(
        public val location: Identifier?,
        public val vertexFormat: VertexFormat?,
        public val drawMode: DrawMode?,
        public val depthTest: DepthTest?,
        public val culling: Boolean?,
        public val colorLogic: ColorLogic?,
        public val blendState: OmniBlendState?,
        public val colorMask: Pair<Boolean, Boolean>?,
        public val depthMask: Boolean?,
        public val polygonOffset: Pair<Float, Float>?,
        public val legacyEffects: LegacyEffects?,
    )

    public enum class DepthTest {
        DISABLED,
        ALWAYS,
        EQUAL,
        LESS_OR_EQUAL,
        LESS,
        GREATER,
    }

    public enum class ColorLogic {
        NONE,
        OR_REVERSE,
    }

    public val location: Identifier
    public val drawMode: DrawMode
    public val vertexFormat: VertexFormat

    //#if MC >= 1.21.5
    public val vanilla: RenderPipeline
    //#endif

    public fun bind()
    public fun unbind()

    public fun newBuilder(): OmniRenderPipelineBuilder

    public fun createBufferBuilder(): OmniBufferBuilder {
        return OmniBufferBuilders.create(this)
    }
}
