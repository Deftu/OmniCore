package com.test

import dev.deftu.omnicore.client.OmniScreen
import dev.deftu.omnicore.client.render.OmniGameRendering
import dev.deftu.omnicore.client.render.OmniMatrixStack
import dev.deftu.omnicore.client.render.pipeline.DrawModes
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.client.render.pipeline.VertexFormats
import dev.deftu.omnicore.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.common.OmniIdentifier
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import java.awt.Color

class TestScreen : OmniScreen(screenTitle = MCSimpleTextHolder("Test Screen")) {

    private val pipeline by lazy {
        OmniRenderPipeline.builderWithDefaultShader(
            identifier = OmniIdentifier.create("testmod", "custom"),
            vertexFormat = VertexFormats.POSITION_COLOR,
            mode = DrawModes.QUADS
        ).build()
    }

    private val topLeftColor = Color(0xFF0000) // Red
    private val topRightColor = Color(0x00FF00) // Green
    private val bottomLeftColor = Color(0x0000FF) // Blue
    private val bottomRightColor = Color(0xFFFF00) // Yellow

    private val renderX1 = 50.0
    private val renderY1 = 50.0
    private val renderX2 = 150.0
    private val renderY2 = 150.0

    override fun handleRender(stack: OmniMatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.handleRender(stack, mouseX, mouseY, tickDelta) // Render vanilla screen

        val text = "Hello, OmniCore!"
        OmniGameRendering.drawCenteredText(
            stack = stack,
            text = text,
            x = (width / 2f),
            y = 25f,
            color = Color.WHITE.rgb
        )

        render(stack)
    }

    private fun render(stack: OmniMatrixStack) {
        val buffer = OmniBufferBuilder.create(DrawModes.QUADS, VertexFormats.POSITION_COLOR)
        buffer
            .vertex(stack, renderX1, renderY1, 0.0)
            .color(topLeftColor)
            .next()
        buffer
            .vertex(stack, renderX2, renderY1, 0.0)
            .color(topRightColor)
            .next()
        buffer
            .vertex(stack, renderX2, renderY2, 0.0)
            .color(bottomRightColor)
            .next()
        buffer
            .vertex(stack, renderX1, renderY2, 0.0)
            .color(bottomLeftColor)
            .next()
        buffer.build()?.drawWithCleanup(pipeline)
    }
}
