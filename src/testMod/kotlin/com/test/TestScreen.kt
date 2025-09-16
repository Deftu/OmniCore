package com.test

import dev.deftu.omnicore.api.client.image.OmniImages
import dev.deftu.omnicore.api.client.render.DefaultVertexFormats
import dev.deftu.omnicore.api.client.render.DrawMode
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.TextShadowType
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.render.state.OmniBlendState
import dev.deftu.omnicore.api.client.render.vertex.OmniBufferBuilders
import dev.deftu.omnicore.api.client.screen.OmniScreen
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import kotlin.io.path.Path

class TestScreen : OmniScreen(screenTitle = MCSimpleTextHolder("Test Screen")) {
    private val pipeline by lazy {
        OmniRenderPipelines.builderWithDefaultShader(
            location = identifierOrThrow("testmod", "custom"),
            vertexFormat = DefaultVertexFormats.POSITION_COLOR,
            drawMode = DrawMode.QUADS
        ).build()
    }

    private val imagePipeline by lazy {
        OmniRenderPipelines.builderWithDefaultShader(
            location = identifierOrThrow("testmod", "image"),
            vertexFormat = DefaultVertexFormats.POSITION_TEXTURE_COLOR,
            drawMode = DrawMode.QUADS,
        ).setBlendState(OmniBlendState.ALPHA).build()
    }

    private val topLeftColor = OmniColor(0xFF0000) // Red
    private val topRightColor = OmniColor(0x00FF00) // Green
    private val bottomLeftColor = OmniColor(0x0000FF) // Blue
    private val bottomRightColor = OmniColor(0xFFFF00) // Yellow

    private val renderX = 50.0
    private val renderY = 50.0
    private val renderWidth = 100.0
    private val renderHeight = 100.0

    private var texture: OmniTextureHandle? = null

    override fun onInitialize(width: Int, height: Int) {
        super.onInitialize(width, height)

        val image = OmniImages.read(Path("D:\\Downloads\\1302430884426944614 (Custom).png"))
        image.saveTo(Path("image.png"))

        texture = OmniTextures.load(image)
        OmniTextures.register(texture!!.location, texture!!)
    }

    override fun onScreenClose() {
        super.onScreenClose()
        texture?.close()
    }

    override fun onRender(ctx: OmniRenderingContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.onRender(ctx, mouseX, mouseY, tickDelta) // Render vanilla screen

        render(ctx)

        val text = "Hello, OmniCore!"
        ctx.renderTextCentered(
            text = text,
            x = (width / 2f),
            y = 25f,
            color = OmniColors.GREEN,
            shadowType = TextShadowType.Outline(OmniColors.BLUE)
        )

        if (texture != null) {
            ctx.renderTexture(
                pipeline = imagePipeline,
                texture = texture!!,
                x = 200f,
                y = 50f,
                width = 300,
                height = 300,
                u = 0f,
                v = 0f,
            )
        }
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
