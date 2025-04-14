package com.test

import dev.deftu.omnicore.client.OmniScreen
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

    override fun handleRender(stack: OmniMatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.handleRender(stack, mouseX, mouseY, tickDelta) // Render vanilla screen

        // Custom rendering
        val topLeftColor = Color(0xFF0000) // Red
        val topRightColor = Color(0x00FF00) // Green
        val bottomLeftColor = Color(0x0000FF) // Blue
        val bottomRightColor = Color(0xFFFF00) // Yellow

        val x = 50.0
        val y = 50.0
        val width = 100.0
        val height = 100.0

        val buffer = OmniBufferBuilder.create(DrawModes.QUADS, VertexFormats.POSITION_COLOR)
        buffer
            .vertex(stack, x, y, 0.0)
            .color(topLeftColor)
            .next()
        buffer
            .vertex(stack, x + width, y, 0.0)
            .color(topRightColor)
            .next()
        buffer
            .vertex(stack, x + width, y + height, 0.0)
            .color(bottomRightColor)
            .next()
        buffer
            .vertex(stack, x, y + height, 0.0)
            .color(bottomLeftColor)
            .next()
        buffer.build()?.drawWithCleanup(pipeline)
    }

}
