package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipelines
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.internal.client.render.ScissorInternals
import dev.deftu.textile.Text
import net.minecraft.text.Text as VanillaText
import net.minecraft.util.Identifier
import java.util.function.Consumer

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
//#endif

//#if MC >= 1.20.1
import net.minecraft.client.gui.DrawContext
//#endif

//#if MC >= 1.16.5 && MC < 1.20.1
//$$ import net.minecraft.client.util.math.MatrixStack
//#endif

public data class OmniRenderingContext(
    //#if MC >= 1.20.1
    val graphics: DrawContext,
    //#endif
    val matrices: OmniMatrixStack,
) : AutoCloseable {
    public companion object {
        @JvmStatic
        public fun from(
            //#if MC >= 1.20.1
            ctx: DrawContext
            //#elseif MC >= 1.16.5
            //$$ matrices: MatrixStack
            //#endif
        ): OmniRenderingContext {
            val stack = OmniMatrixStacks.vanilla(
                //#if MC >= 1.20.1
                ctx
                //#elseif MC >= 1.16.5
                //$$ matrices
                //#endif
            )

            return OmniRenderingContext(
                //#if MC >= 1.20.1
                ctx,
                //#endif
                stack
            )
        }
    }

    private val scissorStack = ArrayDeque<ScissorBox>()

    public val currentScissor: ScissorBox?
        get() = scissorStack.lastOrNull()

    @JvmOverloads
    public fun renderText(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderText(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderText(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadow)
    }

    public fun renderText(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadowType)
    }

    public fun renderText(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadowType)
    }

    public fun renderText(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadowType)
    }

    @JvmOverloads
    public fun renderTextCentered(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderTextCentered(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadow)
    }

    @JvmOverloads
    public fun renderTextCentered(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadow: Boolean = true,
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadow)
    }

    public fun renderTextCentered(
        text: VanillaText,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadowType)
    }

    public fun renderTextCentered(
        text: Text,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadowType)
    }

    public fun renderTextCentered(
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadowType)
    }

    public fun renderGradientQuad(
        pipeline: OmniRenderPipeline,
        x: Float, y: Float,
        width: Int, height: Int,
        topColor: OmniColor,
        bottomColor: OmniColor,
    ) {
        val buffer = pipeline.createBufferBuilder()
        buffer
            .vertex(matrices, x.toDouble(), y.toDouble(), 0.0)
            .color(topColor)
            .next()
        buffer
            .vertex(matrices, (x + width).toDouble(), y.toDouble(), 0.0)
            .color(topColor)
            .next()
        buffer
            .vertex(matrices, (x + width).toDouble(), (y + height).toDouble(), 0.0)
            .color(bottomColor)
            .next()
        buffer
            .vertex(matrices, x.toDouble(), (y + height).toDouble(), 0.0)
            .color(bottomColor)
            .next()
        buffer.buildOrThrow().drawAndClose(pipeline)
    }

    public fun renderGradientQuad(
        x: Float, y: Float,
        width: Int, height: Int,
        topColor: OmniColor,
        bottomColor: OmniColor,
    ) {
        renderGradientQuad(OmniRenderPipelines.POSITION_COLOR, x, y, width, height, topColor, bottomColor)
    }

    @JvmOverloads
    public fun renderTextureRegion(
        pipeline: OmniRenderPipeline,
        location: Identifier,
        x: Float, y: Float,
        width: Int, height: Int,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        val texture = client.textureManager.getTexture(location)
        //#if MC <= 1.16.5
        //$$ if (texture == null) {
        //$$     throw IllegalArgumentException("Texture $location is not loaded")
        //$$ }
        //#endif

        val buffer = pipeline.createBufferBuilder()
        buffer
            .vertex(matrices, x.toDouble(), y.toDouble(), 0.0)
            .texture(u0.toDouble(), v0.toDouble())
            .color(color)
            .next()
        buffer
            .vertex(matrices, (x + width).toDouble(), y.toDouble(), 0.0)
            .texture(u1.toDouble(), v0.toDouble())
            .color(color)
            .next()
        buffer
            .vertex(matrices, (x + width).toDouble(), (y + height).toDouble(), 0.0)
            .texture(u1.toDouble(), v1.toDouble())
            .color(color)
            .next()
        buffer
            .vertex(matrices, x.toDouble(), (y + height).toDouble(), 0.0)
            .texture(u0.toDouble(), v1.toDouble())
            .color(color)
            .next()
        buffer.buildOrThrow().drawAndClose(pipeline) {
            val id =
                //#if MC >= 1.21.5
                (texture.glTexture as GlTexture).glId
                //#else
                //$$ texture.id
                //#endif
            texture(OmniTextureUnit.TEXTURE0, id)
        }
    }

    @JvmOverloads
    public fun renderTextureRegion(
        pipeline: OmniRenderPipeline,
        texture: OmniTextureHandle,
        x: Float, y: Float,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        renderTextureRegion(pipeline, texture.location, x, y, texture.width, texture.height, u0, v0, u1, v1, color)
    }

    @JvmOverloads
    public fun renderTexture(
        pipeline: OmniRenderPipeline,
        location: Identifier,
        x: Float, y: Float,
        width: Int, height: Int,
        u: Float, v: Float,
        textureWidth: Int, textureHeight: Int,
        color: OmniColor = OmniColors.WHITE,
    ) {
        val u1 = u + (width.toFloat() / textureWidth.toFloat())
        val v1 = v + (height.toFloat() / textureHeight.toFloat())
        renderTextureRegion(
            pipeline,
            location,
            x, y,
            width, height,
            u, v,
            u1, v1,
            color
        )
    }

    @JvmOverloads
    public fun renderTexture(
        pipeline: OmniRenderPipeline,
        texture: OmniTextureHandle,
        x: Float, y: Float,
        width: Int, height: Int,
        u: Float, v: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        renderTexture(
            pipeline,
            texture.location,
            x, y,
            width, height,
            u, v,
            texture.width, texture.height,
            color
        )
    }

    // TODO: items, entities

    /** Pushes a scissor box, intersecting with the current top-level scissor box. */
    public fun pushScissor(x: Int, y: Int, width: Int, height: Int) {
        val incoming = ScissorBox(x, y, width, height).transformQuickly(this.matrices)

        val effective = scissorStack.lastOrNull()?.let { top ->
            // We already had scissor; clamp to intersection
            top.intersection(incoming) ?: ScissorBox(0, 0, 0, 0) // <- collapsed to empty
        } ?: incoming // No previous scissor; use new box as-is

        scissorStack.addLast(effective)

        ScissorInternals.applyScissor(effective)
    }

    /** Pops the current scissor; restores previous or disables when empty. */
    public fun popScissor() {
        if (scissorStack.isEmpty()) {
            return
        }

        scissorStack.removeLast()

        val next = scissorStack.lastOrNull()
        if (next == null) {
            ScissorInternals.disableScissor()
        } else {
            ScissorInternals.applyScissor(next)
        }
    }

    public fun doesScissorContain(x: Int, y: Int): Boolean {
        val current = scissorStack.lastOrNull() ?: return true
        return x >= current.x && x < current.x + current.width &&
               y >= current.y && y < current.y + current.height
    }

    public fun doesScissorContain(x: Int, y: Int, width: Int, height: Int): Boolean {
        val current = scissorStack.lastOrNull() ?: return false
        return x + width <= current.x || x >= current.x + current.width ||
               y + height <= current.y || y >= current.y + current.height
    }

    public fun withScissor(x: Int, y: Int, width: Int, height: Int, runnable: Runnable) {
        pushScissor(x, y, width, height)

        try {
            runnable.run()
        } finally {
            popScissor()
        }
    }

    public fun <T> withScissor(x: Int, y: Int, width: Int, height: Int, supplier: () -> T): T {
        pushScissor(x, y, width, height)

        return try {
            supplier()
        } finally {
            popScissor()
        }
    }

    public fun withMatrices(consumer: Consumer<OmniMatrixStack>) {
        matrices.push()
        try {
            consumer.accept(matrices)
        } finally {
            matrices.pop()
        }
    }

    public fun <T> withMatrices(supplier: () -> T): T {
        matrices.push()
        return try {
            supplier()
        } finally {
            matrices.pop()
        }
    }

    /** Submits any necessary closing rendering operations. */
    override fun close() {
        if (scissorStack.isNotEmpty()) {
            scissorStack.clear()
            ScissorInternals.disableScissor()
        }
    }
}
