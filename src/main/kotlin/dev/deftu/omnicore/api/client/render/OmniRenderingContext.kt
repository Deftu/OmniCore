package dev.deftu.omnicore.api.client.render

import dev.deftu.omnicore.api.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStack
import dev.deftu.omnicore.api.client.render.stack.OmniMatrixStacks
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.color.OmniColors
import dev.deftu.omnicore.internal.client.render.ScissorInternals
import net.minecraft.util.Identifier
import java.util.function.Consumer

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

    public data class ScissorBox(val x: Int, val y: Int, val width: Int, val height: Int) {
        public val isEmpty: Boolean
            get() = width <= 0 || height <= 0

        public fun intersection(other: ScissorBox): ScissorBox? {
            val ix = maxOf(this.x, other.x)
            val iy = maxOf(this.y, other.y)
            val ix2 = minOf(this.x + this.width, other.x + other.width)
            val iy2 = minOf(this.y + this.height, other.y + other.height)
            val iw = ix2 - ix
            val ih = iy2 - iy
            return if (iw > 0 && ih > 0) ScissorBox(ix, iy, iw, ih) else null
        }
    }

    private val scissorStack = ArrayDeque<ScissorBox>()

    public val currentScissor: ScissorBox?
        get() = scissorStack.lastOrNull()

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
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.render(this, text, x, y, color, shadowType)
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
        text: String,
        x: Float, y: Float,
        color: OmniColor,
        shadowType: TextShadowType
    ) {
        OmniTextRenderer.renderCentered(this, text, x, y, color, shadowType)
    }

    public fun renderTextureRegion(
        pipeline: OmniRenderPipeline,
        texture: OmniTextureHandle,
        x0: Float, y0: Float,
        x1: Float, y1: Float,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        // TODO
    }

    public fun renderTextureRegion(
        pipeline: OmniRenderPipeline,
        location: Identifier,
        x0: Float, y0: Float,
        x1: Float, y1: Float,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        renderTextureRegion(pipeline, OmniTextures.wrap(location), x0, y0, x1, y1, u0, v0, u1, v1, color)
    }

    public fun renderTexture(
        pipeline: OmniRenderPipeline,
        texture: OmniTextureHandle,
        x: Float, y: Float,
        width: Float, height: Float,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        renderTextureRegion(pipeline, texture, x, y, x + width, y + height, u0, v0, u1, v1, color)
    }

    public fun renderTexture(
        pipeline: OmniRenderPipeline,
        location: Identifier,
        x: Float, y: Float,
        width: Float, height: Float,
        u0: Float, v0: Float,
        u1: Float, v1: Float,
        color: OmniColor = OmniColors.WHITE,
    ) {
        renderTextureRegion(pipeline, OmniTextures.wrap(location), x, y, x + width, y + height, u0, v0, u1, v1, color)
    }

    // TODO: items, entities, player heads

    /** Pushes a scissor box, intersecting with the current top-level scissor box. */
    public fun pushScissor(x: Int, y: Int, width: Int, height: Int) {
        val incoming = ScissorBox(x, y, width, height)

        val effective = scissorStack.lastOrNull()?.let { top ->
            // We already had scissor; clamp to intersection
            top.intersection(incoming) ?: ScissorBox(0, 0, 0, 0) // <- collapsed to empty
        } ?: incoming // No previous scissor; use new box as-is

        scissorStack.addLast(effective)

        if (effective.isEmpty) {
            ScissorInternals.disableScissor(
                //#if MC >= 1.21.6
                graphics
                //#endif
            )
        } else {
            ScissorInternals.applyScissor(
                //#if MC >= 1.21.6
                graphics,
                //#endif
                effective
            )
        }
    }

    /** Pops the current scissor; restores previous or disables when empty. */
    public fun popScissor() {
        if (scissorStack.isEmpty()) return
        scissorStack.removeLast()

        val next = scissorStack.lastOrNull()
        if (next == null) {
            ScissorInternals.disableScissor(
                //#if MC >= 1.21.6
                graphics
                //#endif
            )
        } else {
            ScissorInternals.applyScissor(
                //#if MC >= 1.21.6
                graphics,
                //#endif
                next
            )
        }
    }

    public fun doesScissorContain(x: Int, y: Int): Boolean {
        val current = scissorStack.lastOrNull() ?: return true
        return x >= current.x && x < current.x + current.width &&
               y >= current.y && y < current.y + current.height
    }

    public fun isClippedOut(x: Int, y: Int, width: Int, height: Int): Boolean {
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
            ScissorInternals.disableScissor(
                //#if MC >= 1.21.6
                graphics
                //#endif
            )
        }
    }
}
