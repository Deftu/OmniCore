package com.test

import dev.deftu.omnicore.api.client.image.OmniImages
import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.client.input.OmniMouseButtons
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.TextShadowType
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.screen.OmniScreen
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import kotlin.io.path.Path
import kotlin.math.abs

class TestScreen(private val createsTexture: Boolean = true) : OmniScreen(screenTitle = MCSimpleTextHolder("Test Screen")) {
    companion object {
        private const val MESSAGE_1 = "Hello, OmniCore!"
        private const val MESSAGE_2 = "This is a test screen."
    }

    private val topLeftColor = OmniColors.RED
    private val topRightColor = OmniColors.GREEN
    private val bottomLeftColor = OmniColors.BLUE
    private val bottomRightColor = OmniColors.YELLOW

    private val lineLeftColor = OmniColors.CYAN
    private val lineRightColor = OmniColors.MAGENTA

    private val renderX = 50.0
    private val renderY = 50.0
    private val renderWidth = 100.0
    private val renderHeight = 100.0

    private var text = MESSAGE_1
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

        renderQuad(ctx)
        renderLine(ctx)

        ctx.renderTextCentered(
            text = text,
            x = (width / 2f),
            y = 25f,
            color = OmniColors.GREEN,
            shadowType = TextShadowType.Outline(OmniColors.BLUE)
        )

        ctx.renderGradientQuad(
            pipeline = OmniRenderPipelines.POSITION_COLOR,
            x = 100f,
            y = 300f,
            width = 128,
            height = 128,
            topColor = OmniColors.RED,
            bottomColor = OmniColors.BLUE
        )

        if (texture != null) {
            ctx.renderTexture(
                pipeline = OmniRenderPipelines.TEXTURED,
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

    override fun onMouseClick(button: OmniMouseButton, x: Double, y: Double, modifiers: KeyboardModifiers): Boolean {
        if (button == OmniMouseButtons.LEFT) {
            text = if (text == MESSAGE_1) MESSAGE_2 else MESSAGE_1
            return true
        }

        return super.onMouseClick(button, x, y, modifiers)
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

    private fun renderQuad(ctx: OmniRenderingContext) {
        ctx.withMatrices { matrices ->
            ctx.pushScissor(
                x = renderX.toInt(),
                y = renderY.toInt(),
                width = renderWidth.toInt(),
                height = (renderHeight / 2).toInt()
            )

            val pipeline = OmniRenderPipelines.POSITION_COLOR
            val buffer = pipeline.createBufferBuilder()
            buffer
                .vertex(matrices, renderX, renderY, 0.0)
                .color(topLeftColor)
                .next()
            buffer
                .vertex(matrices, renderX + renderWidth, renderY, 0.0)
                .color(topRightColor)
                .next()
            buffer
                .vertex(matrices, renderX + renderWidth, renderY + renderHeight, 0.0)
                .color(bottomRightColor)
                .next()
            buffer
                .vertex(matrices, renderX, renderY + renderHeight, 0.0)
                .color(bottomLeftColor)
                .next()
            buffer.buildOrThrow().drawAndClose(pipeline)

            ctx.popScissor()
        }
    }

    /** Draws a horizontally straight white line, [renderWidth] across */
    private fun renderLine(ctx: OmniRenderingContext) {
        ctx.withMatrices {
            val pipeline = OmniRenderPipelines.LINES
            val buffer = pipeline.createBufferBuilder()

            buffer.vertex(ctx.matrices, renderX, renderY + renderHeight + 10.0, 0.0)
                .color(lineLeftColor)
                .normal(ctx.matrices, 1f, 0f, 0f)
                .next()
            buffer.vertex(ctx.matrices, renderX + renderWidth, renderY + renderHeight + 10.0, 0.0)
                .color(lineRightColor)
                .normal(ctx.matrices, 1f, 0f, 0f)
                .next()

            buffer.buildOrThrow().drawAndClose(pipeline) {
                setLineWidth(15.0f) // test setting line width
            }
        }
    }
}
