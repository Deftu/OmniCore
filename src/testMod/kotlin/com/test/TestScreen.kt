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
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import kotlin.io.path.Path
import kotlin.math.abs

class TestScreen(private val createsTexture: Boolean = true) : OmniScreen(screenTitle = MCSimpleTextHolder("Test Screen")) {
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

    private val topLeftColor = OmniColors.RED
    private val topRightColor = OmniColors.GREEN
    private val bottomLeftColor = OmniColors.BLUE
    private val bottomRightColor = OmniColors.YELLOW

    private val renderX = 50.0
    private val renderY = 50.0
    private val renderWidth = 100.0
    private val renderHeight = 100.0

    private var texture: OmniTextureHandle? = null

    override fun onInitialize(width: Int, height: Int) {
        super.onInitialize(width, height)

        if (texture != null) {
            OmniTextures.destroy(texture!!.location)
            texture!!.close()
            texture = null
        }

        if (createsTexture) {
            createCheckerboardTexture()
        } else {
            loadResourceTexture()
        }

        OmniTextures.register(texture!!.location, texture!!)
    }

    override fun onScreenClose() {
        super.onScreenClose()
        texture?.close()
        OmniTextures.destroy(texture!!.location)
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
                width = 128,
                height = 128,
                u = 0f,
                v = 0f,
            )
        }
    }

    private fun loadResourceTexture() {
        val id = identifierOrThrow(ID, "textures/test_texture.png")
        val image = OmniImages.resource(id)
            ?: throw IllegalStateException("Failed to load test texture from $id")
        image.saveTo(Path("loaded_test_image.png"))

        val texture = OmniTextures.load(image)
        OmniImages.from(texture)
            .saveTo(Path("load_texture.png"))

        this.texture = texture
    }

    private fun createCheckerboardTexture() {
        val texture = OmniTextures.create(128, 128)

        // Create a simple checkerboard pattern
        val lineTarget = texture.width - 1
        val lineThickness = 5
        val halvedThickness = lineThickness / 2
        texture.writeColor(0, 0, 128, 128, Array(128 * 128) { index ->
            val x = index % 128
            val y = index / 128

            // Top-right → bottom-left diagonal band (thickness pixels)
            if (abs((x + y) - lineTarget) <= halvedThickness) {
                OmniColors.GREEN
            } else if ((x / 16 + y / 16) % 2 == 0) {
                OmniColors.WHITE
            } else {
                OmniColors.BLACK
            }
        })

        // Draw a red square in the center
        texture.writeColor(48, 48, 32, 32, Array(32 * 32) { OmniColors.RED })

        OmniImages.from(texture)
            .saveTo(Path("created_texture.png"))
        this.texture = texture
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
