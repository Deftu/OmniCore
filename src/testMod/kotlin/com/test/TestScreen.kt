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

//#if MC <= 1.12.2
//$$ import net.minecraft.client.renderer.BufferBuilder
//$$ import net.minecraft.client.renderer.Tessellator
//#endif

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

    private val renderX = 50.0
    private val renderY = 50.0
    private val renderWidth = 100.0
    private val renderHeight = 100.0

    override fun handleRender(stack: OmniMatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.handleRender(stack, mouseX, mouseY, tickDelta) // Render vanilla screen

        render(stack)
        //#if MC <= 1.12.2
        //$$ renderVanilla()
        //#endif
    }

    private fun render(stack: OmniMatrixStack) {
        val buffer = OmniBufferBuilder.create(DrawModes.QUADS, VertexFormats.POSITION_COLOR)
        buffer
            .vertex(stack, renderX, renderY, 0.0)
            .color(topLeftColor)
            .next()
        buffer
            .vertex(stack, renderX + renderWidth, renderY, 0.0)
            .color(topRightColor)
            .next()
        buffer
            .vertex(stack, renderX + renderWidth, renderY + renderHeight, 0.0)
            .color(bottomRightColor)
            .next()
        buffer
            .vertex(stack, renderX, renderY + renderHeight, 0.0)
            .color(bottomLeftColor)
            .next()
        buffer.build()?.drawWithCleanup(pipeline)
    }

    //#if MC <= 1.12.2
    //$$ private fun renderVanilla() {
    //$$     val renderX = renderX + renderWidth + 50.0
    //$$
    //$$     val tessellator = Tessellator.getInstance()
    //$$     val buffer = tessellator.buffer
    //$$     buffer.begin(DrawModes.QUADS.vanilla, VertexFormats.POSITION_COLOR.vanilla)
    //$$     buffer
    //$$         .pos(renderX, renderY, 0.0)
    //$$         .color(topLeftColor)
    //$$         .endVertex()
    //$$     buffer
    //$$         .pos(renderX + renderWidth, renderY, 0.0)
    //$$         .color(topRightColor)
    //$$         .endVertex()
    //$$     buffer
    //$$         .pos(renderX + renderWidth, renderY + renderHeight, 0.0)
    //$$         .color(bottomRightColor)
    //$$         .endVertex()
    //$$     buffer
    //$$         .pos(renderX, renderY + renderHeight, 0.0)
    //$$         .color(bottomLeftColor)
    //$$         .endVertex()
    //$$     tessellator.draw()
    //$$ }
    //$$
    //$$ private fun BufferBuilder.color(color: Color): BufferBuilder {
    //$$     return color(color.red, color.green, color.blue, color.alpha)
    //$$ }
    //#endif

}
