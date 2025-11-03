package dev.deftu.omnicore.api.client.render.pipeline

import com.mojang.blaze3d.vertex.VertexFormat
import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import net.minecraft.resources.ResourceLocation
import java.util.function.Consumer

public class OmniRenderPipelineSnippetBuilder {
    @JvmField public var location: ResourceLocation? = null
    @JvmField public var vertexFormat: VertexFormat? = null
    @JvmField public var drawMode: DrawMode? = null

    @JvmField public var depthTest: OmniRenderPipeline.DepthTest? = null
    @JvmField public var culling: Boolean? = null
    @JvmField public var colorLogic: OmniRenderPipeline.ColorLogic? = null
    @JvmField public var blendState: OmniBlendState? = null
    @JvmField public var colorMask: Pair<Boolean, Boolean>? = null
    @JvmField public var depthMask: Boolean? = null
    @JvmField public var polygonOffset: Pair<Float, Float>? = null

    @JvmField public var legacyEffects: LegacyEffects? = null

    @JvmField public var irisType: IrisShaderType? = null

    public fun build(): OmniRenderPipeline.Snippet {
        return OmniRenderPipeline.Snippet(
            location = location,
            vertexFormat = vertexFormat,
            drawMode = drawMode,
            depthTest = depthTest,
            culling = culling,
            colorLogic = colorLogic,
            blendState = blendState,
            colorMask = colorMask,
            depthMask = depthMask,
            polygonOffset = polygonOffset,
            legacyEffects = legacyEffects,
            irisType = irisType,
        )
    }

    public fun reset(): OmniRenderPipelineSnippetBuilder {
        location = null
        vertexFormat = null
        drawMode = null
        depthTest = null
        culling = null
        colorLogic = null
        blendState = null
        colorMask = null
        depthMask = null
        polygonOffset = null
        legacyEffects = null
        irisType = null
        return this
    }

    public fun apply(vararg others: OmniRenderPipeline.Snippet?): OmniRenderPipelineSnippetBuilder {
        for (other in others) {
            if (other == null) {
                continue
            }

            other.location?.let { location = it }
            other.vertexFormat?.let { vertexFormat = it }
            other.drawMode?.let { drawMode = it }
            other.depthTest?.let { depthTest = it }
            other.culling?.let { culling = it }
            other.colorLogic?.let { colorLogic = it }
            other.blendState?.let { blendState = it }
            other.colorMask?.let { colorMask = it }
            other.depthMask?.let { depthMask = it }
            other.polygonOffset?.let { polygonOffset = it }
            other.legacyEffects?.let { legacyEffects = it }
            other.irisType?.let { irisType = it }
        }

        return this
    }

    public fun setLocation(location: ResourceLocation): OmniRenderPipelineSnippetBuilder {
        this.location = location; return this
    }

    public fun setVertexFormat(format: VertexFormat): OmniRenderPipelineSnippetBuilder {
        this.vertexFormat = format; return this
    }

    public fun setVertexFormat(format: DefaultVertexFormats): OmniRenderPipelineSnippetBuilder {
        return setVertexFormat(format.vanilla)
    }

    public fun setDrawMode(mode: DrawMode): OmniRenderPipelineSnippetBuilder {
        this.drawMode = mode; return this
    }

    public fun setDepthTest(mode: OmniRenderPipeline.DepthTest): OmniRenderPipelineSnippetBuilder {
        this.depthTest = mode; return this
    }

    public fun setCulling(enabled: Boolean): OmniRenderPipelineSnippetBuilder {
        this.culling = enabled; return this
    }

    public fun setColorLogic(mode: OmniRenderPipeline.ColorLogic): OmniRenderPipelineSnippetBuilder {
        this.colorLogic = mode; return this
    }

    public fun setBlendState(state: OmniBlendState): OmniRenderPipelineSnippetBuilder {
        this.blendState = state; return this
    }

    public fun setColorMask(writeRgb: Boolean, writeAlpha: Boolean): OmniRenderPipelineSnippetBuilder {
        this.colorMask = writeRgb to writeAlpha; return this
    }

    public fun setDepthMask(writeDepth: Boolean): OmniRenderPipelineSnippetBuilder {
        this.depthMask = writeDepth; return this
    }

    public fun setPolygonOffset(factor: Float, units: Float): OmniRenderPipelineSnippetBuilder {
        this.polygonOffset = factor to units; return this
    }

    public fun setLegacyEffects(effects: LegacyEffects): OmniRenderPipelineSnippetBuilder {
        this.legacyEffects = effects; return this
    }

    public fun configureLegacyEffects(configure: Consumer<LegacyEffects.Builder>): OmniRenderPipelineSnippetBuilder {
        this.legacyEffects = LegacyEffects.Builder().apply(configure::accept).build()
        return this
    }

    public fun configureLegacyEffects(configure: LegacyEffects.Builder.() -> Unit): OmniRenderPipelineSnippetBuilder {
        this.legacyEffects = LegacyEffects.Builder().apply(configure).build()
        return this
    }

    public fun setIrisType(type: IrisShaderType): OmniRenderPipelineSnippetBuilder {
        this.irisType = type; return this
    }
}
