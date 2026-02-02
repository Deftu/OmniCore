package com.test

import dev.deftu.omnicore.api.client.framebuffer.FramebufferTarget
import dev.deftu.omnicore.api.client.framebuffer.OmniFramebuffer
import dev.deftu.omnicore.api.client.framebuffer.OmniFramebuffers
import dev.deftu.omnicore.api.client.image.OmniImages
import dev.deftu.omnicore.api.client.input.KeyboardModifiers
import dev.deftu.omnicore.api.client.input.OmniMouseButton
import dev.deftu.omnicore.api.client.input.OmniMouseButtons
import dev.deftu.omnicore.api.client.render.OmniRenderingContext
import dev.deftu.omnicore.api.client.render.OmniTextRenderer
import dev.deftu.omnicore.api.client.render.OmniTextWrapping
import dev.deftu.omnicore.api.client.render.TextShadowType
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.render.vertex.circle
import dev.deftu.omnicore.api.client.screen.OmniScreen
import dev.deftu.omnicore.api.client.textures.OmniTextureFormat
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.internal.client.framebuffer.FramebufferInternals
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.ClickEvent
import dev.deftu.textile.minecraft.HoverEvent
import dev.deftu.textile.minecraft.MCText
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.minecraft.ChatFormatting
import java.net.URI
import kotlin.io.path.Path
import kotlin.math.abs

class TestScreen(private val createsTexture: Boolean = true) : OmniScreen(screenTitle = Text.literal("Test Screen")) {
    companion object {
        private const val MESSAGE_1 = "Hello, OmniCore!"
        private const val MESSAGE_2 = "This is a test screen."

        val MIXED_MESSAGE: Text =
            Text.literal("Lorem ipsum dolor sit amet, ")
                .setStyle(
                    MCTextStyle()
                        .setColor(TextColors.RED)
                        .setBold(true)
                        .build()
                )
                .append(
                    Text.literal("consectetur adipiscing elit. ")
                        .setStyle(
                            MCTextStyle()
                                .setItalic(true)
                                .build()
                        )
                )
                .append(
                    Text.literal("Proin rhoncus dui sed tortor consequat commodo. ")
                        .setStyle(
                            MCTextStyle()
                                .setColor(TextColors.hex("#3F88C5"))
                                .build()
                        )
                )
                .append(
                    Text.literal("Duis quis ipsum lectus. ")
                        .setStyle(
                            MCTextStyle()
                                .setUnderlined(true)
                                .build()
                        )
                )
                .append(
                    Text.literal("Nullam faucibus tortor urna, ")
                        .setStyle(
                            MCTextStyle()
                                .setBold(true)
                                .setItalic(true)
                                .build()
                        )
                )
                .append(
                    Text.literal("ac porttitor magna feugiat a. ")
                        .setStyle(MCTextStyle().build()) // normal
                )
                .append(
                    Text.literal("Ut at posuere tellus. ")
                        .setStyle(
                            MCTextStyle()
                                .setColor(TextColors.hex("#9B59B6").withFallback(ChatFormatting.DARK_PURPLE))
                                .build()
                        )
                )
                .append(
                    Text.literal("Vivamus maximus dui et nibh venenatis fermentum. ")
                        .setStyle(MCTextStyle().build()) // normal
                )
                .append(
                    Text.literal("Morbi non mauris nec ex mattis maximus sed quis libero. ")
                        .setStyle(
                            MCTextStyle()
                                .setItalic(true)
                                .build()
                        )
                )
                .append(
                    Text.literal("Sed non massa convallis, pharetra massa in, volutpat odio. ")
                        .setStyle(
                            MCTextStyle()
                                .setColor(TextColors.GREEN)
                                .build()
                        )
                )
                .append(
                    Text.literal("Maecenas commodo vulputate condimentum. ")
                        .setStyle(
                            MCTextStyle()
                                .setBold(true)
                                .setClickEvent(ClickEvent.OpenUrl(URI.create("https://deftu.dev")))
                                .build()
                        )
                )
                .append(
                    Text.literal("Quisque fermentum vel velit eget interdum. ")
                        .setStyle(
                            MCTextStyle()
                                .setColor(TextColors.GOLD)
                                .setHoverEvent(HoverEvent.ShowText(Text.literal("Hovered!")))
                                .build()
                        )
                )
                .append(
                    Text.literal("In commodo eros nec diam ornare, ")
                        .setStyle(MCTextStyle().build()) // normal
                )
                .append(
                    Text.literal("ut placerat lorem lacinia. ")
                        .setStyle(
                            MCTextStyle()
                                .setColor(TextColors.hex("#1ABC9C").withFallback(ChatFormatting.AQUA))
                                .setUnderlined(true)
                                .build()
                        )
                )
                .append(
                    Text.literal("Donec eleifend ac risus eget pretium. ")
                        .setStyle(MCTextStyle().build()) // normal
                )
                .append(
                    MCText.translatable("gui.done")
                        .setStyle(
                            MCTextStyle()
                                .setColor(TextColors.YELLOW)
                                .setBold(true)
                                .build()
                        )
                )
    }

    override val isPausingScreen: Boolean = false

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
    private var framebuffer: OmniFramebuffer? = null
    private var texture: OmniTextureHandle? = null

    private fun initFbo() {
        if (framebuffer != null) {
            framebuffer!!.close()
            framebuffer = null
        }

        framebuffer = OmniFramebuffers.create(
            width = width,
            height = height,
            colorFormat = OmniTextureFormat.RGBA8,
            depthFormat = OmniTextureFormat.DEPTH24_STENCIL8,
        )
    }

