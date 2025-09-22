package dev.deftu.omnicore.api.client.render.pipeline

import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.state.OmniBlendState

public object OmniRenderPipelineSnippets {
    @JvmField
    public val POSITION_COLOR: OmniRenderPipeline.Snippet = builder()
        .setVertexFormat(DefaultVertexFormats.POSITION_COLOR)
        .setDrawMode(DrawMode.QUADS)
        .setBlendState(OmniBlendState.ALPHA)
        .configureLegacyEffects {
            OmniTextureUnit.TEXTURE0 equals false
        }.build()

    @JvmField
    public val POSITION_TEXTURE_COLOR: OmniRenderPipeline.Snippet = builder()
        .setVertexFormat(DefaultVertexFormats.POSITION_TEXTURE_COLOR)
        .setDrawMode(DrawMode.QUADS)
        .setBlendState(OmniBlendState.ALPHA)
        .configureLegacyEffects {
            OmniTextureUnit.TEXTURE0 equals true
        }.build()

    @JvmField
    public val LINES: OmniRenderPipeline.Snippet = builder()
        .setVertexFormat(DefaultVertexFormats.POSITION_COLOR_NORMAL)
        .setDrawMode(DrawMode.LINES)
        .setBlendState(OmniBlendState.ALPHA)
        .setCulling(false)
        .configureLegacyEffects {
            OmniTextureUnit.TEXTURE0 equals false
        }.build()

    @JvmStatic
    public fun builder(vararg snippets: OmniRenderPipeline.Snippet): OmniRenderPipelineSnippetBuilder {
        val builder = OmniRenderPipelineSnippetBuilder()
        builder.apply(*snippets)
        return builder
    }
}
