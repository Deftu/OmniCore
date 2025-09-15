package com.test

import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.TextShadowType
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.client.screen.OmniScreen
import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import java.awt.Color

class TestScreen : OmniScreen(screenTitle = MCSimpleTextHolder("Test Screen")) {
    private val pipeline by lazy {
        OmniRenderPipelines.builderWithDefaultShader(
            location = identifierOrThrow("testmod", "custom"),
            vertexFormat = DefaultVertexFormats.POSITION_COLOR,
            drawMode = DrawMode.QUADS
        ).build()
    }

    private val topLeftColor = Color(0xFF0000) // Red
    private val topRightColor = Color(0x00FF00) // Green
    private val bottomLeftColor = Color(0x0000FF) // Blue
    private val bottomRightColor = Color(0xFFFF00) // Yellow

    private val renderX = 50.0
    private val renderY = 50.0
    private val renderWidth = 100.0
    private val renderHeight = 100.0

    override fun onRender(ctx: OmniRenderingContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.onRender(ctx, mouseX, mouseY, tickDelta) // Render vanilla screen

        render(ctx)

        val text = "Hello, OmniCore!"
        ctx.renderTextCentered(
            text = text,
            x = (width / 2f),
            y = 25f,
            color = Color.WHITE.rgb,
            type = TextShadowType.Outline(Color.BLACK.rgb)
        )
    }

    private fun render(ctx: OmniRenderingContext) {
        val buffer = OmniBufferBuilders.create(DrawMode.QUADS, DefaultVertexFormats.POSITION_COLOR)
        buffer
            .vertex(ctx.matrices, renderX, renderY, 0.0)
            .color(topLeftColor)
            .next()
        buffer
            .vertex(ctx.matrices, renderX + renderWidth, renderY, 0.0)
            .color(topRightColor)
            .next()
        buffer
            .vertex(ctx.matrices, renderX + renderWidth, renderY + renderHeight, 0.0)
            .color(bottomRightColor)
            .next()
        buffer
            .vertex(ctx.matrices, renderX, renderY + renderHeight, 0.0)
            .color(bottomLeftColor)
            .next()
        buffer.buildOrThrow().drawAndClose(pipeline)
    }
}