    override fun onInitialize(width: Int, height: Int) {
        super.onInitialize(width, height)

        initFbo()

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

    override fun onResize(width: Int, height: Int) {
        super.onResize(width, height)
        initFbo()
    }

    override fun onScreenClose() {
        super.onScreenClose()
        texture?.close()
        OmniTextures.destroy(texture!!.location)
    }

    override fun onRender(ctx: OmniRenderingContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        super.onRender(ctx, mouseX, mouseY, tickDelta) // Render vanilla screen

        renderQuad(ctx)
//        renderRoundedQuad(ctx)
        renderLine(ctx)

        ctx.renderTextCentered(
            text = text,
            x = (width / 2f),
            y = 25f,
            color = OmniColors.GREEN,
            shadowType = TextShadowType.Outline(OmniColors.BLUE)
        )

        val messageLines = OmniTextWrapping.wrap(MIXED_MESSAGE, maxWidth = width / 3)
        messageLines.forEachIndexed { index, line ->
            ctx.renderTextCentered(
                text = line,
                x = (width /2f),
                y = 50f + index * (OmniTextRenderer.lineHeight + 2),
                color = OmniColors.WHITE,
                shadowType = TextShadowType.Outline(OmniColors.BLACK)
            )
        }

        println("active: " + FramebufferInternals.bound(FramebufferTarget.WRITE))
        framebuffer?.using {
            println("active INSIDE: " + FramebufferInternals.bound(FramebufferTarget.WRITE))
            ctx.renderGradientQuad(
                x = 100f,
                y = 300f,
                width = 128,
                height = 128,
                topColor = OmniColors.RED,
                bottomColor = OmniColors.BLUE
            )

            ctx.renderText(
                text = "Rendered to framebuffer",
                x = 100f,
                y = 450f,
                color = OmniColors.WHITE,
                shadowType = TextShadowType.Outline(OmniColors.BLACK)
            )
        }

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

        framebuffer?.drawColorTexture(ctx.pose, 0f, 0f, this.width.toFloat(), this.height.toFloat(), OmniColors.WHITE)
    }

    override fun onMouseClick(button: OmniMouseButton, x: Double, y: Double, modifiers: KeyboardModifiers): Boolean {
        if (button == OmniMouseButtons.LEFT) {
            text = if (text == MESSAGE_1) MESSAGE_2 else MESSAGE_1
            return true
        }

        if (button == OmniMouseButtons.RIGHT) {
            framebuffer?.colorTexture?.let(OmniImages::from)?.saveTo(Path("framebuffer_color_texture.png"))
            return true
        }

        return super.onMouseClick(button, x, y, modifiers)
    }

    private fun loadResourceTexture() {
        val id = locationOrThrow(ID, "textures/test_texture.png")
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
        ctx.withPose { matrices ->
            matrices.translate(renderX.toFloat(), renderY.toFloat(), 0f)
            matrices.rotate(15f, 0f, 0f, 1f)

            ctx.withScissor(
                x = 0, y = 0,
                width = renderWidth.toInt(),
                height = (renderHeight / 2).toInt()
            ) {
                val pipeline = OmniRenderPipelines.POSITION_COLOR
                val buffer = pipeline.createBufferBuilder()
                buffer.vertex(matrices, 0.0, 0.0, 0.0).color(topLeftColor).next()
                buffer.vertex(matrices, 0.0 + renderWidth,0.0, 0.0).color(topRightColor).next()
                buffer.vertex(matrices, 0.0 + renderWidth,0.0 + renderHeight,0.0).color(bottomRightColor).next()
                buffer.vertex(matrices, 0.0, 0.0 + renderHeight,0.0).color(bottomLeftColor).next()
                buffer.buildOrThrow().drawAndClose(pipeline)
            }
        }
    }


    private fun renderRoundedQuad(ctx: OmniRenderingContext) {
        ctx.withPose { pose ->
            val buffer = OmniRenderPipelines.POSITION_COLOR_TRIANGLES.createBufferBuilder()
//            buffer.roundedQuad(
//                pose = pose,
//                x = renderX,
//                y = renderY,
//                width = renderWidth,
//                height = renderHeight,
//                radius = 50f,
//                color = OmniColors.LIME,
//            )

            buffer.circle(
                pose = pose,
                cx = renderX + renderWidth / 2,
                cy = renderY + renderHeight / 2,
                radius = renderHeight.toFloat() / 2f,
                color = OmniColors.PINK.withAlpha(89),
                segmentScale = 0.2
            )

            buffer.buildOrThrow().drawAndClose(OmniRenderPipelines.POSITION_COLOR_TRIANGLES)
        }
    }

    val pipeline = OmniRenderPipelines.LINES
        .newBuilder()
        .configureLegacyEffects {
            lineStipple = true
        }.build()

    /** Draws a horizontally straight white line, [renderWidth] across */
    private fun renderLine(ctx: OmniRenderingContext) {
        ctx.withPose {
            val buffer = pipeline.createBufferBuilder()

            buffer.vertex(ctx.pose, renderX, renderY + renderHeight + 10.0, 0.0)
                .color(lineLeftColor)
                .normal(ctx.pose, 1f, 0f, 0f)
                .next()
            buffer.vertex(ctx.pose, renderX + renderWidth, renderY + renderHeight + 10.0, 0.0)
                .color(lineRightColor)
                .normal(ctx.pose, 1f, 0f, 0f)
                .next()

            buffer.buildOrThrow().drawAndClose(pipeline) {
                setLineWidth(15.0f) // test setting line width
                setLineStipple(10, 0xAAAA.toShort())
            }
        }
    }
}
